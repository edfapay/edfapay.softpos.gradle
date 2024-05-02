/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package tools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskExecutionException
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipOutputStream

/**
 * A simple 'hello world' plugin.
 */
enum class Mode{
    development, production, local
}

class SoftPosToolsExtension(private val project:Project, val partnerCode:String? = null) {
    var partnerName:String? = null
    var mode = Mode.production
    var dep = ""

    fun validate(task:Task) : SoftPosToolsExtension{
        if (partnerCode.isNullOrEmpty()) {
            throw TaskExecutionException(task, Exception(Errors.missingPartnerCode))
        }else{
            Helper.hexToAscii(partnerCode)?.let {
                partnerName = it
            } ?: throw TaskExecutionException(task, Exception(Errors.invalidPartnerCode))
        }

        return this
    }

    fun install(){

        val depName = "partner~${partnerName}"
        dep = when (mode) {
            Mode.local -> "com.edfapay:card-sdk:1.0.1"
            else -> "com.github.edfapay:edfapay-plugin-android:$depName-$mode-SNAPSHOT"
        }
        println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")
        println("Partner: ︳ $partnerName")
        println("Mode:    ︳ $mode")
        println("Deps:    ︳ $dep")
        println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")

        project.dependencies.add("implementEdfapay", dep)
        project.dependencies.add("implementation", dep)
        configurePartner()
    }

    private fun configurePartner(){
        project.afterEvaluate {
            println("Configuring partner assets:")
            println(">> 1")
            project.configurations.getByName("implementEdfapay").resolve().forEach { file ->
                val partner = "partner~$partnerName"
                val depsAarName = "card-sdk-$partner-$mode-SNAPSHOT.aar"
                println(depsAarName)

                if (file.name.endsWith(depsAarName)) {
                    println("AAR file: ${file.absolutePath}")

                    // Define a temporary directory to extract the AAR contents
                    val fname = file.name.replace(".aar", "")
                    val newAarFolder = File(file.parentFile.absolutePath + "/$fname")
                    newAarFolder.mkdir()

                    // Extract the contents of the AAR file

                    project.copy {
                        it.from(project.zipTree(file))
                        it.into(newAarFolder)
                    }

                    // Move assets to root from partner~assets directory
                    val assets = File(newAarFolder, "assets")
                    val partnerAssets = File(assets, partner)
                    println(">> " + partnerAssets)

                    project.copy {
                        it.from(project.fileTree(partnerAssets))
                        it.into(assets)
                    }


                    // Remove partner assets directories
                    if(assets.listFiles()?.isNotEmpty() == true){
                        assets.listFiles()?.toList()?.forEach {
                            if(it.name.startsWith("partner~")){
                                it.delete()
                            }
                        }
                    }
                    println(">> 2")

                    // Zip and replace modified directory
//                    val dest = File(file.parentFile.absolutePath + "/${file.name}")
//                    val fos = FileOutputStream(dest)
//                    val zipOut = ZipOutputStream(fos)
//                    Helper.ZipDirectory.zipFile(newAarFolder, file.name, zipOut)
//                    zipOut.close()
//                    fos.close()
//                    println()

//                    project.ant.invokeMethod("zip", dest)
//                    println(">> 3")
//                    project.ant.invokeMethod("fileset", newAarFile)
                    println(">> 4")
                }
            }
        }
    }
}

class SoftPosTools: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("edfapay") { task ->
            val partnerCode = System.getenv("EDFAPAY_PARTNER")
            with(SoftPosToolsExtension(project, partnerCode).validate(task)) {
                task.extensions.add("softpos", this)
                project.configurations.create("implementEdfapay"){
                    it.isCanBeResolved = true
                }

            }
        }


//        project.task("edfapay") {
//            println("(hello) Hello from plugin 'tools.SoftPosTools'")
//        }
    }
}