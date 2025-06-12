import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("java")
}

group = "com.github.luiox"
version = "1.6"

subprojects {
    apply(plugin = "java")

    // 设置子项目的属性
    group = rootProject.group
    version = rootProject.version

    // 设置子项目通用的依赖
    dependencies {
        testImplementation(platform("org.junit:junit-bom:${properties["junit_version"] as String}"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
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
}

