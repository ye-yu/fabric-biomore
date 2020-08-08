package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(DebugHud.class)
public class DebugHubMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "getLeftText", at = @At("RETURN"))
	public void leftTextMixin(CallbackInfoReturnable<List<String>> cir) {
		assert this.client.world != null;
		IntegratedServer integratedServer = this.client.getServer();
		assert integratedServer != null;
		if (!integratedServer.getWorlds().iterator().hasNext()) return;
		final ServerWorld world = integratedServer.getWorlds().iterator().next();
		if (!world.getLevelProperties().getGameRules().getBoolean(BiomOreMod.BIOMORE)) return;
		final List<String> returnValue = cir.getReturnValue();
		BlockPos blockPos = Objects.requireNonNull(this.client.getCameraEntity()).getBlockPos();
		final Biome biome = this.client.world.getBiome(blockPos);
		returnValue.add("Biome category: " + StringUtils.capitalize(biome.getCategory().toString()));
		returnValue.add("Obtainable ores:");
		returnValue.add(BiomOreFeatures.BIOME_CONTAINS.getOrDefault(biome, BiomOreFeatures.BIOME_CONTAINS.get(BiomOreFeatures.FALLBACK_BIOME.get(biome.getCategory()))));
	}
}
