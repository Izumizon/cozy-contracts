package com.lucasizumi.cozycontracts.registry;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CozyContracts.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COZY_CONTRACTS =
            CREATIVE_MODE_TABS.register("cozy_contracts", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cozy_contracts"))
                    .icon(() -> ModItems.FAVOUR_TOKEN.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.FAVOUR_TOKEN.get());
                        output.accept(ModBlocks.COMMUNITY_BOARD.get());
                    })
                    .build());

    private ModCreativeModeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
