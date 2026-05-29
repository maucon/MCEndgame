package de.fuballer.mcendgame.main.component.sound

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.MusicSound
import net.minecraft.sound.SoundEvent

@Injectable
object CustomSoundEvents {
    val DESERT_DUNGEON_MUSIC = RegistryUtil.registerSoundEvent("desert_dungeon_music")
    val DESERT_DUNGEON_MUSIC_ENTRY: RegistryEntry<SoundEvent> = RegistryEntry.of(DESERT_DUNGEON_MUSIC)
    val DESERT_DUNGEON_MUSIC_SOUND = MusicSound(DESERT_DUNGEON_MUSIC_ENTRY, 0, 0, true)
}