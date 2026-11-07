# LunarCoreStructures

Paper 26.3 / Java 25 port of YUNG’s structures, published separately from LunarCore. This preview still **requires LunarCore**, including its `NativeGeneratorDelegate` API. It is not a general-purpose standalone Paper plugin.

[中文](README.zh-CN.md) · [Generation behavior](docs/en-US/STRUCTURES.md) · [Historical generation checks](docs/en-US/STRUCTURES-VALIDATION.md)

## Install

Stop the server. Install the compatible LunarCore build from the bridge PR and `LunarCoreStructures.jar` into `plugins/`. Install `lbr-structures-web.jar` into `plugins/LunarCore/modules/`. Fully restart, then open **Server → Structure upgrades** in LunarCore Web.

Generation and management are two separate JARs. The bridge contains no YUNG’s code or data packs. Disabling/reloading the bridge removes management only; do not hot-disable the generation plugin. Worlds containing this plugin's structure data still require the generation plugin on subsequent loads.

Upgrade from the combined PR build by replacing the old generation JAR, keeping the plugin name and `plugins/LunarCoreStructures/structures.json`. Do not keep two copies of the JAR. No configuration migration is required. Settings affect new chunks only and activate after a full restart. With bridge 0.0.6-preview.1 or later, automatic restart requires a readable restart-script. Without it, Web only prompts for a manual restart and keeps the server running. See the bridge's restart setup guide.

## Build

Use JDK 25. The API is compile-only and is never bundled. Build/publish `lbr-api` from the matching LunarCore bridge branch with `./gradlew :lbr-api:publishToMavenLocal`, then:

```sh
./gradlew :plugin:test :plugin:jar
```

Alternatively pass `-PlunarCoreApi=/absolute/path/lbr-api-0.0.6-preview.jar` (API artifact supplied with the release), or `-PlunarCoreRepository=/absolute/path/to/build/repo`. For an already bootstrapped local Paper 26.3 installation, add `-Plbr.paperServerHome=/absolute/path/to/paper-home`; retain the matching Paper launcher JAR and extracted libraries. Without that property, paperweight resolves the pinned Paper 26.3 build 202 development bundle. Output: `plugin/build/libs/LunarCoreStructures.jar`.

`third-party/yungs/` retains upstream snapshots, source hashes and licenses. Ported source/resources and tests live in `plugin/`. Console-only acceptance tools live in `tools/structures-probe/` and must not be installed on production servers.

## Management API v1

The plugin's public `getManagementApi()` returns an object with public `apiVersion(): int` (1), `status(): Map`, `readConfig(): Map`, and `saveConfig(Map): Map`. Only JDK maps/lists/scalars cross the boundary; callers must resolve the public methods from the actual plugin ClassLoader. `status()` runs on the Bukkit main thread. Configuration operations are synchronized by the store and may run on the request thread.

The existing schema-1 configuration and revision checks are unchanged. `saveConfig` returns `{ok, saved, restartRequired}`; validation/stale revisions throw `IllegalArgumentException`, I/O failure throws `IOException`. Saving never changes active generation. `status` includes active/saved snapshots, world support/errors, families, restartRequired and canRestart. The bridge owns authentication, audit and restart scheduling; this plugin registers no Web routes.

The LC bridge source is maintained at https://github.com/rtiags/LunarCore-LC/tree/codex/yungs-paper-structures/modules/lbr-structures-web .

## License

See LICENSE and THIRD-PARTY-NOTICES.md. Upstream YUNG’s attribution and LGPL license texts are preserved; this independent port is not endorsed by YUNG-GANG.

## Clean build CI

The default development-bundle build passed on a fresh Linux runner without restored caches or a precompiled LC API. See [verification](CLEAN-BUILD.md) for the run, reports and source pin.
