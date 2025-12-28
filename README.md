But First... The Thank Yous!
=======
- THANK YOU to Imri, my 3D modeling soul sister and midwest clone!
- THANK YOU to Thodor, who tolerated my 25-year lapse in Java to help me start on this project!
- THANK YOU to Ayar for helping me test this insanity!
- THANK YOU to the LDT team for creating and maintaining the MineColonies mod!

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
- Added healing bed function for players 🛏️
- Added Seaglass
- 32x textures (ongoing)
  - yes, Ray, I know
  - 32x is a nice happy medium that gives a little more detail, a little more variation, without straying too far from the "Minecraft" feel
- 3D models for most blocks (ongoing)
- FLOOFY LEAVES! 🍃
- Lowered the Shield when equipped so you can actually, ya know, see what you're doing
- Lowered the Totem of Undying, see above
- Identified and added missing sound files (ongoing)
- Identified and added missing recipes (ongoing)
- Added the evil Hedge
    - Evil Cackling Ensues
    - Added custom damage for the murder plants: Flora 🌼
    - Can be grown as a crop from Hedge Sprouts
    - Can be harvested with a hoe to bypass damage
- Added Create support, first pass (3D models and textures for ores)
- Added Chipped support, first pass (fluffy leaves, fixed some lantern models, fixed ladders(3D))
- Added farmland protection for MineColonies crops
- Added custom schematic dimensions!
  - flat
  - flat w/ water
  - MCol schematic dimension by Thaylar & the LDT Team with fully pregenerated templates
- Added native support for custom music 🎵
- Added copper blocks/slabs/stairs as pathblocks for MineColonies citizens
- Updated the item models for dyes
- Moved 3D texture models for external mods to a built-in resource pack to override mod load order issues
- Added new MCol specific splash texts for the main menu
- Added new skybox textures ☀️🌙
- Added new weather textures and expanded particle effects ⛅
- Added a new custom hunger/saturation/exhaustion system! 🍪
  - Similar to AppleSkin but written from scratch with clean, nondeprecated code and new icons
  - Used apples for the food icons in homage to AppleSkin
- Added a new custom health / hearts display to mimic Colorful Hearts ❤️
  - This displays all health hearts on one hud line to clean up the display
  - Each set of 20 health is a new color
    - Colors follow from Red through Pink with a step between each color
    - If you somehow have more than 230 health it will wrap back to red
    - Also, how in nine hells do you have more than 230 health?
- Added new custom armor values display to mimic OverloadedArmorBar
  - This displays all armor values on one hud line to clean up the display
  - Each set of 20 armor is a new color
    - Iron → Diamond Blue → Lapis Blue → Emerald Green → Gold → Purple → Red
    - This covers 80 points of armor
    - If you have more than 80 armor it will wrap back to Iron
    - Also, if you somehow have more than 80 armor I'd love to know HOW
- Pale Garden 1:1 backport
  - Because it's awesome and I want it
- Suppressed the "Experimental Features" warning for new instances
- Blocking the gates for animals so we don't have to chase livestock around 🐮
  - Animals don't even acknowledge the gates, so they won't all cluster in front of it, either
  - Still need to put a roof on it for goats and frogs since those buggers jump
- Adding floral hedges, blooming hedges, and creeping hedge
- Added snow under trees
- Improved village placement
  - Village placement now checks for rivers, oceans, and various ravines before plopping a building down
  - Farms follow their own rules so, gods speed, farmers!
- Added improved world and terrain Generation! ⛰️🌳
  - Smoothed terrain overall
  - Larger biomes
  - Better biome placement based on temperature and humidity
    - No frozen slopes next to deserts 🙄
  - Better rivers and lakes
    - Some rivers will cut through mountains!
  - Larger cave features
  - Underground rivers and lakes!
- Doubled the length of day and halved the length of night ⏳
- Added Trash Can functionality 🗑️
  - A new Trash Can icon on the Inventory screen
  - Clicking the Trash It button deletes the inventory of the trash
  - Hitting Esc returns you and the items back into your inventory (the emergency oopsie escape)
- Removed the unnecessary Recipe Book button from the Inventory screen 📖
  - Redundant and subpar as we use JEI as a dependency
- Vanilla recipes that require buckets but should not eat said buckets no longer eat the buckets
- Jukeboxes loop the disc they are playing instead if stopping at the end of the song
  - Now you can set up cool ambient music in your worlds!
  - No more missing discs when jukeboxes stop playing!
- 