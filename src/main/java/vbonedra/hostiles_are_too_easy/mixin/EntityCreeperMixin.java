package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static vbonedra.hostiles_are_too_easy.difficulty_mode.DifficultyMode.get_difficulty_level;

@Mixin(EntityCreeper.class)
public abstract class EntityCreeperMixin extends Entity {
    public EntityCreeperMixin(World par1World) {
        super(par1World);
    }


    @Shadow protected float explosionRadius;
    @Unique private int celestialType = 0;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        int difficulty = get_difficulty_level(world);

        if (this.rand.nextFloat() < difficulty * 0.2F) {
            this.celestialType = 1;
        }
        if (this.rand.nextFloat() < difficulty * 0.1F) {
            this.onStruckByLightning(null);
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("HATECelestialType", this.celestialType);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("HATECelestialType")) {
            this.celestialType = par1NBTTagCompound.getInteger("HATECelestialType");
        }
    }


    @ModifyVariable(method = "onUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityCreeper;getPowered()Z"), ordinal = 0)
    private float explosion_size_vs_blocksModify(float explosion_size_vs_blocks) {
        if (this.getWorld() == null) return explosion_size_vs_blocks;
        return this.explosionRadius * 0.715F + (get_difficulty_level(this.getWorld()) * 0.09625F);
    }
}
