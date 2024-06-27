package ru.smole.mifmoney.net.message.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.gui.SelectChoiceRewardScreen;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.reward.ChoiceReward;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

@RequiredArgsConstructor
public class S2CBuyItemResponseMessage extends BaseS2CMessage {

    private final String rewardTableId;

    public S2CBuyItemResponseMessage(PacketByteBuf buf) {
        this.rewardTableId = buf.readString();
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.BUY_ITEM_RESPONSE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(rewardTableId);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        val choiceReward = new ChoiceReward(null) {
            @Override
            public RewardTable getTable() {
                return ItemOrderComponent.getRewardTable(rewardTableId);
            }
        };
        choiceReward.id = -1;

        new SelectChoiceRewardScreen(choiceReward).openGui();
    }
}
