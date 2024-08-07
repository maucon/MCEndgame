package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.setCanUseAbilities
import de.fuballer.mcendgame.util.extension.EntityExtension.setHitCountBasedHealth
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sqrt

private val velocity = Vector(0.3, 0.3, 0.0)
private const val cloneHitCountBasedHealth = 1

private fun getClonesAmount(mapTier: Int) = min(1 + sqrt(mapTier.toDouble()).toInt(), 5)

object ClonesAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val creature = caster as? Creature ?: return

        val customEntityType = creature.getCustomEntityType() ?: return
        val mapTier = creature.getMapTier() ?: 0

        val rotatedVelocity = velocity.clone()
        val clonesAmount = getClonesAmount(mapTier)
        val rotationPerClone = 2 * PI / clonesAmount

        val clones = SummonerUtil.summonMinions(creature, customEntityType, clonesAmount, creature.location)

        for (clone in clones) {
            clone.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)!!.baseValue = 1.0
            clone.setCanUseAbilities(false)
            clone.setHitCountBasedHealth(cloneHitCountBasedHealth)

            clone.velocity = rotatedVelocity
            rotatedVelocity.rotateAroundY(rotationPerClone)
        }
    }
}