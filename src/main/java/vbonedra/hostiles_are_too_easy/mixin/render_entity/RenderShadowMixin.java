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

@Mixin(RenderShadow.class)
public abstract class RenderShadowMixin {
//
//    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
//
//    @Inject(method = "getEntityTexture(Lnet/minecraft/Entity;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
//    private void getShadowTextures(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) {
//        if (par1Entity.worldObj == null) return;
//
//        int entityId = par1Entity.entityId;
//
//        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
//            if (!CelestialTypeCache.requestedEntities.contains(entityId) && par1Entity.worldObj.isRemote) {
//                Network.sendToServer(new C2SRequestCelestialType(entityId));
//                CelestialTypeCache.requestedEntities.add(entityId);
//            }
//            return;
//        }
//
//        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
//        if (typeObject == null) {
//            return;
//        }
//
//        if (par1Entity.isDead) {
//            CelestialTypeCache.clientCelestialTypeMap.remove(entityId);
//            CelestialTypeCache.requestedEntities.remove(entityId);
//        }
//
//        String templateTexture = cir.getReturnValue().getResourcePath();
//        String spiderTexturePath = "textures/blocks/coal_block.png";
//        String cacheKey = spiderTexturePath + "_" + templateTexture;
//
//        if (!blendedCache.containsKey(cacheKey)) {
//            ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithBlockByBrightness(
//                    spiderTexturePath,
//                    templateTexture,
//                    3.0F,
//                    0.0F,
//                    -1
//            );
//            blendedCache.put(cacheKey, finalBlendedTexture);
//        }
//
//        cir.setReturnValue(blendedCache.get(cacheKey));
//    }
}
