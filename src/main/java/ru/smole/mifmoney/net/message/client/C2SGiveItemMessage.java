package ru.smole.mifmoney.net.message.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.net.MIFMoneyCommonNetwork;

@RequiredArgsConstructor
public class C2SGiveItemMessage extends BaseC2SMessage {

    private final int index;
    private final RewardTable rewardTable;

    public C2SGiveItemMessage(PacketByteBuf buf) {
        this.index = buf.readInt();
        this.rewardTable = new RewardTable(ServerQuestFile.INSTANCE);
        rewardTable.readNetData(buf);
    }

    @Override
    public MessageType getType() {
        return MIFMoneyCommonNetwork.GIVE_ITEM;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(index);
        rewardTable.writeNetData(buf);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        rewardTable.rewards.get(index).reward.claim((ServerPlayerEntity) context.getPlayer(), true);
    }
}
