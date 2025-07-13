description = "some generic passes"

repositories {
    maven { url = uri("https://jitpack.io")  }
}

dependencies {
//    implementation("com.github.luiox:morpher-api:v1.6")
    implementation(project(":morpher-api"))

}

tasks{
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(22)
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    test {
        useJUnitPlatform()
    }
}