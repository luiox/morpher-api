# morpher-api
[![JitPack](https://jitpack.io/v/luiox/morpher-api.svg)](https://jitpack.io/#luiox/morpher-api)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![CI](https://github.com/luiox/morpher-api/actions/workflows/build.yml/badge.svg)](https://github.com/luiox/morpher-api/actions)

基于 ASM 的 JVM 字节码转换工具集，提供通用的 Pass 接口，便于开发自定义的字节码转换器。

## 特性 Features
- 简洁的 Pass 接口，易于扩展
- 适用于多种字节码转换场景
- 兼容 JitPack 快速集成

## 快速开始 Quick Start

### 通过 JitPack 集成

在 `settings.gradle.kts` 中添加 JitPack 仓库：

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

在 `build.gradle.kts` 中添加依赖：

```kotlin
dependencies {
    implementation("com.github.luiox:morpher-api:最新版本号")
}
```
> 最新版本号请参考 [JitPack 页面](https://jitpack.io/#luiox/morpher-api)

## 使用示例 Example

示例请参考 [morpher-example](../morpher-example)。

## 贡献 Contributing
欢迎 issue 和 PR！

## 许可证 License
本项目基于 MIT License 发布，详见 LICENSE 文件。

## 自动打tag

commit信息格式要求：

```
version x.y
```

然后就会自动打tag为 `vx.y` 版本。
