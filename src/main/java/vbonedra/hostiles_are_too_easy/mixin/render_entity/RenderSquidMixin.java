package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.visual_util.ResourcePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.*;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;

@Mixin(RenderSquid.class)
public abstract class RenderSquidMixin {

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

        if (hasCelestialType(celestialType, CelestialShadow.GLOOM.id)) {
            String templateTexture = cir.getReturnValue().getResourcePath();

            String cacheKeyFinal = new StringBuilder("squid_")
                    .append(templateTexture)
                    .toString();

            if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
                cir.setReturnValue(cacheResourceLocation.get(cacheKeyFinal));
                return;
            }

            ResourceLocation finalBlendedTexture = ResourcePacker.oneBlackTransparentPixelResourceLocation;
            cacheResourceLocation.put(cacheKeyFinal, finalBlendedTexture);
            cir.setReturnValue(finalBlendedTexture);
        }
    }
}
