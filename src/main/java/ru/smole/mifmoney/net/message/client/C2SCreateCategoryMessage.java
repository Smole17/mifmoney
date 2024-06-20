package ru.smole.mifmoney.net.message.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;
import ru.smole.mifmoney.net.message.server.S2CSyncShopMessage;

import java.util.Objects;

@RequiredArgsConstructor
public class C2SCreateCategoryMessage extends BaseC2SMessage {

    private final CategoryComponent component;

    public C2SCreateCategoryMessage(PacketByteBuf buf) {
        this.component = new CategoryComponent();
        component.readNet(buf);
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.CREATE_CATEGORY_COMPONENT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        component.writeNet(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MIFMoney.PROXY.getServerShopFile().getCategories().add(component);

        new S2CSyncShopMessage(MIFMoney.PROXY.getServerShopFile()).sendToAll(Objects.requireNonNull(context.getPlayer().getServer()));
    }
}
