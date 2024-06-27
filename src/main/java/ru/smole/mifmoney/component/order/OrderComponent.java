package ru.smole.mifmoney.component.order;

import dev.architectury.utils.EnvExecutor;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.component.NBTDrawer;
import ru.smole.mifmoney.component.NetDrawer;
import ru.smole.mifmoney.gui.shop.button.order.OrderButton;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class OrderComponent implements NBTDrawer, NetDrawer {

    private int id;
    private long price;
    private String name;
    private String questId;

    public abstract OrderButton toButton(Panel panel, String categoryId);

    @Override
    public void writeData(NbtCompound compound) {
        compound.putInt("id", id);
        compound.putLong("price", price);
        compound.putString("name", name);
        compound.putString("questId", questId);
    }

    @Override
    public void readData(NbtCompound compound) {
        this.id = compound.getInt("id");
        this.price = compound.getLong("price");
        this.name = compound.getString("name");
        this.questId = compound.getString("questId");
    }

    @Override
    public void writeNet(PacketByteBuf buf) {
        buf.writeInt(id);
        buf.writeLong(price);
        buf.writeString(name);
        buf.writeString(questId);
    }

    @Override
    public void readNet(PacketByteBuf buf) {
        this.id = buf.readInt();
        this.price = buf.readLong();
        this.name = buf.readString();
        this.questId = buf.readString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OrderComponent && getId() == ((OrderComponent) obj).getId();
    }

    public void update(OrderComponent orderComponent) {
        this.id = orderComponent.getId();
        this.price = orderComponent.getPrice();
        this.name = orderComponent.getName();
        this.questId = orderComponent.getQuestId();
    }

    public Quest getQuest() {
        return questId == null ? null : EnvExecutor.getEnvSpecific(
                () -> () -> ClientQuestFile.INSTANCE.getQuest(ClientQuestFile.INSTANCE.getQuestFile().getID(questId)),
                () -> () -> ServerQuestFile.INSTANCE.getQuest(ServerQuestFile.INSTANCE.getQuestFile().getID(questId))
        );
    }
}
