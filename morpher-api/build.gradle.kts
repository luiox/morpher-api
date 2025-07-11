plugins {
    id("java")
    id("maven-publish")
    //id("java-library")
}

description = "A tool that use custom transformer for modifying java bytecode"

dependencies {
    testImplementation(platform("org.junit:junit-bom:${properties["junit_version"] as String}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.ow2.asm:asm:${properties["asm_version"] as String}")
    implementation("org.ow2.asm:asm-tree:${properties["asm_version"] as String}")
    implementation("org.ow2.asm:asm-commons:${properties["asm_version"] as String}")
    implementation("org.ow2.asm:asm-analysis:${properties["asm_version"] as String}")
    implementation("org.ow2.asm:asm-util:${properties["asm_version"] as String}")

    implementation("ch.qos.logback:logback-classic:${properties["logback_version"] as String}")
    implementation("ch.qos.logback:logback-core:${properties["logback_version"] as String}")

    implementation("org.jetbrains:annotations:${properties["jetbrains_annotations_version"] as String}")
}

tasks {
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(22)
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

//    repositories {
//        maven {
//            //当前项目根目录
//            url = uri("$rootDir/repo")
//        }
//    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = "morpher-api"
                groupId = project.group.toString()
                version = project.version.toString()

                artifact(jar)

                pom.withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")

                    // 遍历项目依赖并添加到 POM
                    project.configurations.implementation.get().allDependencies.forEach { dep ->
                        if (dep.group != null) { // 过滤掉没有 group 的依赖（如本地项目）
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", dep.group)
                            dependencyNode.appendNode("artifactId", dep.name)
                            dependencyNode.appendNode("version", dep.version ?: "")
                        }
                    }
                }
            }
        }
    }
}


