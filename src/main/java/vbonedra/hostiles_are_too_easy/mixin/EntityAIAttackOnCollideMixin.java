package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.EntityAIAttackOnCollide;
import net.minecraft.EntityCreature;
import net.minecraft.EntityIronGolem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAIAttackOnCollide.class)
public class EntityAIAttackOnCollideMixin {

    @Inject(method = "<init>(Lnet/minecraft/EntityCreature;Ljava/lang/Class;DZ)V", at = @At("RETURN"))
    private void init_addGolemTarget(EntityCreature par1EntityCreature, Class par2Class, double par3, boolean par5, CallbackInfo ci) {
        if (par2Class == net.minecraft.EntityPlayer.class && !(par1EntityCreature instanceof EntityIronGolem)) {
            if (par1EntityCreature.tasks != null) {
                EntityAIAttackOnCollide golemAttackTask = new EntityAIAttackOnCollide(par1EntityCreature, EntityIronGolem.class, par3, par5);
                par1EntityCreature.tasks.addTask(3, golemAttackTask);
            }
        }
    }
}
