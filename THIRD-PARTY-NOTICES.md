# Third-party notices

## YUNG’s structures / YUNG’s 结构系列

LunarCoreStructures contains a Paper 26.3 adaptation of YUNG’s API, the nine Better structure projects, YUNG’s Extras and YUNG’s Bridges below. Original authorship and LGPL-3.0 license texts are retained. This is an independent port and does not imply endorsement by YUNG-GANG.

LunarCoreStructures 包含 YUNG’s API、下列九个 Better 结构项目、YUNG’s Extras 与 YUNG’s Bridges 的 Paper 26.3 移植。保留原作者 YUNGNICKYOUNG、各仓库贡献者和原有 LGPL-3.0 许可；本移植不表示获得原作者背书。未修改的上游快照及逐文件 SHA-256 位于 `third-party/yungs/<项目>/upstream` 和 `SOURCE.json`。

| Project / 项目 | Upstream / 上游 | Commit / 固定提交 |
|---|---|---|
| api | [api](https://github.com/YUNG-GANG/YUNGs-API) | `a84778d03087a02ae8f5ae74db10827cb682bc22` |
| bridges | [bridges](https://github.com/YUNG-GANG/YUNGs-Bridges) | `1e47f9fcdbf0c05e3b5225eda8ab0405c398533d` |
| desert-temples | [desert-temples](https://github.com/YUNG-GANG/YUNGs-Better-Desert-Temples) | `0b0f1e1c399dba1c9731946242f7e4ae9dec365a` |
| dungeons | [dungeons](https://github.com/YUNG-GANG/YUNGs-Better-Dungeons) | `631719bfeca523773c01e10016b3361567c44a25` |
| end-island | [end-island](https://github.com/YUNG-GANG/YUNGs-Better-End-Island) | `549ad39efd0761f7c4ca2584d24d5e8ecf922e24` |
| extras | [extras](https://github.com/YUNG-GANG/YUNGs-Extras) | `33c1bde3dee72a310ca545544c1f0ce7aa48691f` |
| fortresses | [fortresses](https://github.com/YUNG-GANG/YUNGs-Better-Fortresses) | `ab41a3ca95801e5cb5e4ac3481c318cbf398acc4` |
| jungle-temples | [jungle-temples](https://github.com/YUNG-GANG/YUNGs-Better-Jungle-Temples) | `b4bd31d18ae8073221fdfddc29e1127372b12ac6` |
| mineshafts | [mineshafts](https://github.com/YUNG-GANG/YUNGs-Better-Mineshafts) | `89399904d42d059e31dd3e0bdf3ab7d3560fab8a` |
| ocean-monuments | [ocean-monuments](https://github.com/YUNG-GANG/YUNGs-Better-Ocean-Monuments) | `2174d15ed87ac9eb654dfd93dc3744d1288712df` |
| strongholds | [strongholds](https://github.com/YUNG-GANG/YUNGs-Better-Strongholds) | `7da28e7054cfc9cec33ff2b85de89c5ec938deca` |
| witch-huts | [witch-huts](https://github.com/YUNG-GANG/YUNGs-Better-Witch-Huts) | `9dae4e7ab320c98fd5925d97e59b87c39f278483` |

All snapshots use the upstream `26.1.2` branch. Ported source and resources are under `plugin`; each source import retains its origin notice. Changes replace loader registration, Mixin accessors, entity processors, terrain adaptations and runtime hooks with Paper bootstrap, version-specific native adapters and Bukkit events. The port adds per-world generation controls. Web management is provided by a separate LunarCore bridge module. Optional Create and other mod-only integrations are not enabled.

所有快照来自上游 `26.1.2` 分支。适配源码与资源位于 `plugin`。改动涉及启动注册、26.3 数据格式、结构处理器、原生地形采样、实体与龙战行为、逐世界设置；Web 管理由独立的 LunarCore 桥接模块提供。不会打包 Fabric、NeoForge 或第三方模组运行时；依赖其他模组的可选内容不启用。原版客户端使用 Minecraft 自带方块和实体显示结构。

The distribution includes the original license files, pinned-source manifests, original snapshots, modified source and build files. Minecraft/Paper development and test binaries are not redistributed in the source bundle.

发行包应同时提供完整对应源码、构建文件、固定来源清单和许可证。测试用 Minecraft/Paper 二进制与存档不属于源码发行内容。
