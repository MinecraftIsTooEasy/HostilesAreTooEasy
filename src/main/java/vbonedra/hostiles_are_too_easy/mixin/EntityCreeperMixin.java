package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static vbonedra.hostiles_are_too_easy.difficulty_mode.DifficultyMode.get_difficulty_level;

@Mixin(EntityCreeper.class)
public abstract class EntityCreeperMixin extends Entity {
    public EntityCreeperMixin(World par1World) {
        super(par1World);
    }

    @Shadow protected float explosionRadius;

    @Unique private boolean difficultyRadiusApplied = false;
    @Unique private int customCreeperType = 0;

    @Unique
    private void applyServerDifficultyBuff() {
        if (this.worldObj != null && !this.worldObj.isRemote && this.entityId > 0 && !this.difficultyRadiusApplied) {
            int difficulty = get_difficulty_level(this.worldObj);
            this.explosionRadius = (1.0F + difficulty * 0.15F) * ((Object) this instanceof EntityInfernalCreeper ? 2f : 1f);

            if (this.rand.nextFloat() < difficulty * 0.2F) {
                this.customCreeperType = 1;
            }
            if (this.rand.nextFloat() < difficulty * 0.1F) {
                this.onStruckByLightning(null);
            }

            this.difficultyRadiusApplied = true;
        }
    }

    @Inject(method = "onUpdate", at = @At("HEAD"))
    private void onCreeperUpdateServerCheck(CallbackInfo ci) {
        if (!this.difficultyRadiusApplied) {
            this.applyServerDifficultyBuff();
        }
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onCreeperConstructed(World world, CallbackInfo ci) {
        this.applyServerDifficultyBuff();
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setBoolean("hate_DifficultyBuffApplied", this.difficultyRadiusApplied);
        par1NBTTagCompound.setInteger("hate_CreeperCustomType", this.customCreeperType);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("hate_DifficultyBuffApplied")) {
            this.difficultyRadiusApplied = par1NBTTagCompound.getBoolean("hate_DifficultyBuffApplied");
        }
        if (par1NBTTagCompound.hasKey("hate_CreeperCustomType")) {
            this.customCreeperType = par1NBTTagCompound.getInteger("hate_CreeperCustomType");
        }
        if (!this.difficultyRadiusApplied) {
            this.applyServerDifficultyBuff();
        }
    }
}
