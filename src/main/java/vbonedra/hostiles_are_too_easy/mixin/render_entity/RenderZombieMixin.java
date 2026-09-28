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
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.*;


import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasAnyCelestialType;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;


@Mixin(RenderZombie.class)
public abstract class RenderZombieMixin {
    @Unique String phaseTexture = "textures/entity/spider/phase_spider.png";
    @Unique String plagueTexture = "textures/items/spider_eye_fermented.png";


    @Inject(method = "func_110863_a(Lnet/minecraft/EntityZombie;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110863_a(EntityZombie par1EntityZombie, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntityZombie.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

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
        int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntityZombie);

        if (hasAnyCelestialType(celestialType)) {
            System.out.println("hasAnyCelestialType");
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = templateTexture;

            boolean phase = false;
            if (hasCelestialType(celestialType, CelestialZombie.PHASE.id)) {
                phase = true;
                cacheKey += "_" + phaseTexture;
            }
            boolean plague = false;
            if (hasCelestialType(celestialType, CelestialZombie.PLAGUE.id)) {
                plague = true;
                cacheKey += "_plague_" + (byte) (celestialSubtype & 0xFF);
            }
            System.out.println("types: " + phase + " " + plague);

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(templateTexture)).getInputStream());
                if (phase) {
                    bufferedImage = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                            ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(phaseTexture)).getInputStream()),
                            bufferedImage,
                            3.0F,
                            0.0F,
                            -1,
                            false
                    );
                }
                if (plague) {
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

                    bufferedImage = TexturePacker.maskTemplateWithPixelSourceByPattern(
                            ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(plagueTexture)).getInputStream()),
                            bufferedImage,
                            1.0F,
                            1.0F,
                            (byte) (celestialSubtype & 0xFF),
                            0.15F
                    );
                }

                DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
                ResourceLocation dynamicLocation = mc.getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
                cacheResourceLocation.put(cacheKey, dynamicLocation);

            }

            cir.setReturnValue(cacheResourceLocation.get(cacheKey));
        }
    }
}
