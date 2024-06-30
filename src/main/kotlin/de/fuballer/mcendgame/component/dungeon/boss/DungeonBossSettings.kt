package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.dungeon.modifier.Modifier
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierOperation
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import de.fuballer.mcendgame.util.EntityUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DungeonBossSettings {
    const val FOLLOW_RANGE = 50.0

    val BOSS_POTION_EFFECTS = listOf(
        PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0, false, false),
        PotionEffect(PotionEffectType.SLOWNESS, Int.MAX_VALUE, 255, false, false)
    )

    val EMPOWERED_LOOT_MODIFIER = Modifier(ModifierType.LOOT_DROP, ModifierOperation.MORE, 0.2, "boss empower")

    fun empowerStats(boss: LivingEntity) {
        EntityUtil.increaseBaseAttribute(boss, Attribute.GENERIC_MAX_HEALTH, 1.15)
        EntityUtil.increaseBaseAttribute(boss, Attribute.GENERIC_ATTACK_DAMAGE, 1.1)
        EntityUtil.increaseBaseAttribute(boss, Attribute.GENERIC_MOVEMENT_SPEED, 1.05)
    }
}