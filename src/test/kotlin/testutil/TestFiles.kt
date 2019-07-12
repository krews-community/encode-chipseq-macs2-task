package testutil
import java.nio.file.*

fun getResourcePath(relativePath: String): Path {

    val url = TestCmdRunner::class.java.classLoader.getResource(relativePath)
     return Paths.get(url.toURI())
}

// Resource Directories
val testInputResourcesDir = getResourcePath("test-input-files")

// Test Working Directories
val testDir = Paths.get("/tmp/chipseq-test")!!
val testInputDir = testDir.resolve("input")!!
val testOutputDir = testDir.resolve("output")!!

val PE = testInputDir.resolve("rep1_R1_R2_align_output.nodup.tagAlign.gz")
val SE = testInputDir.resolve("rep1_align_output.nodup.tagAlign.gz")
val CONTROLSE = testInputDir.resolve("control1_align_output.nodup.tagAlign.gz")

val CHR = testInputDir.resolve("hg38.chrom.sizes.txt")
val BLBED = testInputDir.resolve("hg38.blacklist.bed.gz")