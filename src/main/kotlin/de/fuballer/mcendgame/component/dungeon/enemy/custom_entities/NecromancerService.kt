package de.fuballer.mcendgame.component.dungeon.enemy.custom_entities

import de.fuballer.mcendgame.component.dungeon.enemy.data.CustomEntityType
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Spellcaster
import org.bukkit.event.entity.EntitySpellCastEvent

class NecromancerService {
    fun onEntitySpellCast(event: EntitySpellCastEvent) {
        if (event.entityType != EntityType.EVOKER) return
        if (event.entity.customName != CustomEntityType.NECROMANCER.customName) return

        if (event.spell == Spellcaster.Spell.SUMMON_VEX) {
            summonMinions(event.entity, CustomEntityType.SKELETON, 3)
            event.isCancelled = true
        }
    }

    private fun summonMinions(
        summoner: LivingEntity,
        minionType: CustomEntityType,
        amount: Int
    ) {
        val world = summoner.world

        for (i in 0 until amount) {
            val minion = world.spawnEntity(summoner.location, minionType.type) as LivingEntity

            val equip = minion.equipment!!
            equip.helmet = null
            equip.chestplate = null
            equip.leggings = null
            equip.boots = null
            equip.setItemInMainHand(null)
            equip.setItemInOffHand(null)
        }
    }
}