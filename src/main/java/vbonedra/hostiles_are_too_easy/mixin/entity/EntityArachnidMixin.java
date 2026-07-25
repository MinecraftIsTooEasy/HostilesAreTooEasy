package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;

import static vbonedra.hostiles_are_too_easy.util.RandomUtil.nextIntSafe;

@Mixin(EntityArachnid.class)
public abstract class EntityArachnidMixin extends EntityMob {
    public EntityArachnidMixin(World par1World) {
        super(par1World);
    }
    // TODO: come up with idea for this, idk what to add cause Spider is Too Lame =p

//    @Unique private int celestialType = 0;
//
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    public void init(World world, float scaling, CallbackInfo ci) {
//        if (nextIntSafe(world, get_difficulty_level(world) + 1) >= 1 && this.rand.nextFloat() <= 0.5f) {
//            this.celestialType = 1;
//        }
//
//    }
//
//    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
//    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
//        par1NBTTagCompound.setInteger("HATECelestialType", this.celestialType);
//    }
//
//    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
//    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
//        if (par1NBTTagCompound.hasKey("HATECelestialType")) {
//            this.celestialType = par1NBTTagCompound.getInteger("HATECelestialType");
//        }
//    }
//
//    @Inject(method = "applyEntityAttributes", at = @At("RETURN"))
//    protected void onApplyEntityAttributes(CallbackInfo ci) {
//        if (this.celestialType == 1) {
//            this.setEntityAttribute(SharedMonsterAttributes.attackDamage, 6.0F);
//            this.setEntityAttribute(SharedMonsterAttributes.maxHealth, 12.0F);
//            this.setEntityAttribute(SharedMonsterAttributes.movementSpeed, 1.0F);
//            this.setHealth(this.getMaxHealth());
//        }
//    }

}
