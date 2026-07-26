package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.SilverfishBlockType;

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

            Block block = Block.getBlock(this.celestialType);
            if (block != null) {
                float hardness = block.getBlockHardness(0);
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
            Block block = Block.getBlock(this.celestialType);
            if (block != null && block.stepSound != null) {
                cir.setReturnValue(block.stepSound.getStepSound());
            }
        }
    }

    @Inject(method = "getDeathSound()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void getDeathSound(CallbackInfoReturnable<String> cir) {
        if (this.celestialType > 0) {
            Block block = Block.getBlock(this.celestialType);
            if (block != null && block.stepSound != null) {
                cir.setReturnValue(block.stepSound.getBreakSound());
            }
        }
    }

    @Inject(method = "playStepSound(IIII)V", at = @At("HEAD"), cancellable = true)
    private void onPlayStepSound(int par1, int par2, int par3, int par4, CallbackInfo ci) {
        if (this.celestialType > 0) {
            Block block = Block.getBlock(this.celestialType);
            if (block != null && block.stepSound != null) {
                this.makeSound(block.stepSound.getBreakSound(), 0.15F, 1.0F);
                ci.cancel();
            }
        }
    }

    // TODO: Override is awful, though idk how to move that to greater classes + its better for mine mod to override and force drop instead of other mods making Blockfish useless
    @Override
    protected void dropFewItems(boolean recently_hit_by_player, DamageSource damage_source) {
        if (celestialType > 0) {
            Block block = Block.getBlock(celestialType);

            if (block != null) {
                EntityPlayer player = (damage_source != null && damage_source.getResponsibleEntity() instanceof EntityPlayer)
                        ? (EntityPlayer) damage_source.getResponsibleEntity() : null;

                ItemStack heldItem = (player != null) ? player.getHeldItemStack() : null;

                boolean canMine = heldItem != null && (
                        (block instanceof BlockOre ore && heldItem.hasMaterial(ore.vein_material)) ||
                                (heldItem.getItem() instanceof ItemTool tool && tool.isEffectiveAgainstBlock(block, 0)) && SilverfishBlockType.getReplaceBlockDropForBlockId(block.blockID)
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

                info.block = block;
                info.responsible_entity = player;
                info.responsible_item_stack = player.getHeldItem().getItemStackForStatsIcon();

                block.dropBlockAsEntityItem(info);
                return;
            }
        }

        super.dropFewItems(recently_hit_by_player, damage_source);
    }

}
