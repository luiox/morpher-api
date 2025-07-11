plugins {
    id("java")
}

description = "some generic passes"
group = "com.github.luiox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io")  }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:${properties["junit_version"] as String}"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.github.luiox:morpher-api:v1.6")
//    implementation(project(":morpher-api"))
}

tasks{
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(22)
        }
    }

    test {
        useJUnitPlatform()
    }
}