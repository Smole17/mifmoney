package ru.smole.mifmoney.component;

import net.minecraft.nbt.NbtCompound;

public interface NBTDrawer {

    void writeData(NbtCompound compound);

    void readData(NbtCompound compound);
}
