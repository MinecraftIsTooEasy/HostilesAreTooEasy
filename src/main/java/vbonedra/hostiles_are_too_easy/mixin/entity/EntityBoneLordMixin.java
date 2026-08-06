package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.llamalad7.mixinextras.sugar.Local;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

@Mixin(EntityBoneLord.class)
public abstract class EntityBoneLordMixin extends EntitySkeleton {
    public EntityBoneLordMixin(World world) {
        super(world);
    }

    @Inject(method = "trySummonTroop(Lnet/minecraft/EntityLivingBase;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;spawnEntityInWorld(Lnet/minecraft/Entity;)Z"))
    private void trySummonTroop_celestialTypeShare(EntityLivingBase target, CallbackInfoReturnable<Integer> cir, @Local EntitySkeleton skeleton) {
        if (skeleton != null) {
            ((ICelestialType) skeleton).HATE$setCelestialType(((ICelestialType) this).HATE$getCelestialType());
            skeleton.reapplyEntityAttributes();
            skeleton.setHealth(skeleton.getMaxHealth());
        }
    }
}
