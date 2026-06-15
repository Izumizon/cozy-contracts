package com.lucasizumi.cozycontracts;

import com.lucasizumi.cozycontracts.block.CommunityBoardBlock;
import com.lucasizumi.cozycontracts.command.CozyContractsCommands;
import com.lucasizumi.cozycontracts.contracts.loading.JsonContractReloadListener;
import com.lucasizumi.cozycontracts.registry.ModBlockEntities;
import com.lucasizumi.cozycontracts.registry.ModBlocks;
import com.lucasizumi.cozycontracts.registry.ModCreativeModeTabs;
import com.lucasizumi.cozycontracts.registry.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CozyContracts.MOD_ID)
public class CozyContracts {
    public static final String MOD_ID = "cozy_contracts";

    public CozyContracts(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(CommunityBoardBlock::onRightClickBlock);
        MinecraftForge.EVENT_BUS.addListener(CozyContractsCommands::register);
        MinecraftForge.EVENT_BUS.addListener(JsonContractReloadListener::register);
    }
}
