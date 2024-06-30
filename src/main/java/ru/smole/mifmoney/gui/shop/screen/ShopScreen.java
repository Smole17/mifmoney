package ru.smole.mifmoney.gui.shop.screen;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
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

    public static boolean EDITING_STATE = false;

    private final CategoryPanel categoryPanel = new CategoryPanel(this);
    private CategoryButton currentCategoryButton;

    @Override
    public void addWidgets() {
        add(categoryPanel);

        if (currentCategoryButton == null) return;

        add(new OrderPanel(this, currentCategoryButton.getComponent()));
    }

    @Override
    public void drawBackground(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(matrixStack, theme, x, y, 176, 166);

        val currency = ModComponents.CURRENCY.get(MinecraftClient.getInstance().player).getValue();

        FormatUtil.drawFormattedMoney(currency, matrixStack, x + 125, y - 16, 16, 16, 16);

        val mifMoneyBalanceText = Text.translatable("mifmoney.balance");

        theme.drawString(matrixStack, mifMoneyBalanceText, x + (width - theme.getStringWidth(mifMoneyBalanceText)) - 54, y - 10, Theme.SHADOW);

        if (currentCategoryButton == null) return;

        val categoryName = currentCategoryButton.getComponent().getName();

        theme.drawString(matrixStack, categoryName, x + (width - theme.getStringWidth(categoryName)) - 100, y - 10, Theme.SHADOW);
    }

    @Override
    public void onBack() {
        closeGui(false);
    }

    @Override
    public boolean onClosedByKey(Key key) {
        EDITING_STATE = false;
        return super.onClosedByKey(key);
    }
}
