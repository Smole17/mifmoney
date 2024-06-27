package ru.smole.mifmoney.component.category;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import dev.ftb.mods.ftbquests.util.NetUtils;
import lombok.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import ru.smole.mifmoney.component.NBTDrawer;
import ru.smole.mifmoney.component.NetDrawer;
import ru.smole.mifmoney.component.order.OrderComponent;
import ru.smole.mifmoney.component.order.item.ItemOrderComponent;
import ru.smole.mifmoney.gui.shop.button.category.CategoryButton;
import ru.smole.mifmoney.gui.shop.panel.category.CategoryPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryComponent implements NBTDrawer, NetDrawer {

    private String id = UUID.randomUUID().toString();
    private String name = id;
    private ItemStack itemStack;
    private List<OrderComponent> orderComponents;

    @Override
    public void writeData(NbtCompound compound) {
        compound.putString("name", name);
        NBTUtils.write(compound, "item", itemStack);

        val orderList = new NbtList();

        orderComponents.forEach(orderComponent -> {
            val snbt = new SNBTCompoundTag();

            orderComponent.writeData(snbt);

            orderList.add(snbt);
        });

        compound.put("orders", orderList);
    }

    @Override
    public void readData(NbtCompound compound) {
        name = compound.getString("name");
        itemStack = NBTUtils.read(compound, "item");

        orderComponents = new ArrayList<>();

        val orderList = compound.getList("orders", 10);

        orderList.forEach(nbtElement -> {
            val itemOrderComponent = new ItemOrderComponent();

            itemOrderComponent.readData((SNBTCompoundTag) nbtElement);

            orderComponents.add(itemOrderComponent);
        });
    }

    @Override
    public void writeNet(PacketByteBuf buf) {
        buf.writeString(id);
        buf.writeString(name);
        buf.writeItemStack(itemStack);
        NetUtils.write(buf, orderComponents, (buf1, itemOrderComponent) -> itemOrderComponent.writeNet(buf1));
    }

    @Override
    public void readNet(PacketByteBuf buf) {
        id = buf.readString();
        name = buf.readString();
        itemStack = buf.readItemStack();
        orderComponents = new ArrayList<>();

        NetUtils.read(buf, orderComponents, buf1 -> {
            val itemOrderComponent = new ItemOrderComponent();

            itemOrderComponent.readNet(buf1);

            return itemOrderComponent;
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CategoryComponent && Objects.equals(id, ((CategoryComponent) obj).id);
    }

    public void update(CategoryComponent categoryComponent) {
        id = categoryComponent.getId();
        name = categoryComponent.getName();
        itemStack = categoryComponent.getItemStack();
        orderComponents = categoryComponent.getOrderComponents();
    }

    public Icon getIcon() {
        return CustomIconItem.getIcon(itemStack);
    }

    public CategoryButton toButton(CategoryPanel categoryPanel) {
        return new CategoryButton(categoryPanel, this);
    }
}
