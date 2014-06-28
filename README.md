Weird Science
============

A Minecraft mod intended to add more Space Station 13 style, Dwarf Fortress-style 'fun' to the tech mod experience. This is to say, you can get creative with your technology, and it can go horribly, horribly wrong.

Behold, a blatant copy&paste of my jumbled notes on the subject:

Weird Science ideas
--------

?<21:09:13> "Gyro": Turn horses into horrific groteque meat-unicorns
?<21:09:18> "Gyro": like call them unicorns
?<21:09:28> "Gyro": but they're fucking David Cronenberg shit
?<07:08:50> "Shados": oh lawd
?<07:08:51> "Shados": yes

• All those delicious delicious SA forums reactors.
• Redstone Flux compatible version of a Quarry
° Scratch the whole Quarry thing - a multiblock frame for a redstone-controlled rail crane (term?) 
? Is also an autobuilding system, since you can set the crane to place or break blocks. Basically an autonomous activator on a rail.

• Blood-powered reactor
° Some better way to get blood?
• AE-compatible multiblock tanks - with some kind of twist? Maybe blocks for using actual space as a reservoir? 

• Breedable hamsters for Rodentine Dynamos. If not fed and watered, escape to kill you.
° Putting hamsters in Acidic Experience creates undead hamsters; which do not require water or food but will randomly escape and poison you on attack.
• A generator that creates power by equalizing taint between biomes (read: by spreading taint)
° Combine this in the same tech tree with a generator that creates fuckloads of power when fed taint essentia, and some way to harvest the creep.
• Dirt generator's smog only moves upwards, but is created by a breadth-first-search from the generator, so if you have too much if it in one space it's going to "leak" (read: get in your fucking base)
• Fume hoods for collecting gases; dirtsmog as the reactant for converting liquid XP into acidic XP
• Centrifuge Vorpal Goo to get Vorpal Dust, smelts to Vorpal Ingots; Vorpal parts on TiC tools give beheading.
• Hazmat suit made from Vorpal Ingots and a configurable fabric (enchanted fabric or imp leather being good choices), grants immunity to all status effects except for "on fire". Grants no armor. Still works on taint and wither if at least the chest and shoes are equipped.
• Blizz rod as TiC handle causes weapon to slow enemies. (for separate, TE-TiC bridge?)
• Slow and power-hungry way of synthesizing ender pearls: Fire a laser through a nether portal at some sort of crystal. Diamond sounds too expensive. Ice? Result of putting liquid XP over lava, maybe?
° Firing a laser through a nether portal would look like shit. Maybe fire a laser AT a nether portal and it gets you some crystal blocks to make a focus out of? Hit it with antimatter, perhaps?
° Alternative process: Shoot nether portal blocks with an exceedingly high-powered laser, get crystals. Put crystals in compressor, get nether pearls. This would solve the "Liquid XP + teleporty shit is supposed to be vorpal, isn't it?" problem.
• Extremely power-hungry way of turning Vorpal Goo into a End Portal; returns to Vorpal Goo when power is cut.
° GUI option to go to 1-to-1 spot if ender dragon is dead. If ender dragon is not dead goes to the standard island.
? Our own implementation of End Meteors? Xen terrain?
• Lasers + rotatable mirrors = most awesome wireless redstone. Can hurt foes, players, you. Other uses? Laser-ignited fusion?
° Laser with an ender pearl lens should do all sorts of shit, but namely teleport players all over the place upon hitting them.
? HOLY SHIT! Awesome and (intellectually) difficult teleportation system based on ender laser wavelength and distance from player.
• Vorpal tools that require TiC. Have unique effects, mostly being long-fucking-range.
° Display little ghost pickaxe near the block you're actually breaking.
• Some way to break containment, fucking up bad enough to get a resonance cascade and turn the terrain all evil + doomy
• Shadowmorn is cool: " An engine that runs on ghast tears. On dirt but produces deadly smog for being a cheap bastard. That only works in The End and is fantastic but attracts endermen swarms. A potato based engine that byproducts vodka somehow. A engine that uses cooked food and another that requires to be built on the line between a snow and sand biome that uses the radical heat difference (thanks mmochampion/Dark Legacy!) to produce heat. A kinetic engine that needs you to wind it up, AE crank style."
° " Edit: Fear powered engine. Only works in proximity to fleeing animals or ocelots. Alternatively put a cat and a creeper in it. "
• "A "nuclear" engine that runs off of Hefnerium ingots, Pignite ingots or Chicknite Chunks. Turn an MFR ranch producing liquid meat into a power source when mixing the meat-goo with uranium or yellorite. Bonus points if the waste is edible, creating something akin to the KFC Double Down. Despite only pumping in blocks, nuggets or liquids the engine starts to moo, oink and cluck as it heats up. The hotter it is, the more energy the multiblock reactor is, the louder and more obnoxoius it gets. You can sate/cool it with Milk."
• " Engine that runs on engines. " - the Recursion Engine?Produces fuckloads of power for those willing to sacrifice their ingots. Produces considerably less power for Rodentine Dynamos.
• " -Hypercubic generators that let you have massive power for free; but randomly they will go into consumption mode and drain 10x as much as they normally output. Break the power or block and it explodes like a nuke." This is good with some variation from the original idea - separate leads for in and out, no internal buffer. If it doesn't get the power input needed while in consumption mode, it makes a marginally large explosion *AND SPAWNS A WITHER*
° Which is one way to get a nether star.
° Call it an Occult Dynamo, make it cost a wither skull and run very efficiently on very small amounts of blood.
• http://www.youtube.com/watch?v=n7mQowQSWfs
° This is what happens when you throw raw pork into acidic experience.
• Acidic experience as an alternative way to get slimes to spawn? Throw oredicted raw rubber or cactus sugar into acidic experience, viola.
• Centrifuge netherrack dust, get something which, when thrown into acidic XP, creates blazes?
° This would still be balanced too since you'd need to kill other mobs for the liquid XP - hierarchical mob grinding.
• Bottling acidic experience gets you Soul Draught, which, when drank, is basically Power Within.
• Another essential from the SA thread - gunpowder engine that sets everything near it on fire.
• Hitting 
• Spider eye + ??? = demon eye. Get line of sight between demon eye and a restrainment bed to drain essence.
° ??? = what? Blaze rods, nether quartz? Nether quartz will be used in the bed, perhaps. Hmm. 
° Spider eye + ender pearl + bone.
? Or, ender pearl dropped in blood = bloody eye.
? Blood congeals with ender pearl in it = black eye?
° Special bed for evil magic shit. Occult bed. Made of bones, nether quartz, obsidian, what? Bones, obsidian, gold? 
? Bones over obsidian, like: Bone frame, obsidian slab.
° Process renders zombies, skeletons, undead skelies lifeless. Spiders + creepers become lobotomized.
° This is all too elaborate.
• Instead, just use a thing that can absorb exp orbs.
• burnDictionary class to give to multiple engine classes

