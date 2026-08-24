package de.fuballer.mcendgame.main.component.dungeon.enemy.boss

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes

object DungeonBossSettings {
    const val MORE_LOOT_PER_KILLED_BOSS = 0.25
    const val LESS_DAMAGE_TAKEN_PER_KILLED_BOSS = 0.15
    const val MORE_DAMAGE_PER_KILLED_BOSS = 0.1

    fun getAttributePerKilledBoss(bossesKilled: Int) = listOf(
        getMoreLootAttribute(bossesKilled),
        getLessDamageTakenAttribute(bossesKilled),
        getMoreDamageAttribute(bossesKilled),
    )

    private fun getMoreLootAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.DROP_MORE_LOOT,
        roll = DoubleRoll(DoubleBounds(MORE_LOOT_PER_KILLED_BOSS * bossesKilled))
    )

    private fun getLessDamageTakenAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.MORE_DAMAGE_TAKEN,
        roll = DoubleRoll(DoubleBounds(-LESS_DAMAGE_TAKEN_PER_KILLED_BOSS * bossesKilled))
    )

    private fun getMoreDamageAttribute(bossesKilled: Int) = CustomAttribute(
        CustomAttributeTypes.MORE_DAMAGE,
        roll = DoubleRoll(DoubleBounds(MORE_DAMAGE_PER_KILLED_BOSS * bossesKilled))
    )
}