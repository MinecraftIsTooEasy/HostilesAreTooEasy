package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityArachnid.class)
public abstract class EntityArachnidMixin extends EntityMob {
    @Shadow public abstract boolean peacefulDuringDay();
    public EntityArachnidMixin(World world) {
        super(world);
    }

    @Inject(method = "findNonPlayerToAttack(F)Lnet/minecraft/Entity;", at = @At("RETURN"), cancellable = true)
    private void findNonPlayerToAttack_addEntityIronGolem(float max_distance, CallbackInfoReturnable<Entity> cir) {
        if (this.peacefulDuringDay() && this.getBrightness(1.0F) > 0.5F && this.isOutdoors()) {
            return;
        }
        if (cir.getReturnValue() == null) {
            AxisAlignedBB searchBox = this.boundingBox.expand(max_distance, max_distance / 4.0F, max_distance);

            Entity targetGolem = this.worldObj.findNearestSeenEntityWithinAABB(
                    EntityIronGolem.class,
                    searchBox,
                    this,
                    this.getEntitySenses()
            );
            if (targetGolem != null) {
                cir.setReturnValue(targetGolem);
            }
        }
    }
}
