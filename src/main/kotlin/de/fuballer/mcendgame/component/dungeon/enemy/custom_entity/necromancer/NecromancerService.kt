package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.necromancer

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionsEntity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Spellcaster
import org.bukkit.event.entity.EntitySpellCastEvent
import kotlin.math.min

class NecromancerService(
    private val minionRepo: MinionRepository,
) {
    fun onEntitySpellCast(event: EntitySpellCastEvent) {
        if (event.entityType != EntityType.EVOKER) return
        if (event.entity.customName != CustomEntityType.NECROMANCER.customName) return

        if (event.spell == Spellcaster.Spell.SUMMON_VEX)
            onSummonVexSpell(event)
    }

    private fun onSummonVexSpell(event: EntitySpellCastEvent) {
        val minionsEntity = minionRepo.findById(event.entity.uniqueId)
            ?: MinionsEntity(event.entity.uniqueId)

        updateMinions(minionsEntity)

        val spawnAmount = min(NecromancerSettings.SPAWN_AMOUNT, NecromancerSettings.MAX_MINIONS - minionsEntity.minions.size)
        if (spawnAmount <= 0) {
            event.isCancelled = true
            return
        }

        summonMinions(event.entity, CustomEntityType.SKELETON, spawnAmount)
        event.isCancelled = true
    }

    private fun updateMinions(minionsEntity: MinionsEntity) {
        minionsEntity.minions.removeIf { it.isDead }
    }

    private fun summonMinions(
        summoner: LivingEntity,
        minionType: CustomEntityType,
        amount: Int
    ) {
        val world = summoner.world

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val minion = world.spawnEntity(summoner.location, minionType.type) as LivingEntity

            val equip = minion.equipment!!
            equip.helmet = null
            equip.chestplate = null
            equip.leggings = null
            equip.boots = null
            equip.setItemInMainHand(null)
            equip.setItemInOffHand(null)

            minions.add(minion)
        }

        if (!minionRepo.exists(summoner.uniqueId))
            minionRepo.save(MinionsEntity(summoner.uniqueId, minions))
        else
            minionRepo.getById(summoner.uniqueId).minions.addAll(minions)
    }
}