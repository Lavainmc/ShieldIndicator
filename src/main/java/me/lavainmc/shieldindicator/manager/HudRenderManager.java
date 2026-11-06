package me.lavainmc.shieldindicator.manager;

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
        int x = centerX - 8;
        int y = centerY + config.yOffset;

        // effect render
        RenderSystem.enableBlend();
        if (config.enableBlend) {
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        } else {
            RenderSystem.defaultBlendFunc();
        }

        // shield blocking
        if (blocking) {
            context.drawTexture(SHIELD_INDICATOR_BLOCKING, x, y, 0, 0, 16, 16, 16, 16);
        }

        // shield disabled (冷却中)
        if (disabled) {
            context.drawTexture(SHIELD_INDICATOR_DISABLED, x, y, 0, 0, 16, 16, 16, 16);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}
