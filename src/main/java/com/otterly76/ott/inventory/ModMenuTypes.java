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

    public static final DeferredHolder<MenuType<?>, MenuType<ElevatorMenu>> ELEVATOR_MENU =
            MENUS.register("elevator", () -> IMenuTypeExtension.create(ElevatorMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<RecyclingMenu>> RECYCLING_MENU =
            MENUS.register("recycling", () -> IMenuTypeExtension.create(RecyclingMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<WoodcutterMenu>> WOODCUTTER_MENU =
            MENUS.register("woodcutter", () -> IMenuTypeExtension.create((id, inv, buf) -> new WoodcutterMenu(id, inv)));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
