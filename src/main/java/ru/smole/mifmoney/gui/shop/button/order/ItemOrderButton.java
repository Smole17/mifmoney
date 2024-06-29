package ru.smole.mifmoney.gui.shop.button.order;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.text.Text;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;
import ru.smole.mifmoney.gui.shop.screen.edit.EditItemOrderScreen;
import ru.smole.mifmoney.net.message.client.C2SBuyItemMessage;

@Getter
@Setter
public class ItemOrderButton extends OrderButton {

    private ItemOrderComponent component;

    public ItemOrderButton(Panel panel, String categoryId, ItemOrderComponent component) {
        super(panel, Text.of(component.getName()), CustomIconItem.getIcon(component.getItemStack()), categoryId, component);
        this.component = component;

        setSize(155, 30);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        playClickSound();

        if (ShopScreen.EDITING_STATE) {
            new EditItemOrderScreen(this).openGui();
            return;
        }

        new C2SBuyItemMessage(mouseButton.isRight(), component).sendToServer();
    }

    @Override
    public void update() {
        setTitle(Text.of(component.getName()));
        setIcon(CustomIconItem.getIcon(component.getItemStack()));
    }
}
