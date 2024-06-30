package ru.smole.mifmoney.net.message.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.gui.reward.SelectRewardScreen;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

@RequiredArgsConstructor
public class S2CBuyItemResponseMessage extends BaseS2CMessage {

    private final String rewardTableId;
    private final long price;

    public S2CBuyItemResponseMessage(PacketByteBuf buf) {
        this.rewardTableId = buf.readString();
        this.price = buf.readLong();
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.BUY_ITEM_RESPONSE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(rewardTableId);
        buf.writeLong(price);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        new SelectRewardScreen(ItemOrderComponent.getRewardTable(rewardTableId, true), price).openGui();
    }
}