Instead, have a reactionList you can include in your block or something. I dunno. Stop abstracting for the sake of abstraction.

TODO: Make gas explode (try a CheckReaction() func in gasbase & DoReaction dummy to inherit), make mobs continue to be poisoned, make gas turn water to acid & make acid! Good schtuff. After that, polish the phosphate engine.

Island raising machine that bumps blocks around them upwards slowly, creating stone blocks directly above bedrock. Costs absurd amount of power to run. Alternatively, starts at stone level, builds up under dirt.

Rechargable torch / flare wand. Something like the MPS one combined with Descent flares. "Cherenkov prism" Alternatively just cheap battery into nonexplosive torch recipe. Alternatively, both.

Multiblock tank that works like Xycraft tanks, and with a fake air onNeighborUpdate cascade. Option on the tank to convert fake air to actual fluid blocks where appropriate. Liquid blocks taken into the tank upon placement.

A healing gas you can make that dissipates easily.

" Combine kinetic generator from modular powersuits with saddle and put it on pig so it generates power as it runs around. Lead charged pig onto something like the charge pad from mekanism to discharge it. If you don't do this regularly, the pig overloads leaving some cooked porkchops behind... "

"You may have to have supplemental items for Fear Engines - Fear Ducts to carry the terror. Make it out of wool and ender eyes. Make them slightly purple and occasionally drip blood, like the underside of a block below lava does, just darker."

