package com.otterly76.ott.client.handler;

import com.otterly76.ott.client.render.model.EmissiveModelWrapper;
import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

public class EmissiveModelHandler {
    private static final String[] EMISSIVE_BLOCKS = new String[]{"open_eyeblossom", "potted_open_eyeblossom", "firefly_bush"};

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();

        for(String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation emissiveModelLocation = ModelResourceLocation.standalone(ResourceLocation.withDefaultNamespace("block/" + blockName + "_emissive"));
            BakedModel emissiveModel = modelRegistry.get(emissiveModelLocation);
            if (emissiveModel != null) {
                for(Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
                    ModelResourceLocation location = entry.getKey();
                    if (location.variant().isEmpty()) {
                        String path = location.id().getPath();
                        if (path.equals("block/" + blockName) || path.equals(blockName)) {
                            BakedModel wrappedModel = new EmissiveModelWrapper(entry.getValue(), emissiveModel);
                            modelRegistry.put(location, wrappedModel);
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for(String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation emissiveModelLocation = ModelResourceLocation.standalone(ResourceLocation.withDefaultNamespace("block/" + blockName + "_emissive"));
            event.register(emissiveModelLocation);
        }

    }
}