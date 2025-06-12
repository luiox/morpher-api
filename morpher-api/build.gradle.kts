plugins {
    id("java")
    id("maven-publish")
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

//    publishing{
//        publications{
//            create<MavenPublication>("MavenPublication"){
//                groupId = "com.github.luiox"
//                artifactId = "morpher-api"
//                version = "1.6"
//
//                from(components["java"])
//            }
//        }
//    }
}


