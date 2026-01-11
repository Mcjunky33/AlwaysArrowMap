package de.mcjunky33.mixin;

import de.mcjunky33.AlwaysArrowMapClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.map.MapDecoration;
import net.minecraft.item.map.MapDecorationTypes;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(MapState.class)
public abstract class MapStateMixin {

    @Shadow @Final @Mutable
    private boolean unlimitedTracking;


    // ==========================
    // UnlimitedTracking + Dimension setzen
    // ==========================
    @Inject(method="<init>", at=@At("RETURN"))
    private void initTracking(CallbackInfo ci) {
        this.unlimitedTracking = true;
        AlwaysArrowMapClient.setDimension((MapState)(Object)this, ((MapState)(Object)this).dimension);
    }

    // ==========================
    // Off-Map Pfeile / Distance + Banner-Test
    // ==========================
    @Inject(method="getDecorations", at=@At("RETURN"), cancellable=true)
    private void alwaysArrow(CallbackInfoReturnable<Iterable<MapDecoration>> cir) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        MapState map = (MapState)(Object)this;
        double px = client.player.getX();
        double pz = client.player.getZ();
        byte rot = AlwaysArrowMapClient.calculateRotation(client.player.getYaw());

        List<MapDecoration> out = new ArrayList<>();
        Iterable<MapDecoration> original = cir.getReturnValue();



        // ==========================
        // Off-Map Dekorationen umwandeln + Distance anzeigen
        // ==========================
        for (MapDecoration d : original) {
            if (d.type() == MapDecorationTypes.PLAYER_OFF_MAP || d.type() == MapDecorationTypes.PLAYER_OFF_LIMITS) {
                out.add(new MapDecoration(
                        MapDecorationTypes.PLAYER,
                        d.x(), d.z(),
                        rot,
                        Optional.empty()
                ));
            } else {
                out.add(d);
            }
        }

        cir.setReturnValue(out);
    }

    // ==========================
    // Dimension-Check (Nether->Overworld)
    // ==========================
    @Redirect(method="getPlayerMarkerRotation",
            at=@At(value="FIELD", target="Lnet/minecraft/item/map/MapState;dimension:Lnet/minecraft/registry/RegistryKey;"))
    private RegistryKey<World> redirectDimension(MapState instance) {
        return instance.dimension == World.NETHER ? World.OVERWORLD : instance.dimension;
    }
}
