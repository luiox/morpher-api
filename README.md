# morpher-api

Some asm util for writing jvm bytecode transformer. This is a relatively universal set of Pass interfaces.

## use jitpack

link:[https://jitpack.io/#luiox/morpher-api](https://jitpack.io/#luiox/morpher-api)

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    implementation("com.github.luiox:morpher-api:Tag")
}
```
