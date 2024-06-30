package ru.smole.mifmoney.gui.reward;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.ButtonListBaseScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.quest.loot.WeightedReward;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import ru.smole.mifmoney.net.message.client.C2SBuyRewardMessage;

@RequiredArgsConstructor
public class SelectRewardScreen extends ButtonListBaseScreen {

    private final RewardTable table;
    private final long price;

    @Override
    public void addButtons(Panel panel) {
        table.rewards.forEach(weightedReward -> panel.add(new ChoiceButton(panel, weightedReward)));
    }

    private class ChoiceButton extends SimpleTextButton {
        private final WeightedReward weightedReward;

        private ChoiceButton(Panel panel, WeightedReward r) {
            super(panel, r.reward.getTitle(), r.reward.getIcon());
            this.weightedReward = r;
        }

        public void addMouseOverText(TooltipList list) {
            super.addMouseOverText(list);
            this.weightedReward.reward.addMouseOverText(list);
        }

        public void onClicked(MouseButton button) {
            this.playClickSound();
            this.closeGui();
            new C2SBuyRewardMessage(table.rewards.indexOf(this.weightedReward), table, price).sendToServer();
        }

        public @Nullable Object getIngredientUnderMouse() {
            return this.weightedReward.reward.getIngredient();
        }
    }
}
