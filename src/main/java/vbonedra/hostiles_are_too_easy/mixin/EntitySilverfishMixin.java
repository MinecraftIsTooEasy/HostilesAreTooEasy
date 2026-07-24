package vbonedra.hostiles_are_too_easy.mixin;

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
    public EntitySilverfishMixin(World par1World) {
        super(par1World);
    }

    @Unique private int celestialType = 0;

    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
        this.applyCelestialAttributes();
    }

    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true)
    public void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        if (celestialType == 1) {
            cir.setReturnValue(cir.getReturnValue() / 2);
        }
    }

    protected void dropFewItems(boolean recently_hit_by_player, DamageSource damage_source) {
        if (celestialType != 1) {
            EntityPlayer player = null;
            if (damage_source != null && damage_source.getResponsibleEntity() instanceof EntityPlayer) {
                player = (EntityPlayer) damage_source.getResponsibleEntity();
            }

            BlockBreakInfo info = new BlockBreakInfo(
                    this.getWorld(),
                    Integer.MAX_VALUE,
                    (int) this.posY,
                    (int) this.posZ
            );
            info.block = BlockClay.blockClay;

            info.drop_x = (int) this.posX;
            info.drop_y = (int) this.posY;
            info.drop_z = (int) this.posZ;
            info.responsible_entity = player;
            if (player != null) {
                info.responsible_item_stack = player.getHeldItemStack();
            }

            Block.blockClay.dropBlockAsEntityItem(info);
        }

        super.dropFewItems(recently_hit_by_player, damage_source);
    }
    public boolean isImmuneTo(DamageSource damage_source) {
        if (this.celestialType != 0) {
            if (this.celestialType == 2) return damage_source.getItemAttackedWith().hasMaterial(Material.copper);
        }
        return super.isImmuneTo(damage_source);
    }

    @Inject(method = "applyEntityAttributes()V", at = @At("RETURN"))
    protected void onApplyEntityAttributes(CallbackInfo ci) {
        this.applyCelestialAttributes();
    }

    @Unique
    private void applyCelestialAttributes() {
        if (this.celestialType == 1) {
            this.setEntityAttribute(SharedMonsterAttributes.attackDamage, 1.0F);
            this.setEntityAttribute(SharedMonsterAttributes.maxHealth, 4.0F);
            this.setEntityAttribute(SharedMonsterAttributes.movementSpeed, 0.5F);
        }
        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }
}
