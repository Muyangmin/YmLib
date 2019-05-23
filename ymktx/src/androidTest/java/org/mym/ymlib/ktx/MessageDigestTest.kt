package org.mym.ymlib.ktx

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class MessageDigestTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun testMd5ForEmptyString() = testMd5Internal("", "d41d8cd98f00b204e9800998ecf8427e")

    @Test
    fun testMd5ForShortString() = testMd5Internal("Abc", "35593b7ce5020eae3ca68fd5b6f3e031")

    @Test
    @Suppress("SpellCheckingInspection")
    fun testMd5ForLongString() = testMd5Internal(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec accumsan tempor auctor. Nullam nibh mi, euismod ac est ac, egestas rhoncus augue. Proin feugiat vel nisl id ultricies. Suspendisse porttitor auctor mauris eget rhoncus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Mauris placerat mauris in sapien ultrices consectetur. Praesent a magna nec tortor sodales rhoncus. Vestibulum sagittis vestibulum pretium. Proin pulvinar eleifend accumsan. Nunc blandit gravida massa at imperdiet.",
        "c7df8331cffd58369be7f1b73b85b452"
    )

    private fun testMd5Internal(source: String, expectedResult: String) {
        Assert.assertEquals(expectedResult, source.md5())
    }

    @Test
    fun testMd5ForGifFile() = testFileDigestInternal("img_gif_test.gif", "MD5", "3daa9618ae9b5e9382ed57efd271b2be")

    @Test
    fun testMd5ForEmptyFile() = testFileDigestInternal("empty_file.txt", "MD5", "d41d8cd98f00b204e9800998ecf8427e")

    @Test
    fun testSHA1ForGifFile() =
        testFileDigestInternal("img_gif_test.gif", "SHA1", "5b5b87c68653180263e4859e8e97af8e5d6978e2")

    @Test
    fun testSHA1ForEmptyFile() =
        testFileDigestInternal("empty_file.txt", "SHA1", "da39a3ee5e6b4b0d3255bfef95601890afd80709")

    @Test
    fun testSHA256ForGifFile() = testFileDigestInternal(
        "img_gif_test.gif",
        "SHA256",
        "7d3fedfe75976a8214f910f0721fc70deb60b77d8a618a471fa127e25362b477"
    )

    @Test
    fun testSHA256ForEmptyFile() = testFileDigestInternal(
        "empty_file.txt",
        "SHA256",
        "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    )

    private fun testFileDigestInternal(assetsName: String, algorithm: String, expectedResult: String) {
        val testFile = File(context.filesDir, "test_$assetsName")
        if (testFile.exists()) {
            testFile.delete()
        }
        val fos = FileOutputStream(testFile)
        val ais = context.assets.open(assetsName)

        val buffer = ByteArray(8192)
        var read: Int
        do {
            read = ais.read(buffer)
            if (read != -1) {
                fos.write(buffer, 0, read)
            }
        } while (read != -1)

        fos.close()
        ais.close()

        val digest = testFile.digest(algorithm)
        //Delete before assertion to ensure delete even test failed
        testFile.delete()
        Assert.assertEquals(expectedResult, digest)
    }
}