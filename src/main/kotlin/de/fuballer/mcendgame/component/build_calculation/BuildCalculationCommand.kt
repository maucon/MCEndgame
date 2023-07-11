package de.fuballer.mcendgame.component.build_calculation

import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class BuildCalculationCommand : CommandHandler {
    override fun getCommand() = BuildCalculationSettings.COMMAND_NAME

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) return false

        val commandExecutor = sender as? Player ?: return false
        val damageNumbers = args.mapNotNull { it.toDoubleOrNull() }
        if (damageNumbers.isEmpty()) return false

        val armor = commandExecutor.getAttribute(Attribute.GENERIC_ARMOR)?.value ?: 0.0
        val armorToughness = commandExecutor.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)?.value ?: 0.0
        val health = commandExecutor.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 0.0
        val knockbackResistance = commandExecutor.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)?.value ?: 0.0
        val attackDamage = commandExecutor.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.value ?: 0.0
        val attackSpeed = commandExecutor.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.value ?: 0.0
        val attackKnockback = commandExecutor.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)?.value ?: 0.0
        val luck = commandExecutor.getAttribute(Attribute.GENERIC_LUCK)?.value ?: 0
        val movementSpeed = commandExecutor.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.value ?: 0

        var protection = 0.0
        with(commandExecutor.inventory) {
            protection += helmet?.itemMeta?.enchants?.get(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
            protection += chestplate?.itemMeta?.enchants?.get(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
            protection += leggings?.itemMeta?.enchants?.get(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
            protection += boots?.itemMeta?.enchants?.get(Enchantment.PROTECTION_ENVIRONMENTAL) ?: 0
        }

        // TODO artifacts

        for (damage in damageNumbers) {
            val (damageThroughArmor, finalDamage, armorProtectionPercent, hitsUntilDeath, ehp) =
                calculateEHP(damage, armor, armorToughness, protection, health)

            commandExecutor.sendMessage("=== DAMANGE:$damage ===\ndamageThroughArmor=${damageThroughArmor.round()}\nfinalDamage=$finalDamage\narmorProtectionPercent=$armorProtectionPercent\nhitsUntilDeath=$hitsUntilDeath\nehp=$ehp")
        }
        commandExecutor.sendMessage("=============================")

        return true
    }

    private fun calculateEHP(
        incomingDamage: Double,
        armor: Double,
        armorToughness: Double,
        protection: Double,
        health: Double
    ): List<Double> {
        // see https://minecraft.fandom.com/wiki/Armor#Damage_protection
        val damage = incomingDamage * (1 - min(20.0, max(armor / 5, armor - incomingDamage / (2 + armorToughness / 4))) / 25)

        // see https://minecraft.fandom.com/wiki/Protection#Usage
        val finalDamage = damage * (1 - min(0.8, 0.04 * protection))

        val armorProtectionPercent = 100 - damage / incomingDamage * 100
        val hitsUntilDeath = health / finalDamage
        val ehp = hitsUntilDeath * incomingDamage

        return listOf(damage, finalDamage, armorProtectionPercent, hitsUntilDeath, ehp)
    }

    private fun Double.round() =
        BigDecimal(this)
            .setScale(BuildCalculationSettings.NUMBER_DECIMAL_PLACES, RoundingMode.HALF_UP)
            .toDouble()
}
