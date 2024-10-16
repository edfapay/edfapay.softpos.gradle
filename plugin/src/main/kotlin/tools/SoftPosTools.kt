/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package tools

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskExecutionException
import java.io.File


/**
 * A simple 'hello world' plugin.
 */

class SoftPosToolsExtension(private val project:Project) {
    var mode:String? = null
    var version:String? = null
    var dependency:String? = null
    private var partnerName:String? = null

    val currentTask:Task get() = project.tasks.getByName("edfapay")

    fun install(partnerCode:String?){
        val partnerCode_ = lookForPartnerCode(partnerCode)
        val mode = lookForSdkMode(mode)
        val version = lookForSdkVersion(version)

        this.partnerName = Helper.hexToPartnerCode(partnerCode_ ?: "") ?: throw TaskExecutionException(currentTask, Errors.invalidPartnerCodeToInstall)
        if(!partnerName!!.startsWith("partner~")){
            throw TaskExecutionException(currentTask, Errors.invalidPartnerCodeToInstall)
        }

        partnerName = partnerName?.replace("partner~", "")

        when(dependency == null) {
            true -> {
                val depStr = "com.github.edfapay.android-edfapay-softpos-sdk"
                if(version == null){
                    println("Installing edfapay sdk with parameters: mode:$mode | partnerCode:$partnerCode_")
                    dependency = "$depStr:$partnerName:$mode-SNAPSHOT"
                }else{
                    println("Skipping `mode` as `version` number exists...")
                    println("Installing edfapay sdk with parameters: version:$version | partnerCode:$partnerCode_")
                    dependency = "$depStr:$partnerName:$version"
                }

            }
            false -> {
                println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")
                println("Dependency override: $dependency")
                println(" - `Mode` and `Partner` cannot be considered to create `dependency`")
            }
        }
        println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")
        println("Partner:       ︳ $partnerName")
        println("Mode:          ︳ ${this.mode}")
        println("SDK:           ︳ $dependency")
        println("⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")

        dependency?.let {
//            project.dependencies.add("implementEdfapay", it)
            project.dependencies.add("implementation", it)
        } ?: throw TaskExecutionException(currentTask, Errors.dependencyIsNull)

    }

    private fun lookForPartnerCode(partnerCode:String?) : String?{
        return partnerCode?.let {
            println("Partner Code Found at Gradle Script: edfapay.softpos.install()")
            it
        } ?: (project.findProperty("EDFAPAY_PARTNER"))?.let {
            project.gradle.gradleHomeDir?.absolutePath
            println("Partner Code Found at Global or Project Gradle Properties '~/.gradle/gradle.properties | ./gradle.properties'")
            it as String?
        } ?: System.getenv().get("EDFAPAY_PARTNER")?.let {
            println("Partner Code Found at Environment Variable")
            it
        }
    }

    private fun lookForSdkMode(mode:String?) : String?{
        return mode?.let {
            println("SDK Mode Found at Gradle Script: edfapay.softpos.mode")
            it
        } ?: (project.findProperty("EDFAPAY_SDK_MODE"))?.let {
            project.gradle.gradleHomeDir?.absolutePath
            println("SDK Mode Found at Global or Project Gradle Properties '~/.gradle/gradle.properties | ./gradle.properties'")
            it as String?
        } ?: System.getenv().get("EDFAPAY_SDK_MODE")?.let {
            println("SDK Mode Found at Environment Variable")
            it
        }
    }

    private fun lookForSdkVersion(version:String?) : String?{
        return version?.let {
            println("SDK Version Found at Gradle Script: edfapay.softpos.version")
            it
        } ?: (project.findProperty("EDFAPAY_SDK_VERSION"))?.let {
            project.gradle.gradleHomeDir?.absolutePath
            println("SDK Version Found at Global or Project Gradle Properties '~/.gradle/gradle.properties | ./gradle.properties'")
            it as String?
        } ?: System.getenv().get("EDFAPAY_SDK_VERSION")?.let {
            println("SDK Version Found at Environment Variable")
            it
        }
    }

    private fun configurePartner(){
        project.afterEvaluate {
            project.configurations.getByName("implementEdfapay").resolve().forEach { aarFile ->
                val depsAarName = "card-sdk-$mode-SNAPSHOT.aar"

                if (aarFile.name.endsWith(depsAarName)) {
//                    println("->> ${aarFile.absolutePath}")

                    // Define a temporary directory to extract the AAR contents
                    val fname = aarFile.name.replace(".aar", "")
                    val modifedFolder = File(aarFile.parentFile.absolutePath + "/$fname")
                    modifedFolder.mkdir()

                    // Extract the contents of the AAR aarFile

                    project.copy {
                        it.from(project.zipTree(aarFile))
                        it.into(modifedFolder)
                    }

                    // Move assets to root from partner~assets directory

                    val partnerAssets = File(modifedFolder, "assets/$partnerName")
                    project.copy {
                        it.from(project.fileTree(partnerAssets))
                        it.into(partnerAssets.parentFile)
                    }

                    // Remove partner assets directories
                    if(partnerAssets.parentFile.listFiles()?.isNotEmpty() == true){
                        partnerAssets.parentFile.listFiles()?.toList()?.forEach {
                            if(it.name.startsWith("partner~")){
                                println("File Delete ${it.deleteRecursively()}: ${it.absolutePath}")
                            }
                        }
                    }
                    Helper.zipFolder(modifedFolder.toPath(), aarFile.toPath())
                }
            }
        }
    }
}

class SoftPosTools: Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("edfapay") { task ->
            task.extensions.add("softpos", SoftPosToolsExtension(project))
//            project.configurations.create("implementEdfapay"){
//                it.isCanBeResolved = true
//            }
        }
    }
}
