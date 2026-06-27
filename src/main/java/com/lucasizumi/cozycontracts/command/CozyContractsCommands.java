package com.lucasizumi.cozycontracts.command;

import com.lucasizumi.cozycontracts.block.CommunityBoardBlock;
import com.lucasizumi.cozycontracts.block.entity.CommunityBoardBlockEntity;
import com.lucasizumi.cozycontracts.contracts.CommunityBoardPreviewService;
import com.lucasizumi.cozycontracts.contracts.Contract;
import com.lucasizumi.cozycontracts.contracts.ContractRegistry;
import com.lucasizumi.cozycontracts.contracts.ContractSubmissionService;
import com.lucasizumi.cozycontracts.kitchen.KitchenBoardService;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrder;
import com.lucasizumi.cozycontracts.kitchen.KitchenOrderRegistry;
import com.lucasizumi.cozycontracts.projects.CommunityImprovementType;
import com.lucasizumi.cozycontracts.projects.CommunityProject;
import com.lucasizumi.cozycontracts.projects.CommunityProjectAssignment;
import com.lucasizumi.cozycontracts.projects.CommunityProjectRegistry;
import com.lucasizumi.cozycontracts.projects.CommunityProjectService;
import com.lucasizumi.cozycontracts.projects.ProjectSiteOrder;
import com.lucasizumi.cozycontracts.projects.ProjectSiteOrderRegistry;
import com.lucasizumi.cozycontracts.projects.ProjectValidationResult;
import com.lucasizumi.cozycontracts.settlement.Settlement;
import com.lucasizumi.cozycontracts.settlement.SettlementService;
import com.lucasizumi.cozycontracts.shop.ShopCategory;
import com.lucasizumi.cozycontracts.shop.ShopItem;
import com.lucasizumi.cozycontracts.shop.ShopRegistry;
import com.lucasizumi.cozycontracts.shop.ShopStockService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
                                .executes(context -> debugContracts(context.getSource())))
                        .then(Commands.literal("shop")
                                .executes(context -> debugShop(context.getSource())))
                        .then(Commands.literal("kitchen")
                                .executes(context -> debugKitchen(context.getSource())))
                        .then(Commands.literal("projects")
                                .executes(context -> debugProjects(context.getSource())))
                        .then(Commands.literal("settlement")
                                .executes(context -> debugSettlement(context.getSource())))
                        .then(Commands.literal("contract")
                                .then(Commands.argument("id", ResourceLocationArgument.id())
                                        .executes(context -> debugContract(
                                                context.getSource(),
                                                ResourceLocationArgument.getId(
                                                        context,
                                                        "id"))))));
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

        return ContractSubmissionService.submitContractSlotFromInventory(
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
                .map(Contract::getId)
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

    private static int debugContract(CommandSourceStack source, ResourceLocation contractId) {
        Optional<Contract> contract = ContractRegistry.getById(contractId);
        if (contract.isEmpty()) {
            source.sendFailure(Component.literal("Contract not found: " + contractId));
            return 0;
        }

        Contract value = contract.get();
        source.sendSuccess(() -> Component.literal("Contract: " + value.getId()), false);
        source.sendSuccess(() -> Component.literal("Title: " + value.getTitle()), false);
        source.sendSuccess(() -> Component.literal("Requester: " + value.getRequester()), false);
        source.sendSuccess(() -> Component.literal("Category: " + value.getCategory()), false);
        source.sendSuccess(() -> Component.literal("Difficulty: " + value.getDifficulty()), false);
        source.sendSuccess(() -> Component.literal("Weight: " + value.getWeight()), false);
        source.sendSuccess(
                () -> Component.literal(
                        "Requirement: " + value.getRequirement().getPreviewText()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Reward: " + value.getRewardTokens() + " Favour Tokens"),
                false);
        return 1;
    }

    private static int debugShop(CommandSourceStack source) {
        source.sendSuccess(
                () -> Component.literal(
                        "Shop item total: " + ShopRegistry.getAllItems().size()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Known shop categories: " + List.of(ShopCategory.values())),
                false);

        for (ShopItem item : ShopRegistry.getAllItems()) {
            source.sendSuccess(
                    () -> Component.literal(
                            "- " + item.getId() + " | categories=" + item.getCategories()),
                    false);
        }

        ServerPlayer player = tryGetPlayer(source);
        if (player == null) {
            return 1;
        }

        BlockPos boardPos = findTargetedBoard(player);
        if (boardPos == null) {
            source.sendSuccess(
                    () -> Component.literal("Look at a Community Board to view board shop stock."),
                    false);
            return 1;
        }

        ServerLevel level = player.serverLevel();
        Set<ShopCategory> boardCategories =
                ShopStockService.getShopCategoriesForBoard(level, boardPos);
        List<ResourceLocation> visibleItemIds =
                ShopStockService.getShopItemsForBoard(level, boardPos).stream()
                        .map(ShopItem::getId)
                        .toList();

        source.sendSuccess(
                () -> Component.literal(
                        "Current board: " + boardPos.toShortString()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Current board shop categories: " + boardCategories),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Current board visible stock IDs: " + visibleItemIds),
                false);

        return 1;
    }

    private static int debugKitchen(CommandSourceStack source) {
        ServerPlayer player = tryGetPlayer(source);
        if (player == null) {
            sendKitchenRegistryDebug(source);
            return 1;
        }

        BlockPos boardPos = findTargetedBoard(player);
        if (boardPos == null) {
            sendKitchenRegistryDebug(source);
            source.sendSuccess(
                    () -> Component.literal(
                            "Look at a Community Board to view board kitchen orders."),
                    false);
            return 1;
        }

        ServerLevel level = player.serverLevel();
        long day = level.getDayTime() / 24000L;
        CommunityBoardBlockEntity board =
                level.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity value
                        ? value
                        : null;
        source.sendSuccess(
                () -> Component.literal(
                        "Current board: " + boardPos.toShortString()),
                false);
        source.sendSuccess(
                () -> Component.literal("Minecraft day: " + day),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Total loaded Kitchen orders: "
                                + KitchenOrderRegistry.getAllOrders().size()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Source: "
                                + (KitchenOrderRegistry.isUsingJsonOrders()
                                ? "JSON/datapack kitchen orders"
                                : "Java fallback kitchen orders")),
                false);

        List<KitchenOrder> activeOrders =
                KitchenBoardService.getKitchenOrdersForBoard(level, boardPos);
        source.sendSuccess(
                () -> Component.literal(
                        "Selected active Kitchen orders: " + activeOrders.size()),
                false);

        for (KitchenOrder order : activeOrders) {
            int deliveredCount = board == null
                    ? 0
                    : board.getKitchenDeliveryCount(day, order.getId());
            source.sendSuccess(
                    () -> Component.literal(
                            "- "
                                    + order.getId()
                                    + " | "
                                    + order.getType()
                                    + " | "
                                    + order.getTitle()
                                    + " | requires="
                                    + order.getRequirement().getDebugText()
                                    + " | reward="
                                    + order.getRewardTokens()
                                    + " | limit="
                                    + order.getDailyLimit()
                                    + " | delivered="
                                    + deliveredCount
                                    + "/"
                                    + order.getDailyLimit()
                                    + " | canDeliver="
                                    + (deliveredCount < order.getDailyLimit())),
                    false);
        }

        return 1;
    }

    private static void sendKitchenRegistryDebug(CommandSourceStack source) {
        source.sendSuccess(
                () -> Component.literal(
                        "Kitchen order total: "
                                + KitchenOrderRegistry.getAllOrders().size()),
                false);
        source.sendSuccess(
                () -> Component.literal(
                        "Source: "
                                + (KitchenOrderRegistry.isUsingJsonOrders()
                                ? "JSON/datapack kitchen orders"
                                : "Java fallback kitchen orders")),
                false);
        for (KitchenOrder order : KitchenOrderRegistry.getAllOrders()) {
            source.sendSuccess(
                    () -> Component.literal(
                            "- "
                                    + order.getId()
                                    + " | type="
                                    + order.getType()
                                    + " | requires="
                                    + order.getRequirement().getDebugText()
                                    + " | reward="
                                    + order.getRewardTokens()
                                    + " | limit="
                                    + order.getDailyLimit()),
                    false);
        }
    }

    private static int debugProjects(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) {
            return 0;
        }

        BlockPos boardPos = requireTargetedBoard(
                source,
                player,
                "Look at a Community Board to debug Community Projects.");
        if (boardPos == null) {
            return 0;
        }

        ServerLevel level = player.serverLevel();
        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        List<BlockPos> nearbyMarkers = findNearbyProjectMarkers(level, settlement.getCenter(), 64);

        player.sendSystemMessage(Component.literal("Community Projects Debug"));
        player.sendSystemMessage(Component.literal("Settlement ID: " + settlement.getId()));
        player.sendSystemMessage(Component.literal(
                "Community Improvements: Farming "
                        + settlement.getCommunityImprovementCount(CommunityImprovementType.FARMING)
                        + ", Builder "
                        + settlement.getCommunityImprovementCount(CommunityImprovementType.BUILDER)
                        + ", Decor "
                        + settlement.getCommunityImprovementCount(CommunityImprovementType.DECOR)
                        + ", Kitchen "
                        + settlement.getCommunityImprovementCount(CommunityImprovementType.KITCHEN)));
        player.sendSystemMessage(Component.literal(
                "Completed projects: " + settlement.getCompletedProjectIds()));
        player.sendSystemMessage(Component.literal(
                "Active project assignments: " + settlement.getActiveProjectAssignments()));
        player.sendSystemMessage(Component.literal(
                "Completed Project Sites: " + settlement.getCompletedProjectSites()));
        player.sendSystemMessage(Component.literal(
                "Nearby Project Markers: " + nearbyMarkers.size()));
        for (BlockPos marker : nearbyMarkers.stream().limit(8).toList()) {
            String markerState = "unassigned";
            if (settlement.getCompletedProjectSites().containsValue(marker)) {
                markerState = "completed site";
            } else if (settlement.hasMarkerAssigned(marker)) {
                markerState = "active";
            }
            player.sendSystemMessage(Component.literal(
                    "- Marker: " + marker.toShortString() + " | " + markerState));
        }

        for (CommunityProject project : CommunityProjectRegistry.getStarterProjects()) {
            CommunityProjectAssignment assignment =
                    settlement.getProjectAssignment(project.getId()).orElse(null);
            BlockPos completedSite =
                    settlement.getCompletedProjectSite(project.getId()).orElse(null);
            player.sendSystemMessage(Component.literal(
                    project.getId()
                            + " | "
                            + project.getTitle()
                            + " | completed="
                            + settlement.isProjectCompleted(project.getId())
                            + " | marker="
                            + (assignment == null ? "<none>" : assignment.markerPos().toShortString())
                            + " | projectSite="
                            + (completedSite == null ? "<none>" : completedSite.toShortString())));
            if (completedSite != null) {
                List<ProjectSiteOrder> siteOrders =
                        ProjectSiteOrderRegistry.getAvailableOrdersForProject(project.getId());
                player.sendSystemMessage(Component.literal(
                        "  availableSiteOrders=" + siteOrders.size()));
                for (ProjectSiteOrder order : siteOrders) {
                    player.sendSystemMessage(Component.literal(
                            "  - "
                                    + order.getId()
                                    + " | "
                                    + order.getType().getDisplayName()
                                    + " | requires="
                                    + order.getRequirementPreview()
                                    + " | reward="
                                    + order.getRewardTokens()
                                    + " | modded="
                                    + order.isModded()));
                }
            }
            if (assignment != null && !settlement.isProjectCompleted(project.getId())) {
                ProjectValidationResult validation =
                        CommunityProjectService.validateAssignment(level, assignment, project);
                player.sendSystemMessage(Component.literal(
                        "  validationReady=" + validation.ready()));
                for (String missing : validation.missingRequirements()) {
                    player.sendSystemMessage(Component.literal("  - " + missing));
                }
            }
        }

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

        ServerLevel level = player.serverLevel();
        if (!(level.getBlockEntity(boardPos) instanceof CommunityBoardBlockEntity board)) {
            source.sendFailure(Component.literal("The targeted board has no board block entity."));
            return 0;
        }

        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);

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
        player.sendSystemMessage(Component.literal("Settlement ID: " + settlement.getId()));
        player.sendSystemMessage(Component.literal(
                "Settlement center: " + settlement.getCenter().toShortString()));
        player.sendSystemMessage(Component.literal("Stored active IDs: " + activeIds));
        player.sendSystemMessage(Component.literal("Completed IDs: " + completedIds));

        for (ResourceLocation contractId : activeIds) {
            String title = ContractRegistry.getById(contractId)
                    .map(Contract::getTitle)
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

    private static int debugSettlement(CommandSourceStack source) {
        ServerPlayer player = getPlayer(source);
        if (player == null) {
            return 0;
        }

        BlockPos boardPos = requireTargetedBoard(
                source,
                player,
                "Look at a Community Board to debug its settlement.");
        if (boardPos == null) {
            return 0;
        }

        ServerLevel level = player.serverLevel();
        Settlement settlement = SettlementService.getOrCreateSettlementForBoard(level, boardPos);
        double distance = distanceBetween(boardPos, settlement.getCenter());

        player.sendSystemMessage(Component.literal("Settlement Debug"));
        player.sendSystemMessage(Component.literal("Settlement ID: " + settlement.getId()));
        player.sendSystemMessage(Component.literal(
                "Settlement center: " + settlement.getCenter().toShortString()));
        player.sendSystemMessage(Component.literal(
                "Current board: " + boardPos.toShortString()));
        player.sendSystemMessage(Component.literal(
                "Distance from center: "
                        + String.format(Locale.ROOT, "%.2f", distance)
                        + " blocks"));
        player.sendSystemMessage(Component.literal(
                "Settlements in this dimension: "
                        + SettlementService.getSettlementCount(level)));
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

    private static ServerPlayer tryGetPlayer(CommandSourceStack source) {
        try {
            return source.getPlayerOrException();
        } catch (Exception exception) {
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

    private static double distanceBetween(BlockPos first, BlockPos second) {
        double x = first.getX() - second.getX();
        double y = first.getY() - second.getY();
        double z = first.getZ() - second.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    private static List<BlockPos> findNearbyProjectMarkers(
            ServerLevel level,
            BlockPos center,
            int radius) {
        java.util.ArrayList<BlockPos> markers = new java.util.ArrayList<>();
        int yRadius = 24;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -yRadius; y <= yRadius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.offset(x, y, z);
                    if (center.distSqr(pos) <= radius * radius
                            && level.getBlockState(pos).is(com.lucasizumi.cozycontracts.registry.ModBlocks.PROJECT_MARKER.get())) {
                        markers.add(pos.immutable());
                    }
                }
            }
        }

        return markers;
    }
}
