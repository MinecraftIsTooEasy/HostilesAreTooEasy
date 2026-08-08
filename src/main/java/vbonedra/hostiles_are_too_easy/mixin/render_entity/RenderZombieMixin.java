package vbonedra.hostiles_are_too_easy.mixin.render_entity;


import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import moddedmite.rustedironcore.network.Network;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialSubtype;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.network.CelestialSubtypeGetter;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialSubtypeCache;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.util.HashMap;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.celestialTypeZombiePhase;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.celestialTypeZombiePlague;


@Mixin(RenderZombie.class)
public abstract class RenderZombieMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
    @Unique String phaseTexture = "textures/entity/spider/phase_spider.png";
    @Unique String plagueTexture = "textures/items/spider_eye_fermented.png";


    @Inject(method = "func_110863_a(Lnet/minecraft/EntityZombie;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110860_a(EntityZombie par1EntityZombie, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntityZombie.worldObj == null) return;

        int entityId = par1EntityZombie.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && par1EntityZombie.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityZombie);

        if (celestialType == celestialTypeZombiePhase) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = phaseTexture + "_" + templateTexture;
            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        phaseTexture,
                        templateTexture,
                        3.0F,
                        0.0F
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
        else if (celestialType == celestialTypeZombiePlague) {
            if (!CelestialSubtypeCache.clientCelestialSubtypeMap.containsKey(entityId)) {
                if (!CelestialSubtypeCache.requestedEntities.contains(entityId) && par1EntityZombie.worldObj.isRemote) {
                    Network.sendToServer(new C2SRequestCelestialSubtype(entityId));
                    CelestialSubtypeCache.requestedEntities.add(entityId);
                }
                return;
            }

            Integer subtypeObject = CelestialSubtypeCache.clientCelestialSubtypeMap.get(entityId);
            if (subtypeObject == null) {
                return;
            }

            int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntityZombie);


            String templateTexture = cir.getReturnValue().getResourcePath();
            byte patternId = (byte) (celestialSubtype & 0xFF);
            String cacheKey = "plague_" + patternId + "_" + templateTexture;
            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByPattern(
                        plagueTexture,
                        templateTexture,
                        1.0F,
                        1.0F,
                        patternId,
                        0.15F
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
