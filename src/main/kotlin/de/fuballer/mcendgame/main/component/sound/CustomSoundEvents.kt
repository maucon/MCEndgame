package de.fuballer.mcendgame.main.component.sound

import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.Holder
import net.minecraft.sounds.Music
import net.minecraft.sounds.SoundEvent

@Injectable
object CustomSoundEvents {
    val DESERT_DUNGEON_MUSIC = RegistryUtil.registerSoundEvent("desert_dungeon_music")
    val DESERT_DUNGEON_MUSIC_ENTRY: Holder<SoundEvent> = Holder.direct(DESERT_DUNGEON_MUSIC)
    val DESERT_DUNGEON_MUSIC_SOUND = Music(DESERT_DUNGEON_MUSIC_ENTRY, 0, 0, true)

    val BEASTWEAVER_GROVE_MUSIC = RegistryUtil.registerSoundEvent("beastweaver_grove_music")
    val BEASTWEAVER_GROVE_MUSIC_ENTRY: Holder<SoundEvent> = Holder.direct(BEASTWEAVER_GROVE_MUSIC)
    val BEASTWEAVER_GROVE_MUSIC_SOUND = Music(BEASTWEAVER_GROVE_MUSIC_ENTRY, 0, 0, true)

    val WOLF_HOWL = RegistryUtil.registerSoundEvent("entity.wolf_howl")
}