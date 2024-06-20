package ru.smole.mifmoney.gui.shop.button.edit.add;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import lombok.val;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.component.order.ItemOrderComponent;
import ru.smole.mifmoney.gui.shop.screen.edit.EditItemOrderScreen;
import ru.smole.mifmoney.net.message.client.C2SCreateItemOrderMessage;

public class AddItemOrderButton extends Button {

    private final CategoryComponent categoryComponent;

    public AddItemOrderButton(Panel panel, CategoryComponent categoryComponent) {
        super(panel, Text.translatable("mifmoney.add_order"), ThemeProperties.ADD_ICON.get());
        this.categoryComponent = categoryComponent;

        setSize(155, 30);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        val itemOrderComponent = ItemOrderComponent.builder()
                .id(categoryComponent.getOrderComponents().size())
                .itemStack(Items.DIRT.getDefaultStack())
                .price(100)
                .build();

        val categoryId = categoryComponent.getId();

        new EditItemOrderScreen(itemOrderComponent.toButton(parent, categoryId)).openGui();

        new C2SCreateItemOrderMessage(categoryId, itemOrderComponent).sendToServer();
    }
}
