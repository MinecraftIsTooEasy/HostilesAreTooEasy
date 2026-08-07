package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityAIFleeAttackerOrPanic.class)
public class MixinEntityAIFleeAttackerOrPanic {

    @Shadow private int panic_countdown;
    @Shadow public void startPanicking() {}

    @Inject(method = "isPanicking", at = @At("HEAD"))
    private void isPanicking_fearItemTool(CallbackInfoReturnable<Boolean> cir) {
        EntityAIFleeAttackerOrPanic task = (EntityAIFleeAttackerOrPanic) (Object) this;
        EntityCreature owner = (EntityCreature) task.task_owner;

        if (this.panic_countdown <= 0 && owner != null && owner.worldObj != null) {

            AxisAlignedBB checkArea = owner.boundingBox.expand(8.0D, 4.0D, 8.0D);

            List<EntityLivingBase> nearbyEntities = owner.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, checkArea);

            if (nearbyEntities != null) {
                for (EntityLivingBase entity : nearbyEntities) {
                    if (entity == owner) continue;

                    ItemStack heldItem = entity.getHeldItemStack();
                    if (heldItem != null && heldItem.getItem() != null) {
                        Item item = heldItem.getItem();

                        if (item instanceof ItemTool) {
                            if (owner.hasLineOfStrike(entity)) {
                                this.startPanicking();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
