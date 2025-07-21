package me.lavainmc.shieldindicator.manager;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;

public class HudRenderManager {

    // assets
    private static final Identifier SHIELD_INDICATOR_BLOCKING = Identifier.of("shieldindicator", "textures/gui/shield_indicator_blocking.png");
    private static final Identifier SHIELD_INDICATOR_DISABLED = Identifier.of("shieldindicator", "textures/gui/shield_indicator_disabled.png");

    public static void renderShieldHud(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        // position
        int centerX = client.getWindow().getScaledWidth() / 2;
        int centerY = client.getWindow().getScaledHeight() / 2;
        int x = centerX - 16 / 2;
        int y = centerY + 18;

        //render enable
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f); // 70%透明度

        // shield blocking
        if (client.player == null) return;
        if (client.player.isBlocking()) {
            context.drawTexture(SHIELD_INDICATOR_BLOCKING, x , y, 0, 0, 16, 16, 16, 16);
        }

        // shield disabled
        if (client.player == null) return;
        var player = client.player;
        var cooldownManager = player.getItemCooldownManager();
        if (cooldownManager.isCoolingDown(Items.SHIELD)) {
            context.drawTexture(SHIELD_INDICATOR_DISABLED, x , y, 0, 0, 16, 16, 16, 16);
        }

        //render disable
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.5f);
        RenderSystem.disableBlend();
    }
}
