package de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect

import de.fuballer.mcendgame.main.component.dungeon.enemy.EnemyGenerationSettings
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.entity.Mob
import kotlin.random.Random

@Injectable
class PotionEffectService {
    fun addEffects(
        entity: Mob,
        level: Int,
        canBeInvisible: Boolean,
        random: Random,
    ) {
        val effects = mutableListOf(
            RandomUtil.pickBestOf(EnemyGenerationSettings.STRENGTH_EFFECTS, level, random).option,
            RandomUtil.pickBestOf(EnemyGenerationSettings.RESISTANCE_EFFECTS, level, random).option,
            RandomUtil.pickBestOf(EnemyGenerationSettings.SPEED_EFFECTS, level, random).option,
            RandomUtil.pickBestOf(EnemyGenerationSettings.FIRE_RESISTANCE_EFFECT, level, random).option,
            RandomUtil.pickOne(EnemyGenerationSettings.ON_DEATH_EFFECTS, random).option,
        )
        if (canBeInvisible) {
            effects.add(RandomUtil.pickOne(EnemyGenerationSettings.INVISIBILITY_EFFECT, random).option)
        }

        val effectInstances = effects.filterNotNull().map { it.getEffectInstance(false) }
        effectInstances.forEach { entity.addEffect(it) }
    }
}