package com.lucasizumi.cozycontracts.block;

import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardScreenService;
import com.lucasizumi.cozycontracts.contracts.TemporaryContractSubmission;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class CommunityBoardBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape NORTH_SOUTH_SHAPE = Shapes.or(
            box(1.0D, 5.0D, 6.0D, 15.0D, 16.0D, 10.0D),
            box(2.0D, 0.0D, 7.0D, 4.0D, 6.0D, 9.0D),
            box(12.0D, 0.0D, 7.0D, 14.0D, 6.0D, 9.0D));
    private static final VoxelShape EAST_WEST_SHAPE = Shapes.or(
            box(6.0D, 5.0D, 1.0D, 10.0D, 16.0D, 15.0D),
            box(7.0D, 0.0D, 2.0D, 9.0D, 6.0D, 4.0D),
            box(7.0D, 0.0D, 12.0D, 9.0D, 6.0D, 14.0D));

    public CommunityBoardBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(
                FACING,
                context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            net.minecraft.world.level.BlockGetter level,
            BlockPos pos,
            CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X
                ? EAST_WEST_SHAPE
                : NORTH_SOUTH_SHAPE;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
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
