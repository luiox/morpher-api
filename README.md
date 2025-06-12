# morpher-api
some asm util for writing jvm bytecode transfomer

## use jitpack

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
