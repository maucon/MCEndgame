package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource

@Injectable
class DodgeService {
    @CommandHandler
    fun on(cmd: DodgeCalculationCommand) {
        val attributes = cmd.damagedAttributes[CustomAttributeTypes.DODGE] ?: return
        cmd.dodgeChances.addAll(attributes.map { it.rolls[0].asDoubleRoll().getValue() })
    }

    @EventSubscriber
    fun on(event: LivingEntityDodgedEvent) {
        val entity = event.entity
        val level = entity.level() as? ServerLevel ?: return
        DodgeSettings.PARTICLES.apply(level, entity)

        if (entity is ServerPlayer) sendDodgeSound(entity, SoundSource.PLAYERS, 1.0F, 1.0F)
        val attacker = event.attacker ?: return
        if (attacker is ServerPlayer) sendDodgeSound(attacker, SoundSource.HOSTILE, 0.9F, 0.9F)
    }

    private fun sendDodgeSound(
        player: ServerPlayer,
        source: SoundSource,
        volume: Float,
        pitch: Float,
    ) {
        player.connection.send(
            ClientboundSoundPacket(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(DodgeSettings.SOUND),
                source,
                player.x,
                player.y,
                player.z,
                volume,
                pitch,
                player.getRandom().nextLong()
            )
        )
    }
}