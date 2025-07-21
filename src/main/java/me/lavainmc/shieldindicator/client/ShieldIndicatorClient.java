package me.lavainmc.shieldindicator.client;

import me.lavainmc.shieldindicator.manager.HudRenderManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

@Environment(EnvType.CLIENT)
public class ShieldIndicatorClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            HudRenderManager.renderShieldHud(drawContext, 1);
        });
    }
}

