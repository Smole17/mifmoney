package ru.smole.mifmoney;

import ru.smole.mifmoney.file.client.ClientShopFile;
import ru.smole.mifmoney.file.server.ServerShopFile;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

public class MIFMoneyCommon {

    public void load() {
        MIFMoneyCommonNetwork.init();
    }

    public final ServerShopFile getServerShopFile() {
        return ServerShopFile.INSTANCE;
    }

    public ClientShopFile getClientShopFile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
