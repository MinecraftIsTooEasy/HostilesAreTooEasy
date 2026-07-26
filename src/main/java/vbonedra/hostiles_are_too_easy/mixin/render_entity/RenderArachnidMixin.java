package vbonedra.hostiles_are_too_easy.mixin.render_entity;


import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import moddedmite.rustedironcore.network.Network;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.util.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.util.HashMap;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.util.ICelestialType.celestialTypeArachnidWarp;


@Mixin(RenderArachnid.class)
public abstract class RenderArachnidMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
    @Unique String warpTexture = "textures/items/shards/obsidian.png";

    @Inject(method = "getArachnidTextures(Lnet/minecraft/EntityArachnid;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getArachnidTextures(EntityArachnid par1EntityArachnid, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntityArachnid.worldObj == null) return;

        int entityId = par1EntityArachnid.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && par1EntityArachnid.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = typeObject;

        if (par1EntityArachnid.isDead) {
            CelestialTypeCache.clientCelestialTypeMap.remove(entityId);
            CelestialTypeCache.requestedEntities.remove(entityId);
        }

        if (celestialType == celestialTypeArachnidWarp) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = warpTexture + "_" + templateTexture;
            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        warpTexture,
                        templateTexture,
                        3.0F,
                        0.0F
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
