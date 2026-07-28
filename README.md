# **WARNING! AI-slop-description**
_**I'M SORRY I WAS TOO LAZY TO WRITE ACTUAL CHANGELOG, also that's just a filler readme, ill write normal one by the v1.0.0-r version**_

# Hostiles Are Too Easy (H.A.T.E.) Mod

The mod radically overhauls artificial intelligence, combat mechanics, and vanilla monster attributes by introducing a dynamic system of "Celestial Types". It scales enemy strength based on the world's current difficulty level, turning survival into a brutal challenge.

---

## New Mechanics: Celestial Types

All living entities now implement the ICelestialType interface. When spawning on the server side, mobs can randomly receive a specific subtype. The chance of appearance depends on the world's difficulty level.

Special mob subtypes have a doubled health pool (for certain types) and always grant 2x more experience upon death.

### Spawn Chances for Special Monster Types
* Creeper Mimic (entity.creeper_mimic.name): Difficulty * 20%
* Withered Skeleton (entity.skeleton_withered.name): (Difficulty - X) * 20% (where X = 0 in the Underworld, X = 1 in the Overworld)
* Vampire Ghoul (entity.ghoul_vampire.name): (Difficulty + 1) * 10%
* Phase Zombie (entity.zombie_phase.name): Difficulty * 5% (minimum base chance is 0.5%)
* Warp Spider (entity.arachnid_warp.name): Difficulty * 5% (minimum base chance is 0.5%)
* Gloom Shadow (entity.shadow_gloom.name): Guaranteed (100% chance).

---

## Entity Adjustments and Combat Logic

### Creeper
* Absolute Immunity: Completely protected against any damage type except explosions.
* Smart AI: Spots the player instantly from any distance and sees through walls.
* Powered Creepers: When spawning, they have a chance equal to Difficulty * 10% to become powered by lightning.
* Fast Fuse: Explosion fuse time is reduced by Difficulty * 8%.
* Jump Before Exploding: Exactly 8 ticks before detonating, the creeper performs a rapid jump towards the player with high velocity and faces the target, preventing an easy escape.
* Epicenter Offset: The explosion point is raised to half of the creeper's height to maximize damage to the player's body.
* Double Explosion: At a difficulty level above 1, it creates two simultaneous explosions. The second explosion deals damage only to the environment, creating a massive destructive wave.
* Flaming Blast: If the creeper is on fire at the moment of detonation, its explosion is guaranteed to set surrounding blocks on fire.

### Skeleton
* Withered Subtype:
    * Possesses a doubled maximum health pool.
    * Full immunity to fire and lava.
    * Melee attacks apply the Wither potion effect for 10 seconds (200 ticks).
    * All arrows shot from its bow are automatically set on fire.
    * Drops coal guaranteed upon death. If killed by a player, it has a rare chance (25%) to drop a Wither Skeleton Skull.

### Zombie
* Phase Subtype:
    * Receives a random pool of evasion charges ranging from 2 to 5 + Difficulty.
    * Restores 1 evasion charge every 10 seconds (200 ticks).
    * Upon receiving direct damage (excluding fall, fire, and poison), it consumes a charge to completely cancel the damage and teleports away to a safe spot (at least 3 blocks away from the threat).
    * Teleportation triggers Enderman portal sounds and Runegate particles.

### Ghoul
* Vampire Subtype:
    * Receives a permanent +4 bonus to its natural defense.
    * When successfully attacking a biologically alive entity, it heals itself by the amount of health lost by the target (up to 1.0F health points per hit).

### Arachnids & Phase Spider
* Golem Hunting: If the arachnid has no player targets, it actively searches for and attacks Iron Golems (ignored outdoors during daytime).
* Warp Subtype (Phase Spider):
    * Upon teleportation, it looks for any hostile mob within a 4-block radius.
    * Performs a position swap (swaps places with that mob), evading the player's attack and exposing the ally to danger instead.

