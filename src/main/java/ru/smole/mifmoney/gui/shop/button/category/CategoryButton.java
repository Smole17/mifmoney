package ru.smole.mifmoney.gui.shop.button.category;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import lombok.Getter;
import lombok.val;
import net.minecraft.text.Text;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.gui.shop.panel.category.CategoryPanel;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;
import ru.smole.mifmoney.gui.shop.screen.edit.EditCategoryScreen;

@Getter
public class CategoryButton extends Button {

    private final CategoryComponent component;

    public CategoryButton(CategoryPanel panel, CategoryComponent component) {
        super(panel, Text.of(component.getName()), component.getIcon());
        this.component = component;

        setSize(20, 20);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (ShopScreen.EDITING_STATE) {
            new EditCategoryScreen(this).openGui();
            return;
        }

        val shopScreen = (ShopScreen) parent.parent;

        shopScreen.setCurrentCategoryButton(this);
        shopScreen.refreshWidgets();
    }

    public Icon getIcon() {
        return icon;
    }

    public void update() {
        setTitle(Text.of(component.getName()));
        setIcon(CustomIconItem.getIcon(component.getItemStack()));
    }
}
