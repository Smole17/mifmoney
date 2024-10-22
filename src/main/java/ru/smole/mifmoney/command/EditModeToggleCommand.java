package ru.smole.mifmoney.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lombok.experimental.UtilityClass;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import ru.smole.mifmoney.gui.shop.screen.ShopScreen;

@UtilityClass
public class EditModeToggleCommand {
    
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("editmode")
            .requires(source -> source.hasPermissionLevel(2))
            .executes(EditModeToggleCommand::execute));
        
    }
    
    private int execute(CommandContext<ServerCommandSource> ctx) {
        ShopScreen.EDITING_STATE = !ShopScreen.EDITING_STATE;
        
        ctx.getSource().sendFeedback(Text.translatable("mifmoney.editing.%s".formatted(ShopScreen.EDITING_STATE ? "on" : "off")), true);
        return Command.SINGLE_SUCCESS;
    }
}
