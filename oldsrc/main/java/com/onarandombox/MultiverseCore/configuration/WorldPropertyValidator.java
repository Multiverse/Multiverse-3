package com.onarandombox.MultiverseCore.configuration;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.event.MVWorldPropertyChangeEvent;
import me.main__.util.SerializationConfig.ChangeDeniedException;
import me.main__.util.SerializationConfig.ObjectUsingValidator;
import org.bukkit.Bukkit;

/**
 * Validates world-property-changes.
 * @param <T> The type of the property that should be validated.
 */
public class WorldPropertyValidator<T> extends ObjectUsingValidator<T, MVWorld> {
    /**
     * {@inheritDoc}
     */
    @Override
    public T validateChange(String property, T newValue, T oldValue, MVWorld object) throws ChangeDeniedException {
        MVWorldPropertyChangeEvent<T> event = new MVWorldPropertyChangeEvent<T>(object, null, property, newValue);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            throw new ChangeDeniedException();
        return event.getTheNewValue();
    }
}
