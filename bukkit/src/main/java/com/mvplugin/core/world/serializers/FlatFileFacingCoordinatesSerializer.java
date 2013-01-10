package com.mvplugin.core.world.serializers;

import com.dumptruckman.minecraft.pluginbase.minecraft.location.FacingCoordinates;
import com.dumptruckman.minecraft.pluginbase.minecraft.location.Locations;
import com.dumptruckman.minecraft.pluginbase.properties.PropertySerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class FlatFileFacingCoordinatesSerializer implements PropertySerializer<FacingCoordinates> {
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
