package de.fuballer.mcendgame.main.runtime_worlds.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class VoidChunkGenerator extends ChunkGenerator {
    public static final MapCodec<VoidChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.CODEC.stable().fieldOf("biome").forGetter(VoidChunkGenerator::getBiome)
    ).apply(instance, instance.stable(VoidChunkGenerator::new)));

    private static final NoiseColumn EMPTY_SAMPLE = new NoiseColumn(0, new BlockState[0]);

    private final Holder<Biome> biome;

    public VoidChunkGenerator(Holder<Biome> biome) {
        super(new FixedBiomeSource(biome));
        this.biome = biome;
    }

    @Deprecated
    public VoidChunkGenerator(Supplier<Biome> biome) {
        this(Holder.direct(biome.get()));
    }

    public VoidChunkGenerator(Registry<Biome> biomeRegistry, ResourceKey<Biome> biome) {
        this(biomeRegistry.get(biome).orElseThrow());
    }

    public VoidChunkGenerator(MinecraftServer server, ResourceKey<Biome> biome) {
        this(server.registryAccess().lookupOrThrow(Registries.BIOME), biome);
    }

    @Override
    protected @NonNull MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    protected Holder<Biome> getBiome() {
        return this.biome;
    }

    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager world, StructureManager structureAccessor, ChunkAccess chunk) {
    }

    @Override
    public void createReferences(WorldGenLevel world, StructureManager accessor, ChunkAccess chunk) {
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return -64;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor world, RandomState noiseConfig) {
        return 384;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor world, RandomState noiseConfig) {
        return EMPTY_SAMPLE;
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos) {
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
    }

    @Override
    public int getGenDepth() {
        return 0;
    }

    @Nullable
    @Override
    public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel world, HolderSet<Structure> structures, BlockPos center, int radius, boolean skipReferencedStructures) {
        return null;
    }

    @Override
    public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager accessor, MobCategory group, BlockPos pos) {
        return WeightedList.of();
    }

    @Override
    public void createStructures(RegistryAccess registryManager, ChunkGeneratorStructureState placementCalculator, StructureManager structureAccessor, ChunkAccess chunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> dimension) {
    }

    @Override
    public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSetRegistry, RandomState noiseConfig, long seed) {
        return ChunkGeneratorStructureState.createForFlat(noiseConfig, seed, biomeSource, Stream.empty());
    }
}
