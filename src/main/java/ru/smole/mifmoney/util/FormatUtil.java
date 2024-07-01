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

    public void drawFormattedMoney(long money, MatrixStack matrixStack, int x, int y, int w, int h, int range) {
        val g = (int) money / 10000;
        val s = (int) (money - Currency.GOLD.getRawValue(g)) / 100;
        val b = (int) (money - (Currency.GOLD.getRawValue(g) + Currency.SILVER.getRawValue(s)));

        if (b > 0) {
            CustomIconItem.getIcon(new ItemStack(Currency.BRONZE, b)).draw(matrixStack, x, y, w, h);
        }
        if (s > 0) {
            CustomIconItem.getIcon(new ItemStack(Currency.SILVER, s)).draw(matrixStack, x - range * (b > 0 ? 1 : 0), y, w, h);
        }
        if (g > 0) CustomIconItem.getIcon(new ItemStack(Currency.GOLD, g)).draw(matrixStack, x - range * ((b > 0 ? 1 : 0) + (s > 0 ? 1 : 0)), y, w, h);
    }
}
