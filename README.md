
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


// `TODO`:
- Mob AI Tweaks stolen ideas:
    - sounds: entities create sounds, some entities could hear them and go check where they come from
    - sleeping: if biped entity lost health it could enter sleepy state, it would sit and/or look down, while sleeping has lowered detecting range
    - entities with tools could jump 1 block up of player is 2 blocks higher
    - slimes could shoot slimeballs
    - zombies could hold tiny slimes as weapons and rarely doors as shields
    - skeletons and any other mobs that use ranged weapons back up when approached by their target
    - Cold celestialType Skeleton - throws snowballs time to time
    - Brick celestialType Skeleton - throws bricks time to time
    - Skeleton: 360 shots - skeletons could jump and do 360 before shooting arrow
    - Wither Skeleton: skull shoot - could shoot wither sculls once in a life if target is too far
    - Enderman: hate lock - after attacked wont peaceful until kills attacker
    - Blaze: prepare shot - summons 3-5 fireballs around itself and after short time shoots them
    - Creeper: arrow shot - if got hit by arrows and they didnt broke creeper would summon randomly rotated arrows on explosion
    - Villager: `TODO`: i forgot that they have careers, not all 
        - they could hate player that treats them badly, e.g. attacks them or iron golems, places too many doors next to each other
        - they have inventory player could access if they aren't hated
        - they do staff based in professions
            - Priest: throws healing potions at hurt villagers
            - Blacksmith: heals iron golems, repairs tools other villagers use
            - Farmer: cures blight if has bonemeal, shears sheep if has shears, milks cow if has buckets
            - Librarian: enchant tools villagers use
            - Butcher: bonemeals grass near animals if has bonemeal, feeds and kills animals if has food and dagger
            - Nitwit: could attack monsters if iron golem isnt nearby
        - player could pay villager an emerald for their service:
            - Priest: would throw healing potion at player
            - Blacksmith: would repair items from hotbar if player has nuggets in hotbar
            - Farmer: would give some bread and maybe water bottle
            - Librarian: would enchant items from hotbar
            - Nitwit: would follow player and attack monsters


// `TODO`:
- write better desc, this was written by ai and some sentences are straight maid up lies. i like how it formatted it though
- add Phase Zombie - dodges some attacks, spawns rarely
- add Mimic EarthElemental - mimics block it stands in or on, couldn't be damaged by wrong tool for block
- add

