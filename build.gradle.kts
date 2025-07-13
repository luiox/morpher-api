import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("java")
    id("maven-publish")
}

group = "com.github.luiox"
version = "1.9"

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    // 设置子项目的属性
    group = rootProject.group
    version = rootProject.version

    // 设置子项目通用的依赖
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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifactId = project.name
                groupId = project.group.toString()
                version = project.version.toString()

                from(components["java"])

                pom {
                    name.set("${project.group}:${project.name}")
                    description.set(project.description ?: "")
                    url.set("https://github.com/luiox/morpher-api")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/luiox/morpher-api/blob/main/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.set("luiox")
                            name.set("Canrad")
                            email.set("1517807724@qq.com")
                        }
                    }
                }

//                pom.withXml {
//                    val dependenciesNode = asNode().appendNode("dependencies")
//
//                    // 遍历项目依赖并添加到 POM
//                    project.configurations.implementation.get().allDependencies.forEach { dep ->
//                        if (dep.group != null) { // 过滤掉没有 group 的依赖（如本地项目）
//                            val dependencyNode = dependenciesNode.appendNode("dependency")
//                            dependencyNode.appendNode("groupId", dep.group)
//                            dependencyNode.appendNode("artifactId", dep.name)
//                            dependencyNode.appendNode("version", dep.version ?: "")
//                        }
//                    }
//                }
            }
        }
    }
}

allprojects {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        mavenCentral()
        maven {
            url = uri("https://www.jitpack.io")
        }
    }

    tasks.withType<PublishToMavenRepository> {
        onlyIf {
            // 避免重复发布相同的构件
            repository.name != "MavenLocal" || project.hasProperty("publishToLocal")
        }
    }
}

