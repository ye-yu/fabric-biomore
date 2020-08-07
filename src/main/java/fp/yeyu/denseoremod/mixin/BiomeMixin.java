package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
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
	@Shadow
	@Final
	public static Logger LOGGER;
	public List<ConfiguredFeature<?, ?>> lastOreFeature = new ArrayList<>();
	@Shadow
	@Final
	protected Map<GenerationStep.Feature, List<ConfiguredFeature<?, ?>>> features;

	@Shadow
	public abstract void addFeature(GenerationStep.Feature step, ConfiguredFeature<?, ?> configuredFeature);

	@Shadow
	public abstract Biome.Category getCategory();

	@Shadow
	public abstract Text getName();

	@Inject(method = "generateFeatureStep", at = @At("HEAD"), cancellable = true)
	public void generateFeatureStepMixinHead(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		if (!world.getLevelProperties().getGameRules().getBoolean(BiomOreMod.BIOMORE)) return;
		if (this.getCategory() == Biome.Category.THEEND || this.getCategory() == Biome.Category.NETHER || this.getCategory() == Biome.Category.NONE)
			return;
		// todo: use static array list instead of new arraylist every time
		lastOreFeature = this.features.get(GenerationStep.Feature.UNDERGROUND_ORES);
		this.features.put(GenerationStep.Feature.UNDERGROUND_ORES, new ArrayList<>());
		BiomOreFeatures.methods.getOrDefault(this.getCategory().getName(), BiomOreFeatures::none).accept(this);
	}


	@Inject(method = "generateFeatureStep", at = @At("RETURN"), cancellable = true)
	public void generateFeatureStepMixinTail(GenerationStep.Feature step, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ServerWorldAccess world, long populationSeed, ChunkRandom chunkRandom, BlockPos pos, CallbackInfo ci) {
		if (lastOreFeature.isEmpty()) return;
		this.features.put(GenerationStep.Feature.UNDERGROUND_ORES, lastOreFeature);
	}
}
