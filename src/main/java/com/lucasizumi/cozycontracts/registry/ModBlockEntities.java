package com.lucasizumi.cozycontracts.registry;

import com.lucasizumi.cozycontracts.CozyContracts;
import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CozyContracts.MOD_ID);

    public static final RegistryObject<BlockEntityType<CommunityBoardBlockEntity>> COMMUNITY_BOARD =
            BLOCK_ENTITIES.register(
                    "community_board",
                    () -> BlockEntityType.Builder.of(
                                    CommunityBoardBlockEntity::new,
                                    ModBlocks.COMMUNITY_BOARD.get())
                            .build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
