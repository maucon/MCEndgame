package de.fuballer.mcendgame.main.component.killer

import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.component.killer.db.KillerRepository
import de.fuballer.mcendgame.main.component.killer.networking.KillerEntityPayload
import de.fuballer.mcendgame.main.messaging.misc.PlayerEntityDeathEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Injectable
class KillerService(
    private val killerRepo: KillerRepository,
) {
    fun openKillerInventory(
        commandExecutor: Player,
        killedPlayerUUID: UUID,
    ): Boolean {
        val killerEntity = killerRepo.findById(killedPlayerUUID) ?: return false
        val killerEntityPayload = KillerEntityPayload(killerEntity)
        val killerName = killerEntity.displayName.getOrNull() ?: Component.translatable("entity.mcendgame.unknown")

        val screenHandlerFactory = KillerScreenHandlerFactory(killerEntityPayload, killerName)
        { syncId, _, _ -> KillerScreenHandler(syncId, killerEntityPayload) }

        commandExecutor.openMenu(screenHandlerFactory)

        return true
    }

    @EventSubscriber(sync = true)
    fun on(event: PlayerEntityDeathEvent) {
        val player = event.player
        if (player.level().isClientSide) return
        val killer = event.killer ?: return

        killerRepo.save(KillerEntity.of(player, killer))
    }
}