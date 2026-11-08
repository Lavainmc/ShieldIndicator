package me.lavainmc.shieldindicator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.file.Files;
import java.nio.file.Path;

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
}