package com.lucasizumi.cozycontracts.command;

import com.lucasizumi.cozycontracts.block.CommunityBoardBlock;
import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardPreviewService;
import com.lucasizumi.cozycontracts.contracts.ContractRegistry;
import com.lucasizumi.cozycontracts.contracts.ContractSubmissionService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.List;
import java.util.Set;

public final class CozyContractsCommands {
    private static final double BOARD_TARGET_DISTANCE = 5.0D;

    private CozyContractsCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        registerRoot(dispatcher, "cozycontracts");
        registerRoot(dispatcher, "cc");
    }

    private static void registerRoot(
            CommandDispatcher<CommandSourceStack> dispatcher,
            String commandName) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(commandName)
                .then(Commands.literal("submit")
                        .then(Commands.argument("slot", IntegerArgumentType.integer())
                                .executes(context -> submit(
                                        context.getSource(),
                                        IntegerArgumentType.getInteger(context, "slot")))))
                .then(Commands.literal("preview")
                        .executes(context -> preview(context.getSource())))
                .then(Commands.literal("debug")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("board")
                                .executes(context -> debugBoard(context.getSource())))
                        .then(Commands.literal("contracts")
                                .executes(context -> debugContracts(context.getSource()))));
        dispatcher.register(root);
    }

    private static int submit(CommandSourceStack source, int commandSlot) {
        ServerPlayer player = getPlayer(source);
        if (player == null) {
            return 0;
        }

        if (commandSlot < 1 || commandSlot > 3) {
            source.sendFailure(Component.literal("Request slot must be 1, 2, or 3."));
            return 0;
        }

        BlockPos boardPos = requireTargetedBoard(
                source,
                player,
                "Look at a Community Board to submit a request.");
        if (boardPos == null) {
            return 0;
        }

        return ContractSubmissionService.submitContractSlot(
                player,
                player.level(),
                boardPos,
                commandSlot - 1) ? 1 : 0;
    }

    private static int preview(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) {
            return 0;
        }

        BlockPos boardPos = requireTargetedBoard(
                source,
                player,
                "Look at a Community Board to view requests.");
        if (boardPos == null) {
            return 0;
        }

        CommunityBoardPreviewService.sendPreview(player, player.level(), boardPos);
        return 1;
    }

    private static int debugContracts(CommandSourceStack source) {
        List<ResourceLocation> contractIds = ContractRegistry.getAllContracts().stream()
                .map(contract -> contract.getId())
                .limit(5)
                .toList();

        source.sendSuccess(
                () -> Component.literal(
                        "Contract registry total: "
                                + ContractRegistry.getAllContracts().size()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Source: "
                                + (ContractRegistry.isUsingJsonContracts()
                                ? "JSON/datapack contracts"
                                : "temporary Java fallback")),
                false);
        source.sendSuccess(
                () -> Component.literal("First contract IDs: " + contractIds),
                false);
        return 1;
    }

    private static int debugBoard(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) {
            return 0;
        }

        BlockPos boardPos = requireTargetedBoard(
                source,
                player,
                "Look at a Community Board to debug it.");
        if (boardPos == null) {
            return 0;
        }

        Level level = player.level();
        if (!(level.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity board)) {
            source.sendFailure(Component.literal("The targeted board has no board block entity."));
            return 0;
        }

        long day = level.getDayTime() / 24000L;
        List<ResourceLocation> activeIds =
                board.getStoredActiveContractsDay() == day
                        ? board.getStoredActiveContractIds()
                        : List.of();
        Set<ResourceLocation> completedIds =
                board.getStoredCompletionDay() == day
                        ? board.getStoredCompletedContractIds()
                        : Set.of();

        player.sendSystemMessage(Component.literal("Community Board Debug"));
        player.sendSystemMessage(Component.literal("Position: " + boardPos.toShortString()));
        player.sendSystemMessage(Component.literal(
                "Dimension: " + level.dimension().location()));
        player.sendSystemMessage(Component.literal("Minecraft day: " + day));
        player.sendSystemMessage(Component.literal("Stored active IDs: " + activeIds));
        player.sendSystemMessage(Component.literal("Completed IDs: " + completedIds));

        for (ResourceLocation contractId : activeIds) {
            String title = ContractRegistry.getById(contractId)
                    .map(contract -> contract.getTitle())
                    .orElse("<missing contract>");
            player.sendSystemMessage(Component.literal(
                    "- "
                            + contractId
                            + " | "
                            + title
                            + " | completed="
                            + completedIds.contains(contractId)));
        }

        return 1;
    }

    private static ServerPlayer getPlayer(CommandSourceStack source) {
        try {
            return source.getPlayerOrException();
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return null;
        }
    }

    private static BlockPos requireTargetedBoard(
            CommandSourceStack source,
            ServerPlayer player,
            String failureMessage) {
        BlockPos boardPos = findTargetedBoard(player);
        if (boardPos == null) {
            source.sendFailure(Component.literal(failureMessage));
        }
        return boardPos;
    }

    private static BlockPos findTargetedBoard(ServerPlayer player) {
        HitResult hitResult = player.pick(BOARD_TARGET_DISTANCE, 0.0F, false);
        if (!(hitResult instanceof BlockHitResult blockHit)
                || blockHit.getType() != HitResult.Type.BLOCK) {
            return null;
        }

        BlockPos blockPos = blockHit.getBlockPos();
        return player.level().getBlockState(blockPos).getBlock() instanceof CommunityBoardBlock
                ? blockPos
                : null;
    }
}
