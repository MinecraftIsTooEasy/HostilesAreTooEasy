package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

@Mixin(EntityInvisibleStalker.class)
public abstract class EntityInvisibleStalkerMixin extends EntityLiving implements ICelestialType {
    public EntityInvisibleStalkerMixin(World world) {
        super(world);
    }

    @Inject(method = "onLivingUpdate", at = @At("RETURN"))
    public void onLivingUpdate(CallbackInfo ci) {
        if (this.HATE$getCelestialType() == ICelestialType.celestialTypeInvisibleStalkerMirror) {
            EntityLivingBase target = this.getTarget();
            if (target != null) {
                for (int slot = 0; slot <= 4; slot++) {
                    ItemStack targetEquipment = target.getCurrentItemOrArmor(slot);
                    ItemStack currentEquipment = this.getCurrentItemOrArmor(slot);

                    if (targetEquipment != null) {
                        if (currentEquipment == null || currentEquipment.getItem() != targetEquipment.getItem()) {
                            this.setCurrentItemOrArmor(slot, targetEquipment.copy());
                        }
                    } else {
                        if (currentEquipment != null) {
                            this.setCurrentItemOrArmor(slot, null);
                        }
                    }
                }
            } else {
                for (int slot = 0; slot <= 4; slot++) {
                    if (this.getCurrentItemOrArmor(slot) != null) {
                        this.setCurrentItemOrArmor(slot, null);
                    }
                }
            }
        }
    }
}
