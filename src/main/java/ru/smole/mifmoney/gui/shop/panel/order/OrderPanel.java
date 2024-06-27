package ru.smole.mifmoney.gui.shop.panel.order;

import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftbquests.FTBQuests;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import ru.smole.mifmoney.component.category.CategoryComponent;
import ru.smole.mifmoney.gui.shop.button.edit.add.AddItemOrderButton;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;

@Getter
public class OrderPanel extends Panel {

    private final CategoryComponent categoryComponent;
    private ScrollBar scrollBar;
    private Panel panel;

    public OrderPanel(Panel panel, CategoryComponent categoryComponent) {
        super(panel);
        this.categoryComponent = categoryComponent;
    }

    @Override
    public void addWidgets() {
        panel = new BlankPanel(this) {
            @Override
            public void addWidgets() {
                addAll(categoryComponent
                        .getOrderComponents()
                        .stream()
                        .map(component -> component.toButton(this, categoryComponent.getId()))
                        .filter(button -> {
                                    val quest = button.getComponent().getQuest();
                                    if (quest != null)
                                        return ShopScreen.EDITING_STATE || FTBQuests.PROXY.getClientPlayerData().isCompleted(quest);

                                    return true;
                                }
                        ).toList()
                );
                if (ShopScreen.EDITING_STATE && MinecraftClient.getInstance().player.hasPermissionLevel(2))
                    add(new AddItemOrderButton(this, categoryComponent));
            }

            @Override
            public void alignWidgets() {
                align(WidgetLayout.VERTICAL);
                setSize(
                        widgets.stream().map(widget -> widget.width).max(Integer::compareTo).orElse(20),
                        Math.max(155, widgets.stream().map(widget -> widget.height).max(Integer::compareTo).orElse(155))
                );
            }
        };

        add(panel);

        scrollBar = new PanelScrollBar(this, panel) {

            @Override
            public boolean shouldDraw() {
                return panel.widgets.size() >= 5;
            }
        };

        scrollBar.setSize(10, 155);
        scrollBar.setMaxValue(1);

        add(scrollBar);
    }

    @Override
    public void alignWidgets() {
        align(WidgetLayout.HORIZONTAL);
        setPosAndSize(5, 5, 250, 155);
    }

    @Override
    public void drawBackground(MatrixStack matrixStack, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(matrixStack, theme, x, y, w, h);

        if (panel == null || panel.widgets.size() < 5) return;

        scrollBar.setMaxValue(1 + panel.widgets.size() * 31);
    }
}
