package com.mvplugin.core.world.validators;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.messages.Message;
import pluginbase.properties.PropertyValidator;

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
