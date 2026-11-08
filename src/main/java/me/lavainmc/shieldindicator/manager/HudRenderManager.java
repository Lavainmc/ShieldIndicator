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

    // Hotbar-mode artwork: 20x20 PNGs, drawn with normal alpha blending
    // instead of the crosshair blend effect.
    private static final Identifier SHIELD_INDICATOR_HOTBAR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_blocking.png");
    private static final Identifier SHIELD_INDICATOR_HOTBAR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_hotbar_disabled.png");

    public static void renderShieldHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = ModConfig.load();

        if (!config.showIndicator || client.player == null || client.options.hudHidden) {
            return;
        }

        // The crosshair indicator is first-person only unless the user opts in.
        if (config.mode == IndicatorMode.CROSSHAIR
                && !client.options.getPerspective().isFirstPerson()
                && !config.showCrosshairInThirdPerson) {
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
            // Right side of the hotbar, aligned with the 20x20 hotbar slot area
            // (same anchor as the vanilla hotbar attack indicator).
            x = centerX + 110 + config.hotbarXOffset;
            y = client.getWindow().getScaledHeight() - 23 + config.hotbarYOffset;
        } else {
            // Below the crosshair.
            x = centerX - 8;
            y = centerY + config.yOffset;
        }

        boolean hotbar = config.mode == IndicatorMode.HOTBAR;
        int size = hotbar ? 20 : 16;
        Identifier blockingTexture = hotbar ? SHIELD_INDICATOR_HOTBAR_BLOCKING : SHIELD_INDICATOR_BLOCKING;
        Identifier disabledTexture = hotbar ? SHIELD_INDICATOR_HOTBAR_DISABLED : SHIELD_INDICATOR_DISABLED;

        // The crosshair icon uses the invert blend effect; the hotbar icon is
        // rendered with normal alpha blending, like the vanilla attack indicator.
        RenderSystem.enableBlend();
        if (!hotbar && config.enableBlend) {
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        } else {
            RenderSystem.defaultBlendFunc();
        }

        // shield blocking
        if (blocking) {
            context.drawTexture(blockingTexture, x, y, 0, 0, size, size, size, size);
        }

        // shield disabled (冷却中)
        if (disabled) {
            context.drawTexture(disabledTexture, x, y, 0, 0, size, size, size, size);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
