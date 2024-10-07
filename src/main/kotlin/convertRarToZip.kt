import com.github.junrar.Archive
import com.github.junrar.rarfile.FileHeader
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

//tener instalado unar brew install unar
fun extractRarWithUnar(rarFilePath: String, outputDirPath: String): Boolean {
    return try {
        val process = ProcessBuilder("unar", "-o", outputDirPath, rarFilePath).start()
        process.inputStream.bufferedReader().use { it.lines().forEach { line -> println(line) } }
        val exitCode = process.waitFor()

        // Verificar si `unar` se ejecutó correctamente
        if (exitCode == 0) {
            println("Extracción completada con éxito.")
            true
        } else {
            println("Error al extraer el archivo .rar. Código de salida: $exitCode")
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun convertToZip(sourceDirPath: String, zipFilePath: String) {
    val sourceDir = File(sourceDirPath)
    ZipOutputStream(FileOutputStream(zipFilePath)).use { zipOut ->
        sourceDir.walkTopDown().filter { it.isFile }.forEach { file ->
            val entry = ZipEntry(file.relativeTo(sourceDir).path)
            zipOut.putNextEntry(entry)
            file.inputStream().use { input ->
                input.copyTo(zipOut)
            }
            zipOut.closeEntry()
        }
    }
}

fun convertRarToZip(rarFilePath: String, zipFilePath: String) {
    val tempDir = File("temp_extracted")
    if (!tempDir.exists()) {
        tempDir.mkdir()
    }

    try {
        // Extraer el archivo .rar usando `unar`
        val extractionSuccessful = extractRarWithUnar(rarFilePath, tempDir.absolutePath)

        // Verificar si la extracción fue exitosa antes de proceder
        if (extractionSuccessful) {
            // Comprimir los archivos extraídos en un .zip
            convertToZip(tempDir.absolutePath, zipFilePath)
            println("Archivo convertido a .zip exitosamente en: $zipFilePath")
        } else {
            println("No se pudo convertir el archivo .rar a .zip debido a un error de extracción.")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // Eliminar la carpeta temporal
        tempDir.deleteRecursively()
    }
}

fun main() {
    //elegir la ruta del archivo rar y la ruta donde se guardara el archivo zip
    val rarFilePath = "/Users/brandonluismenesessolorzano/Downloads/SED.rar"
    val zipFilePath = "/Users/brandonluismenesessolorzano/Downloads/SED.zip"


    convertRarToZip(rarFilePath, zipFilePath)
}