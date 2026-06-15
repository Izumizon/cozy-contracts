package com.lucasizumi.cozycontracts.shop;

import com.lucasizumi.cozycontracts.registry.ModItems;
import com.lucasizumi.cozycontracts.util.PlayerInventoryItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class ShopPurchaseService {
    private ShopPurchaseService() {
    }

    public static boolean purchase(ServerPlayer player, ResourceLocation shopItemId) {
        ShopItem shopItem = ShopRegistry.getById(shopItemId).orElse(null);
        if (shopItem == null) {
            player.sendSystemMessage(Component.literal(
                    "That shop item is not available."));
            return false;
        }

        int cost = shopItem.getCostTokens();
        if (!PlayerInventoryItems.removeMatching(
                player,
                stack -> stack.is(ModItems.FAVOUR_TOKEN.get()),
                cost)) {
            player.sendSystemMessage(Component.literal(
                    "You need "
                            + cost
                            + " Favour Tokens to buy "
                            + shopItem.getDisplayName()
                            + "."));
            return false;
        }

        // Always insert a fresh copy; inventory insertion mutates the stack to its leftover count.
        ItemStack rewardToDeliver = shopItem.createRewardStack();
        player.getInventory().add(rewardToDeliver);
        if (!rewardToDeliver.isEmpty()) {
            ItemStack leftoverReward = rewardToDeliver.copy();
            player.spawnAtLocation(leftoverReward, 0.0F);
        }

        player.sendSystemMessage(Component.literal(
                "Purchased "
                        + shopItem.getDisplayName()
                        + " for "
                        + cost
                        + " Favour Tokens."));
        return true;
    }
}
