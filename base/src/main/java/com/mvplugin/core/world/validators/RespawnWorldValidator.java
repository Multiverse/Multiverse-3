package com.mvplugin.core.world.validators;

import com.dumptruckman.minecraft.pluginbase.messaging.Message;
import com.dumptruckman.minecraft.pluginbase.properties.PropertyValidator;
import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RespawnWorldValidator implements PropertyValidator<String> {

    @NotNull
    private final MultiverseCoreAPI api;

    public RespawnWorldValidator(@NotNull final MultiverseCoreAPI api) {
        this.api = api;
    }

    @Override
    public boolean isValid(@Nullable final String s) {
        return s != null && api.getWorldManager().isLoaded(s);
    }

    @NotNull
    @Override
    public Message getInvalidMessage() {
        return PropertyDescriptions.INVALID_RESPAWN_WORLD;
    }
}
