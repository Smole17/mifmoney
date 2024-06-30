package ru.smole.mifmoney.gui.shop.button.order;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import lombok.Getter;
import lombok.Setter;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;
import ru.smole.mifmoney.gui.shop.screen.edit.EditItemOrderScreen;
import ru.smole.mifmoney.net.message.client.C2SBuyItemMessage;

@Getter
@Setter
public class ItemOrderButton extends OrderButton {

    private ItemOrderComponent component;

    public ItemOrderButton(Panel panel, String categoryId, ItemOrderComponent component) {
        super(panel, component.getTextName(), CustomIconItem.getIcon(component.getItemStack()), categoryId, component);
        this.component = component;

        setSize(155, 30);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        playClickSound();

        if (ShopScreen.EDITING_STATE && mouseButton.isRight()) {
            new EditItemOrderScreen(this).openGui();
            return;
        }

        new C2SBuyItemMessage(isCtrlKeyDown(), component).sendToServer();
    }

    @Override
    public void update() {
        setTitle(component.getTextName());
        setIcon(CustomIconItem.getIcon(component.getItemStack()));
    }
}
