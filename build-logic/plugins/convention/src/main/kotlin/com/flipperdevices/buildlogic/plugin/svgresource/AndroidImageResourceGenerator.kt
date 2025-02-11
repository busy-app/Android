package com.flipperdevices.buildlogic.plugin.svgresource

import com.android.ide.common.vectordrawable.Svg2Vector
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Path
import kotlin.reflect.full.functions
import org.slf4j.Logger

/**
 * /commonMain/svgResources/ic_file.svg
 * /commonMain/composeResources/drawable/ic_file.xml
 * @param sourceSetDir commonMain/androidMain/etc...
 * @author https://github.com/icerockdev/moko-resources
 */
internal class AndroidImageResourceGenerator(
    private val sourceSetDir: File,
    private val logger: Logger,
) {
    private fun getSvgResourcesFiles(): List<File> {
        return sourceSetDir
            .resolve("svgResources")
            .listFiles()
            .orEmpty()
            .filterNotNull()
            .filter { file -> file.extension == "svg" }
    }

    fun generateResourceFiles() {
        getSvgResourcesFiles().forEach { file ->
            parseSvgToVectorDrawable(
                svgFile = file,
                vectorDrawableFile = sourceSetDir
                    .resolve("composeResources")
                    .resolve("drawable")
                    .resolve("${file.nameWithoutExtension}.xml")
            )
        }
    }

    private fun parseSvgToVectorDrawable(svgFile: File, vectorDrawableFile: File) {
        try {
            vectorDrawableFile.parentFile.mkdirs()
            vectorDrawableFile.createNewFile()
            FileOutputStream(vectorDrawableFile, false).use { os ->
                parseSvgToXml(svgFile, os)
                    .takeIf { it.isNotEmpty() }
                    ?.let { error -> logger.warn("parse from $svgFile to xml:\n$error") }
            }
        } catch (e: IOException) {
            logger.error("parse from $svgFile to xml error", e)
        }
    }

    private fun parseSvgToXml(file: File, os: OutputStream): String {
        return try {
            Svg2Vector.parseSvgToXml(Path.of(file.absolutePath), os)
        } catch (e: NoSuchMethodError) {
            logger.debug(
                buildString {
                    append("Not found parseSvgToXml function with Path parameter. ")
                    append("Fallback to parseSvgToXml function with File parameter.")
                },
                e
            )
            val parseSvgToXmlFunction = Svg2Vector::class.functions.first {
                // broken ktlint rule Indentation workaround
                if (it.name != "parseSvgToXml") return@first false
                if (it.parameters.size != 2) return@first false
                if (it.parameters[0].type.classifier != File::class) return@first false
                if (it.parameters[1].type.classifier != OutputStream::class) return@first false
                return@first true
            }
            return parseSvgToXmlFunction.call(file, os) as String
        }
    }
}