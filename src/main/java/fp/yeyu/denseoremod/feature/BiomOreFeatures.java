package fp.yeyu.denseoremod.feature;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import fp.yeyu.denseoremod.mixinutil.BlockConfigurationProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BiomOreFeatures {

    public static final double AMP = 3;
    public static final HashMap<Block, Integer> commonVeinSize = Maps.newHashMap();
    public static final HashMap<RegistryKey<Biome>, ArrayList<ConfiguredFeature<?, ?>>> MAPPED_CONFIG_FEATURES = Maps.newHashMap();
    public static final HashMap<RegistryKey<Biome>, String> BIOME_CONTAINS = Maps.newHashMap();
    public static final HashMap<Category, RegistryKey<Biome>> FALLBACK_BIOME = Maps.newHashMap();
    public static final GenerationStep.Feature TARGET_FEATURE = GenerationStep.Feature.UNDERGROUND_ORES;
    private static final Block[] EXCLUDED_BLOCKS = new Block[]{
            Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE,
            Blocks.LAPIS_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE
    };

    static {
        commonVeinSize.put(Blocks.COAL_ORE, 17);
        commonVeinSize.put(Blocks.IRON_ORE, 9);
        commonVeinSize.put(Blocks.GOLD_ORE, 9);
        commonVeinSize.put(Blocks.REDSTONE_ORE, 8);
        commonVeinSize.put(Blocks.DIAMOND_ORE, 8);
        commonVeinSize.put(Blocks.LAPIS_ORE, 7);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.OCEAN, oceanBase(new ArrayList<>(), BiomeKeys.OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.LUKEWARM_OCEAN, lukewarmOcean(new ArrayList<>(), BiomeKeys.LUKEWARM_OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DEEP_FROZEN_OCEAN, deepFrozenOcean(new ArrayList<>(), BiomeKeys.DEEP_FROZEN_OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DEEP_COLD_OCEAN, deepOceans(new ArrayList<>(), BiomeKeys.DEEP_COLD_OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DEEP_OCEAN, deepOceans(new ArrayList<>(), BiomeKeys.DEEP_OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DEEP_LUKEWARM_OCEAN, deepOceans(new ArrayList<>(), BiomeKeys.DEEP_LUKEWARM_OCEAN));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DEEP_WARM_OCEAN, deepOceans(new ArrayList<>(), BiomeKeys.DEEP_WARM_OCEAN));
        FALLBACK_BIOME.put(Category.OCEAN, BiomeKeys.OCEAN);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.PLAINS, plains(new ArrayList<>(), BiomeKeys.PLAINS));
        FALLBACK_BIOME.put(Category.PLAINS, BiomeKeys.PLAINS);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DESERT, desertBase(new ArrayList<>(), BiomeKeys.DESERT));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DESERT_HILLS, desertHills(new ArrayList<>(), BiomeKeys.DESERT_HILLS));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DESERT_LAKES, desertLakes(new ArrayList<>(), BiomeKeys.DESERT_LAKES));
        FALLBACK_BIOME.put(Category.DESERT, BiomeKeys.DESERT);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.MOUNTAINS, extremeHills(new ArrayList<>(), BiomeKeys.MOUNTAINS));
        FALLBACK_BIOME.put(Category.EXTREME_HILLS, BiomeKeys.MOUNTAINS);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.BIRCH_FOREST, forest(new ArrayList<>(), BiomeKeys.BIRCH_FOREST));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DARK_FOREST, darkForests(new ArrayList<>(), BiomeKeys.DARK_FOREST));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.DARK_FOREST_HILLS, darkForests(new ArrayList<>(), BiomeKeys.DARK_FOREST_HILLS));
        FALLBACK_BIOME.put(Category.FOREST, BiomeKeys.BIRCH_FOREST);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.TAIGA, taiga(new ArrayList<>(), BiomeKeys.TAIGA));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SNOWY_TAIGA, snowyTaigas(new ArrayList<>(), BiomeKeys.SNOWY_TAIGA));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SNOWY_TAIGA_HILLS, snowyTaigas(new ArrayList<>(), BiomeKeys.SNOWY_TAIGA_HILLS));
        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SNOWY_TAIGA_MOUNTAINS, snowyTaigas(new ArrayList<>(), BiomeKeys.SNOWY_TAIGA_MOUNTAINS));
        FALLBACK_BIOME.put(Category.TAIGA, BiomeKeys.TAIGA);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SWAMP, swamp(new ArrayList<>(), BiomeKeys.SWAMP));
        FALLBACK_BIOME.put(Category.SWAMP, BiomeKeys.SWAMP);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.RIVER, river(new ArrayList<>(), BiomeKeys.RIVER));
        FALLBACK_BIOME.put(Category.RIVER, BiomeKeys.RIVER);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.NETHER_WASTES, nether(new ArrayList<>(), BiomeKeys.NETHER_WASTES));
        FALLBACK_BIOME.put(Category.NETHER, BiomeKeys.NETHER_WASTES);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.THE_END, theend(new ArrayList<>(), BiomeKeys.THE_END));
        FALLBACK_BIOME.put(Category.THEEND, BiomeKeys.THE_END);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SNOWY_TUNDRA, icy(new ArrayList<>(), BiomeKeys.SNOWY_TUNDRA));
        FALLBACK_BIOME.put(Category.ICY, BiomeKeys.SNOWY_TUNDRA);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.MUSHROOM_FIELDS, mushroom(new ArrayList<>(), BiomeKeys.MUSHROOM_FIELDS));
        FALLBACK_BIOME.put(Category.MUSHROOM, BiomeKeys.MUSHROOM_FIELDS);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.BEACH, beach(new ArrayList<>(), BiomeKeys.BEACH));
        FALLBACK_BIOME.put(Category.BEACH, BiomeKeys.BEACH);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.JUNGLE, jungle(new ArrayList<>(), BiomeKeys.JUNGLE));
        FALLBACK_BIOME.put(Category.JUNGLE, BiomeKeys.JUNGLE);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.SAVANNA, savanna(new ArrayList<>(), BiomeKeys.SAVANNA));
        FALLBACK_BIOME.put(Category.SAVANNA, BiomeKeys.SAVANNA);

        MAPPED_CONFIG_FEATURES.put(BiomeKeys.BADLANDS, mesa(new ArrayList<>(), BiomeKeys.BADLANDS));
        FALLBACK_BIOME.put(Category.MESA, BiomeKeys.BADLANDS);

        FALLBACK_BIOME.put(Category.NONE, BiomeKeys.PLAINS); // Category.NONE should not exists, but just in case...
    }

    public static boolean isExcludedBlock(Block block) {
        for (Block excludedBlock : EXCLUDED_BLOCKS) {
            if (block == excludedBlock) return true;
        }
        return false;
    }

    public static BlockConfigurationProvider getConfiguration(ConfiguredFeature<?, ?> configuredFeature) {
        return getConfiguration(configuredFeature, 0);
    }

    private static BlockConfigurationProvider getConfiguration(ConfiguredFeature<?, ?> configuredFeature, int depth) {
        if (depth > 8) throw new CrashException(new CrashReport("Recursion too deep", new StackOverflowError()));

        final FeatureConfig config = configuredFeature.config;
        if (config instanceof BlockConfigurationProvider) return (BlockConfigurationProvider) config;
        if (config instanceof DecoratedFeatureConfig)
            return getConfiguration(((DecoratedFeatureConfig) config).feature.get(), depth + 1);
        return null;
    }

    @SafeVarargs
    public static <T> void updateContains(RegistryKey<Biome> biome, T... contains) {
        String get = BIOME_CONTAINS.get(biome);
        if (get == null) {
            get = StringUtils.join(contains, ", ");
        } else {
            get += ", " + StringUtils.join(contains, ", ");
        }
        BIOME_CONTAINS.put(biome, get);
    }

    public static void containsBasic(RegistryKey<Biome> biome) {
        updateContains(biome, "Default mineables", "Default disks");
    }

    public static String getBlockStrippedId(Block block) {
        return Registry.BLOCK.getId(block).toString().split(":")[1];
    }

    public static String[] getBlockStrippedId(Block... blocks) {
        return Arrays.stream(blocks).map(BiomOreFeatures::getBlockStrippedId).toArray(String[]::new);
    }

    public static ArrayList<ConfiguredFeature<?, ?>> oceanBase(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> ocean) {
        addThickVeinOre(list, Blocks.DIAMOND_ORE, 0.25f, 5, 0, 64);
        addSurfaceVeinOre(list, Blocks.EMERALD_ORE, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
        containsBasic(ocean);
        updateContains(ocean, getBlockStrippedId(Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> lukewarmOcean(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> lukewarmOcean) {
        oceanBase(list, lukewarmOcean);
        addSingleOre(list, Blocks.GOLD_BLOCK, 0.35f, 5, 0, 64);
        updateContains(lukewarmOcean, getBlockStrippedId(Blocks.GOLD_BLOCK));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> deepFrozenOcean(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> deepFrozenOcean) {
        oceanBase(list, deepFrozenOcean);
        addSingleOre(list, Blocks.EMERALD_BLOCK, 0.35f, 5, 0, 64);
        updateContains(deepFrozenOcean, getBlockStrippedId(Blocks.EMERALD_BLOCK));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> deepOceans(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> biome) {
        oceanBase(list, biome);
        addSurfaceVeinOre(list, Blocks.PRISMARINE, 5, Target.OVERWORLD_SURFACE_BLOCK, 0.2f, 1, 20, 0, 64);
        addSurfaceVeinOre(list, Blocks.SEA_LANTERN, 2, Target.OVERWORLD_SURFACE_BLOCK, 0.2f / 0.5f, 1, 20, 0, 64);
        addSingleOre(list, Blocks.SEA_LANTERN, 0.35f, 5, 0, 64);
        updateContains(biome, getBlockStrippedId(Blocks.PRISMARINE, Blocks.SEA_LANTERN));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> plains(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> plains) {
        addThickVeinOre(list, Blocks.ANDESITE, 50, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        addThickVeinOre(list, Blocks.COAL_ORE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(list, Blocks.IRON_ORE, 35, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
        containsBasic(plains);
        updateContains(plains, getBlockStrippedId(Blocks.ANDESITE, Blocks.COAL_ORE, Blocks.IRON_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> desertBase(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> desert) {
        addThickVeinOre(list, Blocks.GOLD_ORE, 18, Target.NATURAL_STONE, 0.55f, 2, 5, 0, 64);
        addThickVeinOre(list, Blocks.REDSTONE_ORE, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
        addThickVeinOre(list, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 3, 5, 0, 64);
        addSingleOre(list, Blocks.REDSTONE_BLOCK, 0.35f, 5, 0, 64);
        containsBasic(desert);
        updateContains(desert, getBlockStrippedId(Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.COAL_BLOCK, Blocks.REDSTONE_BLOCK));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> desertHills(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> desertHills) {
        desertBase(list, desertHills);
        addThickVeinOre(list, Blocks.GOLD_BLOCK, 5, Target.NATURAL_STONE, 0.2f, 1, 5, 0, 64);
        updateContains(desertHills, getBlockStrippedId(Blocks.GOLD_BLOCK));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> desertLakes(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> desertLakes) {
        desertBase(list, desertLakes);
        list.add(BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.CLAY.getDefaultState(), UniformIntDistribution.of(2, 3), 2, Lists.newArrayList(Blocks.SAND.getDefaultState()))).decorate(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
        updateContains(desertLakes, getBlockStrippedId(Blocks.CLAY));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> extremeHills(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> mountains) {
        addThickVeinOre(list, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(list, Blocks.LAPIS_BLOCK, 12, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
        addThickVeinOre(list, Blocks.MOSSY_COBBLESTONE, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        containsBasic(mountains);
        updateContains(mountains, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.MOSSY_COBBLESTONE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> forest(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> forest) {
        addThickVeinOre(list, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        addThickVeinOre(list, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        containsBasic(forest);
        updateContains(forest, getBlockStrippedId(Blocks.DIAMOND_BLOCK, Blocks.LAPIS_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> darkForests(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> darkForestHills) {
        forest(list, darkForestHills);
        // todo: change generation method
        list.add(BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.MYCELIUM.getDefaultState(), UniformIntDistribution.of(2, 5), 1, Lists.newArrayList(Blocks.GRASS_BLOCK.getDefaultState()))).decorate(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
        addThickVeinOre(list, Blocks.GLOWSTONE, 3, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        updateContains(darkForestHills, getBlockStrippedId(Blocks.MYCELIUM, Blocks.GLOWSTONE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> taiga(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> taiga) {
        addThickVeinOre(list, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        addThickVeinOre(list, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        containsBasic(taiga);
        updateContains(taiga, getBlockStrippedId(Blocks.DIAMOND_BLOCK, Blocks.LAPIS_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> snowyTaigas(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> biome) {
        taiga(list, biome);
        addThickVeinOre(list, Blocks.SNOW_BLOCK, 7, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
        addThickVeinOre(list, Blocks.BLUE_ICE, 5, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
        updateContains(biome, getBlockStrippedId(Blocks.SNOW_BLOCK, Blocks.BLUE_ICE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> swamp(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> swamp) {
        addSurfaceVeinOre(list, Blocks.SLIME_BLOCK, 3, Target.OVERWORLD_SURFACE_BLOCK, 0.65f, 1, 20, 0, 64);
        addThickVeinOre(list, Blocks.EMERALD_BLOCK, 15, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 64);
        addSurfaceVeinOre(list, Blocks.EMERALD_ORE, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
        updateContains(swamp, "Default mineables", "Clay");
        updateContains(swamp, getBlockStrippedId(Blocks.SLIME_BLOCK, Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> river(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> river) {
        addThickVeinOre(list, Blocks.COAL_BLOCK, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 64);
        addThickVeinOre(list, Blocks.IRON_ORE, 25, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
        containsBasic(river);
        updateContains(river, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.IRON_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> nether(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> netherWastes) {
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> theend(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> theEnd) {
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> icy(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> snowyTundra) {
        addThickVeinOre(list, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(list, Blocks.LAPIS_BLOCK, 30, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
        addThickVeinOre(list, Blocks.PACKED_ICE, 7, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        containsBasic(snowyTundra);
        updateContains(snowyTundra, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.PACKED_ICE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> mushroom(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> mushroomFields) {
        List<Block> blocks = Arrays.asList(
                Blocks.COAL_ORE,
                Blocks.COAL_BLOCK,
                Blocks.IRON_ORE,
                Blocks.IRON_BLOCK,
                Blocks.GOLD_ORE,
                Blocks.GOLD_BLOCK,
                Blocks.LAPIS_ORE,
                Blocks.LAPIS_BLOCK,
                Blocks.REDSTONE_ORE,
                Blocks.REDSTONE_BLOCK,
                Blocks.DIAMOND_ORE,
                Blocks.DIAMOND_BLOCK,
                Blocks.EMERALD_ORE,
                Blocks.EMERALD_BLOCK
        );
        for (Block block : blocks) {
            addThickVeinOre(list, block, 30, Target.NATURAL_STONE, 0.33f, 1, 5, 0, 64);
        }
        containsBasic(mushroomFields);
        updateContains(mushroomFields, "Ore Buffet");
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> beach(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> beach) {
        addThickVeinOre(list, Blocks.IRON_ORE, 0.25f, 5, 0, 64);
        addThickVeinOre(list, Blocks.GOLD_ORE, 0.25f, 5, 0, 64);
        containsBasic(beach);
        updateContains(beach, getBlockStrippedId(Blocks.IRON_ORE, Blocks.GOLD_ORE));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> jungle(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> jungle) {
        addThickVeinOre(list, Blocks.IRON_BLOCK, 45, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addSurfaceVeinOre(list, Blocks.DIAMOND_BLOCK, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
        addThickVeinOre(list, Blocks.DIORITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        RandomPatchFeatureConfig lanternConfig = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LANTERN.getDefaultState()), new SimpleBlockPlacer())).tries(1).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();
        // todo: verify
        list.add(Feature.RANDOM_PATCH.configure(lanternConfig).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_SPREAD_DOUBLE));
        containsBasic(jungle);
        updateContains(jungle, getBlockStrippedId(Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.LANTERN));
        return list;
    }

    @Deprecated
    public static ArrayList<ConfiguredFeature<?, ?>> none(ArrayList<ConfiguredFeature<?, ?>> list) {
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> savanna(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> savanna) {
        addThickVeinOre(list, Blocks.REDSTONE_ORE, 30, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(list, Blocks.GRANITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(list, Blocks.LAPIS_BLOCK, 16, Target.NATURAL_STONE, 0.5f, 2, 5, 0, 20);
        containsBasic(savanna);
        updateContains(savanna, getBlockStrippedId(Blocks.REDSTONE_ORE, Blocks.LAPIS_BLOCK));
        return list;
    }

    public static ArrayList<ConfiguredFeature<?, ?>> mesa(ArrayList<ConfiguredFeature<?, ?>> list, RegistryKey<Biome> badlands) {
        addThickVeinOre(list, Blocks.BONE_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 5, 5, 0, 64);
        addThickVeinOre(list, Blocks.IRON_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 64);
        addThickVeinOre(list, Blocks.REDSTONE_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 20);
        addThickVeinOre(list, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 10, 5, 0, 64);
        // todo: verify
        list.add(ConfiguredFeatures.GLOWSTONE_EXTRA);
        containsBasic(badlands);
        updateContains(badlands, getBlockStrippedId(Blocks.BONE_BLOCK, Blocks.IRON_ORE, Blocks.REDSTONE_ORE, Blocks.COAL_BLOCK, Blocks.GLOWSTONE));
        return list;
    }

    public static void addThickVeinOre(ArrayList<ConfiguredFeature<?, ?>> list,
                                       Block block,
                                       int veinSize,
                                       Target target,
                                       float chance,
                                       int count,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        list.add(
                BiomOreMod.BIOM_THICK_VEIN_ORE_FEATURE
                        .configure(
                                new BiomOreVeinFeatureConfig(
                                        target,
                                        block.getDefaultState(),
                                        veinSize))
                        .decorate(
                                BiomOreMod.COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR.configure(
                                        new CountChanceConfig(chance, count, bottomOffset, top - topOffSet, target)))
        );
    }

    public static void addSurfaceVeinOre(ArrayList<ConfiguredFeature<?, ?>> list,
                                         Block block,
                                         int veinSize,
                                         Target target,
                                         float chance,
                                         int count,
                                         int bottomOffset,
                                         int topOffSet,
                                         int top) {
        list.add(
                BiomOreMod.BIOM_FLAT_VEIN_ORE_FEATURE
                        .configure(
                                new BiomOreVeinFeatureConfig(
                                        target,
                                        block.getDefaultState(),
                                        veinSize))
                        .decorate(
                                BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(
                                        new CountChanceConfig(chance, count, bottomOffset, top - topOffSet, target)))
        );
    }

    public static void addThickVeinOre(ArrayList<ConfiguredFeature<?, ?>> list,
                                       Block block,
                                       float chance,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        addThickVeinOre(
                list,
                block,
                (int) Math.round(commonVeinSize.get(block) * AMP),
                Target.NATURAL_STONE,
                chance,
                1,
                bottomOffset,
                topOffSet,
                top);
    }

    public static void addSingleOre(ArrayList<ConfiguredFeature<?, ?>> list,
                                    Block block,
                                    float chance,
                                    int count,
                                    int topOffset,
                                    int top) {
        list.add(
                BiomOreMod.BIOM_SINGLE_ORE_FEATURE
                        .configure(
                                new BiomOreSingleFeatureConfig(
                                        Target.NATURAL_STONE,
                                        block.getDefaultState()
                                )
                        ).decorate(
                        BiomOreMod.COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR.configure(
                                new CountChanceConfig(chance, 2, count, top - topOffset, Target.NATURAL_STONE)
                        )
                )
        );
    }
}
