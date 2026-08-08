package vbonedra.hostiles_are_too_easy.util.celestial_type;

import net.minecraft.*;

import java.util.Objects;

public interface ICelestialType {
    int HATE$getCelestialType();
    void HATE$setCelestialType(int type);
    int HATE$getCelestialSubtype();
    void HATE$setCelestialSubtype(int type);

    int celestialTypeUnset = 0;
    int celestialTypeVanilla = -1;
    int celestialSubtypeUnset = 0;
    int celestialSubtypeVanilla = -1;

    // TODO: use celestialSubtype instead of celestialType to store blockId

    // TODO: mobs that attack animals could scent players with health under 50%
    // TODO: GelatinousCube cant take more than 6 damage, if damage is greater than 6, it follows: 4 + (rt(8 * (damage - 4) + 1) - 1) / 2

    // TODO: blaze's small fireball could be replaced with fireball that cant explode spawner

    // celestialType - main type of entity granted on spawn
    // celestialSubtype - dynamic type of entity granted based on environment, could be mixed with main type, represents states like sickness or conversion phase

    // Squid
    int celestialTypeSquidGlow = 1; // Glow - spawns in dark waters, alongside slowness poisons target
    int celestialTypeSquidKraken = 2; // Kraken - spawns in deep dark waters, large scale, pulls nearby players and animals to itself
    // Villager
    int celestialSubtypeVillagerBloodClump = 1; // BloodClump - villager turns into blood clump, loses all interactions, scares other villagers which prevents trading, could be cured by enchanted golden apple while has strong regeneration effect
    int celestialSubtypeVillagerSick = 1; // Sick - cant trade or breed, must be cured with golden apple
    // Ocelot
    // Wolf
    int celestialTypeWolfBeast = 1; // Beast
    // DireWolf
    // Hellhound
    int celestialTypeHellhoundBloodBeast = 1; // BloodBeast
    // Chicken
    int celestialTypeChickenWild = 1; // Wild - uses custom texture, always afraid of players, halved egg laying speed
    int celestialSubtypeChickenHellspawn = 1; // Hellspawn - instead of laying eggs gives birth to small hostiles like silverfishes
    int celestialSubtypeChickenResource = 1; // Resource - instead laying eggs and dropping feathers drops metal nuggets, each one damages it, cant regenerate health, could be cured by shearing
    // Sheep
    int celestialTypeSheepWild = 1; // Wild - uses custom texture, always afraid of players, has 50% chance to not grow wool, when attacked would try to head bash attacker
    int celestialSubtypeSheepBloated = 1; // Bloated - flies into the sky when has wool
    int celestialSubtypeSheepParasite = 1; // Parasite - full of silverfishes under wool, when dies or gets sheared summons silverfishes, could be cured washing in water
    // Pig
    int celestialTypePigWild = 1; // Wild - uses custom texture, always afraid of players, fights back dealing 1 damage unless has less than 25% health
    int celestialSubtypePigManPig = 1; // ManPig - turns into player like entity that has pig texture, after some time turns into PigZombie
    int celestialSubtypePigGluttony = 1; // Gluttony - eats small animals, eats all grass, each time it eats entity, it gets bigger, when dies drops additional meat
    // Cow
    int celestialTypeCowWild = 1; // Wild - uses custom texture, leg kicks and runs away when attacked or milked
    int celestialSubtypeCowMad = 1; // Mad - would attack anything that approaches it, drops rotten flesh instead of meat, looks sick
    int celestialSubtypeCowSpore = 1; // Spore - spreads mycelium around slowly turning nearby cows into Spore subtype, drops no more than 1 meat, other meat replaced by mushrooms, at some point would turn into Mooshroom
    // Mooshroom
    int celestialTypeMooshroomWild = 1; // Wild - uses custom texture, leg kicks attacker when attacked or milked, won't stop attacking until has less than 25% health
    // Wither
    int celestialTypeWitherBloodMoon = 1; // BloodMoon - wither built in overworld on surface during blood moon, must be killed to enter end and use diamond enchanting table
    // Wight
    int celestialTypeWightLieutenant = 1; // Lieutenant - buffs nearby undead and gives 80% damage shield to nearby wights, wears plate armor and uses swords
    int celestialTypeWightHermit = 1; // Hermit - drains xp faster, wears chain armor and uses clubs
    // Shadow
    int celestialTypeShadowGloom = 1; // Gloom - stats and attack effects based on light level
    int celestialTypeShadowSpectral = 2; // Spectral - phases a few path nodes, transparency based on light level with being fully invisible in darkness
    // Witch
    int celestialTypeWitchCrazy = 1; // Crazy - throws potions faster, when dies turns into big vampire bat that flies away, drops potion of disenchanting, summons stronger foes, when summons foes could turn into one of them to blend in
    // Blaze
    int celestialTypeBlazeMelting = 1; // Melting - instead of shooting it flies towards player and attacks with no delay
    int celestialTypeBlazePyromaniac = 1; // Pyromaniac - throws rows of small fireballs
    // Enderman - once attacked never loses aggro
    int celestialTypeEndermanAlbino = 1; // Albino - water doesn't affect it, has higher stats
    int celestialTypeEndermanVoid = 1; // Void - teleports target when attacks or gets attacked
    int celestialTypeEndermanDelirium = 1; // Delirium - applies visual debuffs to target and nearby entities
    // InvisibleStalker
    int celestialTypeInvisibleStalkerMirror = 1; // Mirror - mirrors targets equipment, wont drop equipment on death
    // FireElemental
    int celestialTypeFireElementalStoked = 1; // Stoked - heat spreads fire and evaporates water, could shoot small fireball if target is too far
    // Ghoul
    int celestialTypeGhoulVampire = 1; // Vampire - when attacks heals dealt damage
    // EarthElemental
    int celestialTypeEarthElementalMimic = 1; // Mimic - mimics surrounding blocks
    int celestialTypeEarthElementalShuffle = 1; // Shuffle - when attacked switches type to random block that could be mined with iron tools
    // Zombie, Revenant, PigZombie
    int celestialTypeZombiePhase = 1; // Phase - dodges attacks
    // Zombie
    int celestialTypeZombiePlague = 2; // Plague - attacks apply strong poison
    int celestialTypeZombieRipper = 3; // Ripper - lunges at target with increased damage when airborne
    // Revenant
    int celestialTypeRevenantBrute = 1; // Brute - when dies heals back 60% max health and loses 3% max health each second
    // PigZombie
    int celestialTypePigZombiePigMan = 1; // PigMan - attacks players without golden armor
    // Creeper, InfernalCreeper
    int celestialTypeCreeperMimic = 1; // FlawedMimic - mimics blocks with flaws
    // Creeper
    int celestialTypeCreeperFire = 1; // Fire - spreads fire on explosion
    int celestialTypeCreeperElectric = 1; // Electric - strikes lightnings at nearby target and at nearby creepers
    // InfernalCreeper
    int celestialTypeInfernalCreeperLava = 1; // Lava - spreads lava on explosion
    int celestialTypeInfernalCreeperDoom = 1; // Doom - insanely high blocks explosion
    // Skeleton, Longdead, LongdeadGuardian, BoneLord, AncientBoneLord
    int celestialTypeSkeletonWithered = 1; // Withered - has wither skeletons traits, drops wither skull rarely
    int celestialTypeSkeletonBloated = 1; // Bloated - explodes with bones when killed with hammer-like weapon
    // Arachnid, CaveSpider, Spider, DemonSpider, WoodSpider, BlackWidowSpider, PhaseSpider
    int celestialTypeArachnidBroodmother = 1; // Broodmother
    // PhaseSpider
    int celestialTypePhaseSpiderWarp = 1; // Warp - teleports nearby entity to its position before teleporting
    // Silverfish, HoarySilverfish, Netherspawn, Copperspine
    int celestialTypeSilverfishBlockfish = 1; // Blockfish
    // Snowman
    int celestialTypeSnowmanBlockGolem = 1; // BlockGolem
    // IronGolem
    int celestialTypeIronGolemMetalGolem = 1; // MetalGolem - golems for each metal
    // MagmaCube - large cubes shoot fireballs
    int celestialTypeMagmaCubeObsidian = 1; // Obsidian - has high damage resistance in normal state, when touches lava enters heated state, in heated state has no resistance, cools down with time
    int celestialTypeMagmaCubeMagma = 1; // Magma - spreads lava on death, sets close entities on fire
    // GelatinousCube, Slime, Jelly, Blob, Pudding, Ooze - large cubes shoot slimeballs
    int celestialTypeGelatinousCubeSludge = 1; // Sludge - looks dirty, large and medium cubes could throw small cubes at target
    // Nightwing
    int celestialTypeNightwingEcho = 1; // Echo - screeches and spreads information about target to nearby monsters
    // VampireBat
    int celestialTypeVampireBatVenomous = 1; // Venomous - poison attacks
    // GiantVampireBat
    int celestialTypeGiantVampireBatStealer = 1; // Stealer - when attacks steals 1 item from inventory and flies away
    // Ghast
    int celestialTypeGhastGhost = 1; // Ghost - could enter ghost state making it 75% invisible and immune to ranged attacks beside fireballs
    int celestialTypeGhastCluster = 1; // Cluster - when dies summons a few ghasts
    int celestialTypeGhastMeteor = 1; // Meteor - shoots rows of fireballs

