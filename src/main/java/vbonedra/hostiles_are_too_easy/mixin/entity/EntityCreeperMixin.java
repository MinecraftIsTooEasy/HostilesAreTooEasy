package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;

@Mixin(EntityCreeper.class)
public abstract class EntityCreeperMixin extends Entity implements ICelestialType {
    @Unique private int celestialType = this.HATE$getCelestialType();
    public EntityCreeperMixin(World par1World) {
        super(par1World);
    }


    @Shadow protected float explosionRadius;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        int difficulty = get_difficulty_level(this.getWorld());

        if (this.rand.nextFloat() < difficulty * 0.1F) {
            this.onStruckByLightning(null);
        }
    }



    @ModifyVariable(method = "onUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityCreeper;getPowered()Z"), ordinal = 0)
    private float explosion_size_vs_blocksModify(float explosion_size_vs_blocks) {
        if (this.getWorld() == null) return explosion_size_vs_blocks;
        return this.explosionRadius * 0.715F + (get_difficulty_level(this.getWorld()) * 0.09625F);
    }
}
