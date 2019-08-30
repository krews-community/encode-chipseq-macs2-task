package step

import mu.KotlinLogging
import java.nio.file.*
import util.CmdRunner
import util.*

private val log = KotlinLogging.logger {}

fun CmdRunner.macs2(taFile:Path, ctaFile:Path?, chrsz:Path?, gensz:String?, pvalThresh:Double, cap_num_peak:Int, makeSignal:Boolean,  pairedEnd: Boolean,shift:Int,fraglen:Path,outDir:Path, outputPrefix:String):macs2Output {
    Files.createDirectories(outDir)

  /*  val basename_ta =strip_ext_ta(taFile.fileName.toString())
    var basename_prefix:String
    if(ctaFile!==null){
        val basename_ctl_ta =strip_ext_ta(ctaFile.fileName.toString())
        basename_prefix = "${basename_ta}_x_${basename_ctl_ta}"
        if(basename_prefix.length>200)
        {
            basename_prefix = "${basename_ta}_x_control"

        }
    }
    else {
        basename_prefix = basename_ta
    }*/
    val fg = readFraglen(fraglen.toString())
    var tmpFiles = mutableListOf<String>() //Delete temp files at the end

    val prefix = outDir.resolve(outputPrefix)
    val  p = "pval${pvalThresh}"

    val npeak = "${prefix}.${p}.${human_readable_number(cap_num_peak)}.narrowPeak.gz"

    var fc_bigwig = "${prefix}.fc.signal.bigWig"
    var pval_bigwig = "${prefix}.pval.signal.bigWig"

    // temporary files
    val npeak_tmp = "${npeak}.tmp"


        var c: String
        if (ctaFile !== null) {
            c = " -c ${ctaFile} "
        } else {
            c = ""
        }

        var cmd1 = "macs2 callpeak "
        if (gensz !== "") {
            cmd1 += "-t ${taFile} ${c} -f BED${if (pairedEnd) "PE" else ""} -n ${prefix} -g ${gensz} -p ${pvalThresh} "

        } else {

            cmd1 += "-t ${taFile} ${c} -f BED${if (pairedEnd) "PE" else ""} -n ${prefix} -p ${pvalThresh} "

        }
        cmd1 += " --nomodel  --shift ${shift} --extsize ${fg} "
        cmd1 += "--keep-dup all"
        cmd1 += " -B --SPMR "

        this.run(cmd1)

       //score should be less than 1000
        val new_min=10
        val new_max=1000
        val scores_col=5
        val sorted_fn = " ${prefix}_peaks.narrowPeak_sorted"
        val rescaled_fn = " ${prefix}_peaks.narrowPeak_rescaled"

        var cmd4 = "LC_COLLATE=C sort -k 5gr,5gr  ${prefix}_peaks.narrowPeak | " +
                "awk 'BEGIN{{FS=\"\\t\";OFS=\"\\t\"}}{{if (NF != 0) print $0}}' > ${sorted_fn}\n"
        this.run(cmd4)
        val min = this.runCommand("head -n 1 ${sorted_fn} | cut -f 5")!!.trim().toFloat()
        val max = this.runCommand("tail -n 1 ${sorted_fn} | cut -f 5")!!.trim().toFloat()

        val cmd5= "cat ${sorted_fn}|\\\n" +
                "awk -v min=${min} -v max=${max} 'BEGIN{{OFS=\"\\t\"}}{{n=$5;a=min;b=max;x=10;y=1000}}\\\n" +
                "{{if (a==b) $5=$5; else $5=int(((n-a)*(y-x)/(b-a))+x) ; print $0}}' > ${rescaled_fn}"
        this.run(cmd5)
        var cmd2 = "LC_COLLATE=C sort -k 8gr,8gr ${rescaled_fn} | "
        cmd2 += "awk \'BEGIN{{OFS='\\t'}}"
        cmd2 += "{{$4=\"Peak_\"NR; if ($2<0) $2=0; if ($3<0) $3=0; printf \"%s\\t%s\\t%s\\t%s\\t%s\\t%s\\t%s\\t%s\\t%s\\t%s\\n\",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10}}\'  > ${npeak_tmp}"
        this.run(cmd2)

        var cmd3 = "head -n ${cap_num_peak} ${npeak_tmp} | gzip -nc > ${npeak}"
        this.run(cmd3)
        rm_f(listOf(npeak_tmp))


    if(makeSignal)
    {
        val fc_bedgraph = "${prefix}.fc.signal.bedgraph"
        val fc_bedgraph_srt = "${prefix}.fc.signal.srt.bedgraph"
        val pval_bedgraph = "${prefix}.pval.signal.bedgraph"
        val pval_bedgraph_srt = "${prefix}.pval.signal.srt.bedgraph"

        var cmd4 = "macs2 bdgcmp -t ${prefix}_treat_pileup.bdg "
        cmd4 += "-c ${prefix}_control_lambda.bdg "
        cmd4 += "--o-prefix \"${prefix}\" -m FE "
        this.run(cmd4)

        var cmd5 = "bedtools slop -i ${prefix}_FE.bdg -g ${chrsz} -b 0 | "
        cmd5 += "awk \'{{if ($3 != -1) print $0}}\' | "
        cmd5 += "bedClip stdin ${chrsz} ${fc_bedgraph}"
        this.run(cmd5)


        var cmd6= "LC_COLLATE=C sort -k1,1 -k2,2n ${fc_bedgraph} | awk \'BEGIN{{OFS='\\t'}}{{if (NR==1 || NR>1 && (prev_chr!=$1 || prev_chr==$1 && prev_chr_e<=$2)){{print $0}}; prev_chr=$1; prev_chr_e=$3;}}\' > ${fc_bedgraph_srt} "
        this.run(cmd6)

        var cmd7 = "bedGraphToBigWig ${fc_bedgraph_srt} ${chrsz} ${fc_bigwig}"
        this.run(cmd7)

        var ta_lc = this.runCommand("zcat -f ${taFile} | wc -l")!!.trim().toFloat()
        var cta_lc = this.runCommand("zcat -f ${ctaFile} | wc -l")!!.trim().toFloat()


        var sval:Double = 0.0
        if(ta_lc < cta_lc)
        {
            sval=(ta_lc)/1000000.0
        } else {
            sval=(cta_lc)/1000000.0
        }

        var cmd8 = "macs2 bdgcmp -t ${prefix}_treat_pileup.bdg "
        cmd8 += "-c ${prefix}_control_lambda.bdg "
        cmd8 += "--o-prefix ${prefix} -m ppois -S ${sval}"
        this.run(cmd8)

        var cmd9 = "bedtools slop -i ${prefix}_ppois.bdg -g ${chrsz} -b 0 | "
        cmd9 += "awk \'{{if ($3 != -1) print $0}}\' |"
        cmd9 += "bedClip stdin ${chrsz} ${pval_bedgraph}"

        this.run(cmd9)

        var cmd10 = "LC_COLLATE=C sort -k1,1 -k2,2n ${pval_bedgraph} | awk 'BEGIN{{OFS='\\t'}}{{if (NR==1 || NR>1 && (prev_chr!=$1 || prev_chr==$1 && prev_chr_e<=$2)){{print $0}}; prev_chr=$1; prev_chr_e=$3;}}' > ${pval_bedgraph_srt}"
        this.run(cmd10)

        var cmd11 = "bedGraphToBigWig ${pval_bedgraph_srt} ${chrsz} ${pval_bigwig}"
        this.run(cmd11)

        tmpFiles.add(fc_bedgraph)
        tmpFiles.add(fc_bedgraph_srt)
        tmpFiles.add(pval_bedgraph)
        tmpFiles.add(pval_bedgraph_srt)

    } else {
        fc_bigwig = "/dev/null"
        pval_bigwig = "/dev/null"
    }

    tmpFiles.add("${prefix}_*")

    //remove temporary files
    rm_f(tmpFiles)

    return macs2Output(npeak, fc_bigwig, pval_bigwig)

}
private fun readFraglen(f:String):Int {
    val s = java.io.File(f).readText(Charsets.UTF_8)
    return s!!.trim().toInt();

}