package com.lunarboreal.structures;

import com.lunarboreal.structures.config.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class StructuresManagementApiTest {
    @TempDir Path temporary;
    @Test void savesPendingRulesWithoutChangingActiveGenerationAndSurvivesRestart() throws Exception {
        Path path = temporary.resolve("structures.json");
        var store = new ConfigStore(path);
        var active = store.saved();
        var api = new StructuresManagementApi(store, active, () -> Map.of("active", active.map()));
        assertEquals(1, api.apiVersion());
        var flags = new LinkedHashMap<String, Object>();
        for (var family : StructureFamily.values()) flags.put(family.id, family == StructureFamily.DUNGEONS);
        var requested = new LinkedHashMap<>(api.readConfig());
        requested.put("defaults", Map.of("structures", flags));
        requested.put("worlds", Map.of("minecraft:overworld", Map.of("dungeons", false)));
        var result = api.saveConfig(requested);
        assertEquals(true, result.get("restartRequired"));
        assertEquals(1L, api.readConfig().get("revision"));
        assertFalse(active.defaults().enabled(StructureFamily.DUNGEONS));
        assertEquals(active.map(), api.status().get("active"));
        flags.put("dungeons", false);
        assertTrue(store.saved().defaults().enabled(StructureFamily.DUNGEONS));
        assertFalse(store.saved().forWorld("minecraft:overworld").enabled(StructureFamily.DUNGEONS));
        assertTrue(store.saved().forWorld("minecraft:another").enabled(StructureFamily.DUNGEONS));
        assertThrows(IllegalArgumentException.class, () -> api.saveConfig(requested));
        var restarted = new ConfigStore(path);
        assertEquals(store.saved(), restarted.saved());
        var nextApi = new StructuresManagementApi(restarted, restarted.saved(), Map::of);
        assertEquals(false, nextApi.saveConfig(nextApi.readConfig()).get("restartRequired"));
    }
    @Test void invalidInputLeavesTheStoredConfigurationIntact() throws Exception {
        var store = new ConfigStore(temporary.resolve("structures.json"));
        var api = new StructuresManagementApi(store, store.saved(), Map::of);
        var before = api.readConfig();
        assertThrows(IllegalArgumentException.class, () -> api.saveConfig(Map.of("schema", 2)));
        assertEquals(before, api.readConfig());
    }
}
