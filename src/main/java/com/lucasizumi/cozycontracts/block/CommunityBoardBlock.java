package com.lucasizumi.cozycontracts.block;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.contracts.TemporaryContractSubmission;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
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

        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            CommunityBoardScreenService.open(serverPlayer, level, pos);
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
}
