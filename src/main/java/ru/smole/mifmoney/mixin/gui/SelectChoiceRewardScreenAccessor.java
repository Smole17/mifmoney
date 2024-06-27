package ru.smole.mifmoney.mixin.gui;

import dev.ftb.mods.ftbquests.gui.SelectChoiceRewardScreen;
import dev.ftb.mods.ftbquests.quest.reward.ChoiceReward;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectChoiceRewardScreen.class)
public interface SelectChoiceRewardScreenAccessor {

    @Accessor
    ChoiceReward getChoiceReward();
}
