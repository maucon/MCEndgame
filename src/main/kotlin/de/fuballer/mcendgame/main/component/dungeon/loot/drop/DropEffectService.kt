package de.fuballer.mcendgame.main.component.dungeon.loot.drop

import de.fuballer.mcendgame.main.component.dungeon.loot.drop.effect.DungeonDropEffects
import de.fuballer.mcendgame.main.component.dungeon.loot.drop.selector.DungeonDropSelectors
import de.fuballer.mcendgame.main.messaging.misc.DungeonItemDropEvent
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.server.level.ServerLevel

private val EFFECTS = mapOf(
    DungeonDropSelectors.UNIQUE_PLAYER_DROPPED to DungeonDropEffects.UNIQUE_PLAYER_DROPPED,
    DungeonDropSelectors.UNIQUE to DungeonDropEffects.UNIQUE,

    DungeonDropSelectors.ASPECT_PLAYER_DROPPED to DungeonDropEffects.ASPECT_PLAYER_DROPPED,
    DungeonDropSelectors.ASPECT to DungeonDropEffects.ASPECT,

    DungeonDropSelectors.CRYSTAL_PLAYER_DROPPED to DungeonDropEffects.CRYSTAL_PLAYER_DROPPED,
    DungeonDropSelectors.CRYSTAL to DungeonDropEffects.CRYSTAL,

    DungeonDropSelectors.TOTEM_BASIC_PLAYER_DROPPED to DungeonDropEffects.TOTEM_BASIC_PLAYER_DROPPED,
    DungeonDropSelectors.TOTEM_BASIC to DungeonDropEffects.TOTEM_BASIC,
    DungeonDropSelectors.TOTEM_EFFECT_PLAYER_DROPPED to DungeonDropEffects.TOTEM_EFFECT_PLAYER_DROPPED,
    DungeonDropSelectors.TOTEM_EFFECT to DungeonDropEffects.TOTEM_EFFECT,
    DungeonDropSelectors.TOTEM_ULTIMATE_PLAYER_DROPPED to DungeonDropEffects.TOTEM_ULTIMATE_PLAYER_DROPPED,
    DungeonDropSelectors.TOTEM_ULTIMATE to DungeonDropEffects.TOTEM_ULTIMATE,
)

@Injectable
class DropEffectService {
    @EventSubscriber(sync = true)
    fun on(event: DungeonItemDropEvent) {
        val serverWorld = event.world as? ServerLevel ?: return

        EFFECTS.forEach {
            if (!it.key(event.stack, event.entity)) return@forEach

            it.value.apply(event.entity, serverWorld)
            return
        }
    }
}