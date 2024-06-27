package ru.smole.mifmoney.component.order.item;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.loot.RewardTable;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.component.order.OrderComponent;
import ru.smole.mifmoney.gui.shop.button.order.ItemOrderButton;
import ru.smole.mifmoney.gui.shop.button.order.OrderButton;
import ru.smole.mifmoney.net.message.server.S2CBuyItemResponseMessage;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemOrderComponent extends OrderComponent {

    public static RewardTable getRewardTable(String rewardTableId) {
        if (rewardTableId == null) return null;
        return ServerQuestFile.INSTANCE.getRewardTable(RewardTable.parseCodeString(rewardTableId));
    }

    private ItemStack itemStack;
    private String rewardTableId;
    private String bulkRewardTableId;

    @Override
    public OrderButton toButton(Panel panel, String categoryId) {
        return new ItemOrderButton(panel, categoryId, this);
    }

    @Override
    public void writeData(NbtCompound compound) {
        super.writeData(compound);
        NBTUtils.write(compound, "item", itemStack);
        compound.putString("rewardTableId", rewardTableId);
        compound.putString("bulkRewardTableId", bulkRewardTableId);
    }

    @Override
    public void readData(NbtCompound compound) {
        super.readData(compound);
        this.itemStack = NBTUtils.read(compound, "item");
        this.rewardTableId = compound.getString("rewardTableId");
        this.bulkRewardTableId = compound.getString("bulkRewardTableId");
    }

    @Override
    public void writeNet(PacketByteBuf buf) {
        super.writeNet(buf);
        buf.writeItemStack(itemStack);
        buf.writeString(rewardTableId);
        buf.writeString(bulkRewardTableId);
    }

    @Override
    public void readNet(PacketByteBuf buf) {
        super.readNet(buf);
        this.itemStack = buf.readItemStack();
        this.rewardTableId = buf.readString();
        this.bulkRewardTableId = buf.readString();
    }

    @Override
    public void update(OrderComponent orderComponent) {
        super.update(orderComponent);

        if (!(orderComponent instanceof ItemOrderComponent itemOrderComponent)) return;

        this.itemStack = itemOrderComponent.itemStack;
        this.rewardTableId = itemOrderComponent.rewardTableId;
        this.bulkRewardTableId = itemOrderComponent.bulkRewardTableId;
    }

    public void give(boolean isBulk, ServerPlayerEntity player) {
        val modifier = isBulk ? 10 : 1;

        val currencyComponent = ModComponents.CURRENCY.get(player);

        val modifiedPrice = getPrice() * modifier;
        if (currencyComponent.getValue() < modifiedPrice) return;

        val currentRewardTableId = isBulk ? bulkRewardTableId : rewardTableId;
        val rewardTable = getRewardTable(currentRewardTableId);

        if (rewardTable == null) return;

        currencyComponent.modify(-modifiedPrice);

        new S2CBuyItemResponseMessage(currentRewardTableId).sendTo(player);
    }
}
