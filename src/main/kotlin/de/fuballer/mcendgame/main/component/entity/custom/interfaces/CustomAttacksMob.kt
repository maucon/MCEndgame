package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import com.geckolib.animatable.GeoEntity
import com.mojang.serialization.Codec
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

interface CustomAttacksMob<T> where T : Mob, T : GeoEntity {
    companion object {
        private const val ATTACK_COOLDOWNS_ID = "attack_cooldowns"
        private val ATTACK_COOLDOWNS_MAP_CODEC: Codec<Map<String, Int>> = Codec.unboundedMap(Codec.STRING, Codec.INT)
    }

    var attackPose: AttackPose
    var attackDuration: Int

    val attacks: List<RandomOption<out Attack<T>>>

    val attackCooldowns: MutableMap<Attack<T>, Int>

    val attackDataInstances: MutableList<DelayedAttackDataInstance>

    fun tickAttacks(
        world: ServerLevel,
        damager: T,
    ) {
        tickCooldowns()
        tickAttackDataInstances(world, damager)

        if (attackDuration > 0) {
            --attackDuration
            return
        }

        if (!canAttack()) return
        if (damager.target?.isAlive == true) return
        if (attackPose == AttackPose.DEFAULT) return
        val resetAttack = getResetAttack() ?: return
        attack(damager, resetAttack)
    }

    fun canAttack() = attackDuration == 0

    fun attack(
        attacker: T,
        attack: Attack<T>,
    ) {
        attackDuration = attack.totalDuration
        attackPose = attack.animationData.endPose

        attackCooldowns[attack] = attack.cooldown(attacker)

        val target = attacker.target
        attack.start(attacker, target)
        attackDataInstances.addAll(attack.getAttackDataInstances(target))
    }

    private fun tickCooldowns() {
        val iterator = attackCooldowns.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val decCD = entry.value - 1
            if (decCD <= 0) {
                iterator.remove()
                continue
            }
            entry.setValue(decCD)
        }
    }

    private fun tickAttackDataInstances(
        world: ServerLevel,
        damager: Mob,
    ) {
        val toRemove = mutableListOf<DelayedAttackDataInstance>()
        val target = damager.target
        for (instance in attackDataInstances) {
            if (!instance.tick(world, damager, target)) continue
            toRemove.add(instance)
        }
        attackDataInstances.removeAll(toRemove)
    }

    fun getRandomAttack(
        attacker: Mob,
        ignoreTriggerConditions: Boolean = false,
    ): Attack<T>? {
        val target = attacker.target
        val possibleAttacks = attacks
            .filter { it.option.animationData.startPose == attackPose }
            .filter { !attackCooldowns.containsKey(it.option) }
            .filter { ignoreTriggerConditions || it.option.canStart(attacker, target) }

        if (possibleAttacks.isNotEmpty()) return RandomUtil.pickOne(possibleAttacks).option
        return null
    }

    private fun getResetAttack(): Attack<T>? {
        val possibleAttacks = attacks
            .filter { it.option.animationData.startPose == attackPose }
            .filter { it.option.animationData.endPose == AttackPose.DEFAULT }
        if (possibleAttacks.isNotEmpty()) return RandomUtil.pickOne(possibleAttacks).option
        return null
    }

    fun addAttackCooldownsSaveData(output: ValueOutput) {
        val idCdMap = attackCooldowns.mapKeys { (attack, _) -> attack.id }
        output.store(ATTACK_COOLDOWNS_ID, ATTACK_COOLDOWNS_MAP_CODEC, idCdMap)
    }

    fun readAttackCooldownsSaveData(input: ValueInput) {
        input.read(ATTACK_COOLDOWNS_ID, ATTACK_COOLDOWNS_MAP_CODEC).ifPresent {
            it.forEach { (id, cd) ->
                val attack = getAttackFromId(id) ?: return@forEach
                attackCooldowns[attack] = cd
            }
        }
    }

    private fun getAttackFromId(id: String) = attacks.firstOrNull { it.option.id == id }?.option
}