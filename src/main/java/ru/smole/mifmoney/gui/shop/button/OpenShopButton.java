package ru.smole.mifmoney.gui.shop.button;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.gui.quests.ChapterPanel;
import dev.ftb.mods.ftbquests.gui.quests.TabButton;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import lombok.val;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.smole.mifmoney.util.FormatUtil;

public class OpenShopButton extends TabButton {

    private final static Text SHOP_TEXT = Text.translatable("mifmoney.shop");
    private final static Icon SHOP_ICON = ThemeProperties.SHOP_ICON.get();

    public OpenShopButton(Panel panel) {
        super(panel, SHOP_TEXT, SHOP_ICON);
    }

    public void addMouseOverText(TooltipList list) {
        val currency = ModComponents.CURRENCY.get(MinecraftClient.getInstance().player).getValue();

        list.add(this.getTitle());
        list.add(FormatUtil.formatMoney(currency));
    }

    public void onClicked(MouseButton button) {
        this.playClickSound();
        this.handleClick("custom:mifmoney:open_shop_gui");
    }

    public static class OpenShopListButton extends ChapterPanel.ListButton {

        private final OpenShopButton shopButton;

        public OpenShopListButton(ChapterPanel panel) {
            super(panel, SHOP_TEXT, SHOP_ICON);
            shopButton = new OpenShopButton(panel);
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            shopButton.addMouseOverText(list);
        }

        @Override
        public void onClicked(MouseButton mouseButton) {
            shopButton.onClicked(mouseButton);
        }
    }
}
