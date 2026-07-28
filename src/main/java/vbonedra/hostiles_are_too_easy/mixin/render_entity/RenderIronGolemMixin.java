package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.util.HashMap;
import java.util.Map;

@Mixin(RenderIronGolem.class)
public abstract class RenderIronGolemMixin {


    @Unique
    private final Map<String, ResourceLocation> HATE$golemBlendedCache = new HashMap<>();

    @Unique
    private String HATE$getBlockTexturePath(Block block, int metadata) {
        if (block == null) return "textures/blocks/iron_block.png";
        try {
            Icon blockIcon = block.getIcon(1, metadata);
            if (blockIcon != null && blockIcon.getIconName() != null) {
                return "textures/blocks/" + blockIcon.getIconName() + ".png";
            }
        } catch (Exception e) {
            System.err.println("IronGolem has hard time getting blockIcon: " + e);
        }
        return "textures/blocks/iron_block.png";
    }

    @Inject(method = "getIronGolemTextures(Lnet/minecraft/EntityIronGolem;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getCustomMetalGolemTextures(EntityIronGolem golem, CallbackInfoReturnable<ResourceLocation> cir) {
        if (golem.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(golem);

        if (celestialType != 0) {
            Block block = Block.getBlock(celestialType);
            if (block == null) {
                block = Block.blockIron;
            }

            int metadata = 0;
            int blockX = MathHelper.floor_double(golem.posX);
            int blockY = MathHelper.floor_double(golem.posY);
            int blockZ = MathHelper.floor_double(golem.posZ);

            int biomeTint = -1;
            int blockColor = block.colorMultiplier(golem.worldObj, blockX, blockY, blockZ);
            if (blockColor != 0xFFFFFF) {
                biomeTint = blockColor;
            }

            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = block.blockID + "_" + metadata + "_" + biomeTint + "_" + templateTexture;

            if (!HATE$golemBlendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        HATE$getBlockTexturePath(block, metadata),
                        templateTexture,
                        3.0F,
                        0.0F,
                        biomeTint
                );
                HATE$golemBlendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(HATE$golemBlendedCache.get(cacheKey));
        }
    }
}
