package vbonedra.hostiles_are_too_easy.util.celestial_type;

import net.minecraft.*;

import java.util.Objects;

public final class CelestialTypeData {
    public static int celestialTypeUnset = 0;
    public static int celestialTypeVanilla = 1; // TODO: move to its own boolean nbt to track initialization?
    public static int celestialSubtypeUnset = 0;
    public static int celestialSubtypeVanilla = -1;


    // TODO: maybe add celestialTypeData which stores bit position as byte and prefix used, would remove hardcoding on getPrefix, though would make it harder to write celestialTypes

    // TODO: creepers could sense only nearby players, its too much when 5+ creepers follow player from very depth of caves to the surface caves

    // TODO: once Enderman attacked it never loses aggro

    // TODO: GelatinousCube cant take more than 6 damage, if damage is greater than 6, it follows: 4 + (rt(8 * (damage - 4) + 1) - 1) / 2

    // TODO: blaze's small fireball could be replaced with fireball that cant explode spawner


    // NOTE: celestialType - 32 bits each one specific for each celestialType for entity group or entity, 1st bit used to tell if mob has any traits
    // NOTE: celestialType written as - celestialType_GROUPNAME_ENTITYNAME_TYPE for entity specific or celestialType_GROUPNAME_TYPE for whole group
    // NOTE: celestialSubtype - dynamic type of entity granted based on environment, represents states like sickness or conversion phase, mainly used to store blockId for mimics

