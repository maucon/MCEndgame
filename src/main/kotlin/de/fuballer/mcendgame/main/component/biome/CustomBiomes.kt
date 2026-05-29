package de.fuballer.mcendgame.main.component.biome

import de.fuballer.mcendgame.main.component.sound.CustomSoundEvents
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.registry.Registerable
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.world.attribute.BackgroundMusic
import net.minecraft.world.attribute.EnvironmentAttributes
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeEffects
import net.minecraft.world.biome.GenerationSettings
import net.minecraft.world.biome.SpawnSettings

@Injectable
object CustomBiomes {
    val DESERT_DUNGEON: RegistryKey<Biome> = RegistryKey.of(RegistryKeys.BIOME, IdentifierUtil.default("desert_dungeon"))

    fun bootstrap(context: Registerable<Biome>) {
        context.register(DESERT_DUNGEON, createDesertDungeon())
    }

    fun createDesertDungeon(): Biome {
        return Biome.Builder()
            .precipitation(false)
            .temperature(0.5f)
            .downfall(0.0f)
            .effects(BiomeEffects.Builder().waterColor(0x3F76E4).build())
            .generationSettings(GenerationSettings.Builder().build())
            .spawnSettings(SpawnSettings.Builder().build())
            .setEnvironmentAttribute(EnvironmentAttributes.BACKGROUND_MUSIC_AUDIO, BackgroundMusic(CustomSoundEvents.DESERT_DUNGEON_MUSIC_SOUND))
            .build()
    }
}