"For an Engine that runs on LiquidXP/MobEssence/Monsterjuice can you make it so little green ghosts puff out of the engine instead of smoke?
That'd be fab."
" So basically the same thing that forestry apiaries do, just with ghosts instead of BEES?"
"
Lunar Generator. When exposed to night sky, generates power based on how full the moon is.
Enchantment Engine. Insert enchanted (vanilla, so as to avoid things like meteor armor) items and it strips the enchantments, increases its "fuel" meter, and generates power over time burning it. "
"Real talk: I'd kill for a lunar generator."
"Entropic Engine
Slowly grinds nearby stone into cobblestone, cobblestone into gravel, and gravel into sand. Once there's no stone, cobble, or gravel nearby, it stops generating power. "

"
Thaumcraft taint generator. Generates power based on how tainted the area is or something. Leads to "fun."

Photosynthesis generator. You put a tree on top of it and it generates power based on how much leaf blocks are on the tree."

" Hoarder generator. Put items into it like an extremely large chest, and only hold one of each item and high value items give more power. To get the items back, you must destroy the generator. "

"What I would like to see in solar generation something OTHER then photovoltaic. Give me a proper Heliostat tower, where properly aligned mirrors are the key to good production, or even an Updraft Tower. If you make it a proper multiblock structure I can have an awesome tower on the top of my mountain generating power for my base.
Also, proper multiblock windmills that convert rotational mechanical energy into store-able electric energy. I really kinda dislike plonking down a single block and stuffing coal into it to generate power. I like designing stuff. Even if I always end up with function over form. "

" Make it so that tossing food over the focal point of an operational heliostat tower cooks it. I'd set up a chest that outputs raw food when its daylight, and something to suck up the end product after a timer."

Blood powered reactor is a thing you could do really quickly. "Hemoionic Dynamo"

Worms style grappling hook / hookshot

Aluminum extraction from clay, make aluminum a requirement for a lot of shit.
!SUPER-SIMPLE: Acid turns clay to aluminum dust!

Aluminum dust can be used to make alum which is an insta-heal.

Some easy way to repair items that has a chance of adding curses to them.

Optional Nether Portal overhaul that completely changes Obsidian (makes it easy to harvest) but accessing the Nether requires an entirely different (and more technical) process.


Throwing clay into acid nets you Aluminosillicate Slurry Block
Cooking Aluminosillicate Slurry Block nets you one unit of Alum (which is the new coagulant).
Throwing Alum into water nets you Aluminum Hydroxide Solution
Cooking Aluminum Hydroxide Solution nets you Aluminum Dust.

Thermite needs to be a thing: Smog instantly turns iron blocks into rust blocks. Pulverizing rust blocks gives you rust dust. Rust dust + aluminum dust = thermite dust.

Get vampirism, you burn in the day but can drink blood to heal. Shift-clicking while holding blood bucket drinks it.

Mercury as another chemistry crafting ingredient.

Centrifuge Soul Sand to get sand and liquidexp

Standing in unstable ecto converts your health to XP

Chemical battery: 3 gold, 3 Aluminum, 2 iron (top and bottom), one acid bucket.

Drills are made like so: X = Iron Ingot C = Cell S = Stick
  X
XCX
  S
