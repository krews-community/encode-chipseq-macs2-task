import org.junit.jupiter.api.*
import step.*
import testutil.*
import testutil.cmdRunner
import util.*
import org.assertj.core.api.Assertions.*

class Macs2Tests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    //TODO: update tests
    @Test fun `run macs2 step makeSignal singleEnd ENCF000ASP `() {


       var mo:macs2Output =  cmdRunner.macs2(SE, null, CHR, "hs", 0.01, 500000, false,false,0,fl, testOutputDir, "ENCFF000ASP")
     /*   assertThat(testOutputDir.resolve("rep1_align_output.fc.signal.bigWig")).exists()
        assertThat(testOutputDir.resolve("rep1_align_output.fc.signal.bigWig")).exists()
        assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.narrowPeak.gz")).exists()*/

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, null,false, testOutputDir)
     //   assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.narrowPeak.gz")).exists()


        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR,false, testOutputDir)
     //   assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.narrowPeak.bb")).exists()


        /*   cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
           assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.frip.qc")).exists()*/


    }
    @Test fun `run macs2 step makeSignal singleEnd ENCF000ASU `() {


        var mo:macs2Output =  cmdRunner.macs2(chromtestTA1, chromtestCTL, CHR, "hs", 0.01, 500000, true,false,0,fl, testOutputDir, "ENCFF000ASU")
        /*   assertThat(testOutputDir.resolve("rep1_align_output.fc.signal.bigWig")).exists()
           assertThat(testOutputDir.resolve("rep1_align_output.fc.signal.bigWig")).exists()
           assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.narrowPeak.gz")).exists()*/

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED,false, testOutputDir)
        //   assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.narrowPeak.gz")).exists()


        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR,false, testOutputDir)
        //   assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.narrowPeak.bb")).exists()


        /*   cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
           assertThat(testOutputDir.resolve("rep1_align_output.pval0.01.500K.bfilt.frip.qc")).exists()*/


    }



@Test fun `test chrom bug`(){
        var mo:macs2Output =  cmdRunner.macs2(SE, CONTROLSE, CHR, "", 0.01, 500000, true,false,0,fl, testOutputDir, "testchrom")

    }

    @Test fun `run macs2 step makeSignal singleEnd encode`() {


        var mo:macs2Output =  cmdRunner.macs2(ENCODETA, ENCODECONTROLTA, CHR, "hs", 0.01, 500000, true,false,0,fl, testOutputDir, "ENCF000ASP_ENCF000ARK")
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.fc.signal.bigwig")).exists()
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.fc.signal.bigwig")).exists()
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.pval0.01.500K.narrowPeak.gz")).exists()

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED,false, testOutputDir)
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.pval0.01.500K.bfilt.narrowPeak.gz")).exists()


        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR,false, testOutputDir)
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.pval0.01.500K.bfilt.narrowPeak.bb")).exists()


        cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
        assertThat(testOutputDir.resolve("ENCF000ASP_ENCF000ARK.pval0.01.500K.bfilt.frip.qc")).exists()

    }
    @Test fun `run macs2 step makeSignal pooled macs2`() {


        var mo:macs2Output =  cmdRunner.macs2(pooledTa, CONTROLSE, CHR, "", 0.01, 300000, true,false,0,fl, testOutputDir, "pooled_macs2")
        assertThat(testOutputDir.resolve("pooled_macs2.fc.signal.bigwig")).exists()
        assertThat(testOutputDir.resolve("pooled_macs2.fc.signal.bigwig")).exists()
        assertThat(testOutputDir.resolve("pooled_macs2.pval0.01.300K.narrowPeak.gz")).exists()

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED,true, testOutputDir)
        assertThat(testOutputDir.resolve("pooled_macs2.pval0.01.300K.bfilt.narrowPeak.gz")).exists()

        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR,true, testOutputDir)
        assertThat(testOutputDir.resolve("pooled_macs2.pval0.01.300K.bfilt.narrowPeak.bb")).exists()

        cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
        assertThat(testOutputDir.resolve("pooled_macs2.pval0.01.300K.bfilt.frip.qc")).exists()



    }
     @Test fun `run macs2 step makeSignal pairedEnd`() {


       var mo:macs2Output =  cmdRunner.macs2(epigaba_pe, epigaba_ctl, CHR, "hs", 0.01, 300000, false,true,0,fl, testOutputDir, "rep1_R1_R2_align_output")
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.fc.signal.bigwig")).exists()
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.fc.signal.bigwig")).exists()
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.pval0.01.300K.narrowPeak.gz")).exists()

       var bfil =   cmdRunner.blacklist_filter(mo.npeak, null,false,  testOutputDir)
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.pval0.01.300K.bfilt.narrowPeak.gz")).exists()

        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, false, testOutputDir)
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.pval0.01.300K.bfilt.narrowPeak.bb")).exists()

        cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
         assertThat(testOutputDir.resolve("rep1_R1_R2_align_output.pval0.01.300K.bfilt.frip.qc")).exists()


     }
    @Test fun `run macs2 step no Signal SE`() {


        var mo:macs2Output =  cmdRunner.macs2(SE, null, CHR, "hs", 0.01, 300000, false,false,0,fl, testOutputDir, "signalopt")
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.narrowPeak.gz")).exists()

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, null,false,  testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.narrowPeak.gz")).exists()


        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, false, testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.narrowPeak.bb")).exists()


        cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.frip.qc")).exists()


    }
    @Test fun `run macs2 step no Signal PE`() {


        var mo:macs2Output =  cmdRunner.macs2(PE, null, CHR, "", 0.01, 300000, true,true, 0,fl,testOutputDir, "signalopt")
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.narrowPeak.gz")).exists()

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED,false,  testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.narrowPeak.gz")).exists()


        cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, false, testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.narrowPeak.bb")).exists()


        cmdRunner.frip_shifted(SE,bfil, fl, CHR, testOutputDir)
        assertThat(testOutputDir.resolve("signalopt.pval0.01.300K.bfilt.frip.qc")).exists()

    }
}