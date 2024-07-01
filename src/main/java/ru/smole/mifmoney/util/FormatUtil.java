package ru.smole.mifmoney.util;

import com.glisco.numismaticoverhaul.currency.Currency;
import dev.ftb.mods.ftbquests.item.CustomIconItem;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@UtilityClass
public class FormatUtil {

    public Text formatMoney(long money) {
        return Text.literal(String.format("◎ %,d", money)).formatted(Formatting.GOLD);
    }

    public void drawFormattedMoney(long money, MatrixStack matrixStack, int x, int y, int w, int h, int range, boolean isInverse) {
        val g = (int) money / 10000;
        val s = (int) (money - Currency.GOLD.getRawValue(g)) / 100;
        val b = (int) (money - (Currency.GOLD.getRawValue(g) + Currency.SILVER.getRawValue(s)));

        if (g > 0) {
            val goldX = isInverse ? x : x - range * ((b > 0 ? 1 : 0) + (s > 0 ? 1 : 0));
            CustomIconItem.getIcon(new ItemStack(Currency.GOLD, g)).draw(matrixStack, goldX, y, w, h);
        }
        if (s > 0) {
            val silverX = isInverse ? x + range * (g > 0 ? 1 : 0) : x - range * (b > 0 ? 1 : 0);
            CustomIconItem.getIcon(new ItemStack(Currency.SILVER, s)).draw(matrixStack, silverX, y, w, h);
        }
        if (b > 0) {
            val bronzeX = isInverse ? x + range * ((s > 0 ? 1 : 0) + (g > 0 ? 1 : 0)) : x;
            CustomIconItem.getIcon(new ItemStack(Currency.BRONZE, b)).draw(matrixStack, bronzeX, y, w, h);
        }
    }
}
