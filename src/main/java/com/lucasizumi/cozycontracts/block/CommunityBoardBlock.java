package com.lucasizumi.cozycontracts.block;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardCompletions;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardContractState;
import com.lucasizumi.cozycontracts.contracts.Contract;
import com.lucasizumi.cozycontracts.contracts.TemporaryContractSubmission;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Set;

public class CommunityBoardBlock extends BaseEntityBlock {
    public CommunityBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CommunityBoardBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                TemporaryContractSubmission.trySubmitHeldItem(player, level, pos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            showContractPreview(player, level, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();

        if (event.getHand() != InteractionHand.MAIN_HAND
                || !player.isShiftKeyDown()
                || !(level.getBlockState(event.getPos()).getBlock() instanceof CommunityBoardBlock)) {
            return;
        }

        if (!level.isClientSide) {
            TemporaryContractSubmission.trySubmitHeldItem(player, level, event.getPos());
        }

        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        event.setCanceled(true);
    }

    private static void showContractPreview(Player player, Level level, BlockPos boardPos) {
        long day = level.getDayTime() / 24000L;
        Set<ResourceLocation> completed =
                CommunityBoardCompletions.getCompleted(level, boardPos, day);
        player.sendSystemMessage(Component.literal("Community Requests - Day " + day));

        int number = 1;
        for (Contract contract : CommunityBoardContractState.getActiveContracts(level, boardPos)) {
            String completionPrefix = completed.contains(contract.getId()) ? "[Completed] " : "";
            player.sendSystemMessage(Component.literal(
                    number
                            + ". "
                            + completionPrefix
                            + contract.getTitle()
                            + " \u2014 "
                            + contract.getRequester()
                            + " needs: "
                            + contract.getRequirement().getPreviewText()
                            + " \u2014 Reward: "
                            + contract.getRewardTokens()
                            + " Favour Tokens"));
            number++;
        }

        player.sendSystemMessage(Component.literal(
                "Hold the requested item and use /cozycontracts submit <1-3> "
                        + "to submit a specific request."));
    }
}
