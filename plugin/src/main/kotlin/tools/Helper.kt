package tools

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


object Helper {
    fun hexToAscii(hexStr: String): String? {
        val output = StringBuilder("")
        var i = 0
        while (i < hexStr.length) {
            val str = hexStr.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return when (output.isEmpty()) {
            true -> null
            false -> output.toString()
        }
    }

    object ZipDirectory {
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val sourceFile = "zipTest"
            val fos = FileOutputStream("dirCompressed.zip")
            val zipOut = ZipOutputStream(fos)
            val fileToZip = File(sourceFile)
            zipFile(fileToZip, fileToZip.getName(), zipOut)
            zipOut.close()
            fos.close()
        }

        @Throws(IOException::class)
        fun zipFile(fileToZip: File, fileName: String, zipOut: ZipOutputStream) {
            if (fileToZip.isHidden()) {
                return
            }
            if (fileToZip.isDirectory()) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(ZipEntry(fileName))
                    zipOut.closeEntry()
                } else {
                    zipOut.putNextEntry(ZipEntry("$fileName/"))
                    zipOut.closeEntry()
                }
                val children: Array<File> = fileToZip.listFiles()
                for (childFile in children) {
                    zipFile(childFile, fileName + "/" + childFile.getName(), zipOut)
                }
                return
            }
            val fis: FileInputStream = FileInputStream(fileToZip)
            val zipEntry = ZipEntry(fileName)
            zipOut.putNextEntry(zipEntry)
            val bytes = ByteArray(1024)
            var length: Int
            while (fis.read(bytes).also { length = it } >= 0) {
                zipOut.write(bytes, 0, length)
            }
            fis.close()
        }
    }
    object ZipManager {

        fun zip(files: List<File>, zipFile: File) {
            ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { output ->
                files.forEach { file ->
                    if(file.length() > 1) {
                        FileInputStream(file).use { input ->
                            BufferedInputStream(input).use { origin ->
                                val entry = ZipEntry(file.name)
                                output.putNextEntry(entry)
                                origin.copyTo(output, 1024)
                            }
                        }
                    }
                }
            }
        }

        //If we do not set encoding as "ISO-8859-1", European characters will be replaced with '?'.
        fun unzip(files: List<File>, zipFile: ZipFile) {
            zipFile.use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        BufferedReader(InputStreamReader(input, "ISO-8859-1")).use { reader ->
                            files.find { it.name.contains(entry.name) }?.run {
                                BufferedWriter(FileWriter(this)).use { writer ->
                                    var line: String? = null
                                    while ({ line = reader.readLine(); line }() != null) {
                                        writer.append(line).append('\n')
                                    }
                                    writer.flush()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}