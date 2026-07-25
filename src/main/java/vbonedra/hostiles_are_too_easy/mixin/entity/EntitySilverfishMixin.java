package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.SilverfishBlockType;

@Mixin(EntitySilverfish.class)
public abstract class EntitySilverfishMixin extends EntityMob implements ICelestialType {
    @Unique private int celestialType = 0; // celestialType must be negative for non-SilverfishBlockType
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

            EnumEquipmentMaterial material = SilverfishBlockType.getEquipmentMaterialForBlockId(this.celestialType);

            if (material != null) {
                damage = 1.0F + (float) Math.cbrt(material.durability);
                health = 4.0F + (float) Math.sqrt(material.durability) * 4.0F;
                if (material.durability > 16.0F) {
                    speed = 0.35F;
                }
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

    // TODO: instead of Override it must be Inject at HEAD of super.method
    @Override
    public float getNaturalDefense(DamageSource damage_source) {
        if (this.celestialType > 0) {
            EnumEquipmentMaterial material = SilverfishBlockType.getEquipmentMaterialForBlockId(this.celestialType);
            if (material != null) {
                return super.getNaturalDefense(damage_source) + (float) Math.cbrt(material.durability) * 4.0F;
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
