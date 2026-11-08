package me.lavainmc.shieldindicator.manager;

import me.lavainmc.shieldindicator.config.IndicatorMode;
import me.lavainmc.shieldindicator.config.ModConfig;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class HudRenderManager {

    // assets
    private static final Identifier SHIELD_INDICATOR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_blocking.png");
    private static final Identifier SHIELD_INDICATOR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_disabled.png");

    private static final Identifier SHIELD_INDICATOR_HOTBAR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_blocking.png");
    private static final Identifier SHIELD_INDICATOR_HOTBAR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_disabled.png");

    public static void renderShieldHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = ModConfig.load();

        if (!config.showIndicator || client.player == null || client.options.hudHidden) {
            return;
        }

        boolean blocking = client.player.isBlocking();
        boolean disabled = client.player.getItemCooldownManager().isCoolingDown(new ItemStack(Items.SHIELD));
        if (!blocking && !disabled) {
            return;
        }

        // position
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        int x;
        int y;
        if (config.mode == IndicatorMode.HOTBAR) {
            x = centerX + 120 + config.hotbarXOffset;
            y = client.getWindow().getScaledHeight() - 22 + config.hotbarYOffset;
        } else {
            // Below the crosshair.
            x = centerX - 8;
            y = centerY + config.yOffset;
        }

        boolean hotbar = config.mode == IndicatorMode.HOTBAR;
        int size = hotbar ? 20 : 16;
        Identifier blockingTexture = hotbar ? SHIELD_INDICATOR_HOTBAR_BLOCKING : SHIELD_INDICATOR_BLOCKING;
        Identifier disabledTexture = hotbar ? SHIELD_INDICATOR_HOTBAR_DISABLED : SHIELD_INDICATOR_DISABLED;

        // The crosshair icon uses the textured invert pipeline (the one vanilla
        // uses for the crosshair); the hotbar icon uses normal GUI alpha.
        RenderPipeline pipeline = !hotbar && config.enableBlend
                ? RenderPipelines.CROSSHAIR
                : RenderPipelines.GUI_TEXTURED;

        // shield blocking
        if (blocking) {
            context.drawTexture(pipeline, blockingTexture, x, y, 0, 0, size, size, size, size);
        }

        // shield disabled (冷却中)
        if (disabled) {
            context.drawTexture(pipeline, disabledTexture, x, y, 0, 0, size, size, size, size);
        }
    }
}
