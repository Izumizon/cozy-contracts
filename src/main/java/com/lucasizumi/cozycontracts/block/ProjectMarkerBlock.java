package com.lucasizumi.cozycontracts.block;

import com.lucasizumi.cozycontracts.projects.CommunityProjectService;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.level.BlockEvent;

public class ProjectMarkerBlock extends Block {
    private static final VoxelShape SHAPE = Block.box(
            5.0D,
            0.0D,
            5.0D,
            11.0D,
            16.0D,
            11.0D);
    private static final VoxelShape COLLISION_SHAPE = Block.box(
            6.0D,
            0.0D,
            6.0D,
            10.0D,
            12.0D,
            10.0D);

    public ProjectMarkerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (!level.isClientSide) {
            player.sendSystemMessage(Component.literal(CommunityProjectService.getMarkerStatusMessage(
                    (ServerLevel) level,
                    pos)));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)
                || !(event.getPlayer() instanceof ServerPlayer player)
                || !event.getState().is(com.lucasizumi.cozycontracts.registry.ModBlocks.PROJECT_MARKER.get())) {
            return;
        }

        CommunityProjectService.handleMarkerBroken(player, level, event.getPos());
    }
}
