package ru.smole.mifmoney.net.message.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.component.order.ItemOrderComponent;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;
import ru.smole.mifmoney.net.message.server.S2CSyncShopMessage;

import java.util.Objects;

@RequiredArgsConstructor
public class C2SDeleteItemOrderMessage extends BaseC2SMessage {

    private final String categoryId;
    private final ItemOrderComponent orderComponent;

    public C2SDeleteItemOrderMessage(PacketByteBuf buf) {
        this.categoryId = buf.readString();
        this.orderComponent = new ItemOrderComponent();
        orderComponent.readNet(buf);
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.DELETE_ITEM_ORDER_COMPONENT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(categoryId);
        orderComponent.writeNet(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MIFMoney.PROXY.getServerShopFile()
                .getCategories()
                .stream()
                .filter(categoryComponent -> categoryComponent.getId().equals(categoryId))
                .findFirst()
                .ifPresent(categoryComponent -> categoryComponent.getOrderComponents().remove(orderComponent));

        new S2CSyncShopMessage(MIFMoney.PROXY.getServerShopFile()).sendToAll(Objects.requireNonNull(context.getPlayer().getServer()));
    }
}
