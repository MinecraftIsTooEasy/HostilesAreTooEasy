package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import moddedmite.rustedironcore.network.Network;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

import java.util.HashMap;
import java.util.Map;

@Mixin(RenderShadow.class)
public abstract class RenderShadowMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();

    @Unique String gloomTexture = "#1f001f";

    @Inject(method = "getEntityTexture(Lnet/minecraft/Entity;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getEntityTexture(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (!(par1Entity instanceof EntityShadow shadow)) {
            return;
        }

        if (shadow.worldObj == null) {
            return;
        }

        int entityId = shadow.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && shadow.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = CelestialTypeGetter.getCelestialType(shadow);


        if (celestialType == ICelestialType.celestialTypeShadowGloom) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = gloomTexture + "_" + templateTexture;

            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.blendHexColorOnTexture(
                        templateTexture,
                        gloomTexture,
                        1.0F,
                        1.0F
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
