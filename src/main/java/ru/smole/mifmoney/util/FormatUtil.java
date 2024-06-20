package ru.smole.mifmoney.util;

import lombok.experimental.UtilityClass;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@UtilityClass
public class FormatUtil {

    public Text format(long value) {
        return Text.literal(String.format("◎ %,d", value)).formatted(Formatting.GOLD);
    }
}
