# Hostiles Are Too Easy
World Progression Based Difficulty Mod

## Overview
Hostiles Are Too Easy is a difficulty scaling mod for Minecraft MITE that adapts monster behavior, attributes, and visuals based on world progression thresholds.

---

## World Progression
The world dynamically shifts its difficulty, triggering new mob adaptations past specific thresholds:
* Hard Mode: Creepers unlock Mimic and powered states.
* Extreme Mode: Skeletons in the Underworld begin converting into deadly Withered variants.
* Legendary Mode: Withered Skeletons break free from the Underworld and begin invading the Overworld.

---

## Celestial Type System
The core mechanic introduces the Celestial Type, a special hidden type that modifies a mob's appearance, statistics, sounds, and behavior based on environment or origin.

### Zombie
* Evasion: Advanced celestial variants can dynamically dodge incoming player attacks, making standard melee combat highly unpredictable.

### Creeper (Mimic)
* Block Camouflage: Creepers past Hard Mode can blend into their environment by copying the appearance of the block they are standing on.
* Visual Blending:
    * Standard Creepers: Use imperfect camouflage. The block's texture is overlaid onto the Creeper, but their original green color is still partially visible, allowing alert players to spot them.
    * Infernal Creepers: Achieve Perfect Mimicry. Their texture perfectly matches the block below them, making them completely invisible in dark caves.
* Movement Tracking: If a Creeper walks into the air or falls, it retains the appearance of the last solid block it stood on to avoid breaking cover mid-air.

### Skeleton (Withered)
* Underworld Invasions: Skeletons spawning past Extreme Mode in the Underworld (and past Legendary Mode in the Overworld) have a chance to become Withered Skeletons.
* Shared Abilities: They gain the classic Wither Skeleton status effects, increased reach, and adaptive behavior.
* Visual Merging: Instead of using static files, the game dynamically merges the standard Skeleton appearance with the dark Wither Skeleton look.

### Silverfish (Blockspawn)
* Dynamic Infestation: Mining specific natural blocks (Clay, Coal, Copper, Iron, Gold, Lapis, Redstone, Diamond, Emerald, Quartz, Silver, Mithril, Adamantium) has a precise chance to release a Blockspawn Silverfish.
* Impenetrable Armor: The Silverfish is completely immune to all generic damage sources (swords, fists, arrows, explosions). It can only be harmed by a tool effective against its base block type (e.g., a pickaxe for ore variants, a shovel for clay variants).
* Hardness-Based Statistics: Attack damage, and natural defense scale dynamically based on the hardness and durability of the block they spawned from.
* Adaptive Sounds: They discard standard bug noises, instead producing the exact Step, Break, and Place sounds of their parent block when moving, getting hurt, or dying.
* Simulated Harvesting: If killed using a tool made of the block's exact vein material (or an effective mining tool if it has no tool material), the Silverfish properly drops the block's loot items, taking player skills and Fortune multipliers into account.
