package com.otterly76.ott.inventory;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, Constants.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TrashMenu>> TRASH_MENU =
            MENUS.register("trash_menu", () -> IMenuTypeExtension.create(TrashMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<ModAnvilMenu>> ANVIL_MENU_TYPE =
            MENUS.register("repair", () -> new MenuType<>(ModAnvilMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>, MenuType<WeatheringStationMenu>> WEATHERING_STATION_MENU =
            MENUS.register("weathering_station", () -> IMenuTypeExtension.create(WeatheringStationMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}