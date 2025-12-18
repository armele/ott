But First... The Thank Yous!
=======
- THANK YOU to Imri, my 3D modeling soul sister and midwest clone!
- THANK YOU to Thodor, who tolerated my 25-year lapse in Java to help me start on this project!
- THANK YOU to Ayar for helping me test this insanity!
- THANK YOU to the LDT team for creating and maintaining the MineColonies mod!

Mixins Used:
=======
- Yes, this is mainly for Ray's benefit
  - Overriding vanilla weather particles to add custom particle textures
  - Overriding the vanilla health/hearts sprite to add custom icons
  - Foliage placement in the Pale Garden backport
  - Suppressing the experimental features warning for new instances
  - Blocking animal pathfinding for fence gates
  - Allowing players to jump over fences, fence gates, and walls
  - Adding game tips to the bottom of the loading screen

FAQ:
=======
1. Can you backport this to 1.yadda?
   - Nope. Move forward, not backwards.
   
2. Such and such (insert Mekanism, etc. here) are way more powerful; why don't you blarg?
    - Because the point of this mod is to be a companion to MineColonies and to avoid OP mod functions.

Running List:
=======
- Added Gradient Concrete, Concrete Powder, Terracotta, Wool, and Stained Glass blocks
- Added Limestone blocks
- Added a set of 16 Test Blocks for various Ray request testings
- Added healing bed function for players
- Added Seaglass
- 32x textures (ongoing)
  - yes, Ray, I know
  - 32x is a nice happy medium that gives a little more detail, a little more variation, without straying too far from the "Minecraft" feel
- 3D models for most blocks (ongoing)
- FLOOFY LEAVES!
- Lowered the Shield when equipped so you can actually, ya know, see what you're doing
- Lowered the Totem of Undying, see above
- Identified and added missing sound files (ongoing)
- Identified and added missing recipes (ongoing)
- Added the evil Hedge
    - Evil Cackling Ensues
    - Added custom damage for the murder plants: Flora
    - Can be grown as a crop from Hedge Sprouts
    - Can be harvested with a hoe to bypass damage
- Added Create support, first pass (3D models and textures for ores)
- Added Chipped support, first pass (fluffy leaves, fixed some lantern models, fixed ladders(3D))
- Added farmland protection for MineColonies crops
- Added custom schematic dimensions!
  - flat
  - flat w/ water
  - MCol schematic dimension by Thaylar & the LDT Team with fully pregenerated templates
- Added native support for custom music
- Added copper blocks/slabs/stairs as pathblocks for MineColonies citizens
- Updated the item models for dyes
- Moved 3D texture models for external mods to a built-in resource pack to override mod load order issues
- Added new MCol specific splash texts for the main menu
- Added new skybox textures
- Added new weather textures and expanded particle effects
- Added a new custom hunger/saturation/exhaustion system!
  - Similar to AppleSkin but written from scratch with clean, nondeprecated code and new icons
  - Used apples for the food icons in homage to AppleSkin ♥
- Added a new custom health / hearts display to mimic Colorful Hearts
  - This displays all health hearts on one hud line to clean up the display
  - Each set of 20 health is a new color (red, orange, yellow, etc.)
- Added new custom armor values display to mimic OverloadedArmorBar
  - This displays all armor values on one hud line to clean up the display
  - Each set of 20 armor is a new color (brown, iron, gold, etc.)
- Pale Garden 1:1 backport
  - Because it's awesome and I want it
- Suppressed the "Experimental Features" warning for new instances
- Blocking the gates for animals so we don't have to chase livestock around
  - Animals don't even acknowledge the gates, so they won't all cluster in front of it, either
  - Still need to put a roof on it for goats and frogs since those buggers jump
- Adding floral hedges, blooming hedges, and creeping hedge
- Added snow under trees
- 