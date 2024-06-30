package ru.smole.mifmoney.file.client;

import lombok.Getter;
import lombok.Setter;
import ru.smole.mifmoney.file.ShopFile;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;

@Getter
@Setter
public class ClientShopFile extends ShopFile {

    private ShopScreen shopScreen;

    public final static ClientShopFile INSTANCE = new ClientShopFile();

    @Override
    public void load() {
        if (shopScreen != null) {
            shopScreen.getCategoryPanel().refreshWidgets();
            shopScreen.refreshWidgets();
            System.out.println("Refreshed the shop screen");
        }
        System.out.println("Successfully sync the shop");
    }

    public void openShopGui() {
        shopScreen = new ShopScreen();
        shopScreen.openGui();
    }
}
