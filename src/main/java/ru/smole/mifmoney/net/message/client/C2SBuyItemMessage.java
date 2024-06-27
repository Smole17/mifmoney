package ru.smole.mifmoney.net.message.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

@RequiredArgsConstructor
public class C2SBuyItemMessage extends BaseC2SMessage {

    private final boolean isBulk;
    private final ItemOrderComponent itemOrderComponent;

    public C2SBuyItemMessage(PacketByteBuf buf) {
        this.isBulk = buf.readBoolean();
        this.itemOrderComponent = new ItemOrderComponent();
        itemOrderComponent.readNet(buf);
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.BUY_ITEM;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBoolean(isBulk);
        itemOrderComponent.writeNet(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        itemOrderComponent.give(isBulk, (ServerPlayerEntity) context.getPlayer());
    }
}