- Entities to modify
    - LightningBolt: strike player with condition in thunder, though iTF/iTE does it? not sure
    - EnderCrystal: could be damaged by arrows, some towers have metal bars. some completely hidden in obsidian, though instead of being worldgen changes it must be special logic that runs on init and checks if its in end, i hope theres something that tells game that crystal is natural dragon crystal, so ill binf placement to it
    - LivingBase
        - Dragon: just add something beside player-perches T~T
        - Player: ??? maybe BTW-like nerfs based on health and hunger, though in MiTE they are dynamic.
        - Living
            - Creature: `TODO` BTW-like possession, dimensional portals possess in <32 distance or 2 chunks at any Y level, rune portals possess in <16 distance or 1 chunk away chunk
                - WaterMob
                    - Squid: `TODO` BTW-like attacks; Ghast possession-celestialType - changes texture and becomes constantly aggressive, then jumps out of the water anr turns into ghast
                - Ageable: `TODO` Flesh possession-celestialType - uses pixels of rotten flesh, drop rotten flesh instead of any items, cant be bred, sheep loses wool, villagers stop trading and turn into zombie villager randomly, cows cant produce liquids, pigs turn into zombie pigmen randomly, chickens turn into small monster randomly, is base for some possession-celestialType
                    - Villager: `TODO` Witch possession-celestialType - uses witch blended texture, turn into witch randomly, could be cured by regeneration effect
                    - Animal
                        - Horse: `TODO` 4 apocalypse riders - possession-celestialType for each one
                            - White-Conquest possession-celestialType: gets wight rider that wears golden helmet and white colored leather armor and shoots arrows that apply harmful effects
                            - Red-War possession-celestialType: gets rider that wears metal plate armor and has sword, turns tameable against owners
                            - Black-Famine possession-celestialType: gets rider that makes it move to crops and trees, blights and removes those, makes nearby animals hungry and targeted players unable to eat or drink
                            - Yellow-Death possession-celestialType: gets wither skeleton rider that wears black colored leather armor and has scythe, rider summons Hades zombie nearby that gets respawned right after gets killed, those kill anything besides undead creatures and get healed and buffed
                        - Tameable
                            - Wolf: `TODO` Roots possession-celestialType - if sitting stands up, gets bloodier look, spreads possession in same chunk
                                - DireWolf: `TODO` Beast possession-celestialType - larger and always angry, cant be tamed
                                - Hellhound
                            - Ocelot
                        - Livestock: `TODO` Statue possession-celestialType - uses stone texture, cant move on its own, feed, drops less meat, could be damaged only by pickaxe, cant be bred, cant produce anything, still attracts other mobs
                            - Pig
                            - Sheep: `TODO` Bloat possession-celestialType - when unsheared gravity is reverted and looks top-bottom inverted, looks a bit saturated
                            - Cow: `TODO` Hellspawn possession-celestialType - when breed spawns small monster mod, looks corrupted
                                - Mooshroom: `TODO` Node possession-celestialType - looks more mushroom-ish, after some time grows into a large mushroom
                            - Chicken: `TODO` Gravel possession-celestialType - could lay gravel non-gravel drop instead of egg, uses gravel texture, has nether gravel variant; Netherrack possession-celestialType - has chance of exploding on laying egg, has nether ore variants that lay corresponding chips or nuggets
                - Mob: `TODO` no magic ability - cant be damaged with magic
                    - Wither: `TODO` cant be damaged by explosion; when spawned under sea level starts rapidly exploding and flying up, summons wither skeletons when loses 25% health, corrupts world nearby by turning blocks into their worse variant (e.g. grass -> dirt, stone/ore -> cobblestone -> gravel, breaks leaves and flower-like plants, blights crops, resets farmlands manure and wet states, etc.), Sinful celestialType could be summoned by building with beacon at center in nether, Sinful celestialType must be killed to enter legendary mode; Sinful celestialType has more health, shoots faster, applies harder effects, when looses 50% health becomes immune to damage and starts charging for a few seconds and then explodes, after exploded cant health past 50% health
                    - GiantZombie
                    - Wight: `TODO` Lieutenant celestialType - wears rusted iron armor, attacks have 100% to drain xp, when drains xp increases basic protection, frenzies nearby undead mobs just like bone lord
                    - Shadow: `TODO` Gloom celestialType - moves faster in darkness, when attacks from darkness gives slowness, removes nearby light sources, has larger follow range
                    - Witch
                    - Blaze: `TODO` Fire celestialType - heals when in fire state, shoots more fireballs at once
                    - Enderman: `TODO` Rook celestialType - on teleportation switches places with player unless it makes them too far from each other, when hit with arrow teleports player to itself and teleports randomly
                    - InvisibleStalker: `TODO` Light celestialType - doesn't remove light, invisibility and damaged based on brightness, armor increases with how invisible it is, making it almost invincible in darkness; Thief celestialType - moves faster, when attacks player it steals random item from inventory and runs away, when attacked in melee while running away it tries to attack and if succeed runs away again
                    - FireElemental: `TODO` Water celestialType - spawns in dark water if in underworld or during blood moon, reverse of fire elemental, moves fast in water, takes away air when attacks, could throw snowballs that take away air, when air drained deals more damage and each attack prevents air regeneration for next few seconds
                    - AnimalWatcher
                        - EarthElemental: `TODO` Mimic celestialType - mimics blocks it stands on, drops its actual block on death, cant be damaged unless tool correct for block
                            - ClayGolem: `TODO` cant have celestialType?
                        - Ghoul: `TODO` Vampire celestialType - heal on attacks to non-undead.
                        - Zombie: `ADDED` Phase celestialType - dodges attacks; `TODO` Bloodthirsty celestialType - does something, i just wanna see zombie-zombiepigman textures that do something
                            - Revenant: `TODO` Temerarious celestialType - cant be physically damaged while has armor, each physical attack removes 1 random armor piece, when all armor broken it gets increased speed
                    - Creeper: `ADDED` Mimic celestialType - mimics blocks it stands on with flaws
                        - InfernalCreeper: `ADDED`: Mimic celestialType - perfectly mimics
                    - Skeleton: `ADDED` Withered celestialType - wither skeleton
                        - Longdead
                            - LongdeadGuardian
                        - BoneLord: `TODO`: summon corresponding celestialType
                            - AncientBoneLord
                    - Arachnid: `TODO` come up with celestialType, idk what to add
                        - Spider: `TODO` Demonic celestialType - same as demon spider, needed to remove demon spider replacing spiders, any non demon spider could become Demonic; `TODO` Broodmother celestialType - big spider that spawns spiders on death, spawned spiders are forced to be smaller than they usually are and have less health
                            - DemonSpider
                        - CaveSpider 
                        - WoodSpider: `TODO` Widow celestialType - black widow but a bit weaker, needed to remove replacing spiders with black widows on spawn
                            - BlackWidowSpider
                            - PhaseSpider: `TODO` Warp celestialType - when path-phases it also teleports nearest to itself mob that targets same entity if it would make it closer to target
                    - Silverfish: `ADDED` Blockfish celestialType - spawns from some blocks
                        - HoarySilverfish: `ADDED` extend Blockfish celestialType in high grade ores
                        - Netherspawn: `ADDED` extend Blockfish celestialType in nether blocks
                        - Copperspine: `ADDED` extend Blockfish celestialType in ores
                - Golem
                    - Snowman: `TODO` Cloth/HardenedClay/Brick celestialType - built with wool blocks, doesnt die in non-cold biomes, corresponding throwable and armor; who even spawned one in MiTE? snow is hard to keep in non snowy biomes...
                    - IronGolem: `TODO` add other block golems, though ill hate texture modifications; `TODO` make mobs it would attack hate it and attack 1st; `TODO` add custom health percent based textures like in modern minecraft
            - Cubic
                - MagmaCube: `TODO` Obsidian celestialType - spawns with obsidian-blended texture, must be near lava or fire to change texture to normal one; Mantle celestialType - has mantle-blended texture, sets blocks under it on fire, when dies spawns lava based on size
                - GelatinousCube
                    - Slime
                    - Jelly
                    - Blob
                    - Ooze
                    - Pudding
            - AmbientCreature
                - Bat: `TODO` Gnat celestialType - darker texture, smaller, flies faster, after attack flies away for a few seconds
                    - VampireBat
                        - GiantVampireBat
                - Nightwing
            - Flying
                - Ghast: `TODO` Soul celestialType - does something soulsand related, idk, sounds cool; `TODO` Mantle celestialType - shoots rows of small fireballs with almost no explosion