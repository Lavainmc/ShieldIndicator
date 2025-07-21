package me.lavainmc.shieldindicator;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShieldIndicator implements ModInitializer {
    public static final String MOD_ID = "shieldindicator";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Shield Indicator Mod initialized");
    }
}