and can attack any substance with equal efficiency. Vorpal drills, X is Vorpal Ingot, and they can hit at range (Upgradeable?). Replacing the stick with a Resonant Vorpal Receiver results in an item that can be placed in a Resonant Vorpal Transmitter (I'll get to that in a bit), so that it might receive power at range. What range? We'll have to figure out. Anyway, receiving energy for tools from *anywhere* should require the transmitter to have a nether star in some way or form, but not the tools.

Nether portal overhaul: 
• Obsidian Overhaul (separate enabler):
° Obsidian is easy to break, yielding Obsidian Shards.
° Either Obsidian Shards or Obsidian Blocks will then need to be thrown into blood. This does not consume the blood, but it does result in a Cursed Obsidian Block, which then has the durability of vanilla Obsidian and can be used for portals / enchanting tables / etc.
° Perhaps leave the vanilla class and ID for Obsidian as-is, rename it as Cursed Obsidian, and reflect into / replace the Lava + Water behavior for the fragile obsidian? 
• Mere fire cannot create a Nether Portal. Instead, you need Thermite Dust.

Direct next step is to unify items onto a few item IDs. Same with behaviorless blocks. Don't do anything before you do that. Then, iron block to rust block to rust, rust + aluminum dust = thermite is next. Thermite behavior can come later.

Need to think of an alternative acid recipe. Sulphur is an obvious choice but then we can't call it "nitric acid" anymore and have to go back to the generic "acid"

Oh man oh man. Saltpeter and black powder contain nitrates. Just centrifuge those to get sulphur *and* gunpowder.

Also: Added complexity, perhaps require cooking of inactive chemical bucket to get active chemical bucket? Dunno what sort of IRL compound does this.

Though, we want aluminium to make centrifuges. 

Recipe for lowest tier centrifuge: redstone wiring makes redstone motor.
X    X
X X X
   M
where X = iron and M = motor.

Multiblock centrifuges sound like the bees knees.

Before centrifuges, get back to the original purpose of the mod a little. Chemistry is for nerds. Our primary purpose is crazy shit and generators. Namely: Occult Dynamo. Lunar Generator. Photosynthesis Parasite Engine (which should work off of leaves (moreso) and grass (less so)). Hoarder generator. Thaumcraft taint (spread) generator. Segue into liquidXP-related shit: Spooky skull. Soul Siphon (xp orb to liquid xp hopper). Soul Engine (oh yeah). Combo of blood + unstable XP = MFR Mob Essence. Then, the vorpal mechanics. Then, the centrifuge. The centrifuge existed originally for vorpal mechanics, after all.
Parasite engine should be a relatively late-game thing meant for people who want to live in trees like elves but with technology.

Soul Engine is a 

When you're centrifuging vorpal dust out of vorpal goo you made yourself and using it to build things like occult dynamos and Thaumcraft taint spreader engines, then it's time to make a release on the WIP Mods forum. After centrifuges & mixers, after XP mechanics, after the easier half of the fucked-up generators, but before hamster wheels, before lasers, before multiblock centrifuges, before Vorpal tools, and before doomsday generators / Xen world. Might not even need a single multiblock.

ALSO: Before WIP release, port to all post 1.6.4 Forge versions. 


Liquid ghast tears? Perhaps this is our source of durum? Or perhaps blaze powder is?

Generic tile entities via inner classes.

http://io9.com/this-fizzy-reaction-could-run-your-car-or-make-it-ex-1524273092
Aluminum + lye solution = hydrogen gas, eh?



Need a Salis Mundis recipe. Will involve Vorpal Dust somehow but I'm not clear on how quite yet.


Make a ghast spawning method via unstable ecto (mushroom?), and use ghast tears as an essential resource.


Occult engine is just a blood tank altar. Place things like a Wither Skull or the Dragon Egg on top of it to make it into a real generator

Gunpowder engine takes: Gunpowder, thermite, blaze powder, what else..?


A bag full of spiders as an item.

What if heat handlers are already made aware of other heat handlers in radius at block place time?

Dropping XP orbs on a heater / burner turns them to XP gas. Need to condense it to get the liquidXP / ectoplasm you want.

Durum (or perhaps "Urdite"?) can be used to make walls that consume Redstone Flux power to become indestructible.

Builder's Wand alternative

Railgunderbuss that can fire any metal item (well it would be a coilgun but let's not be pedantic.) Fire giant buzzsaws for automated tree farming.