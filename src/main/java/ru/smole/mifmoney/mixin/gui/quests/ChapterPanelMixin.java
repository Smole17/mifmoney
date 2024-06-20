package ru.smole.mifmoney.mixin.gui.quests;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.gui.quests.ChapterPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.smole.mifmoney.gui.shop.button.OpenShopButton;

@Mixin(ChapterPanel.class)
public abstract class ChapterPanelMixin extends Panel {

    protected ChapterPanelMixin(Panel panel) {
        super(panel);
    }

    @Inject(at = @At("TAIL"), method = "addWidgets", remap = false)
    public void addWidgets(CallbackInfo ci) {
        add(new OpenShopButton.OpenShopListButton(((ChapterPanel) (Object) this)));
    }
}
