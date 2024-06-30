package ru.smole.mifmoney.gui.shop.button.order;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import lombok.Getter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.smole.mifmoney.component.order.OrderComponent;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;
import ru.smole.mifmoney.util.FormatUtil;

@Getter
public class OrderButton extends Button {

    private final String categoryId;
    private final OrderComponent component;

    public OrderButton(Panel panel, Text t, Icon i, String categoryId, OrderComponent component) {
        super(panel, t, i);
        this.categoryId = categoryId;
        this.component = component;

        setSize(155, 30);
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        list.add(Text.translatable("mifmoney.click_to_buy"));
    }

    @Override
    public void drawIcon(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        icon.draw(matrixStack, x - 15, y + 10, w - 80, h - 12);
    }

    @Override
    public void draw(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        GuiHelper.setupDrawing();

        drawBackground(matrixStack, theme, x, y, w, h);

        theme.drawString(matrixStack, getTitle(), x + 5, y + 2, Theme.SHADOW);

        FormatUtil.drawFormattedMoney(component.getPrice() * (ShopScreen.isCtrlKeyDown() ? 10 : 1), matrixStack, x + 70, y + 7, w - 80, h - 12, 16);

        icon.draw(matrixStack, x - 15, y + 10, w - 80, h - 12);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {

    }

    public void update() {}

    public Icon getIcon() {
        return icon;
    }
}
