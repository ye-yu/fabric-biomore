package fp.yeyu.denseoremod.feature;

import fp.yeyu.denseoremod.mixin.StructureAccessorWorld;
import fp.yeyu.denseoremod.mixinutil.BlockConfigurationProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public class GenerationUtil {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final HashMap<Biome, List<Supplier<ConfiguredFeature<?, ?>>>> BIOMORE_CONFIGURATION = new HashMap<>();
    private static final Identifier NULL_ID = new Identifier("biomore", "modded_or_invalid");

    private static RegistryKey<? extends Biome> getKeyOrDefault(StructureAccessorWorld structureAccessorWorld, Biome biome) {
        return structureAccessorWorld.getWorld().getRegistryManager().get(Registry.BIOME_KEY).getKey(biome).orElse(BiomeKeys.PLAINS);
    }

    private static Identifier getIdOrDefault(StructureAccessorWorld structureAccessorWorld, Biome biome) {
        return Optional.ofNullable(structureAccessorWorld.getWorld().getRegistryManager().get(Registry.BIOME_KEY).getId(biome)).orElse(NULL_ID);
    }

    public static void generateFeatureStep(Biome biome, Map<Integer, List<StructureFeature<?>>> structureFeatures, StructureAccessor structureAccessor, ChunkRandom random, ChunkRegion region, ChunkGenerator chunkGenerator, long populationSeed, BlockPos pos, boolean printDebug) {
        final List<Supplier<ConfiguredFeature<?, ?>>> oreFeature = biome.getGenerationSettings().getFeatures().get(BiomOreFeatures.TARGET_FEATURE.ordinal());
        final List<Supplier<ConfiguredFeature<?, ?>>> experimentalGeneration = BIOMORE_CONFIGURATION.getOrDefault(biome, new ArrayList<>());
        if (!experimentalGeneration.isEmpty()) {
            generateExperimental(biome, structureFeatures, structureAccessor, random, region, chunkGenerator, populationSeed, pos);
            return;
        }

        if (printDebug) {
            LOGGER.info(String.format("For biome %s:", getIdOrDefault((StructureAccessorWorld) structureAccessor, biome)));
        }

        for (Supplier<ConfiguredFeature<?, ?>> featureSupplier : oreFeature) {
            final ConfiguredFeature<?, ?> configuredFeature = featureSupplier.get();
            final BlockConfigurationProvider blockConfigurationProvider = BiomOreFeatures.getConfiguration(configuredFeature);
            if (blockConfigurationProvider == null) {
                experimentalGeneration.add(featureSupplier);
                continue;
            }

            if (BiomOreFeatures.isExcludedBlock(blockConfigurationProvider.getGenerationBlock())) {
                LOGGER.info(String.format("Excluding vanilla generation for %s ore", blockConfigurationProvider.getGenerationBlock()));
                continue;
            }
            experimentalGeneration.add(featureSupplier);
        }

        final RegistryKey<Biome> fallbackBiome = BiomOreFeatures.FALLBACK_BIOME.getOrDefault(biome.getCategory(), BiomeKeys.PLAINS);
        final ArrayList<ConfiguredFeature<?, ?>> oreFeatures = BiomOreFeatures.MAPPED_CONFIG_FEATURES.getOrDefault(getKeyOrDefault((StructureAccessorWorld) structureAccessor, biome), BiomOreFeatures.MAPPED_CONFIG_FEATURES.get(fallbackBiome));
        oreFeatures.forEach(e -> experimentalGeneration.add(() -> e));
        BIOMORE_CONFIGURATION.put(biome, experimentalGeneration);
        generateExperimental(biome, structureFeatures, structureAccessor, random, region, chunkGenerator, populationSeed, pos);
    }

    private static void generateExperimental(Biome biome, Map<Integer, List<StructureFeature<?>>> structureFeatures, StructureAccessor structureAccessor, ChunkRandom random, ChunkRegion region, ChunkGenerator chunkGenerator, long populationSeed, BlockPos pos) {
        if (BIOMORE_CONFIGURATION.get(biome) == null) {
            LOGGER.warn("BiomOre ore generation is not configured despite generateExperimental is called!", new Throwable());
        }

        List<List<Supplier<ConfiguredFeature<?, ?>>>> list = biome.getGenerationSettings().getFeatures();
        for (int featureStepIndex = 0; featureStepIndex < GenerationStep.Feature.values().length; ++featureStepIndex) {
            int structureOrdinal = 0;
            if (structureAccessor.shouldGenerateStructures()) {
                List<StructureFeature<?>> structures = structureFeatures.getOrDefault(featureStepIndex, Collections.emptyList());

                for (StructureFeature<?> structureFeature : structures) {
                    random.setDecoratorSeed(populationSeed, structureOrdinal, featureStepIndex);
                    int chunkX = pos.getX() >> 4;
                    int chunkZ = pos.getZ() >> 4;
                    int trimmedBlockX = chunkX << 4;
                    int trimmedBlockZ = chunkZ << 4;

                    try {
                        structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(pos), structureFeature).forEach((structureStart) -> structureStart.generateStructure(region, structureAccessor, chunkGenerator, random, new BlockBox(trimmedBlockX, trimmedBlockZ, trimmedBlockX + 15, trimmedBlockZ + 15), new ChunkPos(chunkX, chunkZ)));
                    } catch (Exception var21) {
                        CrashReport crashReport = CrashReport.create(var21, "Feature placement");
                        crashReport.addElement("Feature").add("Id", Registry.STRUCTURE_FEATURE.getId(structureFeature)).add("Description", structureFeature::toString);
                        throw new CrashException(crashReport);
                    }
                }
            }


            final List<Supplier<ConfiguredFeature<?, ?>>> featureGeneration =
                    (featureStepIndex == BiomOreFeatures.TARGET_FEATURE.ordinal()) ?
                            BIOMORE_CONFIGURATION.getOrDefault(biome, list.get(featureStepIndex)) :
                            list.get(featureStepIndex);

            for (Supplier<ConfiguredFeature<?, ?>> supplier : featureGeneration) {
                ConfiguredFeature<?, ?> configuredFeature = supplier.get();

                random.setDecoratorSeed(populationSeed, structureOrdinal, featureStepIndex);

                try {
                    configuredFeature.generate(region, chunkGenerator, random, pos);
                } catch (Exception var22) {
                    CrashReport crashReport2 = CrashReport.create(var22, "Feature placement");
                    throw new CrashException(crashReport2);
                }
            }
        }
    }
}
