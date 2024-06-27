package ru.smole.mifmoney.net.message.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.file.ShopFile;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

public class S2CSyncShopMessage extends BaseS2CMessage {

    private final ShopFile shopFile;

    public S2CSyncShopMessage(PacketByteBuf buf) {
        this.shopFile = MIFMoney.PROXY.getClientShopFile();
        System.out.println("Reading the shop from syncing...");
        this.shopFile.readNet(buf);
    }

    public S2CSyncShopMessage(ShopFile shopFile) {
        this.shopFile = shopFile;
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.SYNC_SHOP;
    }

    @Override
    public void write(PacketByteBuf buf) {
        shopFile.writeNet(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        System.out.println("Syncing the shop process...");
        shopFile.load();
    }
}
