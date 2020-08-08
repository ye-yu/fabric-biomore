package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
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
	public List<ConfiguredFeature<?, ?>> lastOreFeature = EMPTY;

	@Shadow
	@Final
	protected Map<GenerationStep.Feature, List<ConfiguredFeature<?, ?>>> features;

	@Shadow
	public abstract Biome.Category getCategory();

	@Inject(method = "generateFeatureStep", at = @At("HEAD"), cancellable = true)
	public void generateFeatureStepMixinHead(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		Biome biome = (Biome) (Object) this;
		if (!world.getLevelProperties().getGameRules().getBoolean(BiomOreMod.BIOMORE)) return;
		if (this.getCategory() == Biome.Category.THEEND || this.getCategory() == Biome.Category.NETHER || this.getCategory() == Biome.Category.NONE)
			return;
		lastOreFeature = this.features.get(BiomOreFeatures.TARGET_FEATURE);
		final Biome fallbackBiome = BiomOreFeatures.FALLBACK_BIOME.getOrDefault(this.getCategory(), Biomes.PLAINS);
		final ArrayList<ConfiguredFeature<?, ?>> oreFeatures = BiomOreFeatures.MAPPED_CONFIG_FEATURES.getOrDefault(biome, BiomOreFeatures.MAPPED_CONFIG_FEATURES.get(fallbackBiome));
		this.features.put(BiomOreFeatures.TARGET_FEATURE, oreFeatures);
	}


	@Inject(method = "generateFeatureStep", at = @At("RETURN"), cancellable = true)
	public void generateFeatureStepMixinTail(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		if (lastOreFeature.isEmpty()) return;
		this.features.put(BiomOreFeatures.TARGET_FEATURE, lastOreFeature);
		lastOreFeature = EMPTY;
	}
}
