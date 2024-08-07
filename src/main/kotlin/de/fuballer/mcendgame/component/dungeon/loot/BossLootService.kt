package de.fuballer.mcendgame.component.dungeon.loot

import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierUtil
import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.isBoss
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Service
class BossLootService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        val entity = event.entity
        if (!entity.isBoss()) return

        val mapTier = entity.getMapTier() ?: return
        dropCraftingOrbs(entity, mapTier)
    }

    private fun dropCraftingOrbs(
        entity: Entity,
        mapTier: Int
    ) {
        val world = entity.world
        val location = entity.location

        val baseOrbAmountChance = LootSettings.getBossOrbAmount(mapTier)
        val lootMultiplier = ModifierUtil.getModifierMultiplier(entity, ModifierType.LOOT_DROP)
        val finalOrbAmountChance = baseOrbAmountChance * lootMultiplier

        val orbAmount = finalOrbAmountChance.toInt() + if (Math.random() < finalOrbAmountChance % 1) 1 else 0

        for (i in 0 until orbAmount) {
            val bossLootOptions = LootSettings.getBossLootOptions(mapTier)
            val loot = RandomUtil.pick(bossLootOptions).option
            world.dropItemNaturally(location, loot.clone())
        }
    }
}