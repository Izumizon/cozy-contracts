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
    private static final int PANEL_WIDTH = 350;
    private static final int PANEL_HEIGHT = 230;
    private static final int ENTRY_HEIGHT = 48;

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
        int entryY = top + 52;

        for (int index = 0; index < contracts.size(); index++) {
            int slotIndex = index;
            Button submitButton = Button.builder(
                            Component.literal("Submit"),
                            button -> ModNetworking.sendToServer(
                                    new SubmitCommunityBoardContractPacket(boardPos, slotIndex)))
                    .bounds(left + PANEL_WIDTH - 76, entryY + 8, 62, 20)
                    .build();
            submitButton.active = !contracts.get(index).completed();
            addRenderableWidget(submitButton);
            entryY += ENTRY_HEIGHT;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);

        int left = (width - PANEL_WIDTH) / 2;
        int top = (height - PANEL_HEIGHT) / 2;
        graphics.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xED17120D);
        graphics.renderOutline(left, top, PANEL_WIDTH, PANEL_HEIGHT, 0xFF9B7546);

        graphics.drawCenteredString(font, title, width / 2, top + 11, 0xFFFFDFA3);
        graphics.drawCenteredString(
                font,
                "Community Requests - Day " + day,
                width / 2,
                top + 29,
                0xFFE6D5B8);

        int entryY = top + 52;
        for (int index = 0; index < contracts.size(); index++) {
            int rowColor = contracts.get(index).completed()
                    ? 0x60302D29
                    : 0x604B3925;
            graphics.fill(
                    left + 10,
                    entryY - 4,
                    left + PANEL_WIDTH - 10,
                    entryY + 39,
                    rowColor);
            renderContract(graphics, left + 16, entryY, index + 1, contracts.get(index));
            entryY += ENTRY_HEIGHT;
        }

        graphics.drawCenteredString(
                font,
                "Submit uses matching items from your inventory.",
                width / 2,
                top + PANEL_HEIGHT - 22,
                0xFFC8BBA8);
        graphics.drawString(
                font,
                "Board: " + boardPos.toShortString(),
                left + 8,
                top + PANEL_HEIGHT - 11,
                0xFF746B61,
                false);

        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void renderContract(
            GuiGraphics graphics,
            int x,
            int y,
            int slot,
            OpenCommunityBoardScreenPacket.ContractEntry contract) {
        int statusColor = contract.completed() ? 0xFF88847D : 0xFFFFD38A;
        int detailColor = contract.completed() ? 0xFF85817B : 0xFFE5DED2;
        int rewardColor = contract.completed() ? 0xFF77736D : 0xFFFFC85C;
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
                detailColor,
                false);
        graphics.drawString(
                font,
                "Reward: " + contract.rewardTokens() + " Favour Tokens",
                x + 10,
                y + 24,
                rewardColor,
                false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
