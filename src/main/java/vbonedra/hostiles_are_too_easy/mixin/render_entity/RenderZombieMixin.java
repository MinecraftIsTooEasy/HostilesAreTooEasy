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

import java.util.HashMap;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.celestialTypeZombiePhase;


@Mixin(RenderZombie.class)
public abstract class RenderZombieMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
    @Unique String phaseTexture = "textures/entity/spider/phase_spider.png";
    @Unique String bloodyTexture3 = "textures/items/ruby.png";
    @Unique String bloodyTexture2 = "textures/items/spider_eye_fermented.png";
    @Unique String bloodyTexture = "textures/entity/creeper/infernal_creeper.png";


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
    }
}
