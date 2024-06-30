package ru.smole.mifmoney.gui.shop.screen.edit;

import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.*;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.ConfigIconItemStack;
import lombok.val;
import net.minecraft.text.Text;
import org.apache.commons.lang3.math.NumberUtils;
import ru.smole.mifmoney.gui.shop.button.category.CategoryButton;
import ru.smole.mifmoney.net.message.client.C2SDeleteCategoryMessage;
import ru.smole.mifmoney.net.message.client.C2SEditCategoryMessage;

public class EditCategoryScreen extends EditScreen {

    private final CategoryButton categoryButton;

    public EditCategoryScreen(CategoryButton categoryButton) {
        setFullscreen();
        this.categoryButton = categoryButton;
    }

    @Override
    public void addWidgets() {
        add(new EditCategoryPanel(this));
        val saveButton = new SimpleTextButton(this, Text.translatable("mifmoney.save_button"), Color4I.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                new C2SEditCategoryMessage(categoryButton.getComponent()).sendToServer();

                playClickSound();
                closeGui();
            }
        };

        val deleteButton = new SimpleTextButton(this, Text.translatable("mifmoney.delete_button"), Color4I.EMPTY) {
            @Override
            public void onClicked(MouseButton mouseButton) {
                new C2SDeleteCategoryMessage(categoryButton.getComponent()).sendToServer();

                playClickSound();
                closeGui();
            }
        };

        saveButton.setPos(50, 115);
        deleteButton.setPos(50, 140);

        add(saveButton);
        add(deleteButton);
    }

    private class EditCategoryPanel extends Panel {

        public EditCategoryPanel(Panel panel) {
            super(panel);
        }

        @Override
        public void addWidgets() {
            val component = categoryButton.getComponent();

            val mainPanel = new BlankPanel(this) {

                @Override
                public void addWidgets() {
                    val externalPanel = new BlankPanel(this) {
                        @Override
                        public void addWidgets() {
                            val iconButton = new SimpleButton(this, Text.translatable("mifmoney.pick_icon"), categoryButton.getIcon(), (simpleButton, mouseButton) -> {
                                val configIcon = new ConfigIconItemStack();
                                new SelectItemStackScreen(configIcon, b -> {
                                    if (b) {
                                        component.setItemStack(configIcon.value);
                                        categoryButton.update();
                                        refreshWidgets();
                                    }

                                    closeGui();
                                    openGui();
                                }).openGui();
                            });

                            add(iconButton);

                            val nameBox = new TextBox(this) {
                                @Override
                                public void onTextChanged() {
                                    component.setName(getText());
                                    categoryButton.update();
                                }
                            };

                            nameBox.setText(component.getName());
                            nameBox.ghostText = "Category name";

                            nameBox.setSize(100, 15);

                            add(nameBox);
                        }

                        @Override
                        public void alignWidgets() {
                            align(WidgetLayout.HORIZONTAL);
                            setPosAndSize(0, 0, 200, 20);
                        }
                    };

                    add(externalPanel);

                    val renderPriorityBox = new TextBox(this) {
                        @Override
                        public void onTextChanged() {
                            component.setRenderPriority(Integer.parseInt(getText()));
                            categoryButton.update();
                        }

                        @Override
                        public boolean isValid(String txt) {
                            return NumberUtils.isParsable(txt);
                        }
                    };

                    renderPriorityBox.setText("" + component.getRenderPriority());
                    renderPriorityBox.ghostText = "Render priority";

                    renderPriorityBox.setPosAndSize(posX + 16, 0, 100, 15);

                    add(renderPriorityBox);
                }

                @Override
                public void alignWidgets() {
                    align(WidgetLayout.VERTICAL);
                    setPosAndSize(25, 0, 200, 45);
                }
            };

            add(mainPanel);
        }

        @Override
        public void alignWidgets() {
            align(WidgetLayout.VERTICAL);
            setPosAndSize(0, 10, parent.width, parent.height);
        }
    }
}