### Silverfish — Blockfish
* Block Integration: When ores or blocks are broken on the server, a Blockfish tied to that block's ID can spawn based on the block's rarity and world difficulty.
* Loot Replacement: For valuable blocks (diamond, emerald, lapis, redstone, coal, nether quartz), the silverfish completely destroys the original block drop upon spawning.
* Attributes and Adaptation:
    * Attack damage scales with the block's hardness value (1.0F + hardness).
    * Hurt, step, and death sounds are replaced by the step and break sounds of the corresponding block.
    * Natural defense increases based on the block's hardness.
* Mining Requirements: The silverfish is protected against standard weapons. To retrieve the block resource, you must kill it using a tool effective against the original block (e.g., a pickaxe of the correct tier). Killing it with a sword or an improper tool drops nothing. Fall damage and Iron Golem attacks bypass this restriction.

---

## Custom Iron Golem System

Players can build Iron Golems out of various metal or precious blocks. The standard T-shaped structure with a pumpkin on top automatically detects the material.

### Attribute Calculations
All stats dynamically scale based on the durability value of the block's equipment material:
* Max Health: 24 * sqrt(durability)
* Base Damage: 4 * cbrt(durability)
* Natural Defense: cbrt(durability) (added to the golem's defense when taking damage that bypasses mundane armor)
* Death Experience: sqrt(durability^3)

### Material Characteristics
* Copper / Adamantium: Possess a 2x slower health regeneration rate.
* Silver: Deals damage with a silver aspect.
* Gold: Deals damage with a magic aspect and features a 3x faster regeneration rate.
* Mithril: Deals damage with a magic aspect and features a 2x faster regeneration rate.
* Nether Quartz: Deals damage with a magic aspect and features a 1.5x faster regeneration rate.
* Emerald / Diamond: Deal damage with a magic aspect.
* Unique Loot: When killed by a player, custom golems drop 3 to 5 nuggets or shards matching their block type (increased by the Looting modifier) instead of standard iron ingots.

---

## Boss Overhauls

### Wither
* Immunity: Full immunity to all types of damage except explosions.
* Health Pool: Increased to 400 units.
* Initial Charging Phase: Invulnerability duration upon spawning increased to 330 ticks (16.5 seconds).
* Reward: Experience dropped on death increased by 10x.
* Predictive Projectiles: When shooting skulls, it analyzes the player's distance and velocity to fire predictive shots, aiming where the player is going to be.
* Ruinous Arsenal: Has a 2.5% chance to emit a Ghast scream and launch a Large Fireball that destroys blocks instead of a standard skull.
* Defensive Ring of Fire: During its first stage (before getting its ranged armor shield), taking damage grants a 10% chance to unleash a perfect circle of 8 Small Fireballs in all directions.

### Boss Kill Progressions
* When the Wither or the Ender Dragon dies on the server, achievements are granted to all players within a 128-block radius (16384 squared distance).
* Killing the Wither grants nearby players the progress achievements for Legenday Mode.
* Killing the Ender Dragon grants nearby players the endgame progression tracker.

---

## Mod Achievements and Progression Tree

* **Normal Mode** (achievement.normalMode)
    * *Description:* Start a new world... (achievement.normalMode.desc)
* **Hard Mode** (achievement.hardMode)
    * *Description:* to enter deepest depths... (achievement.hardMode.desc)
* **Extreme Mode** (achievement.extremeMode)
    * *Description:* and hottest places... (achievement.extremeMode.desc)
* **Legendary Mode** (achievement.legendaryMode)
    * *Description:* to challenge the Demigod... (achievement.legendaryMode.desc)
* **Endgame Mode** (achievement.endgameMode)
    * *Description:* and shatter last interdimensional seal. (achievement.endgameMode.desc)

### Special Boss Objectives
* **The Beginning...** (achievement.spawnWither)
    * *Description:* Challenge the Demigod... (achievement.spawnWither.desc)
* **of Withering Heights** (achievement.killWither)
    * *Description:* and don't hold back. (achievement.killWither.desc)

### In-game Notifications
* If a player attempts an action before the boss is defeated: **The Wither is yet to be slayed.** (notification.end_frame_block)
* If the boss setup is restricted by blocks: **The Wither needs more free space.** (notification.wither_block)
