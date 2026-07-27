package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

@Mixin(EntityGhoul.class)
public abstract class EntityGhoulMixin extends EntityAnimalWatcher implements ICelestialType {
    public EntityGhoulMixin(World world) {
        super(world);
    }

    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    public void attackEntityAsMob(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        if (this.HATE$getCelestialType() == ICelestialType.celestialTypeGhoulVampire) {
            EntityDamageResult result = cir.getReturnValue();
            if (result != null) {
                if (((EntityLivingBase) target).isEntityBiologicallyAlive()) {
                    float health = this.getHealth();
                    if (health > 0.0F) {
                        if (health < this.getMaxHealth()) {
                            this.setHealth(health + Math.min(1.0F, result.getAmountOfHealthLost()));
                        }
                    }
                }
            }
        }
    }
}
