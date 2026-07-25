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

@Mixin(RenderSilverfish.class)
public abstract class RenderSilverfishMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();

    @Unique
    private String getBlockTexturePath(Block block, int metadata) {
        if (block == null) return "textures/blocks/dirt.png";
        try {
            Icon blockIcon = block.getIcon(1, metadata);
            if (blockIcon != null && blockIcon.getIconName() != null) {
                return "textures/blocks/" + blockIcon.getIconName() + ".png";
            }
        } catch (Exception e) {
            System.err.println("Silverfish has hard time getting blockIcon: " + e);
        }
        return "textures/blocks/dirt.png";
    }

    @Inject(method = "getSilverfishTextures(Lnet/minecraft/EntitySilverfish;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getSilverfishTextures(EntitySilverfish par1EntitySilverfish, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntitySilverfish.worldObj == null) return;

        int entityId = par1EntitySilverfish.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && par1EntitySilverfish.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = typeObject;

        if (par1EntitySilverfish.isDead) {
            CelestialTypeCache.clientCelestialTypeMap.remove(entityId);
            CelestialTypeCache.requestedEntities.remove(entityId);
        }

        if (celestialType > 0) {
            Block block = Block.getBlock(celestialType);
            if (block == null) {
                block = Block.dirt;
            }

            int metadata = 0;
            int blockX = MathHelper.floor_double(par1EntitySilverfish.posX);
            int blockY = MathHelper.floor_double(par1EntitySilverfish.posY);
            int blockZ = MathHelper.floor_double(par1EntitySilverfish.posZ);

            int biomeTint = -1;
            int blockColor = block.colorMultiplier(par1EntitySilverfish.worldObj, blockX, blockY, blockZ);
            if (blockColor != 0xFFFFFF) {
                biomeTint = blockColor;
            }

            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = block.blockID + "_" + metadata + "_" + biomeTint + "_" + templateTexture;

            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithBlock(
                        getBlockTexturePath(block, metadata),
                        templateTexture,
                        3.0F,
                        0.0F,
                        biomeTint
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
