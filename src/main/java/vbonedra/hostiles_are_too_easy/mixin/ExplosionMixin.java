package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.Entity;
import net.minecraft.EntityCreeper;
import net.minecraft.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {

    @Shadow public boolean isFlaming;
    @Shadow public Entity exploder;

    @Inject(method = "doExplosionA()V", at = @At("HEAD"))
    private void onDoExplosionA(CallbackInfo ci) {
        if (this.exploder instanceof EntityCreeper && ((Entity) this.exploder).fire > 0) {
            this.isFlaming = true;
        }
    }

}
