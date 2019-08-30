package testutil
import java.nio.file.*

fun getResourcePath(relativePath: String): Path {

    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
     return Paths.get(url.toURI())
}

// Resource Directories
val testInputResourcesDir = getResourcePath("test-input-files")
val testOutputResourcesDir = getResourcePath("test-output-files")

// Test Working Directories
val testDir = Paths.get("/tmp/chipseq-test")!!
val testInputDir = testDir.resolve("input")!!
val testOutputDir = testDir.resolve("output")!!

val PE = testInputDir.resolve("rep1_R1_R2_align_output.nodup.tagAlign.gz")
val SE = testInputDir.resolve("rep1_align_output.nodup.tagAlign.gz")
val CONTROLSE = testInputDir.resolve("control1_align_output.nodup.tagAlign.gz")

val CHR = testInputDir.resolve("hg38.chrom.sizes")
val BLBED = testInputDir.resolve("hg38.blacklist.bed.gz")
val pooledTa = testInputDir.resolve("pooled_ta.pooled.tagAlign.gz")

val ENCODETA =  testInputDir.resolve("ENCFF000ASP.nodup.tagAlign.gz")
val ENCODECONTROLTA = testInputDir.resolve("ENCFF000ARK.nodup.tagAlign.gz")

val fl =  testInputDir.resolve("fraglen.txt")


val chromtestTA = testInputDir.resolve("ENCFF000ASP.tagAlign.gz")
val chromtestTA1 = testInputDir.resolve("ENCFF000ASU.tagAlign.gz")
val chromtestCTL= testInputDir.resolve("pooled_ta_ctl.pooled.tagAlign.gz")

val epigaba_pe =  testInputDir.resolve("EpiGABA_H.276.GLU.27ac.tagAlign.gz")
val epigaba_ctl =  testInputDir.resolve("epigaba.pooled.tagalign.gz")