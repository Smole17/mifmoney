package ru.smole.mifmoney.gui.shop.button.edit.add;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import lombok.val;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.gui.shop.panel.category.CategoryPanel;
import ru.smole.mifmoney.gui.shop.screen.edit.EditCategoryScreen;
import ru.smole.mifmoney.net.message.client.C2SCreateCategoryMessage;

import java.util.ArrayList;
import java.util.UUID;

public class AddCategoryButton extends Button {

    public AddCategoryButton(Panel panel) {
        super(panel, Text.translatable("mifmoney.add_category"), ThemeProperties.ADD_ICON.get());

        setSize(20, 20);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        val categoryPanel = (CategoryPanel) parent;

        val categoryComponent = CategoryComponent
                .builder()
                .id(UUID.randomUUID().toString())
                .name("Dirty category")
                .itemStack(Items.DIRT.getDefaultStack())
                .orderComponents(new ArrayList<>())
                .build();

        new EditCategoryScreen(categoryComponent.toButton(categoryPanel)).openGui();

        new C2SCreateCategoryMessage(categoryComponent).sendToServer();
    }
}
