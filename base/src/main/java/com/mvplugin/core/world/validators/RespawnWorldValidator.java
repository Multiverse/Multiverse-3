package com.mvplugin.core.world.validators;

import com.mvplugin.core.MultiverseCoreAPI;
import com.mvplugin.core.util.PropertyDescriptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.field.PropertyVetoException;
import pluginbase.config.field.Validator;
import pluginbase.messages.Message;

public class RespawnWorldValidator implements Validator<String> {

    @NotNull
    private final MultiverseCoreAPI api;

    public RespawnWorldValidator(@NotNull final MultiverseCoreAPI api) {
        this.api = api;
    }

    @Nullable
    @Override
    public String validateChange(@Nullable String s, @Nullable String s2) throws PropertyVetoException {
        if (s == null || !api.getWorldManager().isLoaded(s)) {
            throw new PropertyVetoException(Message.bundleMessage(PropertyDescriptions.INVALID_RESPAWN_WORLD));
        }
        return s;
    }
}
