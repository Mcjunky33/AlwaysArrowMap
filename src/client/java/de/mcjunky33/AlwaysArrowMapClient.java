package de.mcjunky33;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class AlwaysArrowMapClient implements ClientModInitializer {

    // ==========================
    // Dimension-Tracker
    // ==========================
    // WeakHashMap verhindert Memory-Leaks bei MapState
    private static final Map<MapState, RegistryKey<World>> dimensionData = new WeakHashMap<>();

    @Override
    public void onInitializeClient() {
    }

    public static void setDimension(MapState map, RegistryKey<World> dimension) {
        if (map != null && dimension != null) {
            dimensionData.put(map, dimension);
        }
    }

    public static RegistryKey<World> getDimension(MapState map) {
        return dimensionData.get(map);
    }

    // ==========================
    // Rotation (CRASH-SAFE)
    // ==========================
    private static final float DEG = 22.5f;
    private static final byte[] ROT = new byte[360];

    static {
        for (int i = 0; i < 360; i++) {
            ROT[i] = (byte) (Math.round(i / DEG) & 15);
        }
    }


    public static byte calculateRotation(float yaw) {
        int index = Math.floorMod(Math.round(yaw), 360);
        return ROT[index];
    }
}
