package ru.smole.mifmoney.gui.shop.screen;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.smole.mifmoney.gui.shop.button.category.CategoryButton;
import ru.smole.mifmoney.gui.shop.panel.category.CategoryPanel;
import ru.smole.mifmoney.gui.shop.panel.order.OrderPanel;
import ru.smole.mifmoney.util.FormatUtil;

@Getter
@Setter
public class ShopScreen extends BaseScreen {

    public static boolean EDITING_STATE;

    private final CategoryPanel categoryPanel;
    private final TextBox orderSearchBox;
    private final SimpleButton closeButton;
    private CategoryButton currentCategoryButton;

    public ShopScreen() {
        orderSearchBox = new TextBox(this) {
            @Override
            public void onTextChanged() {
                ShopScreen.this.refreshWidgets();
            }
        };
        orderSearchBox.setPosAndSize(0, -20, 176, 15);
        orderSearchBox.setFocused(true);
        orderSearchBox.ghostText = Text.translatable("mifmoney.search.enter_order_name").getString();

        categoryPanel = new CategoryPanel(this);
        closeButton = new SimpleButton(this, Text.empty(), Icons.CLOSE, (simpleButton, mouseButton) -> closeGui(false));
        closeButton.setPosAndSize(162, 0, 15, 15);
    }

    @Override
    public void addWidgets() {
        add(orderSearchBox);
        add(categoryPanel);

        if (currentCategoryButton == null) return;

        add(new OrderPanel(this, currentCategoryButton.getComponent(), orderSearchBox.getText()));
        add(closeButton);
    }

    @Override
    public void drawBackground(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(matrixStack, theme, x, y, 176, 190);
        drawBalance(matrixStack, theme, x, y);
        drawCurrentCategoryName(matrixStack, theme, x, y);
    }

    @Override
    public boolean onClosedByKey(Key key) {
        EDITING_STATE = false;
        return super.onClosedByKey(key);
    }

    private void drawBalance(MatrixStack matrixStack, Theme theme, int x, int y) {
        val currency = ModComponents.CURRENCY.get(MinecraftClient.getInstance().player).getValue();

        FormatUtil.drawFormattedMoney(currency, matrixStack, x + 50, y + 169, 16, 16, 16);

        val mifMoneyBalanceText = Text.translatable("mifmoney.balance");

        theme.drawString(matrixStack, mifMoneyBalanceText, x + (width - theme.getStringWidth(mifMoneyBalanceText)) - 130, y + 174, Theme.SHADOW);
    }

    private void drawCurrentCategoryName(MatrixStack matrixStack, Theme theme, int x, int y) {
        if (currentCategoryButton == null) return;

        val currentCategoryButtonComponent = currentCategoryButton.getComponent();
        val categoryName = currentCategoryButtonComponent.getName();

        val dependX = x + (width - theme.getStringWidth(categoryName)) / 2 - 15;
        CustomIconItem.getIcon(currentCategoryButtonComponent.getItemStack()).draw(matrixStack, dependX, y - 40, 16, 16);
        theme.drawString(matrixStack, categoryName, dependX + 18, y - 35, Theme.SHADOW);
    }
}
