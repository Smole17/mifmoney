package ru.smole.mifmoney.component;

import net.minecraft.network.PacketByteBuf;

public interface NetDrawer {

    void writeNet(PacketByteBuf buf);

    void readNet(PacketByteBuf buf);
}
