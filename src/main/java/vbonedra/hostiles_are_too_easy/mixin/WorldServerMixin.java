package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

import static vbonedra.hostiles_are_too_easy.difficulty_mode.DifficultyMode.get_difficulty_level;


@Mixin(WorldServer.class)
public abstract class WorldServerMixin extends World {

    @Unique int nextIntSafe(int n) {
        return n > 0 ? this.rand.nextInt(n) : 0;
    }

    public WorldServerMixin(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler, ILogAgent par6ILogAgent, long world_creation_time, long total_world_time) {
        super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, par5Profiler, par6ILogAgent, world_creation_time, total_world_time);
    }

    @Inject(method = "getSuitableCreature", at = @At("RETURN"), cancellable = true)
    private void catchFailedSpawnsWithFullLogic(EnumCreatureType creature_type, int x, int y, int z, CallbackInfoReturnable<Class<?>> cir) {
        boolean is_in_overworld = this.isOverworld();
        boolean is_in_underworld = this.isUnderworld();
        boolean is_in_hell = this.isTheNether();

        boolean can_see_sky = this.hasSkylight();

        int difficulty_level = get_difficulty_level(this.getWorld());
        int difficulty_level_for_random = difficulty_level + 1; // when used in nextIntSafe: 0 - never, 1 - 50%, 2 - 66%, 3 - 75%, 4 - 80%
        boolean is_hard_mode = difficulty_level >= 1;
        boolean is_extreme_mode = difficulty_level >= 2;
        boolean is_legendary_mode = difficulty_level >= 3;
        boolean is_endgame_mode = difficulty_level >= 4;

        boolean depth_random_overworld = this.nextIntSafe(48 * difficulty_level) >= y;
        boolean random_underworld = is_in_underworld && ((is_extreme_mode && this.rand.nextFloat() >= 0.5f) || is_legendary_mode);

        // "evolve" mobs
        if (cir.getReturnValue() != null) {
            if (cir.getReturnValue() == EntityBat.class) {
                if (depth_random_overworld || random_underworld) {
                    cir.setReturnValue(EntityVampireBat.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntitySkeleton.class) {
                if (depth_random_overworld && is_extreme_mode) {
                    cir.setReturnValue(EntityLongdead.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntityBoneLord.class) {
                if (depth_random_overworld && is_extreme_mode) {
                    cir.setReturnValue(EntityAncientBoneLord.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntitySpider.class) {
                if (depth_random_overworld || random_underworld) {
                    cir.setReturnValue(EntityDemonSpider.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntityWoodSpider.class) {
                if (this.nextIntSafe(difficulty_level_for_random) >= 1) {
                    cir.setReturnValue(EntityBlackWidowSpider.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntityCreeper.class) {
                if (depth_random_overworld || random_underworld) {
                    cir.setReturnValue(EntityInfernalCreeper.class);
                    return;
                }
            }

            if (cir.getReturnValue() == EntityJelly.class) {
                if (is_in_overworld ? this.nextIntSafe(difficulty_level_for_random) >= 1 : this.nextIntSafe(difficulty_level_for_random - 1) >= 1) {
                    cir.setReturnValue(EntityBlob.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntityBlob.class) {
                if (is_in_overworld ? this.nextIntSafe(difficulty_level_for_random) >= 1 : this.nextIntSafe(difficulty_level_for_random - 1) >= 1) {
                    cir.setReturnValue(EntityPudding.class);
                    return;
                }
            }
            if (cir.getReturnValue() == EntityPudding.class || cir.getReturnValue() == EntityOoze.class) {
                if (is_extreme_mode && (is_in_overworld ? this.nextIntSafe(difficulty_level_for_random) >= 1 : this.nextIntSafe(difficulty_level_for_random - 1) >= 1)) {
                    cir.setReturnValue(EntityMagmaCube.class);
                    return;
                }
            }

        }


        boolean overworld_heigh_spawn = is_hard_mode && depth_random_overworld && (!can_see_sky || is_legendary_mode);

        boolean is_blood_moon_up = this.isBloodMoon(true);
        boolean is_freezing_biome = this.getBiomeGenForCoords(x, z).isFreezing();
        boolean is_desert_biome = this.getBiomeGenForCoords(x, z).isDesertBiome();

        boolean can_spawn_ghouls_on_surface = is_blood_moon_up || is_hard_mode;
        boolean can_spawn_wights_on_surface = (is_hard_mode && is_freezing_biome) || is_extreme_mode;
        boolean can_spawn_shadows_on_surface = (is_hard_mode && is_desert_biome) || is_extreme_mode;

        // add new mob conditions
        for(int attempt = 0; attempt < 16; ++attempt) {
            List possible_creatures = this.getChunkProvider().getPossibleCreatures(creature_type, x, y, z);
            if (possible_creatures == null || possible_creatures.isEmpty()) {
                cir.setReturnValue(null);
                return;
            }

            SpawnListEntry entry = (SpawnListEntry)WeightedRandom.getRandomItem(this.rand, possible_creatures);
            Class<?> entity_class = entry.entityClass;

            if (entity_class == EntityInfernalCreeper.class) {
                if (!is_in_overworld || is_in_hell || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityDemonSpider.class) {
                if (!is_in_overworld || is_in_hell || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityHellhound.class) {
                if (!is_in_overworld || is_in_hell || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
//            if (entity_class == EntityVampireBat.class) {
//                if (!is_in_overworld || y <= 48 || is_blood_moon_up) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }
//            if (entity_class == EntityNightwing.class) {
//                if (!is_in_overworld || y <= 32 || is_blood_moon_up) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }

            // TODO: add EntitySlime.class to BiomeGenBase but don't spawn it in non swamp biomes in normal_mode
//            if (entity_class == EntitySlime.class) {
//                if (!this.blockTypeIsAbove(Block.stone, x, y, z)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }
//            if (entity_class == EntityJelly.class) {
//                if (this.blockTypeIsAbove(Block.stone, x, y, z)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }
//            if (entity_class == EntityBlob.class) {
//                if ((!is_in_overworld || y <= 40) && this.blockTypeIsAbove(Block.stone, x, y, z)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }
//            if (entity_class == EntityPudding.class) {
//                if ((!is_in_overworld || y <= 24) && this.getBlock(x, y - 1, z) == Block.stone && this.blockTypeIsAbove(Block.stone, x, y, z)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }

//            if (entity_class == EntityOoze.class) {
//                if ((!is_in_overworld || y <= 32) && this.getBlock(x, y - 1, z) == Block.stone && this.blockTypeIsAbove(Block.stone, x, y, z)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }


            if (entity_class == EntityGhoul.class) {
                if (!is_in_overworld || can_spawn_ghouls_on_surface) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityWight.class) {
                if (!is_in_overworld || overworld_heigh_spawn || can_spawn_wights_on_surface) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityRevenant.class) {
                if (!is_in_overworld || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityInvisibleStalker.class) {
                if (!is_in_overworld || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityEarthElemental.class) {
                if (!is_in_overworld || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityBoneLord.class) {
                if (!is_in_overworld || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityHellhound.class) {
                if (!is_in_overworld || overworld_heigh_spawn) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
            if (entity_class == EntityShadow.class) {
                if (!is_in_overworld || (overworld_heigh_spawn || can_spawn_shadows_on_surface) && !can_see_sky) {
                    cir.setReturnValue(entity_class);
                    return;
                }
            }
//            if (entity_class == EntityWoodSpider.class) {
//                if ((this.canBlockSeeTheSky(x, y, z) || this.blockTypeIsAbove(Block.leaves, x, y, z) || this.blockTypeIsAbove(Block.wood, x, y, z))
//                        && this.blockTypeIsNearTo(Block.wood.blockID, x, y, z, 5, 2)
//                        && this.blockTypeIsNearTo(Block.leaves.blockID, x, y + 5, z, 5, 5)) {
//                    cir.setReturnValue(entity_class);
//                    return;
//                }
//            }

        }
    }
}
