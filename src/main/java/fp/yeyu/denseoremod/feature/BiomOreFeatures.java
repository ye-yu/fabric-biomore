package fp.yeyu.denseoremod.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Arrays;
import java.util.HashMap;
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
    }

    public static void forest(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        addSingleOre(biome, Blocks.DIAMOND_BLOCK, 0.15f, 5, 5, 0, 64);
        addThickVeinOre(biome, Blocks.LAPIS_ORE, 30, Target.NATURAL_STONE, 0.25f, 2, 5, 0, 120);
    }

    public static void taiga(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void swamp(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void river(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void nether(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void theend(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void icy(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void mushroom(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void beach(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void jungle(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void none(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void savanna(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void mesa(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
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
