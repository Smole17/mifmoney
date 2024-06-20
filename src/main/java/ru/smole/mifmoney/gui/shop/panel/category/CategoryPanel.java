package ru.smole.mifmoney.gui.shop.panel.category;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.MinecraftClient;
import ru.smole.mifmoney.MIFMoney;
import ru.smole.mifmoney.gui.shop.button.category.CategoryButton;
import ru.smole.mifmoney.gui.shop.button.edit.EditToggleButton;
import ru.smole.mifmoney.gui.shop.button.edit.add.AddCategoryButton;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;

@Getter
@Setter
public class CategoryPanel extends Panel {

    public CategoryPanel(Panel panel) {
        super(panel);
    }

    @Override
    public void addWidgets() {
        if (MinecraftClient.getInstance().player.hasPermissionLevel(2)) add(new EditToggleButton(this));

        addAll(MIFMoney.PROXY
                .getClientShopFile()
                .getCategories()
                .stream()
                .map(categoryComponent -> categoryComponent.toButton(this))
                .toList());

        if (ShopScreen.EDITING_STATE && MinecraftClient.getInstance().player.hasPermissionLevel(2)) add(new AddCategoryButton(this));

        val scrollBar = new PanelScrollBar(this, this);

        scrollBar.setMaxValue(widgets.size() > 8 ? 1 + widgets.size() * 21 : 0);

        scrollBar.setCanAlwaysScroll(true);
        add(scrollBar);
    }

    @Override
    public void alignWidgets() {
        align(WidgetLayout.VERTICAL);
        setPosAndSize(-25, 5, parent.width, parent.height - 5);

        val shopScreen = (ShopScreen) parent;

        val currentCategoryButton = shopScreen.getCurrentCategoryButton();
        if (currentCategoryButton != null) {
            val categoryId = currentCategoryButton.getComponent().getId();
            widgets.stream()
                    .filter(widget -> widget instanceof CategoryButton categoryButton && categoryButton.getComponent().getId().equals(categoryId))
                    .map(widget -> (CategoryButton) widget)
                    .findFirst()
                    .ifPresent(shopScreen::setCurrentCategoryButton);
            shopScreen.refreshWidgets();
            return;
        }

        widgets.stream()
                .filter(widget -> widget instanceof CategoryButton)
                .map(widget -> (CategoryButton) widget)
                .findFirst()
                .ifPresent(shopScreen::setCurrentCategoryButton);
        shopScreen.refreshWidgets();
    }
}
