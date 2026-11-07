package com.lunarboreal.structures;

import com.lunarboreal.structures.config.ConfigStore;
import com.lunarboreal.structures.config.StructuresConfig;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

/** Public v1 API. status() must run on the server thread; config methods are thread safe.
 * Values contain only JDK maps, lists, strings, numbers and booleans. No live mutable state escapes.
 * Saving never changes the running generator. A full restart activates saved rules.
 */
public final class StructuresManagementApi {
    private final ConfigStore store;
    private final StructuresConfig active;
    private final Supplier<Map<String, Object>> status;

    StructuresManagementApi(ConfigStore store, StructuresConfig active, Supplier<Map<String, Object>> status) {
        this.store = store;
        this.active = active;
        this.status = status;
    }

    public int apiVersion() { return 1; }
    public Map<String, Object> status() { return status.get(); }
    public Map<String, Object> readConfig() { return store.saved().map(); }
    public Map<String, Object> saveConfig(Map<String, Object> requested) throws IOException {
        var saved = store.save(StructuresConfig.parse(requested));
        return Map.of("ok", true, "saved", saved.map(), "restartRequired", !saved.sameRules(active));
    }
}
