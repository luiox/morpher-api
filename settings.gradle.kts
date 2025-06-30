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

val isJitPack by extra {
    System.getenv("CI") != null || System.getenv("JITPACK_IO") != null || System.getenv("TRAVIS") != null
}

println("Is JitPack build: $isJitPack") // 调试用，看是否判断正确

include("morpher-api")

if(!isJitPack){
    include("morpher-example")
}