    public enum CelestialSquid {
        GLOW(1, "entity.squid.glow"), // Glow - spawns in dark waters, alongside slowness poisons target
        KRAKEN(2, "entity.squid.kraken"); // Kraken - spawns in deep dark waters, large scale, pulls nearby players and animals to itself
        public final byte id;
        public final String langKey;
        CelestialSquid(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialVillager {
        SICK(1, "entity.villager.sick"), // Sick - cant trade or breed, must be cured with golden apple
        BLOOD_CLUMP(2, "entity.villager.bloodclump"); // BloodClump - villager turns into blood clump, loses all interactions, scares other villagers which prevents trading, could be cured by enchanted golden apple while has strong regeneration effect
        public final byte id;
        public final String langKey;
        CelestialVillager(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialWolf {
        BEAST(1, "entity.wolf.beast"); // Beast
        public final byte id;
        public final String langKey;
        CelestialWolf(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialHellhound {
        BLOOD_BEAST(1, "entity.hellhound.bloodbeast"); // BloodBeast
        public final byte id;
        public final String langKey;
        CelestialHellhound(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialChicken {
        WILD(1, "entity.chicken.wild"), // Wild - uses custom texture, always afraid of players, halved egg laying speed
        HELLSPAWN(2, "entity.chicken.hellspawn"), // Hellspawn - instead of laying eggs gives birth to small hostiles like silverfishes
        RESOURCE(3, "entity.chicken.resource"); // Resource - instead laying eggs and dropping feathers drops metal nuggets, each one damages it, cant regenerate health, could be cured by shearing
        public final byte id;
        public final String langKey;
        CelestialChicken(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialSheep {
        WILD(1, "entity.sheep.wild"), // Wild - uses custom texture, always afraid of players, has 50% chance to not grow wool, when attacked would try to head bash attacker
        BLOATED(2, "entity.sheep.bloated"), // Bloated - flies into the sky when has wool
        PARASITE(3, "entity.sheep.parasite"); // Parasite - full of silverfishes under wool, when dies or gets sheared summons silverfishes, could be cured washing in water
        public final byte id;
        public final String langKey;
        CelestialSheep(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialPig {
        WILD(1, "entity.pig.wild"), // Wild - uses custom texture, always afraid of players, fights back dealing 1 damage unless has less than 25% health
        MAN_PIG(2, "entity.pig.manpig"), // ManPig - turns into player like entity that has pig texture, after some time turns into PigZombie
        GLUTTONY(3, "entity.pig.gluttony"); // Gluttony - eats small animals, eats all grass, each time it eats entity, it gets bigger, when dies drops additional meat
        public final byte id;
        public final String langKey;
        CelestialPig(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialCow {
        WILD(1, "entity.cow.wild"), // Wild - uses custom texture, leg kicks and runs away when attacked or milked
        MAD(2, "entity.cow.mad"), // Mad - would attack anything that approaches it, drops rotten flesh instead of meat, looks sick
        SPORE(3, "entity.cow.spore"); // Spore - spreads mycelium around slowly turning nearby cows into Spore subtype, drops no more than 1 meat, other meat replaced by mushrooms, at some point would turn into Mooshroom
        public final byte id;
        public final String langKey;
        CelestialCow(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialMooshroom {
        WILD(1, "entity.mooshroom.wild"); // Wild - uses custom texture, leg kicks attacker when attacked or milked, won't stop attacking until has less than 25% health
        public final byte id;
        public final String langKey;
        CelestialMooshroom(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialWither {
        BLOOD_MOON(1, "entity.wither.bloodmoon"); // BloodMoon - wither built in overworld on surface during blood moon, must be killed to enter end and use diamond enchanting table
        public final byte id;
        public final String langKey;
        CelestialWither(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialWight {
        LIEUTENANT(1, "entity.wight.lieutenant"), // Lieutenant - buffs nearby undead and gives 80% damage shield to nearby wights, wears plate armor and uses swords
        HERMIT(2, "entity.wight.hermit"); // Hermit - drains xp faster, wears chain armor and uses clubs
        public final byte id;
        public final String langKey;
        CelestialWight(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialShadow {
        GLOOM(1, "entity.shadow.gloom"), // Gloom - stats and attack effects based on light level
        SPECTRAL(2, "entity.shadow.spectral"); // Spectral - phases a few path nodes, transparency based on light level with being fully invisible in darkness
        public final byte id;
        public final String langKey;
        CelestialShadow(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialWitch {
        CRAZY(1, "entity.witch.crazy"), // Crazy - throws potions faster, when dies turns into big vampire bat that flies away, drops potion of disenchanting, summons stronger foes, when summons foes could turn into one of them to blend in
        ZOMBIE(2, "entity.witch.zombie"); // Zombie - summons zombies
        public final byte id;
        public final String langKey;
        CelestialWitch(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialBlaze {
        MELTING(1, "entity.blaze.melting"), // Melting - instead of shooting it flies towards player and attacks with no delay
        PYROMANIAC(2, "entity.blaze.pyromaniac"); // Pyromaniac - throws rows of small fireballs
        public final byte id;
        public final String langKey;
        CelestialBlaze(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialEnderman {
        ALBINO(1, "entity.enderman.albino"), // Albino - water doesn't affect it, has higher stats
        VOID(2, "entity.enderman.void"), // Void - teleports target when attacks or gets attacked
        DELIRIUM(3, "entity.enderman.delirium"); // Delirium - applies visual debuffs to target and nearby entities
        public final byte id;
        public final String langKey;
        CelestialEnderman(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialInvisibleStalker {
        MIRROR(1, "entity.stalker.mirror"); // Mirror - mirrors targets equipment, wont drop equipment on death
        public final byte id;
        public final String langKey;
        CelestialInvisibleStalker(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialFireElemental {
        STOKED(1, "entity.fire_elemental.stoked"); // Stoked - heat spreads fire and evaporates water, could shoot small fireball if target is too far
        public final byte id;
        public final String langKey;
        CelestialFireElemental(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialGhoul {
        VAMPIRIC(1, "entity.ghoul.vampire"); // Vampire - when attacks heals dealt damage
        public final byte id;
        public final String langKey;
        CelestialGhoul(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialEarthElemental {
        MIMIC(1, "entity.earth_elemental.mimic"), // Mimic - mimics surrounding blocks
        SHUFFLE(2, "entity.earth_elemental.shuffle"); // Shuffle - when attacked switches type to random block that could be mined with iron tools
        public final byte id;
        public final String langKey;
        CelestialEarthElemental(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialZombie {
        PHASE(1, "entity.zombie.phase"), // Phase - dodges attacks
        PLAGUE(2, "entity.zombie.plague"), // Plague - attacks apply strong poison
        RIPPER(3, "entity.zombie.ripper"), // Ripper - lunges at target with increased damage when airborne
        BRUTE(4, "entity.zombie.brute"), // Brute - when dies heals back 60% max health and loses 3% max health each second
        PIGMAN(5, "entity.zombie.pigman"); // PigMan - attacks players without golden armor
        public final byte id;
        public final String langKey;
        CelestialZombie(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialCreeper {
        MIMIC(1, "entity.creeper.mimic"), // FlawedMimic - mimics blocks with flaws
        FIRE(2, "entity.creeper.fire"), // Fire - spreads fire on explosion
        ELECTRIC(3, "entity.creeper.electric"), // Electric - strikes lightnings at nearby target and at nearby creepers
        LAVA(4, "entity.creeper.lava"), // Lava - spreads lava on explosion
        DOOM(5, "entity.creeper.doom"); // Doom - insanely high blocks explosion
        public final byte id;
        public final String langKey;
        CelestialCreeper(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialSkeleton {
        WITHERED(1, "entity.skeleton.withered"), // Withered - has wither skeletons traits, drops wither skull rarely
        BLOATED(2, "entity.skeleton.bloated"); // Bloated - explodes with bones when killed with hammer-like weapon
        public final byte id;
        public final String langKey;
        CelestialSkeleton(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialArachnid {
        BROODMOTHER(1, "entity.arachnid.broodmother"), // Broodmother
        WARP(2, "entity.arachnid.warp"); // Warp - teleports nearby entity to its position before teleporting
        public final byte id;
        public final String langKey;
        CelestialArachnid(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialSilverfish {
        BLOCKFISH(1, "entity.silverfish.blockfish"), // Blockfish - spawns from broken blocks, usually from ores
        FACEHUGGER(2, "entity.silverfish.facehugger"); // Facehugger - jumps like spider
        public final byte id;
        public final String langKey;
        CelestialSilverfish(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialSnowman {
        BLOCK_GOLEM(1, "entity.snowman.blockgolem"); // BlockGolem
        public final byte id;
        public final String langKey;
        CelestialSnowman(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialIronGolem {
        METAL_GOLEM(1, "entity.iron_golem.metalgolem"); // MetalGolem - golems for each metal
        public final byte id;
        public final String langKey;
        CelestialIronGolem(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialMagmaCube {
        OBSIDIAN(1, "entity.magma_cube.obsidian"), // Obsidian - has high damage resistance in normal state, when touches lava enters heated state, in heated state has no resistance, cools down with time
        MAGMA(2, "entity.magma_cube.magma"); // Magma - spreads lava on death, sets close entities on fire
        public final byte id;
        public final String langKey;
        CelestialMagmaCube(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialGelatinousCube {
        SLUDGE(1, "entity.gelatinous_cube.sludge"); // Sludge - looks dirty, large and medium cubes could throw small cubes at target
        public final byte id;
        public final String langKey;
        CelestialGelatinousCube(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialNightwing {
        ECHO(1, "entity.nightwing.echo"); // Echo - screeches and spreads information about target to nearby monsters
        public final byte id;
        public final String langKey;
        CelestialNightwing(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialVampireBat {
        VENOMOUS(1, "entity.vampire_bat.venomous"); // Venomous - poison attacks
        public final byte id;
        public final String langKey;
        CelestialVampireBat(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialGiantVampireBat {
        STEALER(1, "entity.giant_vampire_bat.stealer"); // Stealer - when attacks steals 1 item from inventory and flies away
        public final byte id;
        public final String langKey;
        CelestialGiantVampireBat(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }
    public enum CelestialGhast {
        GHOST(1, "entity.ghast.ghost"), // Ghost - could enter ghost state making it 75% invisible and immune to ranged attacks beside fireballs
        CLUSTER(2, "entity.ghast.cluster"), // Cluster - when dies summons a few ghasts
        METEOR(3, "entity.ghast.meteor"); // Meteor - shoots rows of fireballs
        public final byte id;
        public final String langKey;
        CelestialGhast(int id, String langKey) { this.id = (byte) id; this.langKey = langKey; }
    }


    public static boolean hasCelestialType(int celestialType, byte celestialTypeTarget) {
        return (celestialType & (1 << celestialTypeTarget)) != 0;
    }
    public static boolean hasAnyCelestialType(int celestialType) {
        return (celestialType & 1) != 0;
    }
    public static int addCelestialType(int celestialType, byte celestialTypeTarget) {
        return celestialType | (1 << celestialTypeTarget);
    }

    // TODO: add map of Entity.class -> CelestialEntity.class and check for it instead of this clump of mess
    public static String getPrefix(Object entity, int celestialType) {
        if (celestialType == celestialTypeUnset || celestialType == celestialTypeVanilla) {
            return "";
        }
        StringBuilder prefixBuilder = new StringBuilder();
        String pre = StatCollector.translateToLocal("entity.prefix.pre");
        pre = Objects.equals(pre, "") ? " " : pre;
        String post = StatCollector.translateToLocal("entity.prefix.post");
        post = Objects.equals(post, "") ? " " : post;
        if (entity instanceof EntitySquid) {
            for (CelestialSquid type : CelestialSquid.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityVillager) {
            for (CelestialVillager type : CelestialVillager.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityWolf) {
            if (entity instanceof EntityHellhound) {
                for (CelestialHellhound type : CelestialHellhound.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            } else {
                for (CelestialWolf type : CelestialWolf.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            }
        } else if (entity instanceof EntityChicken) {
            for (CelestialChicken type : CelestialChicken.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntitySheep) {
            for (CelestialSheep type : CelestialSheep.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityPig) {
            for (CelestialPig type : CelestialPig.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityCow) {
            if (entity instanceof EntityMooshroom) {
                for (CelestialMooshroom type : CelestialMooshroom.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            }
            else {
                for (CelestialCow type : CelestialCow.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            }
        } else if (entity instanceof EntityWither) {
            for (CelestialWither type : CelestialWither.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityWight) {
            for (CelestialWight type : CelestialWight.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityShadow) {
            for (CelestialShadow type : CelestialShadow.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityWitch) {
            for (CelestialWitch type : CelestialWitch.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityBlaze) {
            for (CelestialBlaze type : CelestialBlaze.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityEnderman) {
            for (CelestialEnderman type : CelestialEnderman.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityInvisibleStalker) {
            for (CelestialInvisibleStalker type : CelestialInvisibleStalker.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityFireElemental) {
            for (CelestialFireElemental type : CelestialFireElemental.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityGhoul) {
            for (CelestialGhoul type : CelestialGhoul.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityEarthElemental) {
            for (CelestialEarthElemental type : CelestialEarthElemental.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityZombie) {
            for (CelestialZombie type : CelestialZombie.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityCreeper) {
            for (CelestialCreeper type : CelestialCreeper.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntitySkeleton) {
            for (CelestialSkeleton type : CelestialSkeleton.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityArachnid) {
            for (CelestialArachnid type : CelestialArachnid.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntitySilverfish) {
            for (CelestialSilverfish type : CelestialSilverfish.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntitySnowman) {
            for (CelestialSnowman type : CelestialSnowman.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityIronGolem) {
            for (CelestialIronGolem type : CelestialIronGolem.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityMagmaCube) {
            for (CelestialMagmaCube type : CelestialMagmaCube.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityGelatinousCube) {
            for (CelestialGelatinousCube type : CelestialGelatinousCube.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityNightwing) {
            for (CelestialNightwing type : CelestialNightwing.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        } else if (entity instanceof EntityVampireBat) {
            if (entity instanceof EntityGiantVampireBat) {
                for (CelestialGiantVampireBat type : CelestialGiantVampireBat.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            } else {
                for (CelestialVampireBat type : CelestialVampireBat.values()) {
                    if (hasCelestialType(celestialType, type.id)) {
                        appendPrefix(prefixBuilder, type.langKey, pre, post);
                    }
                }
            }
        } else if (entity instanceof EntityGhast) {
            for (CelestialGhast type : CelestialGhast.values()) {
                if (hasCelestialType(celestialType, type.id)) {
                    appendPrefix(prefixBuilder, type.langKey, pre, post);
                }
            }
        }
        return prefixBuilder.toString();
    }

    private static void appendPrefix(StringBuilder builder, String translationKey, String pre, String post) {
        if (!builder.isEmpty()) {
            builder.append(" ");
        }
        builder.append(pre).append(StatCollector.translateToLocal(translationKey)).append(post);
    }
}
