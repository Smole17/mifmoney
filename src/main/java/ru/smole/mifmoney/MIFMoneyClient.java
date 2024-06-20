package ru.smole.mifmoney;

import dev.architectury.event.EventResult;
import dev.ftb.mods.ftblibrary.ui.CustomClickEvent;
import lombok.Getter;
import net.minecraft.util.Identifier;
import ru.smole.mifmoney.file.client.ClientShopFile;

@Getter
public class MIFMoneyClient extends MIFMoneyCommon {

    public final static Identifier SHOP_GUI_IDENTIFIER = new Identifier("mifmoney", "open_shop_gui");

    @Override
    public void load() {
        super.load();
        CustomClickEvent.EVENT.register(this::handleClick);
    }

    @Override
    public ClientShopFile getClientShopFile() {
        return ClientShopFile.INSTANCE;
    }

    private EventResult handleClick(CustomClickEvent event) {
        if (event.id().equals(SHOP_GUI_IDENTIFIER)) {
            getClientShopFile().openShopGui();

            return EventResult.interruptDefault();
        }

        return EventResult.pass();
    }
}
