import org.junit.jupiter.api.*
import step.*
import testutil.*
import testutil.cmdRunner
import testutil.setupTest
import util.*
import org.assertj.core.api.Assertions

class Macs2Tests {
    @BeforeEach fun setup() = setupTest()
    @AfterEach fun cleanup() = cleanupTest()

    @Test fun `run macs2 step makeSignal singleEnd`() {


        var mo:macs2Output =  cmdRunner.macs2(SE, CONTROLSE, CHR, "", 0.01, 300000, true,false, testOutputDir, "signalopt")
        Assertions.assertThat(mo.fc_bigwig)
        Assertions.assertThat(mo.npeak)
        Assertions.assertThat(mo.pval_bigwig)

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED, testOutputDir)
        Assertions.assertThat(bfil)

        var bb = cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, testOutputDir)
        Assertions.assertThat(bb)

        val frip_qc=  cmdRunner.frip_shifted(SE,bfil, 1, CHR, testOutputDir)
        Assertions.assertThat(frip_qc)

    }
     @Test fun `run macs2 step makeSignal pairedEnd`() {


       var mo:macs2Output =  cmdRunner.macs2(PE, null, CHR, "", 0.01, 300000, true,true, testOutputDir, "signalopt")
       Assertions.assertThat(mo.fc_bigwig)
       Assertions.assertThat(mo.npeak)
       Assertions.assertThat(mo.pval_bigwig)

       var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED, testOutputDir)
       Assertions.assertThat(bfil)

       var bb = cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, testOutputDir)
       Assertions.assertThat(bb)

       val frip_qc=  cmdRunner.frip_shifted(SE,bfil, 1, CHR, testOutputDir)
       Assertions.assertThat(frip_qc)

     }
    @Test fun `run macs2 step no Signal SE`() {


        var mo:macs2Output =  cmdRunner.macs2(SE, null, CHR, "", 0.01, 300000, false,false, testOutputDir, "signalopt")
        Assertions.assertThat(mo.npeak)

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED, testOutputDir)
        Assertions.assertThat(bfil)

        var bb = cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, testOutputDir)
        Assertions.assertThat(bb)

        val frip_qc=  cmdRunner.frip_shifted(SE,bfil, 1, CHR, testOutputDir)
        Assertions.assertThat(frip_qc)

    }
    @Test fun `run macs2 step no Signal PE`() {


        var mo:macs2Output =  cmdRunner.macs2(PE, null, CHR, "", 0.01, 300000, false,true, testOutputDir, "signalopt")
        Assertions.assertThat(mo.npeak)

        var bfil =   cmdRunner.blacklist_filter(mo.npeak, BLBED, testOutputDir)
        Assertions.assertThat(bfil)

        var bb = cmdRunner.peak_to_bigbed(bfil, "narrowPeak", CHR, testOutputDir)
        Assertions.assertThat(bb)

        val frip_qc=  cmdRunner.frip_shifted(SE,bfil, 1, CHR, testOutputDir)
        Assertions.assertThat(frip_qc)

    }
}