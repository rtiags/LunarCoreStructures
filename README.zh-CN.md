# LunarCoreStructures

独立发布的 YUNG’s 结构 Paper 26.3 移植，使用 Java 25。本轮仍依赖 LunarCore，要求核心带有 `NativeGeneratorDelegate` API；不能在未安装 LC 的纯 Paper 服上运行。

[English](README.md) · [生成规则](docs/zh-CN/STRUCTURES.md) · [历史生成验证](docs/zh-CN/STRUCTURES-VALIDATION.md)

## 安装与升级

停服后，将桥接 PR 对应的兼容核心和 `LunarCoreStructures.jar` 放入 `plugins/`，把 `lbr-structures-web.jar` 放入 `plugins/LunarCore/modules/`。完整重启后，在 Web 的“服务器 → 结构美化”管理全服默认、逐世界覆盖和继承设置。

生成插件和管理模块是两个 JAR。禁用或重载桥接模块只影响管理页面。生成插件不能热卸载；已经包含其结构数据的世界后续加载仍需要此插件。

从原 PR 的合体版本升级时替换原生成 JAR，保留 `plugins/LunarCoreStructures/structures.json`，不要同时保留两个插件 JAR。配置格式和插件名不变，无需迁移数据。保存只写入待生效配置，完整重启才应用到后续新区块；旧区块不重建。桥接模块 0.0.6-preview.1 起，自动重启要求可读的 restart-script；缺少时只提示从服务端面板手动重启，服务器继续运行。

## 构建

在对应 LunarCore 桥接分支执行 `./gradlew :lbr-api:publishToMavenLocal`，再在本仓库用 JDK 25 执行 `./gradlew :plugin:test :plugin:jar`。

也可直接指定发行附件 API：`-PlunarCoreApi=/绝对路径/lbr-api-0.0.6-preview.jar`，或指定本地 Maven 仓库 `-PlunarCoreRepository=/绝对路径/build/repo`。已有启动过的 Paper 26.3 时增加 `-Plbr.paperServerHome=/绝对路径/Paper目录`，目录中保留匹配的启动 JAR 和已解压依赖。未指定时使用 paperweight 下载固定的 build 202 开发包。产物位于 `plugin/build/libs/LunarCoreStructures.jar`，API 不打包进插件。

生成代码、资源和测试位于 `plugin/`；上游快照、哈希和许可位于 `third-party/yungs/`；测试探针位于 `tools/structures-probe/`，不用于正式服。

## 桥接协议

`getManagementApi()` 返回公开管理对象，版本 `apiVersion()` 为 1。接口 `status()`、`readConfig()`、`saveConfig(Map)` 只传递 JDK Map/List/基本值；桥接从真实插件 ClassLoader 解析公开方法，不引用私有实现。

`status()` 必须在服务端主线程执行；配置读取和保存由存储锁保护。保存沿用 schema=1、revision 并发检查和原子写入，返回 `ok/saved/restartRequired`。非法输入或旧 revision 抛 `IllegalArgumentException`，文件错误抛 `IOException`。鉴权、审计、重启由桥接模块负责，生成插件不注册 Web 接口。

桥接源码：https://github.com/rtiags/LunarCore-LC/tree/codex/yungs-paper-structures/modules/lbr-structures-web

许可见 LICENSE 与 THIRD-PARTY-NOTICES.md，保留 YUNG’s 原始署名及 LGPL 许可。本移植不代表获得原作者背书。
