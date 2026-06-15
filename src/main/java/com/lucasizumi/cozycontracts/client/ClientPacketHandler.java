package com.lucasizumi.cozycontracts.client;

import com.lucasizumi.cozycontracts.client.screen.CommunityBoardScreen;
import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientPacketHandler {
    private ClientPacketHandler() {
    }

    public static void openCommunityBoard(OpenCommunityBoardScreenPacket packet) {
        Minecraft.getInstance().setScreen(new CommunityBoardScreen(
                packet.boardPos(),
                packet.day(),
                packet.contracts()));
    }
}
