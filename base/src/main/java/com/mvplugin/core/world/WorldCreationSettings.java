package com.mvplugin.core.world;

import com.mvplugin.core.minecraft.WorldEnvironment;
import com.mvplugin.core.minecraft.WorldType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorldCreationSettings {

    @NotNull
    private final String name;
    @Nullable
    private WorldEnvironment env;
    @Nullable
    private Long seed;
    @Nullable
    private WorldType type;
    @Nullable
    private Boolean generateStructures;
    @Nullable
    private String generator;
    private boolean adjustSpawn = true;

    public WorldCreationSettings(@NotNull final String name) {
        this.name = name;
    }

    @NotNull
    public String name() {
        return name;
    }

    @Nullable
    public WorldEnvironment env() {
        return env;
    }

    @Nullable
    public Long seed() {
        return seed;
    }

    @Nullable
    public WorldType type() {
        return type;
    }

    @Nullable
    public Boolean generateStructures() {
        return generateStructures;
    }

    @Nullable
    public String generator() {
        return generator;
    }

    public boolean adjustSpawn() {
        return adjustSpawn;
    }

    @NotNull
    public WorldCreationSettings env(@Nullable final WorldEnvironment e) {
        this.env = e;
        return this;
    }

    @NotNull
    public WorldCreationSettings seed(final Long l) {
        this.seed = l;
        return this;
    }

    @NotNull
    public WorldCreationSettings seed(@Nullable final String seedString) {
        if (seedString != null && !seedString.isEmpty()) {
            try {
                return seed(Long.parseLong(seedString));
            } catch (NumberFormatException numberformatexception) {
                return seed((long) seedString.hashCode());
            }
        }
        return this;
    }

    @NotNull
    public WorldCreationSettings type(@Nullable final WorldType t) {
        this.type = t;
        return this;
    }

    @NotNull
    public WorldCreationSettings generateStructures(final Boolean b) {
        this.generateStructures = b;
        return this;
    }

    @NotNull
    public WorldCreationSettings generator(@Nullable final String g) {
        this.generator = g;
        return this;
    }

    @NotNull
    public WorldCreationSettings adjustSpawn(final boolean a) {
        this.adjustSpawn = a;
        return this;
    }
}
