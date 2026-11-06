package me.lavainmc.shieldindicator.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.lavainmc.shieldindicator.config.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

   public ModMenuIntegration() {
   }

   // ModMenuApi
   public ConfigScreenFactory<?> getModConfigScreenFactory() {
      return (parent) -> {
         return ModConfig.createConfigScreen(parent);
      };
   }
}
