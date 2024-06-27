package ru.smole.mifmoney.gui.shop.button.edit;

import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import net.minecraft.text.Text;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;

public class EditToggleButton extends Button {

    public EditToggleButton(Panel panel) {
        super(panel,
                Text.translatable("mifmoney.editing.%s".formatted(ShopScreen.EDITING_STATE ? "on" : "off")),
                (ShopScreen.EDITING_STATE ? ThemeProperties.EDITOR_ICON_ON : ThemeProperties.EDITOR_ICON_OFF).get()
        );

        setSize(20, 20);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        ShopScreen.EDITING_STATE = !ShopScreen.EDITING_STATE;

        playClickSound();
        parent.refreshWidgets();
        parent.parent.refreshWidgets();
    }
}
