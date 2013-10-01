package com.mvplugin.core.world.serializers;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.serializers.Serializer;
import pluginbase.minecraft.location.FacingCoordinates;
import pluginbase.minecraft.location.Locations;

import java.util.LinkedHashMap;
import java.util.Map;

public class BukkitFacingCoordinatesSerializer implements Serializer<FacingCoordinates> {

    @Nullable
    @Override
    public FacingCoordinates deserialize(@Nullable Object o, @NotNull Class<FacingCoordinates> clazz) {
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

    @Nullable
    @Override
    public Object serialize(@Nullable final FacingCoordinates facingCoordinates) {
        if (facingCoordinates == null) {
            return Locations.NULL_FACING;
        }
        Map<String, Object> result = new LinkedHashMap<String, Object>(6);
        result.put("x", facingCoordinates.getX());
        result.put("y", facingCoordinates.getY());
        result.put("z", facingCoordinates.getZ());
        result.put("pitch", facingCoordinates.getPitch());
        result.put("yaw", facingCoordinates.getYaw());
        return result;
    }
}
