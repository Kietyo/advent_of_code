import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.8"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

configure<AllOpenExtension> {
    annotation("org.openjdk.jmh.annotations.State")
}

sourceSets.configureEach {
    java.setSrcDirs(listOf("$name/src"))
    resources.setSrcDirs(listOf("$name/resources"))
}


repositories {
    mavenCentral()
}

sourceSets.forEach {
    println("sourceSets: $it")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.4")
    testImplementation(kotlin("test"))
    implementation(project(":ktruth"))

}

tasks.test {
    useJUnitPlatform()
}


kotlin {
    jvmToolchain(8)

    sourceSets.all {
        println("kotlin source set: ${this}")
    }

    //    sourceSets.all {
    //        languageSettings {
    //            languageVersion = "2.0"
    //        }
    //    }
}

benchmark {
    configurations {
        named("main") {
            warmups = 3
            iterations = 5
            iterationTime = 1
            iterationTimeUnit = "sec"
//            mode = "avgt"
        }
    }

    targets {
//                register("jvm")
        register("main") {
            this as JvmBenchmarkTarget
            jmhVersion = "1.21"
        }
    }
}

//application {
//    mainClass.set("MainKt")
//}