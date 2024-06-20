package ru.smole.mifmoney.net.message.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;
import ru.smole.mifmoney.net.message.server.S2CSyncShopMessage;

import java.util.Objects;

@RequiredArgsConstructor
public class C2SEditCategoryMessage extends BaseC2SMessage {

    private final CategoryComponent component;

    public C2SEditCategoryMessage(PacketByteBuf buf) {
        this.component = new CategoryComponent();
        component.readNet(buf);
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.EDIT_CATEGORY_COMPONENT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        component.writeNet(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        MIFMoney.PROXY.getServerShopFile()
                .getCategories()
                .stream()
                .filter(component -> component.equals(this.component))
                .findFirst()
                .ifPresent(component -> component.update(this.component));

        new S2CSyncShopMessage(MIFMoney.PROXY.getServerShopFile()).sendToAll(Objects.requireNonNull(context.getPlayer().getServer()));
    }
}
