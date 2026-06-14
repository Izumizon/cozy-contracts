package com.lucasizumi.cozycontracts.registry;

import com.lucasizumi.cozycontracts.CozyContracts;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CozyContracts.MOD_ID);

    public static final RegistryObject<Item> FAVOUR_TOKEN =
            ITEMS.register("favour_token", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COMMUNITY_BOARD =
            ITEMS.register("community_board",
                    () -> new BlockItem(ModBlocks.COMMUNITY_BOARD.get(), new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
