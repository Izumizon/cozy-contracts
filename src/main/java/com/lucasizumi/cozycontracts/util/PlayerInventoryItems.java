package com.lucasizumi.cozycontracts.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public final class PlayerInventoryItems {
    private PlayerInventoryItems() {
    }

    public static int countMatching(
            ServerPlayer player,
            Predicate<ItemStack> matches) {
        int total = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (matches.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    public static boolean removeMatching(
            ServerPlayer player,
            Predicate<ItemStack> matches,
            int amount) {
        if (countMatching(player, matches) < amount) {
            return false;
        }

        int remaining = amount;
        for (int slot = 0;
             slot < player.getInventory().getContainerSize() && remaining > 0;
             slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (!matches.test(stack)) {
                continue;
            }

            int removed = Math.min(stack.getCount(), remaining);
            stack.shrink(removed);
            remaining -= removed;
        }

        player.getInventory().setChanged();
        return true;
    }
}
