package com.lucasizumi.cozycontracts.client.screen;

import com.lucasizumi.cozycontracts.network.ModNetworking;
import com.lucasizumi.cozycontracts.network.packet.OpenCommunityBoardScreenPacket;
import com.lucasizumi.cozycontracts.network.packet.SubmitCommunityBoardContractPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CommunityBoardScreen extends Screen {
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 210;

    private final BlockPos boardPos;
    private final long day;
    private final List<OpenCommunityBoardScreenPacket.ContractEntry> contracts;

    public CommunityBoardScreen(
            BlockPos boardPos,
            long day,
            List<OpenCommunityBoardScreenPacket.ContractEntry> contracts) {
        super(Component.literal("Community Board"));
        this.boardPos = boardPos;
        this.day = day;
        this.contracts = List.copyOf(contracts);
    }

    @Override
    protected void init() {
        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        int entryY = top + 48;

        for (int index = 0; index < contracts.size(); index++) {
            int slotIndex = index;
            Button submitButton = Button.builder(
                            Component.literal("Submit"),
                            button -> ModNetworking.sendToServer(
                                    new SubmitCommunityBoardContractPacket(boardPos, slotIndex)))
                    .bounds(left + PANEL_WIDTH - 70, entryY + 5, 58, 20)
                    .build();
            submitButton.active = !contracts.get(index).completed();
            addRenderableWidget(submitButton);
            entryY += 46;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xE0101010);
        graphics.renderOutline(left, top, PANEL_WIDTH, PANEL_HEIGHT, 0xFF8B6B3F);

        graphics.drawCenteredString(font, title, width / 2, top + 12, 0xFFF2D7A1);
        graphics.drawCenteredString(
                font,
                "Community Requests - Day " + day,
                width / 2,
                top + 28,
                0xFFFFFFFF);

        int entryY = top + 48;
        for (int index = 0; index < contracts.size(); index++) {
            renderContract(graphics, left + 16, entryY, index + 1, contracts.get(index));
            entryY += 46;
        }

        graphics.drawCenteredString(
                font,
                "Hold the requested item, then click Submit.",
                width / 2,
                top + PANEL_HEIGHT - 20,
                0xFFB8B8B8);
        graphics.drawString(
                font,
                "Board: " + boardPos.toShortString(),
                left + 8,
                top + PANEL_HEIGHT - 10,
                0xFF777777,
                false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderContract(
            GuiGraphics graphics,
            int x,
            int y,
            int slot,
            OpenCommunityBoardScreenPacket.ContractEntry contract) {
        int statusColor = contract.completed() ? 0xFF7DBB78 : 0xFFF2D7A1;
        String status = contract.completed() ? "[Completed]" : "[Available]";

        graphics.drawString(
                font,
                slot + ". " + status + " " + contract.title(),
                x,
                y,
                statusColor,
                false);
        graphics.drawString(
                font,
                contract.requester() + " needs: " + contract.requirementText(),
                x + 10,
                y + 12,
                0xFFE0E0E0,
                false);
        graphics.drawString(
                font,
                "Reward: " + contract.rewardTokens() + " Favour Tokens",
                x + 10,
                y + 24,
                0xFFFFD36A,
                false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
