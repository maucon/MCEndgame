package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.component.dungeon.modifier.Modifier
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierOperation
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DungeonBossSettings {
    const val FOLLOW_RANGE = 50.0

    val BOSS_POTION_EFFECTS = listOf(
        PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0, false, false),
        PotionEffect(PotionEffectType.SLOW, Int.MAX_VALUE, 255, false, false)
    )

    val EMPOWERED_LOOT_MODIFIER = Modifier(ModifierType.LOOT, ModifierOperation.MORE, 0.2)
}
