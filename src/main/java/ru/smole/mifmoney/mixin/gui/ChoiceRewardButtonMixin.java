package ru.smole.mifmoney.mixin.gui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.gui.SelectChoiceRewardScreen;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import lombok.val;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.smole.mifmoney.net.message.client.C2SGiveItemMessage;

@Mixin(targets = "dev.ftb.mods.ftbquests.gui.SelectChoiceRewardScreen$ChoiceRewardButton")
public abstract class ChoiceRewardButtonMixin extends SimpleTextButton {

    @Shadow
    @Final
    SelectChoiceRewardScreen this$0;

    @Shadow
    @Final
    private WeightedReward weightedReward;

    public ChoiceRewardButtonMixin(Panel panel, Text txt, Icon icon) {
        super(panel, txt, icon);
    }

    @Inject(at = @At("HEAD"), method = "onClicked", remap = false, cancellable = true)
    public void onClicked(MouseButton button, CallbackInfo ci) {

        val choiceReward = ((SelectChoiceRewardScreenAccessor) this$0).getChoiceReward();

        if (choiceReward.id != -1) return;
        playClickSound();

        val table = choiceReward.getTable();

        if (table == null) {
            closeGui();
            ci.cancel();
            return;
        }

        new C2SGiveItemMessage(table.rewards.indexOf(this.weightedReward), table).sendToServer();

        closeGui();
        ci.cancel();
    }
}