    static String getPrefix(Object entity, int celestialType) {
        String prefix = null;

        if (entity instanceof EntityPhaseSpider) {
            if (celestialType == celestialTypePhaseSpiderWarp) {
                prefix = "entity.arachnid.warp";
            }
        } else if (entity instanceof EntityCreeper) {
            if (celestialType == celestialTypeCreeperMimic) {
                prefix = "entity.creeper.mimic";
            }
        } else if (entity instanceof EntitySkeleton) {
            if (celestialType == celestialTypeSkeletonWithered) {
                prefix = "entity.skeleton.withered";
            }
        } else if (entity instanceof EntityZombie) {
            if (celestialType == celestialTypeZombiePhase) {
                prefix = "entity.zombie.phase";
            } else if (celestialType == celestialTypeZombiePlague) {
                prefix = "entity.zombie.plague";
            }
        } else if (entity instanceof EntityGhoul) {
            if (celestialType == celestialTypeGhoulVampire) {
                prefix = "entity.ghoul.vampire";
            }
        } else if (entity instanceof EntityShadow) {
            if (celestialType == celestialTypeShadowGloom) {
                prefix = "entity.shadow.gloom";
            } else if (celestialType == celestialTypeShadowSpectral) {
                prefix = "entity.shadow.spectral";
            }
        } else if (entity instanceof EntitySquid) {
            if (celestialType == celestialTypeSquidGlow) {
                prefix = "entity.squid.glow";
            }
        } else if (entity instanceof EntityInvisibleStalker) {
            if (celestialType == celestialTypeInvisibleStalkerMirror) {
                prefix = "entity.stalker.mirror";
            }
        }

        if (prefix != null) {
            String pre = StatCollector.translateToLocal("entity.prefix.pre");
            pre = Objects.equals(pre, "") ? " " : pre;
            String post = StatCollector.translateToLocal("entity.prefix.post");
            post = Objects.equals(post, "") ? " " : post;
            return pre + StatCollector.translateToLocal(prefix) + post;
        } else {
            return "";
        }
    }


}
