package ru.smole.mifmoney.gui.shop.button.order;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import dev.ftb.mods.ftbquests.quest.Quest;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.smole.mifmoney.component.order.ItemOrderComponent;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;
import ru.smole.mifmoney.gui.shop.screen.edit.EditItemOrderScreen;
import ru.smole.mifmoney.net.message.client.C2SBuyItemMessage;
import ru.smole.mifmoney.util.FormatUtil;

@Getter
@Setter
public class ItemOrderButton extends Button {

    private final String categoryId;
    private ItemOrderComponent component;
    private Quest quest;

    public ItemOrderButton(Panel panel, String categoryId, ItemOrderComponent component) {
        super(panel, component.getItemStack().getName(), CustomIconItem.getIcon(component.getItemStack()));
        this.categoryId = categoryId;
        this.component = component;
        this.quest = component.getQuest();

        setSize(155, 30);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (ShopScreen.EDITING_STATE) {
            new EditItemOrderScreen(this).openGui();
            return;
        }

        new C2SBuyItemMessage(component).sendToServer();
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
        theme.drawString(matrixStack, FormatUtil.format(component.getPrice()), x + 130 - theme.getStringWidth("" + component.getPrice()), y + 11, Theme.SHADOW);

        icon.draw(matrixStack, x - 15, y + 10, w - 80, h - 12);
    }

    public Icon getIcon() {
        return icon;
    }

    public void update() {
        setTitle(component.getItemStack().getName());
        setIcon(CustomIconItem.getIcon(component.getItemStack()));
        setQuest(component.getQuest());
    }
}
