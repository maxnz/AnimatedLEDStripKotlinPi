/*
 * Copyright (c) 2018-2021 AnimatedLEDStrip
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

tasks.wrapper {
    gradleVersion = "6.7.1"
}

plugins {
    kotlin("jvm") version "1.4.32"
    id("java-library")
    signing
    id("de.marcphilipp.nexus-publish") version "0.4.0"
    id("io.codearte.nexus-staging") version "0.30.0"
}

repositories {
    jcenter()
    mavenCentral()
    mavenLocal()
}

kotlin {
    sourceSets {
        main {
            dependencies {
                implementation("io.github.animatedledstrip:animatedledstrip-core-jvm:1.0.1")
                implementation("com.github.mbelling:rpi-ws281x-java:2.0.0-SNAPSHOT")
                implementation("org.apache.logging.log4j:log4j-core:2.13.2")
                implementation("org.apache.logging.log4j:log4j-api:2.13.2")
            }
        }
    }
}

group = "io.github.animatedledstrip"
version = "1.0.1"
description = "A library for using the AnimatedLEDStrip library on Raspberry Pis"

val javadoc = tasks.named("javadoc")

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(javadoc)
}

publishing {
    publications.create<MavenPublication>("mavenPublication") {
        artifact(tasks.jar.get())
        artifact(javadocJar)
        artifact(tasks.kotlinSourcesJar.get())
        pom {
            name.set("AnimatedLEDStrip Device - Raspberry Pi")
            description.set("A library for using the AnimatedLEDStrip library on Raspberry Pis")
            url.set("https://github.com/AnimatedLEDStrip/device-pi")

            licenses {
                license {
                    name.set("MIT License")
                    url.set("http://www.opensource.org/licenses/mit-license.php")
                }
            }

            developers {
                developer {
                    name.set("Max Narvaez")
                    email.set("mnmax.narvaez3@gmail.com")
                    organization.set("AnimatedLEDStrip")
                    organizationUrl.set("https://animatedledstrip.github.io")
                }
            }

            scm {
                connection.set("scm:git:https://github.com/AnimatedLEDStrip/device-pi.git")
                developerConnection.set("scm:git:https://github.com/AnimatedLEDStrip/device-pi.git")
                url.set("https://github.com/AnimatedLEDStrip/device-pi")
            }
        }

    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

nexusPublishing {
    repositories {
        sonatype {
            val nexusUsername: String? by project
            val nexusPassword: String? by project
            username.set(nexusUsername)
            password.set(nexusPassword)
        }
    }
}
