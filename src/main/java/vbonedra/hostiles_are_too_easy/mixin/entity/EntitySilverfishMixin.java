package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

@Mixin(EntitySilverfish.class)
public abstract class EntitySilverfishMixin extends EntityMob implements ICelestialType {
    @Unique private int celestialType = 0;
    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
        this.applyCelestialAttributes();
    }
    public EntitySilverfishMixin(World par1World) {
        super(par1World);
    }

    @Unique private void applyCelestialAttributes() {
        if (this.celestialType > 0) {
            float damage = 1.0F;
            float health = 4.0F;
            float speed = 0.5F;

            Block associatedBlock = Block.getBlock(this.celestialType);
            if (associatedBlock != null) {
                float hardness = associatedBlock.getBlockHardness(0);
                if (hardness < 0.0F) hardness = 0.0F;

                damage = 1.0F + hardness;
            }

            this.setEntityAttribute(SharedMonsterAttributes.attackDamage, damage);
            this.setEntityAttribute(SharedMonsterAttributes.maxHealth, health);
            this.setEntityAttribute(SharedMonsterAttributes.movementSpeed, speed);
        }

        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true)
    public void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        if (celestialType > 0) {
            cir.setReturnValue(cir.getReturnValue() / 2);
        }
    }

    @Inject(method = "updateEntityActionState()V", at = @At("HEAD"), cancellable = true)
    private void onUpdateEntityActionState(CallbackInfo ci) {
        if (this.celestialType > 0) {
            super.updateEntityActionState();
            if (!this.worldObj.isRemote) {
                if (!this.hasPath()) {
                    if (this.entityToAttack == null) {
                        this.updateWanderPath();
                    } else {
                        this.entityToAttack = null;
                    }
                }
            }
            ci.cancel();
        }
    }

    @Inject(method = "getHurtSound()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void getHurtSound(CallbackInfoReturnable<String> cir) {
        if (this.celestialType > 0) {
            Block associatedBlock = Block.getBlock(this.celestialType);
            if (associatedBlock != null && associatedBlock.stepSound != null) {
                cir.setReturnValue(associatedBlock.stepSound.getStepSound());
            }
        }
    }

    @Inject(method = "getDeathSound()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<String> cir) {
        if (this.celestialType > 0) {
            Block associatedBlock = Block.getBlock(this.celestialType);
            if (associatedBlock != null && associatedBlock.stepSound != null) {
                cir.setReturnValue(associatedBlock.stepSound.getBreakSound());
            }
        }
    }

    @Inject(method = "playStepSound(IIII)V", at = @At("HEAD"), cancellable = true)
    private void onPlayStepSound(int par1, int par2, int par3, int par4, CallbackInfo ci) {
        if (this.celestialType > 0) {
            Block associatedBlock = Block.getBlock(this.celestialType);
            if (associatedBlock != null && associatedBlock.stepSound != null) {
                this.makeSound(associatedBlock.stepSound.getBreakSound(), 0.15F, 1.0F);
                ci.cancel();
            }
        }
    }



    @Override
    public float getNaturalDefense(DamageSource damage_source) {
        if (this.celestialType > 0) {
            Block associatedBlock = Block.getBlock(this.celestialType);
            if (associatedBlock != null) {
                float hardness = associatedBlock.getBlockHardness(0);
                if (hardness < 0.0F) hardness = 0.0F;
                float extraDefense = hardness;
                return super.getNaturalDefense(damage_source) + extraDefense;
            }
        }
        return super.getNaturalDefense(damage_source);
    }

    @Override
    protected void dropFewItems(boolean recently_hit_by_player, DamageSource damage_source) {
        if (celestialType > 0) {
            Block associatedBlock = Block.getBlock(celestialType);

            if (associatedBlock != null) {
                EntityPlayer player = (damage_source != null && damage_source.getResponsibleEntity() instanceof EntityPlayer)
                        ? (EntityPlayer) damage_source.getResponsibleEntity() : null;

                ItemStack heldItem = (player != null) ? player.getHeldItemStack() : null;

                boolean canMine = heldItem != null && (
                        (associatedBlock instanceof BlockOre ore && heldItem.hasMaterial(ore.vein_material)) ||
                                (heldItem.getItem() instanceof ItemTool tool && tool.isEffectiveAgainstBlock(associatedBlock, 0))
                );

                if (!canMine) {
                    return;
                }

                int posX_int = MathHelper.floor_double(this.posX);
                int posY_int = MathHelper.floor_double(this.posY);
                int posZ_int = MathHelper.floor_double(this.posZ);

                BlockBreakInfo info = new BlockBreakInfo(
                        this.getWorld(),
                        Integer.MAX_VALUE,
                        posY_int,
                        posZ_int
                );

                info.drop_x = posX_int;
                info.drop_y = posY_int;
                info.drop_z = posZ_int;

                info.block = associatedBlock;
                info.responsible_entity = player;
                info.responsible_item_stack = player.getHeldItem().getItemStackForStatsIcon();

                associatedBlock.dropBlockAsEntityItem(info);
                return;
            }
        }

        super.dropFewItems(recently_hit_by_player, damage_source);
    }

    @Override
    public boolean isImmuneTo(DamageSource damage_source) {
        if (this.celestialType == 0 || damage_source == null) {
            return super.isImmuneTo(damage_source);
        }

        Block associatedBlock = Block.getBlock(this.celestialType);
        if (associatedBlock == null) {
            return super.isImmuneTo(damage_source);
        }

        ItemStack item_stack = damage_source.getItemAttackedWith();
        if (item_stack != null && item_stack.getItem() instanceof ItemTool) {
            return !item_stack.getItemAsTool().isEffectiveAgainstBlock(associatedBlock, 0);
        }

        return true;
    }
}
