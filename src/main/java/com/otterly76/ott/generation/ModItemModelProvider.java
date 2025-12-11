package com.otterly76.ott.generation;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import com.otterly76.ott.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "ott", existingFileHelper);
    }

    protected void registerModels() {
        // Placeholder implementation used in ModItems
        if (ModItems.CREAKING_SPAWN_EGG != null) {
            this.withExistingParent(ModItems.CREAKING_SPAWN_EGG.getId().getPath(), this.mcLoc("item/template_spawn_egg"));
        }
    }
}