package ru.smole.mifmoney.net.message.client;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

@RequiredArgsConstructor
public class C2SBuyRewardMessage extends BaseC2SMessage {

    private final int index;
    private final RewardTable rewardTable;
    private final long price;

    public C2SBuyRewardMessage(PacketByteBuf buf) {
        this.index = buf.readInt();
        this.rewardTable = new RewardTable(ServerQuestFile.INSTANCE);
        rewardTable.readNetData(buf);
        this.price = buf.readLong();
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.BUY_REWARD;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(index);
        rewardTable.writeNetData(buf);
        buf.writeLong(price);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        val player = (ServerPlayerEntity) context.getPlayer();

        rewardTable.rewards.get(index).reward.claim(player, true);
        ModComponents.CURRENCY.get(player).modify(-price);
    }
}
