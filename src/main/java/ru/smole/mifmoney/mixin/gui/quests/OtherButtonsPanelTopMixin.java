package ru.smole.mifmoney.mixin.gui.quests;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.gui.quests.OtherButtonsPanel;
import dev.ftb.mods.ftbquests.gui.quests.OtherButtonsPanelTop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.smole.mifmoney.gui.shop.button.OpenShopButton;

@Mixin(OtherButtonsPanelTop.class)
public abstract class OtherButtonsPanelTopMixin extends OtherButtonsPanel {

    protected OtherButtonsPanelTopMixin(Panel panel) {
        super(panel);
    }

    @Inject(at = @At(value = "TAIL"), method = "addWidgets", remap = false)
    public void addWidgets(CallbackInfo ci) {
        add(new OpenShopButton(((Panel) (Object) this)));
    }
}
