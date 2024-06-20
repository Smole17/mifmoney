package ru.smole.mifmoney;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.utils.EnvExecutor;
import dev.ftb.mods.ftbteams.event.PlayerLoggedInAfterTeamEvent;
import dev.ftb.mods.ftbteams.event.TeamEvent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import ru.smole.mifmoney.file.server.ServerShopFile;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;
import ru.smole.mifmoney.net.message.server.S2CSyncShopMessage;

public class MIFMoney implements ModInitializer {

    public final static String MOD_ID = "mifmoney";

    public static MIFMoneyCommon PROXY;

    @Override
    public void onInitialize() {
        PROXY = EnvExecutor.getEnvSpecific(
                () -> MIFMoneyClient::new,
                () -> MIFMoneyCommon::new
        );

        registerEvents();

        PROXY.load();
    }

    private void registerEvents() {
        LifecycleEvent.SERVER_BEFORE_START.register(this::serverAboutToStart);
        LifecycleEvent.SERVER_STARTED.register(this::serverStarted);
        LifecycleEvent.SERVER_STOPPING.register(this::serverStopped);
        LifecycleEvent.SERVER_LEVEL_SAVE.register(this::worldSaved);
        TeamEvent.PLAYER_LOGGED_IN.register(this::playerLoggedIn);
    }

    private void serverAboutToStart(MinecraftServer server) {
        ServerShopFile.INSTANCE = new ServerShopFile();
    }

    private void serverStarted(MinecraftServer server) {
        ServerShopFile.INSTANCE.load();
    }

    private void serverStopped(MinecraftServer server) {
        ServerShopFile.INSTANCE.save();
        ServerShopFile.INSTANCE = null;
    }

    private void worldSaved(ServerWorld level) {
        if (ServerShopFile.INSTANCE != null) ServerShopFile.INSTANCE.save();
    }

    private void playerLoggedIn(PlayerLoggedInAfterTeamEvent event) {
        new S2CSyncShopMessage(ServerShopFile.INSTANCE).sendTo(event.getPlayer());
    }
}
