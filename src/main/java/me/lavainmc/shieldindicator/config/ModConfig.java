package me.lavainmc.shieldindicator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class ModConfig {
    private static final Path CONFIG_PATH = Path.of("config/shieldindicator.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean showIndicator = true;
    public IndicatorMode mode = IndicatorMode.CROSSHAIR;
    public boolean enableBlend = true;
    public int yOffset = 20;
    public int hotbarXOffset = 0;
    public int hotbarYOffset = 0;

    public static ModConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                ModConfig config = GSON.fromJson(Files.readString(CONFIG_PATH), ModConfig.class);
                if (config != null) {
                    config.normalize();
                    return config;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ModConfig config = new ModConfig();
        config.save(); // 保存默认配置
        return config;
    }

    private void normalize() {
        if (mode == null) {
            mode = IndicatorMode.CROSSHAIR;
        }
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Screen createConfigScreen(Screen parent) {
        ModConfig config = load();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.shieldindicator.config"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.shieldindicator.general"));

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.shieldindicator.show_indicator"), config.showIndicator)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showIndicator = newValue)
                .build());

        general.addEntry(entryBuilder.startEnumSelector(
                        Text.translatable("option.shieldindicator.mode"),
                        IndicatorMode.class,
                        config.mode)
                .setDefaultValue(IndicatorMode.CROSSHAIR)
                .setSaveConsumer(newValue -> config.mode = newValue)
                .setEnumNameProvider(value -> Text.translatable(
                        "option.shieldindicator.mode." + value.name().toLowerCase(Locale.ROOT)))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.shieldindicator.enable_blend"), config.enableBlend)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.enableBlend = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.translatable("option.shieldindicator.y_offset"), config.yOffset, -60, 100)
                .setDefaultValue(20)
                .setSaveConsumer(newValue -> config.yOffset = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.translatable("option.shieldindicator.hotbar_x_offset"), config.hotbarXOffset, -100, 100)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.hotbarXOffset = newValue)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.translatable("option.shieldindicator.hotbar_y_offset"), config.hotbarYOffset, -60, 60)
                .setDefaultValue(0)
                .setSaveConsumer(newValue -> config.hotbarYOffset = newValue)
                .build());

        return builder.build();
    }
}