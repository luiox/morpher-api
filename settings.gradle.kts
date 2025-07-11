pluginManagement {
    repositories {
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "morpher-api"

include(
    "morpher-api",
    "morpher-passes"
)


