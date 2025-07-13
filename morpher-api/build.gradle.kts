description = "A tool that use custom transformer for modifying java bytecode"

dependencies {


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


}


