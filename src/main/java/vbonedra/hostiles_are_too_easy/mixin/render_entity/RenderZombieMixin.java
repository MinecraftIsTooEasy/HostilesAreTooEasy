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
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasAnyCelestialType;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;

@Mixin(RenderZombie.class)
public abstract class RenderZombieMixin {
    @Unique private static final ResourceLocation PHASE_RL = new ResourceLocation("textures/entity/spider/phase_spider.png");
    @Unique private static final ResourceLocation PLAGUE_RL = new ResourceLocation("textures/items/spider_eye_fermented.png");

    @Inject(method = "func_110863_a(Lnet/minecraft/EntityZombie;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110863_a(EntityZombie par1EntityZombie, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
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
        if (typeObject == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityZombie);
        if (!hasAnyCelestialType(celestialType)) return;

        boolean isPhase = hasCelestialType(celestialType, CelestialZombie.PHASE.id);
        boolean isPlague = hasCelestialType(celestialType, CelestialZombie.PLAGUE.id);
        int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntityZombie);
        byte plagueSubtype = (byte) (celestialSubtype & 0xFF);

        if (isPlague) {
            if (!CelestialSubtypeCache.clientCelestialSubtypeMap.containsKey(entityId)) {
                if (!CelestialSubtypeCache.requestedEntities.contains(entityId) && par1EntityZombie.worldObj.isRemote) {
                    Network.sendToServer(new C2SRequestCelestialSubtype(entityId));
                    CelestialSubtypeCache.requestedEntities.add(entityId);
                }
                return;
            }
            if (CelestialSubtypeCache.clientCelestialSubtypeMap.get(entityId) == null) return;
        }

        String templateTexture = cir.getReturnValue().getResourcePath();

        StringBuilder sb = new StringBuilder(templateTexture);
        if (isPhase) sb.append("_").append(PHASE_RL.getResourcePath());
        if (isPlague) sb.append("_plague_").append(plagueSubtype);
        String cacheKeyFinal = sb.toString();

        if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
            cir.setReturnValue(cacheResourceLocation.get(cacheKeyFinal));
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                templateTexture,
                cacheKeyFinal,
                (baseImg) -> {
                    BufferedImage current = baseImg;

                    if (isPhase) {
                        BufferedImage mask = ImageIO.read(mc.getResourceManager().getResource(PHASE_RL).getInputStream());
                        current = TexturePacker.maskTemplateWithPixelSourceByBrightness(mask, current, 1.0F, 0.0F, -1, false);
                    }

                    if (isPlague) {
                        BufferedImage mask = ImageIO.read(mc.getResourceManager().getResource(PLAGUE_RL).getInputStream());
                        current = TexturePacker.maskTemplateWithPixelSourceByPattern(mask, current, 1.0F, 1.0F, plagueSubtype, 0.15F);
                    }

                    return current;
                }
        );

        if (dynamicResource != null) {
            cir.setReturnValue(dynamicResource);
        }
    }
}
