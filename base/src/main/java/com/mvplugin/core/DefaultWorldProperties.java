package com.mvplugin.core;

import com.mvplugin.core.world.WorldProperties;
import org.jetbrains.annotations.NotNull;
import pluginbase.messages.PluginBaseException;
import pluginbase.properties.ListProperty;
import pluginbase.properties.MappedProperty;
import pluginbase.properties.NestedProperties;
import pluginbase.properties.NestedProperty;
import pluginbase.properties.Observer;
import pluginbase.properties.Properties;
import pluginbase.properties.PropertyValidator;
import pluginbase.properties.SimpleProperty;
import pluginbase.properties.ValueProperty;

import java.util.List;
import java.util.Map;

class DefaultWorldProperties implements WorldProperties {

    @NotNull
    private final Properties properties;

    DefaultWorldProperties(@NotNull Properties properties) {
        this.properties = properties;
    }

    @Override
    public void flush() throws PluginBaseException {
        properties.flush();
    }

    @Override
    public void reload() throws PluginBaseException {
        properties.reload();
    }

    @Override
    public <T> T get(final SimpleProperty<T> tSimpleProperty) {
        return properties.get(tSimpleProperty);
    }

    @Override
    public <T> List<T> get(final ListProperty<T> tListProperty) {
        return properties.get(tListProperty);
    }

    @Override
    public <T> Map<String, T> get(final MappedProperty<T> tMappedProperty) {
        return properties.get(tMappedProperty);
    }

    @Override
    public <T> T get(final MappedProperty<T> tMappedProperty, final String s) {
        return properties.get(tMappedProperty, s);
    }

    @Override
    public NestedProperties get(final NestedProperty nestedProperty) {
        return properties.get(nestedProperty);
    }

    @Override
    public <T> boolean set(final SimpleProperty<T> tSimpleProperty, final T t) {
        return properties.set(tSimpleProperty, t);
    }

    @Override
    public <T> boolean set(final ListProperty<T> tListProperty, final List<T> ts) {
        return properties.set(tListProperty, ts);
    }

    @Override
    public <T> boolean set(final MappedProperty<T> tMappedProperty, final Map<String, T> stringTMap) {
        return properties.set(tMappedProperty, stringTMap);
    }

    @Override
    public <T> boolean set(final MappedProperty<T> tMappedProperty, final String s, final T t) {
        return properties.set(tMappedProperty, s, t);
    }

    @Override
    public <T> void setPropertyValidator(final ValueProperty<T> tValueProperty, final PropertyValidator<T> tPropertyValidator) {
        properties.setPropertyValidator(tValueProperty, tPropertyValidator);
    }

    @Override
    public <T> boolean isValid(final ValueProperty<T> tValueProperty, final T t) {
        return properties.isValid(tValueProperty, t);
    }

    @Override
    public boolean addObserver(@NotNull final Observer observer) {
        return properties.addObserver(observer);
    }

    @Override
    public boolean deleteObserver(@NotNull final Observer observer) {
        return properties.deleteObserver(observer);
    }
}
