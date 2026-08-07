package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

@Mixin(EntitySquid.class)
public abstract class EntitySquidMixin extends EntityWaterMob implements ICelestialType {
    public EntitySquidMixin(World world) {
        super(world);
    }

    @Inject(method = "onCollideWithPlayer", at = @At("RETURN"))
    public void onCollideWithPlayer(EntityPlayer player, CallbackInfo ci) {
        if (!this.worldObj.isRemote && this.getDistanceToEntity(player) < 1.0F) {
            if (this.HATE$getCelestialType() == ICelestialType.celestialTypeSquidGlow) {
                player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 4));
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                player.addPotionEffect(new PotionEffect(Potion.confusion.id, 40, 0));
                player.setAir(player.getAir() - 1);
            }
        }
    }
    @Inject(method = "collideWithEntity", at = @At("RETURN"))
    public void collideWithEntity(Entity entity, CallbackInfo ci) {
        if (this.onServer() && this.preysUpon(entity) && entity.isEntityLiving() && this.hasLineOfStrike(entity)) {
            if (this.HATE$getCelestialType() == ICelestialType.celestialTypeSquidGlow) {
                entity.getAsEntityLiving().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 4));
                entity.getAsEntityLiving().addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                entity.getAsEntityLiving().addPotionEffect(new PotionEffect(Potion.confusion.id, 40, 0));
                entity.setAir(entity.getAir() - 1);
            }
        }
    }

}
