package fp.yeyu.denseoremod.feature;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class BiomOreFeatures {
	
	@Deprecated
	public static final HashMap<String, Consumer<Object>> methods = Maps.newHashMap();
	public static final HashMap<Block, Integer> commonVeinSize = Maps.newHashMap();
	public static final double AMP = 3;
	public static final HashMap<Biome, ArrayList<ConfiguredFeature<?, ?>>> MAPPED_CONFIG_FEATURES = Maps.newHashMap();
	public static final HashMap<Biome, String> BIOME_CONTAINS = Maps.newHashMap();
	public static final HashMap<Category, Biome> FALLBACK_BIOME = Maps.newHashMap();
	public static final GenerationStep.Feature TARGET_FEATURE = GenerationStep.Feature.UNDERGROUND_ORES;

	static {
		commonVeinSize.put(Blocks.COAL_ORE, 17);
		commonVeinSize.put(Blocks.IRON_ORE, 9);
		commonVeinSize.put(Blocks.GOLD_ORE, 9);
		commonVeinSize.put(Blocks.REDSTONE_ORE, 8);
		commonVeinSize.put(Blocks.DIAMOND_ORE, 8);
		commonVeinSize.put(Blocks.LAPIS_ORE, 7);

		MAPPED_CONFIG_FEATURES.put(Biomes.OCEAN, oceanBase(new ArrayList<>(), Biomes.OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.LUKEWARM_OCEAN, lukewarmOcean(new ArrayList<>(), Biomes.LUKEWARM_OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.DEEP_FROZEN_OCEAN, deepFrozenOcean(new ArrayList<>(), Biomes.DEEP_FROZEN_OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.DEEP_COLD_OCEAN, deepOceans(new ArrayList<>(), Biomes.DEEP_COLD_OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.DEEP_OCEAN, deepOceans(new ArrayList<>(), Biomes.DEEP_OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.DEEP_LUKEWARM_OCEAN, deepOceans(new ArrayList<>(), Biomes.DEEP_LUKEWARM_OCEAN));
		MAPPED_CONFIG_FEATURES.put(Biomes.DEEP_WARM_OCEAN, deepOceans(new ArrayList<>(), Biomes.DEEP_WARM_OCEAN));
		FALLBACK_BIOME.put(Category.OCEAN, Biomes.OCEAN);

		MAPPED_CONFIG_FEATURES.put(Biomes.PLAINS, plains(new ArrayList<>(), Biomes.PLAINS));
		FALLBACK_BIOME.put(Category.PLAINS, Biomes.PLAINS);

		MAPPED_CONFIG_FEATURES.put(Biomes.DESERT, desertBase(new ArrayList<>(), Biomes.DESERT));
		MAPPED_CONFIG_FEATURES.put(Biomes.DESERT_HILLS, desertHills(new ArrayList<>(), Biomes.DESERT_HILLS));
		MAPPED_CONFIG_FEATURES.put(Biomes.DESERT_LAKES, desertLakes(new ArrayList<>(), Biomes.DESERT_LAKES));
		FALLBACK_BIOME.put(Category.DESERT, Biomes.DESERT);

		MAPPED_CONFIG_FEATURES.put(Biomes.MOUNTAINS, extremeHills(new ArrayList<>(), Biomes.MOUNTAINS));
		FALLBACK_BIOME.put(Category.EXTREME_HILLS, Biomes.MOUNTAINS);

		MAPPED_CONFIG_FEATURES.put(Biomes.BIRCH_FOREST, forest(new ArrayList<>(), Biomes.BIRCH_FOREST));
		MAPPED_CONFIG_FEATURES.put(Biomes.DARK_FOREST, darkForests(new ArrayList<>(), Biomes.DARK_FOREST));
		MAPPED_CONFIG_FEATURES.put(Biomes.DARK_FOREST_HILLS, darkForests(new ArrayList<>(), Biomes.DARK_FOREST_HILLS));
		FALLBACK_BIOME.put(Category.FOREST, Biomes.BIRCH_FOREST);

		MAPPED_CONFIG_FEATURES.put(Biomes.TAIGA, taiga(new ArrayList<>(), Biomes.TAIGA));
		MAPPED_CONFIG_FEATURES.put(Biomes.SNOWY_TAIGA, snowyTaigas(new ArrayList<>(), Biomes.SNOWY_TAIGA));
		MAPPED_CONFIG_FEATURES.put(Biomes.SNOWY_TAIGA_HILLS, snowyTaigas(new ArrayList<>(), Biomes.SNOWY_TAIGA_HILLS));
		MAPPED_CONFIG_FEATURES.put(Biomes.SNOWY_TAIGA_MOUNTAINS, snowyTaigas(new ArrayList<>(), Biomes.SNOWY_TAIGA_MOUNTAINS));		FALLBACK_BIOME.put(Category.TAIGA, Biomes.TAIGA);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.SWAMP, swamp(new ArrayList<>(), Biomes.SWAMP));
		FALLBACK_BIOME.put(Category.SWAMP, Biomes.SWAMP);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.RIVER, river(new ArrayList<>(), Biomes.RIVER));
		FALLBACK_BIOME.put(Category.RIVER, Biomes.RIVER);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.NETHER_WASTES, nether(new ArrayList<>(), Biomes.NETHER_WASTES));
		FALLBACK_BIOME.put(Category.NETHER, Biomes.NETHER_WASTES);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.THE_END, theend(new ArrayList<>(), Biomes.THE_END));
		FALLBACK_BIOME.put(Category.THEEND, Biomes.THE_END);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.SNOWY_TUNDRA, icy(new ArrayList<>(), Biomes.SNOWY_TUNDRA));
		FALLBACK_BIOME.put(Category.ICY, Biomes.SNOWY_TUNDRA);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.MUSHROOM_FIELDS, mushroom(new ArrayList<>(), Biomes.MUSHROOM_FIELDS));
		FALLBACK_BIOME.put(Category.MUSHROOM, Biomes.MUSHROOM_FIELDS);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.BEACH, beach(new ArrayList<>(), Biomes.BEACH));
		FALLBACK_BIOME.put(Category.BEACH, Biomes.BEACH);
		
		MAPPED_CONFIG_FEATURES.put(Biomes.JUNGLE, jungle(new ArrayList<>(), Biomes.JUNGLE));
		FALLBACK_BIOME.put(Category.JUNGLE, Biomes.JUNGLE);

		MAPPED_CONFIG_FEATURES.put(Biomes.SAVANNA, savanna(new ArrayList<>(), Biomes.SAVANNA));
		FALLBACK_BIOME.put(Category.SAVANNA, Biomes.SAVANNA);

		MAPPED_CONFIG_FEATURES.put(Biomes.BADLANDS, mesa(new ArrayList<>(), Biomes.BADLANDS));
		FALLBACK_BIOME.put(Category.MESA, Biomes.BADLANDS);
		
		FALLBACK_BIOME.put(Category.NONE, Biomes.PLAINS); // Category.NONE should not exists, but just in case...
	}

	public static void addMineables(ArrayList<ConfiguredFeature<?, ?>> list) {
		list.add(Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIRT.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 256))));
		list.add(Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GRAVEL.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 256))));
		list.add(Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GRANITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
		list.add(Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIORITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
		list.add(Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.ANDESITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
	}

	public static void addDefaultDisks(ArrayList<ConfiguredFeature<?, ?>> list) {
		list.add(Feature.DISK.configure(new DiskFeatureConfig(Blocks.SAND.getDefaultState(), 7, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState()))).createDecoratedFeature(Decorator.COUNT_TOP_SOLID.configure(new CountDecoratorConfig(3))));
		list.add(Feature.DISK.configure(new DiskFeatureConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState()))).createDecoratedFeature(Decorator.COUNT_TOP_SOLID.configure(new CountDecoratorConfig(1))));
		list.add(Feature.DISK.configure(new DiskFeatureConfig(Blocks.GRAVEL.getDefaultState(), 6, 2, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.GRASS_BLOCK.getDefaultState()))).createDecoratedFeature(Decorator.COUNT_TOP_SOLID.configure(new CountDecoratorConfig(1))));
	}

	public static void addClay(ArrayList<ConfiguredFeature<?, ?>> list) {
		list.add(Feature.DISK.configure(new DiskFeatureConfig(Blocks.CLAY.getDefaultState(), 4, 1, Lists.newArrayList(Blocks.DIRT.getDefaultState(), Blocks.CLAY.getDefaultState()))).createDecoratedFeature(Decorator.COUNT_TOP_SOLID.configure(new CountDecoratorConfig(1))));
	}

	@SafeVarargs
	public static <T> void updateContains(Biome biome, T... contains) {
		String get = BIOME_CONTAINS.get(biome);
		if (get == null) {
			get = StringUtils.join(contains, ", ");
		} else {
			get += ", " + StringUtils.join(contains, ", ");
		}
		BIOME_CONTAINS.put(biome, get);
	}

	public static void containsBasic(Biome biome) {
		updateContains(biome, "Default mineables", "Default disks");
	}

	public static String getBlockStrippedId(Block block) {
		return Registry.BLOCK.getId(block).toString().split(":")[1];
	}

	public static String[] getBlockStrippedId(Block... blocks) {
		return Arrays.stream(blocks).map(BiomOreFeatures::getBlockStrippedId).toArray(String[]::new);
	}

	public static ArrayList<ConfiguredFeature<?,?>> oceanBase(ArrayList<ConfiguredFeature<?, ?>> list, Biome ocean) {
		addMineables(list);
		addDefaultDisks(list);
		containsBasic(ocean);
		return list;
	}
	
	public static ArrayList<ConfiguredFeature<?, ?>> lukewarmOcean(ArrayList<ConfiguredFeature<?, ?>> list, Biome lukewarmOcean) {
		oceanBase(list, lukewarmOcean);
		addSingleOre(list, Blocks.GOLD_BLOCK, 0.35f, 5, 0, 64);
		updateContains(lukewarmOcean, getBlockStrippedId(Blocks.GOLD_BLOCK));
		return list;
	}
	
	public static ArrayList<ConfiguredFeature<?, ?>> deepFrozenOcean(ArrayList<ConfiguredFeature<?, ?>> list, Biome deepFrozenOcean) {
		oceanBase(list, deepFrozenOcean);
		addSingleOre(list, Blocks.EMERALD_BLOCK, 0.35f, 5, 0, 64);
		updateContains(deepFrozenOcean, getBlockStrippedId(Blocks.EMERALD_BLOCK));
		return list;
	}
	
	public static ArrayList<ConfiguredFeature<?, ?>> deepOceans(ArrayList<ConfiguredFeature<?, ?>> list, Biome biome) {
		oceanBase(list, biome);
		addSurfaceVeinOre(list, Blocks.PRISMARINE, 5, Target.OVERWORLD_SURFACE_BLOCK, 0.2f, 1, 20, 0, 64);
		addSurfaceVeinOre(list, Blocks.SEA_LANTERN, 2, Target.OVERWORLD_SURFACE_BLOCK, 0.2f / 0.5f, 1, 20, 0, 64);
		addSingleOre(list, Blocks.SEA_LANTERN, 0.35f, 5, 0, 64);
		updateContains(biome, getBlockStrippedId(Blocks.PRISMARINE, Blocks.SEA_LANTERN));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> plains(ArrayList<ConfiguredFeature<?, ?>> list, Biome plains) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.ANDESITE, 50, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
		addThickVeinOre(list, Blocks.COAL_ORE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addThickVeinOre(list, Blocks.IRON_ORE, 35, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
		containsBasic(plains);
		updateContains(plains, getBlockStrippedId(Blocks.ANDESITE, Blocks.COAL_ORE, Blocks.IRON_ORE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> desertBase(ArrayList<ConfiguredFeature<?, ?>> list, Biome desert) {
		addMineables(list);
		addDefaultDisks(list);

		addThickVeinOre(list, Blocks.GOLD_ORE, 18, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 64);
		addThickVeinOre(list, Blocks.REDSTONE_ORE, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
		addThickVeinOre(list, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 3, 5, 0, 64);
		addSingleOre(list, Blocks.REDSTONE_BLOCK, 0.35f, 5, 0, 64);
		containsBasic(desert);
		updateContains(desert, getBlockStrippedId(Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.COAL_BLOCK, Blocks.REDSTONE_BLOCK));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> desertHills(ArrayList<ConfiguredFeature<?, ?>> list, Biome desertHills) {
		desertBase(list, desertHills);
		addThickVeinOre(list, Blocks.GOLD_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 1, 5, 0, 64);
		updateContains(desertHills, getBlockStrippedId(Blocks.GOLD_BLOCK));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> desertLakes(ArrayList<ConfiguredFeature<?, ?>> list, Biome desertLakes) {
		desertBase(list, desertLakes);
		list.add(BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.CLAY.getDefaultState(), 5, 2, Lists.newArrayList(Blocks.SAND.getDefaultState()))).createDecoratedFeature(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
		updateContains(desertLakes, getBlockStrippedId(Blocks.CLAY));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> extremeHills(ArrayList<ConfiguredFeature<?, ?>> list, Biome mountains) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addThickVeinOre(list, Blocks.LAPIS_BLOCK, 12, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
		addThickVeinOre(list, Blocks.MOSSY_COBBLESTONE, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
		containsBasic(mountains);
		updateContains(mountains, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.MOSSY_COBBLESTONE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> forest(ArrayList<ConfiguredFeature<?, ?>> list, Biome forest) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
		addThickVeinOre(list, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
		containsBasic(forest);
		updateContains(forest, getBlockStrippedId(Blocks.DIAMOND_BLOCK, Blocks.LAPIS_ORE));
		return list;
	}
	
	public static ArrayList<ConfiguredFeature<?, ?>> darkForests(ArrayList<ConfiguredFeature<?, ?>> list, Biome darkForestHills) {
		forest(list, darkForestHills);
		// todo: change generation method
		list.add(BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.MYCELIUM.getDefaultState(), 5, 1, Lists.newArrayList(Blocks.GRASS_BLOCK.getDefaultState()))).createDecoratedFeature(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
		addThickVeinOre(list, Blocks.GLOWSTONE, 3, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		updateContains(darkForestHills, getBlockStrippedId(Blocks.MYCELIUM, Blocks.GLOWSTONE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> taiga(ArrayList<ConfiguredFeature<?, ?>> list, Biome taiga) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
		addThickVeinOre(list, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
		containsBasic(taiga);
		updateContains(taiga, getBlockStrippedId(Blocks.DIAMOND_BLOCK, Blocks.LAPIS_ORE));
		return list;
	}
	
	public static ArrayList<ConfiguredFeature<?, ?>> snowyTaigas(ArrayList<ConfiguredFeature<?, ?>> list, Biome biome) {
		taiga(list, biome);
		addThickVeinOre(list, Blocks.SNOW_BLOCK, 7, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
		addThickVeinOre(list, Blocks.BLUE_ICE, 5, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
		updateContains(biome, getBlockStrippedId(Blocks.SNOW_BLOCK, Blocks.BLUE_ICE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> swamp(ArrayList<ConfiguredFeature<?, ?>> list, Biome swamp) {
		addMineables(list);
		addClay(list);
		addSurfaceVeinOre(list, Blocks.SLIME_BLOCK, 3, Target.OVERWORLD_SURFACE_BLOCK, 0.65f, 1, 20, 0, 64);
		addThickVeinOre(list, Blocks.EMERALD_BLOCK, 15, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 64);
		addSurfaceVeinOre(list, Blocks.EMERALD_ORE, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
		updateContains(swamp, "Default mineables", "Clay");
		updateContains(swamp, getBlockStrippedId(Blocks.SLIME_BLOCK, Blocks.EMERALD_BLOCK, Blocks.EMERALD_ORE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> river(ArrayList<ConfiguredFeature<?, ?>> list, Biome river) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.COAL_BLOCK, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 64);
		addThickVeinOre(list, Blocks.IRON_ORE, 25, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
		containsBasic(river);
		updateContains(river, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.IRON_ORE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> nether(ArrayList<ConfiguredFeature<?, ?>> list, Biome netherWastes) {
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> theend(ArrayList<ConfiguredFeature<?, ?>> list, Biome theEnd) {
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> icy(ArrayList<ConfiguredFeature<?, ?>> list, Biome snowyTundra) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addThickVeinOre(list, Blocks.LAPIS_BLOCK, 30, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
		addThickVeinOre(list, Blocks.PACKED_ICE, 7, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
		containsBasic(snowyTundra);
		updateContains(snowyTundra, getBlockStrippedId(Blocks.COAL_BLOCK, Blocks.LAPIS_BLOCK, Blocks.PACKED_ICE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> mushroom(ArrayList<ConfiguredFeature<?, ?>> list, Biome mushroomFields) {
		addMineables(list);
		addDefaultDisks(list);
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

	public static ArrayList<ConfiguredFeature<?, ?>> beach(ArrayList<ConfiguredFeature<?, ?>> list, Biome beach) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.IRON_ORE, 0.25f, 5, 0, 64);
		addThickVeinOre(list, Blocks.GOLD_ORE, 0.25f, 5, 0, 64);
		containsBasic(beach);
		updateContains(beach, getBlockStrippedId(Blocks.IRON_ORE, Blocks.GOLD_ORE));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> jungle(ArrayList<ConfiguredFeature<?, ?>> list, Biome jungle) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.IRON_BLOCK, 45, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addSurfaceVeinOre(list, Blocks.DIAMOND_BLOCK, 3, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
		addThickVeinOre(list, Blocks.DIORITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		RandomPatchFeatureConfig lanternConfig = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LANTERN.getDefaultState()), new SimpleBlockPlacer())).tries(1).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();
		list.add(Feature.RANDOM_PATCH.configure(lanternConfig).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(55))));
		containsBasic(jungle);
		updateContains(jungle, getBlockStrippedId(Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.LANTERN));
		return list;
	}

	@Deprecated
	public static ArrayList<ConfiguredFeature<?, ?>> none(ArrayList<ConfiguredFeature<?, ?>> list) {
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> savanna(ArrayList<ConfiguredFeature<?, ?>> list, Biome savanna) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.REDSTONE_ORE, 30, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addThickVeinOre(list, Blocks.GRANITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
		addThickVeinOre(list, Blocks.LAPIS_BLOCK, 12, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
		containsBasic(savanna);
		updateContains(savanna, getBlockStrippedId(Blocks.REDSTONE_ORE, Blocks.LAPIS_BLOCK));
		return list;
	}

	public static ArrayList<ConfiguredFeature<?, ?>> mesa(ArrayList<ConfiguredFeature<?, ?>> list, Biome badlands) {
		addMineables(list);
		addDefaultDisks(list);
		addThickVeinOre(list, Blocks.BONE_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 5, 5, 0, 64);
		addThickVeinOre(list, Blocks.IRON_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 64);
		addThickVeinOre(list, Blocks.REDSTONE_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 20);
		addThickVeinOre(list, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 10, 5, 0, 64);
		list.add(Feature.GLOWSTONE_BLOB.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.LIGHT_GEM_CHANCE.configure(new CountDecoratorConfig(8))));
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
						.createDecoratedFeature(
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
						.createDecoratedFeature(
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
						).createDecoratedFeature(
						BiomOreMod.COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR.configure(
								new CountChanceConfig(chance, 2, count, top - topOffset, Target.NATURAL_STONE)
						)
				)
		);
	}
}
