package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAINearestAttackableTarget.class)
public class EntityAINearestAttackableTargetMixin {

    @Inject(method = "<init>(Lnet/minecraft/EntityCreature;Ljava/lang/Class;IZZLnet/minecraft/IEntitySelector;)V", at = @At("RETURN"))
    private void init_addGolemTarget(EntityCreature par1EntityCreature, Class par2Class, int par3, boolean par4, boolean par5, IEntitySelector par6IEntitySelector, CallbackInfo ci) {
        if (par2Class == net.minecraft.EntityPlayer.class && !(par1EntityCreature instanceof EntityIronGolem)) {
            if (par1EntityCreature.targetTasks != null) {
                par1EntityCreature.targetTasks.addTask(3, new EntityAINearestAttackableTarget(par1EntityCreature, EntityIronGolem.class, par3, par4, par5, par6IEntitySelector));
            }
        }
    }
}
