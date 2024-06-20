package ru.smole.mifmoney.gui.shop.screen.edit;

import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.ConfigIconItemStack;
import lombok.val;
import net.minecraft.text.Text;
import org.apache.commons.lang3.math.NumberUtils;
import ru.smole.mifmoney.gui.shop.button.order.ItemOrderButton;
import ru.smole.mifmoney.net.message.client.C2SDeleteItemOrderMessage;
import ru.smole.mifmoney.net.message.client.C2SEditItemOrderMessage;

public class EditItemOrderScreen extends BaseScreen {

    private final ItemOrderButton orderButton;

    public EditItemOrderScreen(ItemOrderButton orderButton) {
        setFullscreen();
        this.orderButton = orderButton;
    }

    @Override
    public void addWidgets() {
        add(new EditItemPanel(this));
        val saveButton = new SimpleTextButton(this, Text.translatable("mifmoney.save_button"), Color4I.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                new C2SEditItemOrderMessage(orderButton.getCategoryId(), orderButton.getComponent()).sendToServer();

                closeGui();
            }
        };
        val deleteButton = new SimpleTextButton(this, Text.translatable("mifmoney.delete_button"), Color4I.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                new C2SDeleteItemOrderMessage(orderButton.getCategoryId(), orderButton.getComponent()).sendToServer();

                closeGui();
            }
        };

        saveButton.setPos(50, 115);
        deleteButton.setPos(50, 140);

        add(saveButton);
        add(deleteButton);
    }

    private class EditItemPanel extends Panel {

        public EditItemPanel(Panel panel) {
            super(panel);
        }

        @Override
        public void addWidgets() {
            val component = orderButton.getComponent();

            val mainPanel = new BlankPanel(this) {
                @Override
                public void addWidgets() {
                    val componentPanel = new BlankPanel(this) {

                        @Override
                        public void addWidgets() {
                            val iconButton = new SimpleButton(this, Text.translatable("mifmoney.pick_item"), orderButton.getIcon(), (simpleButton, mouseButton) -> {
                                val configIcon = new ConfigIconItemStack();
                                new SelectItemStackScreen(configIcon, b -> {
                                    if (b) {
                                        component.setItemStack(configIcon.value);
                                        orderButton.update();
                                        refreshWidgets();
                                    }

                                    closeGui();
                                    openGui();
                                }).openGui();
                            });

                            add(iconButton);

                            val priceBox = new TextBox(this) {
                                @Override
                                public void onTextChanged() {
                                    component.setPrice(Long.parseLong(getText()));
                                }

                                @Override
                                public boolean isValid(String txt) {
                                    return NumberUtils.isParsable(txt);
                                }
                            };

                            priceBox.setText("" + component.getPrice());
                            priceBox.ghostText = "Price";

                            priceBox.setSize(100, 15);

                            add(priceBox);
                        }

                        @Override
                        public void alignWidgets() {
                            align(WidgetLayout.HORIZONTAL);
                            setPosAndSize(0, 0, 200, 20);
                        }
                    };

                    add(componentPanel);

                    val questIdBox = new TextBox(this) {
                        @Override
                        public void onTextChanged() {
                            component.setQuestId(getText());
                            orderButton.update();
                        }
                    };

                    val questId = component.getQuestId();

                    questIdBox.setText(questId == null ? "" : questId);
                    questIdBox.ghostText = "Quest id";

                    questIdBox.setPosAndSize(posX + 16, 0, 100, 15);

                    add(questIdBox);
                }

                @Override
                public void alignWidgets() {
                    align(WidgetLayout.VERTICAL);
                    setPosAndSize(25, 0, 200, 70);
                }
            };

            add(mainPanel);
        }

        @Override
        public void alignWidgets() {
            setPosAndSize(0, 10, parent.width, parent.height);
        }
    }
}