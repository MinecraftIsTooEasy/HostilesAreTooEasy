package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.RenderArachnid;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderArachnid.class)
public abstract class RenderArachnidMixin {

//    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
//    @Unique String blackWidowTexture = "textures/entity/spider/black_widow.png";
//
//    @Inject(method = "getArachnidTextures(Lnet/minecraft/EntityArachnid;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
//    private void onGetArachnidTextures(EntityArachnid par1EntityArachnid, CallbackInfoReturnable<ResourceLocation> cir) {
//        if (par1EntityArachnid.worldObj == null) return;
//
//        int entityId = par1EntityArachnid.entityId;
//
//        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
//            if (!CelestialTypeCache.requestedEntities.contains(entityId) && par1EntityArachnid.worldObj.isRemote) {
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
//        int celestialType = typeObject;
//
//        if (par1EntityArachnid.isDead) {
//            CelestialTypeCache.clientCelestialTypeMap.remove(entityId);
//            CelestialTypeCache.requestedEntities.remove(entityId);
//        }
//
//        if (celestialType == 1) {
//            String templateTexture = cir.getReturnValue().getResourcePath();
//            String cacheKey = blackWidowTexture + "_" + templateTexture;
//
//            if (!blendedCache.containsKey(cacheKey)) {
//                ResourceLocation finalBlendedTexture = TexturePacker.blendTextures(
//                        blackWidowTexture,
//                        templateTexture,
//                        2, 1
//                );
//                blendedCache.put(cacheKey, finalBlendedTexture);
//            }
//
//            cir.setReturnValue(blendedCache.get(cacheKey));
//        }
//    }
}
