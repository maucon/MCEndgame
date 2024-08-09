package de.fuballer.mcendgame.component.totem.totems.wolf_companion

import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.EntityExtension.isEnemy
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestTotemTier
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

private val WOLF_VARIANTS = listOf(
    Wolf.Variant.PALE,
    Wolf.Variant.SPOTTED,
    Wolf.Variant.SNOWY,
    Wolf.Variant.BLACK,
    Wolf.Variant.ASHEN,
    Wolf.Variant.RUSTY,
    Wolf.Variant.WOODS,
    Wolf.Variant.CHESTNUT,
    Wolf.Variant.STRIPED,
)

private val WOLF_ARMOR = ItemStack(Material.WOLF_ARMOR)

fun getWolfArmor() = WOLF_ARMOR.clone()

fun getArmorProbability(tier: TotemTier) = when (tier) {
    TotemTier.COMMON -> 0.05
    TotemTier.UNCOMMON -> 0.15
    TotemTier.RARE -> 0.3
    TotemTier.LEGENDARY -> 0.5
}

@Service
class WolfCompanionEffectService : Listener {
    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val player = event.player
        val tier = player.getHighestTotemTier(WolfCompanionTotemType) ?: return

        val (count, strength) = WolfCompanionTotemType.getValues(tier)
        val realStrength = strength - 1

        for (i in 1..count.toInt()) {
            val targetWorld = event.locationToTeleport.world!!
            val wolf = targetWorld.spawnEntity(event.locationToTeleport, EntityType.WOLF, false) as Wolf
            wolf.owner = player

            val potionEffect = PotionEffect(PotionEffectType.STRENGTH, Int.MAX_VALUE, realStrength.toInt(), false, false)
            wolf.addPotionEffect(potionEffect)

            wolf.isCollidable = false
            wolf.isInvulnerable = true
            wolf.variant = WOLF_VARIANTS.random()
            wolf.collarColor = DyeColor.entries.random()

            if (Random.nextDouble() > getArmorProbability(tier)) continue
            wolf.equipment.setItem(EquipmentSlot.BODY, getWolfArmor())
        }
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val player = event.player

        event.dungeonWorld.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.remove() }
    }

    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity

        if (!entity.world.isDungeonWorld()) return

        if (entity.isEnemy()) return
        if (entity !is Wolf) return

        if (event.target !is Player) return

        event.cancel()
    }
}