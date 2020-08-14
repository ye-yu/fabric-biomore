package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(Biome.class)
public abstract class BiomeMixin {
	private static final ArrayList<ConfiguredFeature<?, ?>> EMPTY = new ArrayList<>();
	@Shadow
	@Final
	public static Logger LOGGER;
	public List<ConfiguredFeature<?, ?>> lastOreFeature = EMPTY;
	public ArrayList<ConfiguredFeature<?, ?>> experimentalGeneration = EMPTY;
	@Shadow
	@Final
	protected Map<GenerationStep.Feature, List<ConfiguredFeature<?, ?>>> features;
	private boolean printDebug = true;

	@Shadow
	public abstract Biome.Category getCategory();

	@Inject(method = "generateFeatureStep", at = @At("HEAD"), cancellable = true)
	public void generateFeatureStepMixinHead(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		Biome biome = (Biome) (Object) this;
		if (!canGenerate(world)) return;
		if (this.getCategory() == Biome.Category.THEEND || this.getCategory() == Biome.Category.NETHER || this.getCategory() == Biome.Category.NONE)
			return;
		truncateOres(biome);
	}

	private boolean canGenerate(ServerWorldAccess world) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) return true;
		return BiomOreMod.BIOMORE != null && world.getLevelProperties().getGameRules().getBoolean(BiomOreMod.BIOMORE);
	}

	protected void truncateOres(Biome biome) {
		lastOreFeature = this.features.get(BiomOreFeatures.TARGET_FEATURE);
		if (!experimentalGeneration.isEmpty()) {
			this.features.put(BiomOreFeatures.TARGET_FEATURE, experimentalGeneration);
			return;
		}

		experimentalGeneration = new ArrayList<>();
		if (printDebug) {
			LOGGER.info(String.format("For biome %s:", biome.getTranslationKey()));
		}
		for (ConfiguredFeature<?, ?> configuredFeature : lastOreFeature) {
			FeatureConfig config = configuredFeature.config;
			if (config instanceof DecoratedFeatureConfig) {
				config = ((DecoratedFeatureConfig) config).feature.config;
			}
			if (!(config instanceof OreFeatureConfig)) {

				if (config instanceof EmeraldOreFeatureConfig) {
					if (printDebug) {
						LOGGER.info(String.format("Skipping vanilla generation for %s", Blocks.EMERALD_BLOCK));
					}
					continue;
				}

				if (config instanceof DiskFeatureConfig) {
					if (BiomOreFeatures.isBiomOreDisksBlock(((DiskFeatureConfig) config).state.getBlock())) {
						if (printDebug) {
							LOGGER.info("Skipping vanilla generation for disks ore");
						}
						continue;
					}
				}

				experimentalGeneration.add(configuredFeature);
				continue;
			}

			// do not add those feature of vanilla ores
			if (BiomOreFeatures.isBiomOreFeatureBlock(((OreFeatureConfig) config).state.getBlock())) {
				if (printDebug) {
					final Block block = ((OreFeatureConfig) config).state.getBlock();
					LOGGER.info(String.format("Skipping vanilla generation for %s", block));
				}
				continue;
			}
			experimentalGeneration.add(configuredFeature);
		}

		final Biome fallbackBiome = BiomOreFeatures.FALLBACK_BIOME.getOrDefault(this.getCategory(), Biomes.PLAINS);
		final ArrayList<ConfiguredFeature<?, ?>> oreFeatures = BiomOreFeatures.MAPPED_CONFIG_FEATURES.getOrDefault(biome, BiomOreFeatures.MAPPED_CONFIG_FEATURES.get(fallbackBiome));

		experimentalGeneration.addAll(oreFeatures);

		if (printDebug) {
			LOGGER.info("Vanilla feature config:");
			for (ConfiguredFeature<?, ?> configuredFeature : lastOreFeature) {
				FeatureConfig config = configuredFeature.config;
				if (config instanceof DecoratedFeatureConfig) {
					config = ((DecoratedFeatureConfig) config).feature.config;
				}

				if (config instanceof OreFeatureConfig)
					LOGGER.info(String.format(" - %s : %s", config.getClass().getSimpleName(), ((OreFeatureConfig) config).state.getBlock()));
				else
					LOGGER.info(String.format(" - %s", config.getClass().getSimpleName()));
			}

			LOGGER.info("BiomOre feature config:");
			for (ConfiguredFeature<?, ?> configuredFeature : experimentalGeneration) {
				FeatureConfig config = configuredFeature.config;
				if (config instanceof DecoratedFeatureConfig) {
					config = ((DecoratedFeatureConfig) config).feature.config;
				}

				if (config instanceof OreFeatureConfig)
					LOGGER.info(String.format(" - %s : %s", config.getClass().getSimpleName(), ((OreFeatureConfig) config).state.getBlock()));
				else if (config instanceof BiomOreSingleFeatureConfig)
					LOGGER.info(String.format(" - %s : %s", config.getClass().getSimpleName(), ((BiomOreSingleFeatureConfig) config).state.getBlock()));
				else if (config instanceof BiomOreVeinFeatureConfig)
					LOGGER.info(String.format(" - %s : %s", config.getClass().getSimpleName(), ((BiomOreVeinFeatureConfig) config).state.getBlock()));
				else
					LOGGER.info(String.format(" - %s", config.getClass().getSimpleName()));
			}
		}

		printDebug = false;
		this.features.put(BiomOreFeatures.TARGET_FEATURE, experimentalGeneration);
	}

	@Inject(method = "generateFeatureStep", at = @At("RETURN"), cancellable = true)
	public void generateFeatureStepMixinTail(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		if (lastOreFeature.isEmpty()) return;
		this.features.put(BiomOreFeatures.TARGET_FEATURE, lastOreFeature);
		lastOreFeature = EMPTY;
	}
}
