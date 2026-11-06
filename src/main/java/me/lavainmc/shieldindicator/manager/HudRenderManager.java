package me.lavainmc.shieldindicator.manager;

import me.lavainmc.shieldindicator.config.IndicatorMode;
import me.lavainmc.shieldindicator.config.ModConfig;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class HudRenderManager {

    // assets
    private static final Identifier SHIELD_INDICATOR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_blocking.png");
    private static final Identifier SHIELD_INDICATOR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_disabled.png");

    // Optional hotbar-mode artwork: 16x16 PNGs. Until they are added, the
    // crosshair textures above are used as a fallback.
    private static final Identifier SHIELD_INDICATOR_HOTBAR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_blocking.png");
    private static final Identifier SHIELD_INDICATOR_HOTBAR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_disabled.png");

    public static void renderShieldHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = ModConfig.load();

        if (!config.showIndicator || client.player == null || client.options.hudHidden) {
            return;
        }

        boolean blocking = client.player.isBlocking();
        boolean disabled = client.player.getItemCooldownManager().isCoolingDown(Items.SHIELD);
        if (!blocking && !disabled) {
            return;
        }

        // position
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        int x;
        int y;
        if (config.mode == IndicatorMode.HOTBAR) {
            // Right side of the hotbar, vertically aligned with the hotbar slots
            // (same anchor as the vanilla hotbar attack indicator).
            x = centerX + 91 + 6 + config.hotbarXOffset;
            y = client.getWindow().getScaledHeight() - 19 + config.hotbarYOffset;
        } else {
            // Below the crosshair.
            x = centerX - 8;
            y = centerY + config.yOffset;
        }

        Identifier blockingTexture = config.mode == IndicatorMode.HOTBAR
                ? textureOrFallback(SHIELD_INDICATOR_HOTBAR_BLOCKING, SHIELD_INDICATOR_BLOCKING)
                : SHIELD_INDICATOR_BLOCKING;
        Identifier disabledTexture = config.mode == IndicatorMode.HOTBAR
                ? textureOrFallback(SHIELD_INDICATOR_HOTBAR_DISABLED, SHIELD_INDICATOR_DISABLED)
                : SHIELD_INDICATOR_DISABLED;

        // effect render
        RenderSystem.enableBlend();
        if (config.enableBlend) {
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        } else {
            RenderSystem.defaultBlendFunc();
        }

        // shield blocking
        if (blocking) {
            context.drawTexture(blockingTexture, x, y, 0, 0, 16, 16, 16, 16);
        }

        // shield disabled (冷却中)
        if (disabled) {
            context.drawTexture(disabledTexture, x, y, 0, 0, 16, 16, 16, 16);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private static Identifier textureOrFallback(Identifier preferred, Identifier fallback) {
        return MinecraftClient.getInstance().getResourceManager().getResource(preferred).isPresent()
                ? preferred
                : fallback;
    }
}
