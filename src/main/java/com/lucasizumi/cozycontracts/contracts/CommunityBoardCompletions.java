package com.lucasizumi.cozycontracts.contracts;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public final class CommunityBoardCompletions {
    private CommunityBoardCompletions() {
    }

    public static boolean isCompleted(
            Level level,
            BlockPos boardPos,
            long day,
            ResourceLocation contractId) {
        CommunityBoardBlockEntity board = getBoard(level, boardPos);
        return board != null && board.isCompleted(day, contractId);
    }

    public static void markCompleted(
            Level level,
            BlockPos boardPos,
            long day,
            ResourceLocation contractId) {
        CommunityBoardBlockEntity board = getBoard(level, boardPos);
        if (board != null) {
            board.markCompleted(day, contractId);
        }
    }

    public static Set<ResourceLocation> getCompleted(Level level, BlockPos boardPos, long day) {
        CommunityBoardBlockEntity board = getBoard(level, boardPos);
        return board == null ? Set.of() : board.getCompleted(day);
    }

    private static CommunityBoardBlockEntity getBoard(Level level, BlockPos boardPos) {
        BlockEntity blockEntity = level.getBlockEntity(boardPos);
        return blockEntity instanceof CommunityBoardBlockEntity board ? board : null;
    }
}
