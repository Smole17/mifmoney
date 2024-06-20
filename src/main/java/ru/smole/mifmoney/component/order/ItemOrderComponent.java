package ru.smole.mifmoney.component.order;

import com.glisco.numismaticoverhaul.ModComponents;
import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.utils.EnvExecutor;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import lombok.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import ru.smole.mifmoney.component.NBTDrawer;
import ru.smole.mifmoney.component.NetDrawer;
import ru.smole.mifmoney.gui.shop.button.order.ItemOrderButton;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderComponent implements NBTDrawer, NetDrawer {

    private int id;
    private ItemStack itemStack;
    private long price;
    private String questId;

    @Override
    public void writeData(NbtCompound compound) {
        compound.putInt("id", id);
        NBTUtils.write(compound, "item", itemStack);
        compound.putLong("price", price);
        compound.putString("questId", questId);
    }

    @Override
    public void readData(NbtCompound compound) {
        this.id = compound.getInt("id");
        this.itemStack = NBTUtils.read(compound, "item");
        this.price = compound.getLong("price");
        this.questId = compound.getString("questId");
    }

    @Override
    public void writeNet(PacketByteBuf buf) {
        buf.writeInt(id);
        buf.writeItemStack(itemStack);
        buf.writeLong(price);
        buf.writeString(questId);
    }

    @Override
    public void readNet(PacketByteBuf buf) {
        this.id = buf.readInt();
        this.itemStack = buf.readItemStack();
        this.price = buf.readLong();
        this.questId = buf.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemOrderComponent && id == ((ItemOrderComponent) obj).id;
    }

    public void update(ItemOrderComponent orderComponent) {
        this.id = orderComponent.getId();
        this.itemStack = orderComponent.getItemStack();
        this.price = orderComponent.getPrice();
        this.questId = orderComponent.getQuestId();
    }

    public void give(ServerPlayerEntity player) {
        val currencyComponent = ModComponents.CURRENCY.get(player);

        if (currencyComponent.getValue() < price) return;

        currencyComponent.modify(-price);
        ItemStackHooks.giveItem(player, itemStack);
    }

    public Quest getQuest() {
        return questId == null ? null : EnvExecutor.getEnvSpecific(
                () -> () -> ClientQuestFile.INSTANCE.getQuest(ClientQuestFile.INSTANCE.getQuestFile().getID(questId)),
                () -> () -> ServerQuestFile.INSTANCE.getQuest(ServerQuestFile.INSTANCE.getQuestFile().getID(questId))
        );
    }

    public ItemOrderButton toButton(Panel panel, String categoryId) {
        return new ItemOrderButton(panel, categoryId, this);
    }
}
