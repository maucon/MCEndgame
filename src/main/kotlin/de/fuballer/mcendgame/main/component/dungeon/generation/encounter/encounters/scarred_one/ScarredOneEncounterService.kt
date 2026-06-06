package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.EncounterType
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging.CollectScarredOneEffectCountCommand
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.messaging.ScarredOneInteractEvent
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking.ScarredOneEffectsPayload
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.messaging.CollectDungeonEncountersCommand
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.messaging.GenerateDungeonEncountersEvent
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.scarred_one.ScarredOneEntity
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toVec3d
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.server.level.ServerPlayer
import java.util.*

@Injectable
class ScarredOneEncounterService {
    @CommandHandler
    fun on(cmd: CollectDungeonEncountersCommand) {
        if (cmd.playerSeed.hasBeenUsed) return
        if (cmd.random.nextDouble() >= ScarredOneEncounterSettings.getSpawnProbability(cmd.dungeonLevel)) return
        cmd.add(EncounterType.SCARRED_ONE)
    }

    @EventSubscriber(sync = true)
    fun on(event: GenerateDungeonEncountersEvent) {
        val amount = event.encounters[EncounterType.SCARRED_ONE] ?: return
        if (amount <= 0) return

        val locations = event.takeStartLocations(1) // limited to one
        if (locations.isEmpty()) return

        val encounterLocation = locations.first()

        val world = event.world
        val entity = ScarredOneEntity(CustomEntities.SCARRED_ONE, world)
        entity.setPos(encounterLocation.location.toVec3d().add(0.5, 0.0, 0.5))
        entity.isInvulnerable = true
        entity.lookAt(EntityAnchorArgument.Anchor.EYES, encounterLocation.facingToLocation.toVec3d().add(0.5, 1.0, 0.5))
        world.addFreshEntity(entity)
    }

    @EventSubscriber(sync = true)
    fun on(event: ScarredOneInteractEvent) {
        val scarredOne = event.scarredOne
        if (scarredOne.gotResponse) return

        if (!scarredOne.hasRolledEffects()) {
            val dungeonWorld = event.serverWorld

            val baseCount = ScarredOneEncounterSettings.getBaseEffectCount(dungeonWorld.getDungeonLevel())
            val cmd = CommandGateway.apply(CollectScarredOneEffectCountCommand(dungeonWorld, baseCount, baseCount))

            scarredOne.positiveEffects = ScarredOneEncounterSettings.getPositiveEffects(cmd.positive)
            scarredOne.negativeEffects = ScarredOneEncounterSettings.getNegativeEffects(cmd.negative)
        }

        val payload = ScarredOneEffectsPayload(scarredOne.positiveEffects, scarredOne.negativeEffects, scarredOne.uuid)
        ServerPlayNetworking.send(event.player, payload)
    }

    fun response(
        player: ServerPlayer,
        accept: Boolean,
        scarredOneUuid: UUID,
    ) {
        val world = player.level()
        val scarredOne = world.getEntity(scarredOneUuid) as? ScarredOneEntity ?: return
        if (scarredOne.gotResponse) return

        scarredOne.respond(player, accept, world)
    }
}