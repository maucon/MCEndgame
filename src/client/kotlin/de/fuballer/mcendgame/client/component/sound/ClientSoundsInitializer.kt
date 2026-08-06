package de.fuballer.mcendgame.client.component.sound

import de.fuballer.mcendgame.main.component.sound.Sounds
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer

@Injectable
class ClientSoundsInitializer {
    @Initializer
    fun init() {
        Sounds.setPlayer(ClientSoundPlayer())
    }
}