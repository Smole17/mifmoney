package ru.smole.mifmoney.mixin.gui;

import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.misc.ButtonListBaseScreen;
import dev.ftb.mods.ftbquests.gui.SelectChoiceRewardScreen;
import dev.ftb.mods.ftbquests.quest.reward.ChoiceReward;
import lombok.val;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ru.smole.mifmoney.net.message.client.C2SGiveItemMessage;

@Mixin(SelectChoiceRewardScreen.class)
public abstract class SelectChoiceRewardScreenMixin extends ButtonListBaseScreen {

    @Shadow @Final private ChoiceReward choiceReward;

    @Override
    public boolean onClosedByKey(Key key) {
        if (choiceReward.id != -1) return super.onClosedByKey(key);

        val table = choiceReward.getTable();

        if (table == null || table.rewards.isEmpty()) return super.onClosedByKey(key);

        new C2SGiveItemMessage(0, table).sendToServer();
        return super.onClosedByKey(key);
    }
}
