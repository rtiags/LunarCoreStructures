# Clean build / 空缓存构建

2026-11-07 在全新 Ubuntu 24.04 runner、Temurin 25.0.5 下验证通过：[CI 运行](https://github.com/rtiags/LunarCoreStructures/actions/runs/42347757015)。验证提交 `e19868498fa721faab9ee53f3946c8ed855729e2`，14 项测试通过，无跳过。

工作流先从固定 LC 提交 `f943ea2d916ac74e858c9e07d5aab54e8bc836a7` 编译 API 并发布到该次 checkout 的 build/repo，随后使用另一份空 Gradle 缓存构建生成插件。Maven 本地目录、Java user.home 均隔离，没有复制已有 API JAR、Paper 服务端目录或旧缓存。两个阶段分别约 49 秒和 53 秒；日志无 FROM-CACHE。

默认 Paper development bundle 构建路径通过，无需 lbr.paperServerHome。本地 API 可用 README 中的源码发布命令产生；完整空缓存复现步骤见 [LC 构建记录](https://github.com/rtiags/LunarCore-LC/blob/codex/yungs-paper-structures/docs/zh-CN/CLEAN-BUILD.md)。精确复现时 checkout 上述验证提交。

首次 CI 配置曾因 job env 中的 runner 上下文而未进入编译，修正初始化步骤后重跑成功。本次不额外声称 Windows/macOS 的完整 Java 空缓存构建已通过，也不声称不同 JDK 产物逐字节相同。测试报告和 JAR 可从上述运行的 artifacts 下载。

The standalone plugin passed all 14 tests on a fresh Ubuntu 24.04 runner using Temurin 25.0.5. Its LC API was compiled from the pinned source above, not taken from an existing installation. API and generator builds used separate empty Gradle caches, isolated Maven local/user.home, disabled build/configuration caches and the public Paper development bundle. No pre-existing Paper server or API JAR was used. Reports and JARs are attached to the run; see the LC guide for reproduction. The initial workflow parsing failure was corrected before the passing run. This is not a cross-platform or byte-for-byte reproducibility claim.
