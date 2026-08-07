package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

import java.util.HashMap;
import java.util.Map;

@Mixin(RenderSquid.class)
public abstract class RenderSquidMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();

    @Inject(method = "getEntityTexture(Lnet/minecraft/Entity;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getEntityTexture(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (!(par1Entity instanceof EntitySquid squid)) {
            return;
        }

        if (squid.worldObj == null) {
            return;
        }

        int entityId = squid.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && squid.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = CelestialTypeGetter.getCelestialType(squid);

        if (celestialType == ICelestialType.celestialTypeShadowGloom) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = "squid_" + templateTexture;

            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.getEmptyTransparentTexture();
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
