package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.entity.LivingEntity

fun getSummonVineAmount(bossLevel: Int) = bossLevel / 5

object SummonVinesAbility : Ability {
    override fun cast(caster: LivingEntity, target: LivingEntity) {
        val mapTier = PersistentDataUtil.getValue(caster, DataTypeKeys.MAP_TIER) ?: 1

        val amount = getSummonVineAmount(mapTier)

        val vines = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val vine = CustomEntityType.spawnCustomEntity(CustomEntityType.VINE, caster.location, mapTier) as LivingEntity

            PersistentDataUtil.setValue(vine, DataTypeKeys.IS_MINION, true)

            vines.add(vine)
        }

        val event = DungeonEnemySpawnedEvent(caster.world, vines)
        EventGateway.apply(event)
    }
}