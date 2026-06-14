package com.lucasizumi.cozycontracts.command;

import com.lucasizumi.cozycontracts.block.CommunityBoardBlock;
import com.lucasizumi.cozycontracts.contracts.ContractSubmissionService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.RegisterCommandsEvent;

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
                                        IntegerArgumentType.getInteger(context, "slot")))));
        dispatcher.register(root);
    }

    private static int submit(CommandSourceStack source, int commandSlot) {
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception exception) {
            source.sendFailure(Component.literal("This command must be run by a player."));
            return 0;
        }

        if (commandSlot < 1 || commandSlot > 3) {
            source.sendFailure(Component.literal("Request slot must be 1, 2, or 3."));
            return 0;
        }

        BlockPos boardPos = findTargetedBoard(player);
        if (boardPos == null) {
            source.sendFailure(Component.literal(
                    "Look at a Community Board to submit a request."));
            return 0;
        }

        Level level = player.level();
        boolean completed = ContractSubmissionService.submitContractSlot(
                player,
                level,
                boardPos,
                commandSlot - 1);
        return completed ? 1 : 0;
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
