package de.fuballer.mcendgame.main.component.biome

import de.fuballer.mcendgame.main.component.sound.CustomSoundEvents
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.attribute.BackgroundMusic
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

@Injectable
object CustomBiomes {
    val STRONGHOLD_DUNGEON: ResourceKey<Biome> = of("stronghold_dungeon")
    val DESERT_DUNGEON: ResourceKey<Biome> = of("desert_dungeon")
    val BEASTWEAVER_GROVE_DUNGEON: ResourceKey<Biome> = of("beastweaver_grove")

    private fun of(id: String) = ResourceKey.create(Registries.BIOME, IdentifierUtil.default(id))

    fun bootstrap(context: BootstrapContext<Biome>) {
        context.register(STRONGHOLD_DUNGEON, createStrongholdDungeon())
        context.register(DESERT_DUNGEON, createDesertDungeon())
        context.register(BEASTWEAVER_GROVE_DUNGEON, createBeastweaverDungeon())
    }

    private fun createStrongholdDungeon(): Biome {
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(0.5f)
            .downfall(0.0f)
            .specialEffects(BiomeSpecialEffects.Builder().waterColor(0x3A4654).build())
            .generationSettings(BiomeGenerationSettings.PlainBuilder().build())
            .mobSpawnSettings(MobSpawnSettings.Builder().build())
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic(CustomSoundEvents.STRONGHOLD_DUNGEON_MUSIC_SOUND))
            .build()
    }

    private fun createDesertDungeon(): Biome {
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(0.5f)
            .downfall(0.0f)
            .specialEffects(BiomeSpecialEffects.Builder().waterColor(0x3F76E4).build())
            .generationSettings(BiomeGenerationSettings.PlainBuilder().build())
            .mobSpawnSettings(MobSpawnSettings.Builder().build())
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic(CustomSoundEvents.DESERT_DUNGEON_MUSIC_SOUND))
            .build()
    }

    private fun createBeastweaverDungeon(): Biome {
        return Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(0.5f)
            .downfall(0.0f)
            .specialEffects(BiomeSpecialEffects.Builder().waterColor(0x3F9B5F).build())
            .generationSettings(BiomeGenerationSettings.PlainBuilder().build())
            .mobSpawnSettings(MobSpawnSettings.Builder().build())
            .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic(CustomSoundEvents.BEASTWEAVER_GROVE_MUSIC_SOUND))
            .build()
    }
}