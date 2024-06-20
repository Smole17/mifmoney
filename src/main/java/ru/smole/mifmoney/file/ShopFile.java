package ru.smole.mifmoney.file;

import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbquests.util.FileUtils;
import dev.ftb.mods.ftbquests.util.NetUtils;
import lombok.Getter;
import lombok.val;
import net.minecraft.network.PacketByteBuf;
import org.apache.commons.io.FilenameUtils;
import ru.smole.mifmoney.component.NetDrawer;
import ru.smole.mifmoney.component.category.CategoryComponent;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ShopFile implements NetDrawer {

    private final List<CategoryComponent> categories = new ArrayList<>();
    private Path categoriesPath;

    @Override
    public void writeNet(PacketByteBuf buf) {
        NetUtils.write(buf, categories, (buf1, categoryComponent) -> categoryComponent.writeNet(buf1));
    }

    @Override
    public void readNet(PacketByteBuf buf) {
        categories.clear();

        NetUtils.read(buf, categories, buf1 -> {
            val categoryComponent = new CategoryComponent();

            categoryComponent.readNet(buf1);

            return categoryComponent;
        });

        System.out.printf("Successfully loaded %s categories from net", categories.size());
    }

    public void load() {
        categoriesPath = Platform.getConfigFolder().resolve("mifmoney/categories");
        val files = categoriesPath.toFile().listFiles();

        if (files == null || files.length == 0) return;

        Arrays.stream(files)
                .map(File::toPath)
                .filter(path -> path.toString().endsWith(".snbt"))
                .forEach(path -> {
                    val readCategoryCompound = SNBT.read(path);

                    if (readCategoryCompound == null) return;

                    val categoryComponent = new CategoryComponent();

                    categoryComponent.readData(readCategoryCompound);
                    categoryComponent.setId(FilenameUtils.getBaseName(path.getFileName().toString()));

                    categories.add(categoryComponent);
                });

        System.out.printf("Successfully loaded %s categories from NBT", categories.size());
    }

    public void save() {
        if (categoriesPath == null) return;

        categories.forEach(categoryComponent -> {
            val snbt = new SNBTCompoundTag();

            categoryComponent.writeData(snbt);

            SNBT.write(categoriesPath.resolve("%s.snbt".formatted(categoryComponent.getId())), snbt);
        });
    }

    public void deleteCategory(CategoryComponent categoryComponent) {
        categories.remove(categoryComponent);
        FileUtils.delete(categoriesPath.resolve("%s.snbt".formatted(categoryComponent.getId())).toFile());
    }
}
