package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

import static vbonedra.hostiles_are_too_easy.HostilesAreTooEasyMod.HATE_LOGGER;
import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;

@Mixin(EntityCreeper.class)
public abstract class EntityCreeperMixin extends Entity implements ICelestialType {
    public EntityCreeperMixin(World par1World) {
        super(par1World);
    }

    @Shadow private int fuseTime;
    @Shadow private int timeSinceIgnited;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        int difficulty = get_difficulty_level(this.getWorld());

        if (world.isWorldServer()) {
            if (this.rand.nextFloat() < difficulty * 0.1F) {
                this.onStruckByLightning(null);
            }
        }

        this.fuseTime = (int) (this.fuseTime * (1.0F - (difficulty * 0.08F)));
    }

    @Redirect(method = "onUpdate()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;createExplosion(Lnet/minecraft/Entity;DDDFFZ)Lnet/minecraft/Explosion;"))
    private Explosion onUpdate_creeperExplosion(
            World world,
            Entity exploder,
            double posX,
            double posY,
            double posZ,
            float explosion_size_vs_blocks,
            float explosion_size_vs_living_entities,
            boolean is_smoking
    ) {
        EntityCreeper creeper = (EntityCreeper) exploder;

        double vanillaY = creeper.posY + (creeper.height / 4.0F);
        double targetY = creeper.posY + (creeper.height / 2.0F);

        if (Math.abs(posY - vanillaY) < 0.01) {
            posY = targetY;
        } else {
            HATE_LOGGER.warn("Detected that Creeper's explosion pos was already changed by probably another mod! Current vanillaY / targetY / posY: {} / {} / {}", vanillaY, targetY, posY);
        }

        int difficulty = 0;
        if (creeper.getWorld() != null) difficulty = get_difficulty_level(this.getWorld());

        if (difficulty > 1) {
            world.createExplosion(
                    creeper,
                    posX, posY, posZ,
                    explosion_size_vs_blocks + (difficulty * 0.09625F),
                    explosion_size_vs_living_entities,
                    is_smoking
            );
            return world.createExplosion(
                    creeper,
                    posX, posY, posZ,
                    explosion_size_vs_blocks + (difficulty * 0.09625F),
                    0,
                    is_smoking
            );
        } else {
            return world.createExplosion(
                    creeper,
                    posX, posY, posZ,
                    explosion_size_vs_blocks + (difficulty * 0.09625F),
                    explosion_size_vs_living_entities,
                    is_smoking
            );
        }

    }


@Inject(method = "onUpdate()V", at = @At("HEAD"))
private void onUpdate_jumpBeforeExploding(CallbackInfo ci){
    EntityCreeper creeper = (EntityCreeper) (Object)this;
    if (this.timeSinceIgnited == (this.fuseTime - 8) && creeper.getCreeperState() == 1) {
        EntityLivingBase target = creeper.getAttackTarget();

        if(target != null) {
            if (this.posY - 2.5 < target.posY) {
                    creeper.motionY = 0.38;
                }
                double var1 = target.posX - creeper.posX;
                double var2 = target.posZ - creeper.posZ;
                Vec3 vector = Vec3.createVectorHelper(var1, 0, var2);
                vector.normalize();
                creeper.motionX = vector.xCoord * 0.18;
                creeper.motionZ = vector.zCoord * 0.18;
                this.faceEntity(target, 100.0F, 100.0F);
            }
        }
    }

    @Unique
    public void faceEntity(Entity entity, float f, float g) {
        EntityCreeper creeper = (EntityCreeper) (Object)this;
        double var4 = entity.posX - creeper.posX;
        double var8 = entity.posZ - creeper.posZ;
        double var6;
        if (entity instanceof EntityLivingBase var10) {
            var6 = var10.posY + (double)var10.getEyeHeight() - (creeper.posY + (double)creeper.getEyeHeight());
        } else {
            var6 = (entity.boundingBox.minY + entity.boundingBox.maxY) / (double)2.0F - (creeper.posY + (double)creeper.getEyeHeight());
        }

        double var14 =MathHelper.sqrt_double(var4 * var4 + var8 * var8);
        float var12 = (float)(Math.atan2(var8, var4) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
        float var13 = (float)(-(Math.atan2(var6, var14) * (double)180.0F / (double)(float)Math.PI));
        creeper.rotationPitch = this.updateRotation(creeper.rotationPitch, var13, g);
        creeper.rotationYaw = this.updateRotation(creeper.rotationYaw, var12, f);
    }

    @Unique
    private float updateRotation(float f, float g, float h) {
        float var4 = MathHelper.wrapAngleTo180_float(g - f);
        if (var4 > h) {
            var4 = h;
        }
        if (var4 < -h) {
            var4 = -h;
        }
        return f + var4;
    }

}
