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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class BiomOreFeatures {
    public static final HashMap<String, Consumer<Object>> methods = Maps.newHashMap();
    public static final HashMap<Block, Integer> commonVeinSize = Maps.newHashMap();
    public static final double AMP = 3;

    static {
        methods.put("ocean", BiomOreFeatures::ocean);
        methods.put("plains", BiomOreFeatures::plains);
        methods.put("desert", BiomOreFeatures::desert);
        methods.put("extreme_hills", BiomOreFeatures::extremeHills);
        methods.put("forest", BiomOreFeatures::forest);
        methods.put("taiga", BiomOreFeatures::taiga);
        methods.put("swamp", BiomOreFeatures::swamp);
        methods.put("river", BiomOreFeatures::river);
        methods.put("nether", BiomOreFeatures::nether);
        methods.put("theend", BiomOreFeatures::theend);
        methods.put("icy", BiomOreFeatures::icy);
        methods.put("mushroom", BiomOreFeatures::mushroom);
        methods.put("beach", BiomOreFeatures::beach);
        methods.put("jungle", BiomOreFeatures::jungle);
        methods.put("none", BiomOreFeatures::none);
        methods.put("savanna", BiomOreFeatures::savanna);
        methods.put("mesa", BiomOreFeatures::mesa);

        commonVeinSize.put(Blocks.COAL_ORE, 17);
        commonVeinSize.put(Blocks.IRON_ORE, 9);
        commonVeinSize.put(Blocks.GOLD_ORE, 9);
        commonVeinSize.put(Blocks.REDSTONE_ORE, 8);
        commonVeinSize.put(Blocks.DIAMOND_ORE, 8);
        commonVeinSize.put(Blocks.LAPIS_ORE, 7);
    }

    public static void ocean(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);

        // custom ores generation
        addThickVeinOre(biome, Blocks.DIAMOND_ORE, 0.25f, 5, 0, 64);
        addSurfaceVeinOre(biome, Blocks.EMERALD_ORE, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
        if (biome == Biomes.LUKEWARM_OCEAN)
            addSingleOre(biome, Blocks.GOLD_BLOCK, 0.35f, 5, 5, 0, 64);
        if (biome == Biomes.DEEP_FROZEN_OCEAN)
            addSingleOre(biome, Blocks.EMERALD_BLOCK, 0.35f, 5, 5, 0, 64);
        if (Arrays.asList(new Biome[]{Biomes.DEEP_FROZEN_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_WARM_OCEAN}).contains(biome)) {
            addSurfaceVeinOre(biome, Blocks.PRISMARINE, 5, Target.OVERWORLD_SURFACE_BLOCK, 0.2f, 1, 20, 0, 64);
            addSurfaceVeinOre(biome, Blocks.SEA_LANTERN, 2, Target.OVERWORLD_SURFACE_BLOCK, 0.2f / 0.5f, 1, 20, 0, 64);
            addSingleOre(biome, Blocks.SEA_LANTERN, 0.35f, 5, 5, 0, 64);
        }

    }

    public static void plains(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.ANDESITE, 50, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        addThickVeinOre(biome, Blocks.COAL_ORE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(biome, Blocks.IRON_ORE, 35, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
    }

    public static void desert(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);

        addThickVeinOre(biome, Blocks.GOLD_ORE, 18, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 64);
        addThickVeinOre(biome, Blocks.REDSTONE_ORE, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
        addThickVeinOre(biome, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 3, 5, 0, 64);
        addSingleOre(biome, Blocks.REDSTONE_BLOCK, 0.35f, 5, 5, 0, 64);
        if (biome == Biomes.DESERT_HILLS) {
            addThickVeinOre(biome, Blocks.GOLD_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 1, 5, 0, 64);
        }
        if (biome == Biomes.DESERT_LAKES) {
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.CLAY.getDefaultState(), 5, 2, Lists.newArrayList(Blocks.SAND.getDefaultState()))).createDecoratedFeature(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
        }
    }

    public static void extremeHills(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(biome, Blocks.LAPIS_BLOCK, 12, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
        addThickVeinOre(biome, Blocks.MOSSY_COBBLESTONE, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
    }

    public static void forest(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        addThickVeinOre(biome, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        if (biome == Biomes.DARK_FOREST || biome == Biomes.DARK_FOREST_HILLS) {
            // do: change generation method
            biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, BiomOreMod.DRY_DISK_FEATURE.configure(new DiskFeatureConfig(Blocks.MYCELIUM.getDefaultState(), 5, 1, Lists.newArrayList(Blocks.GRASS_BLOCK.getDefaultState()))).createDecoratedFeature(BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(new CountChanceConfig(0.15f, 1, 40, 40, Target.OVERWORLD_SURFACE_BLOCK))));
            addThickVeinOre(biome, Blocks.GLOWSTONE, 3, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        }
    }

    public static void taiga(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.DIAMOND_BLOCK, 5, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
        addThickVeinOre(biome, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
        if (Arrays.asList(Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS).contains(biome)) {
            addThickVeinOre(biome, Blocks.SNOW_BLOCK, 7, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
            addThickVeinOre(biome, Blocks.BLUE_ICE, 5, Target.NATURAL_STONE, 0.5f, 3, 5, 0, 120);
        }
    }

    public static void swamp(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addClay(biome);
        addSurfaceVeinOre(biome, Blocks.SLIME_BLOCK, 3, Target.OVERWORLD_SURFACE_BLOCK, 0.65f, 1, 20, 0, 64);
        addThickVeinOre(biome, Blocks.EMERALD_BLOCK, 15, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 64);
        addSurfaceVeinOre(biome, Blocks.EMERALD_ORE, 12, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
    }

    public static void river(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.COAL_BLOCK, 12, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 64);
        addThickVeinOre(biome, Blocks.IRON_ORE, 25, Target.NATURAL_STONE, 0.45f, 2, 5, 0, 20);
    }

    public static void nether(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void theend(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void icy(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.COAL_BLOCK, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(biome, Blocks.LAPIS_BLOCK, 30, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
        addThickVeinOre(biome, Blocks.PACKED_ICE, 7, Target.NATURAL_STONE, 0.25f, 3, 5, 0, 120);
    }

    public static void mushroom(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
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
        for (Block block: blocks) {
            addThickVeinOre(biome, block, 3, Target.NATURAL_STONE, 0.33f, 1, 5, 0, 64);
        }
    }

    public static void beach(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.IRON_ORE, 0.25f, 5, 0, 64);
        addThickVeinOre(biome, Blocks.GOLD_ORE, 0.25f, 5, 0, 64);
    }

    public static void jungle(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.IRON_BLOCK, 45, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addSurfaceVeinOre(biome, Blocks.DIAMOND_BLOCK, 3, Target.OVERWORLD_SURFACE_BLOCK, 0.08f, 1, 20, 0, 64);
        addThickVeinOre(biome, Blocks.DIORITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        RandomPatchFeatureConfig lanternConfig = (new RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.LANTERN.getDefaultState()), new SimpleBlockPlacer())).tries(1).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK)).cannotProject().build();
        biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Feature.RANDOM_PATCH.configure(lanternConfig).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(55))));
    }

    public static void none(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void savanna(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.REDSTONE_ORE, 30, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(biome, Blocks.GRANITE, 60, Target.NATURAL_STONE, 0.25f, 1, 5, 0, 120);
        addThickVeinOre(biome, Blocks.LAPIS_BLOCK, 12, Target.NATURAL_STONE, 0.4f, 2, 5, 0, 20);
    }

    public static void mesa(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);
        addThickVeinOre(biome, Blocks.BONE_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 5, 5, 0, 64);
        addThickVeinOre(biome, Blocks.IRON_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 64);
        addThickVeinOre(biome, Blocks.REDSTONE_ORE, 24, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 20);
        addThickVeinOre(biome, Blocks.COAL_BLOCK, 3, Target.NATURAL_STONE, 0.2f, 10, 5, 0, 64);
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_DECORATION, Feature.GLOWSTONE_BLOB.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.LIGHT_GEM_CHANCE.configure(new CountDecoratorConfig(8))));
    }

    public static void addThickVeinOre(Biome biome,
                                       Block block,
                                       int veinSize,
                                       Target target,
                                       float chance,
                                       int count,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
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

    public static void addSurfaceVeinOre(Biome biome,
                                         Block block,
                                         int veinSize,
                                         Target target,
                                         float chance,
                                         int count,
                                         int bottomOffset,
                                         int topOffSet,
                                         int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
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

    public static void addThickVeinOre(Biome biome,
                                       Block block,
                                       float chance,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        addThickVeinOre(
                biome,
                block,
                (int) Math.round(commonVeinSize.get(block) * AMP),
                Target.NATURAL_STONE,
                chance,
                1,
                bottomOffset,
                topOffSet,
                top);
    }

    public static void addSingleOre(Biome biome,
                                    Block block,
                                    float chance,
                                    int count,
                                    int bottomOffset,
                                    int topOffset,
                                    int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
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
