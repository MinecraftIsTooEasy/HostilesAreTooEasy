package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.Block;
import net.minecraft.DamageSource;
import net.minecraft.Entity;
import net.minecraft.EntitySilverfish;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isImmuneTo(Lnet/minecraft/DamageSource;)Z", at = @At("RETURN"), cancellable = true)
    private void isImmuneTo(DamageSource damage_source, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof EntitySilverfish) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();

            if (celestialType == 0 || damage_source == null) return;

            Block associatedBlock = Block.getBlock(celestialType);
            if (associatedBlock == null) return;

            ItemStack item_stack = damage_source.getItemAttackedWith();
            if (item_stack != null && item_stack.getItem() instanceof ItemTool) {
                cir.setReturnValue(!item_stack.getItemAsTool().isEffectiveAgainstBlock(associatedBlock, 0));
                return;
            }

            cir.setReturnValue(true);
        }
    }
}
