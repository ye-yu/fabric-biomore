package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biomes.class)
public class BiomesMixin {
	private static final Logger LOGGER = LogManager.getLogger("Biomes");

	@Inject(method = "register", at = @At("RETURN"))
	private static void registerMixin(int rawId, RegistryKey<Biome> registryKey, Biome biome, CallbackInfoReturnable<Biome> cir) {
		if (biome == null) {
			LOGGER.warn("Registering null biome");
			return;
		}

		if (registryKey == null) {
			LOGGER.warn("Registering biome with null registry key");
			return;
		}

		BiomOreMod.BIOME_REGISTRY_KEY_HASH_MAP.put(biome, registryKey);
		final String log = String.format("Registering %s with key %s", biome.toString(), registryKey.toString());
		if (LOGGER != null) LOGGER.info(log);
		else System.out.println(log);
	}
}
