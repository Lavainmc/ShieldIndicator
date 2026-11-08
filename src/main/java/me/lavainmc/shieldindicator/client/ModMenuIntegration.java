package me.lavainmc.shieldindicator.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.lavainmc.shieldindicator.ShieldIndicator;
import me.lavainmc.shieldindicator.config.ModConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

   public ModMenuIntegration() {
   }

   // ModMenuApi
   public ConfigScreenFactory<?> getModConfigScreenFactory() {
      return (parent) -> {
         try {
            return ModConfigScreen.create(parent);
         } catch (LinkageError e) {
            // Cloth Config is optional; never take the game down if it is missing.
            ShieldIndicator.LOGGER.warn("Cloth Config is missing; cannot open the ShieldIndicator config screen", e);
            return parent;
         }
      };
   }
}
