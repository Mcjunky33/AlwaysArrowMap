package de.mcjunky33;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class AlwaysArrowMapClient implements ClientModInitializer {

    private static boolean showDistance = true;
    public static KeyBinding toggleDistance;


    // Dimension-Tracker
    private static final Map<MapState, RegistryKey<World>> dimensionData = new HashMap<>();

    @Override
    public void onInitializeClient() {
    }

    // ==========================
    // Dimension-Tracker
    // ==========================
    public static void setDimension(MapState map, RegistryKey<World> dimension) {
        dimensionData.put(map, dimension);
    }

    public static RegistryKey<World> getDimension(MapState map) {
        return dimensionData.get(map);
    }


    // ==========================
    // Rotation
    // ==========================
    private static final float DEG = 22.5f;
    private static final byte[] ROT = new byte[360];
    static { for (int i=0;i<360;i++) ROT[i]=(byte)(Math.round(i/DEG)&15); }
    public static byte calculateRotation(float yaw) { return ROT[((int)yaw+360)%360]; }

}
