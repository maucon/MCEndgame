package de.fuballer.mcendgame.component.dungeon.loot

import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierUtil
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class BossLootService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (!entity.isBoss()) return

        val mapTier = entity.getMapTier() ?: return
        dropBossLoot(entity, mapTier)
    }

    private fun dropBossLoot(
        entity: Entity,
        mapTier: Int
    ) {
        val world = entity.world
        val location = entity.location

        val orbAmountChance = LootSettings.getBossOrbAmount(mapTier)
        val finalOrbAmountChance = ModifierUtil.calculateFinalModifierValue(entity, ModifierType.LOOT, orbAmountChance)

        val orbAmount = finalOrbAmountChance.toInt() + if (Math.random() < finalOrbAmountChance % 1) 1 else 0

        for (i in 0 until orbAmount) {
            val orb = RandomUtil.pick(LootSettings.BOSS_ORBS).option.clone()
            world.dropItemNaturally(location, orb)
        }
    }
}