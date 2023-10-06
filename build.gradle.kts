import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.3.8"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.teamvoided.iridium") version "3.0.2"
}

group = project.properties["maven_group"]!!
version = project.properties["mod_version"]!!
base.archivesName.set(project.properties["archives_base_name"] as String)
description = "TeamVoided Template Mod"

repositories {
    mavenCentral()
    maven {
        name = "brokenfuseReleases"
        url = uri("https://maven.teamvoided.org/releases")
    }
    maven("https://libs.azuredoom.com:4443/mods")

}
val modid = base.archivesName.get()


modSettings {
    modId(modid)
    modName("Team Voided Template Mod")

    entrypoint("main", "org.teamvoided.template.Template::commonInit")
    entrypoint("client", "org.teamvoided.template.Template::clientInit")
    entrypoint("fabric-datagen", "org.teamvoided.template.TemplateData")
}

loom {
    runs {
        create("data") {
            client()
            ideConfigGenerated(true)
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            vmArg("-Dfabric-api.datagen.modid=${modid}")
            runDir("build/datagen")
        }
    }
}

dependencies{
//    modImplementation("org.teamvoided:voidlib-core:1.5.7+1.20.1")
    modImplementation(files("voidlib-core-1.5.7+1.20.1.jar"))

    modImplementation ("mod.azure.azurelib:azurelib-fabric-1.20.1:1.0.31")
}

tasks {
    val targetJavaVersion = 17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }
}
sourceSets["main"].resources.srcDir("src/main/generated")