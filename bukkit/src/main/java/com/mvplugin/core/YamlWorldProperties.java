package com.mvplugin.core;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.dumptruckman.minecraft.pluginbase.properties.PropertySerializer;
import com.dumptruckman.minecraft.pluginbase.properties.YamlProperties;
import com.mvplugin.core.world.WorldProperties;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML implementation of {@link WorldProperties} which will store all the world properties in a
 * YAML file named after the world.
 */
class YamlWorldProperties extends YamlProperties implements WorldProperties {

    YamlWorldProperties(@NotNull final File configFile) throws IOException {
        super(false, true, configFile, WorldProperties.class);
    }

    @Override
    protected void registerSerializers() {
        super.registerSerializers();
        setPropertySerializer(FacingCoordinates.class, new FacingCoordinatesSerializer());
    }

    private static class FacingCoordinatesSerializer implements PropertySerializer<FacingCoordinates> {
        @NotNull
        @Override
        public FacingCoordinates deserialize(Object o) {
            double x = 0D;
            double y = 0D;
            double z = 0D;
            float pitch = 0F;
            float yaw = 0F;
            if (o instanceof ConfigurationSection) {
                o = ((ConfigurationSection) o).getValues(false);
            }
            if (o instanceof Map) {
                Map map = (Map) o;
                try {
                    x = Double.valueOf(map.get("x").toString());
                } catch (Exception ignore) { }
                try {
                    y = Double.valueOf(map.get("y").toString());
                } catch (Exception ignore) { }
                try {
                    z = Double.valueOf(map.get("z").toString());
                } catch (Exception ignore) { }
                try {
                    pitch = Float.valueOf(map.get("pitch").toString());
                } catch (Exception ignore) { }
                try {
                    yaw = Float.valueOf(map.get("yaw").toString());
                } catch (Exception ignore) { }
            }
            return Locations.getFacingCoordinates(x, y, z, pitch, yaw);
        }

        @NotNull
        @Override
        public Object serialize(@NotNull final FacingCoordinates facingCoordinates) {
            Map<String, Object> result = new LinkedHashMap<String, Object>(6);
            result.put("x", facingCoordinates.getX());
            result.put("y", facingCoordinates.getY());
            result.put("z", facingCoordinates.getZ());
            result.put("pitch", facingCoordinates.getPitch());
            result.put("yaw", facingCoordinates.getYaw());
            return result;
        }
    }
}
