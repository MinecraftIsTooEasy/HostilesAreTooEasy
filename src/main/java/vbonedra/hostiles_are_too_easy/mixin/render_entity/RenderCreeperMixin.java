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

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.celestialTypeCreeperMimic;

@Mixin(RenderCreeper.class)
public abstract class RenderCreeperMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
    @Unique private final Map<Integer, Integer> lastStoodBlockMap = new HashMap<>();
    @Unique private final Map<Integer, Integer> lastStoodMetadataMap = new HashMap<>();

    @Unique
    private String getBlockTexturePath(Block block, int metadata) {
        if (block == null) return "textures/blocks/dirt.png";
        try {
            Icon blockIcon = block.getIcon(1, metadata);
            if (blockIcon != null && blockIcon.getIconName() != null) {
                return "textures/blocks/" + blockIcon.getIconName() + ".png";
            }
        } catch (Exception e) {
            System.err.println("Creeper has hard time getting blockIcon: " + e);
        }
        return "textures/blocks/dirt.png";
    }


    @Inject(method = "getCreeperTextures(Lnet/minecraft/EntityCreeper;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getCreeperTextures(EntityCreeper par1EntityCreeper, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntityCreeper.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityCreeper);

        if (celestialType == celestialTypeCreeperMimic) {
            int entityId = par1EntityCreeper.entityId;

            int blockX = MathHelper.floor_double(par1EntityCreeper.posX);
            int blockY = MathHelper.floor_double(par1EntityCreeper.posY);
            int blockZ = MathHelper.floor_double(par1EntityCreeper.posZ);

            int blockId = par1EntityCreeper.worldObj.getBlockId(blockX, blockY, blockZ);
            int metadata = 0;

            if (blockId == 0 || Block.blocksList[blockId] == null) {
                int underBlockY = MathHelper.floor_double(par1EntityCreeper.posY - 0.1D);
                blockId = par1EntityCreeper.worldObj.getBlockId(blockX, underBlockY, blockZ);
                if (blockId != 0) {
                    metadata = par1EntityCreeper.worldObj.getBlockMetadata(blockX, underBlockY, blockZ);
                } else {
                    blockId = par1EntityCreeper.worldObj.getBlockId(blockX, underBlockY - 1, blockZ);
                    if (blockId != 0) {
                        metadata = par1EntityCreeper.worldObj.getBlockMetadata(blockX, underBlockY - 1, blockZ);
                    }
                }
            } else {
                metadata = par1EntityCreeper.worldObj.getBlockMetadata(blockX, blockY, blockZ);
            }

            if (blockId == 0 || Block.blocksList[blockId] == null) {
                if (lastStoodBlockMap.containsKey(entityId)) {
                    blockId = lastStoodBlockMap.get(entityId);
                    metadata = lastStoodMetadataMap.getOrDefault(entityId, 0);
                } else {
                    blockId = Block.dirt.blockID;
                    metadata = 0;
                }
            } else {
                lastStoodBlockMap.put(entityId, blockId);
                lastStoodMetadataMap.put(entityId, metadata);
            }

            int biomeTint = -1;
            Block block = Block.blocksList[blockId];
            if (block != null) {
                int blockColor = block.colorMultiplier(par1EntityCreeper.worldObj, blockX, blockY, blockZ);
                if (blockColor != 0xFFFFFF) {
                    biomeTint = blockColor;
                }
            }

            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = blockId + "_" + metadata + "_" + biomeTint + "_" + templateTexture;

            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        getBlockTexturePath(block, metadata),
                        templateTexture,
                        3.0F,
                        par1EntityCreeper.getClass() == EntityInfernalCreeper.class ? 0.0f : 1.0F,
                        biomeTint
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
