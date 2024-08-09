package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsString
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getSecondAsInt
import de.fuballer.mcendgame.util.extension.EntityExtension.getActiveSupportWolf
import de.fuballer.mcendgame.util.extension.EntityExtension.setActiveSupportWolf
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.EntitiesUnloadEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

private val slotMap = mapOf(
    EquipmentSlot.HEAD to SlotType.HEAD,
    EquipmentSlot.CHEST to SlotType.CHEST,
    EquipmentSlot.LEGS to SlotType.LEGS,
    EquipmentSlot.FEET to SlotType.FEET,
)

@Service
class SummonSupportWolfSpawningService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerArmorChangeEvent) {
        if (event.newItem.getCustomAttributes() == event.oldItem.getCustomAttributes()) return // item hasn't changed

        val player = event.player
        val newAttributes = event.newItem.getCustomAttributes()
            ?.filter { it.type == CustomAttributeTypes.SUMMON_SUPPORT_WOLF }

        removeWolves(player, event.slotType)
        spawnWolves(player, newAttributes, event.slotType)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerChangedWorldEvent) {
        removeWolves(event.player, event.from)
        removeWolves(event.player)

        spawnWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerDeathEvent) {
        removeWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerPostRespawnEvent) {
        removeWolves(event.player)
        spawnWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerQuitEvent) {
        removeWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: EntitiesUnloadEvent) {
        event.entities
            .filter { it is Wolf && it.getActiveSupportWolf() != null }
            .map { it as Wolf }
            .forEach {
                val owner = it.owner as? Player
                if (owner == null) {
                    it.remove()
                    return@forEach
                }

                it.teleport(owner.location)
            }
    }

    private fun removeWolves(player: Player, world: World = player.world) {
        getPlayerWolfs(player, world)
            .filter { it.getActiveSupportWolf() != null }
            .forEach { it.remove() }
    }

    private fun removeWolves(player: Player, slot: SlotType) {
        getPlayerWolfs(player, player.world)
            .filter { it.getActiveSupportWolf()?.slot == slot }
            .forEach { it.remove() }
    }

    private fun spawnWolves(player: Player) {
        val equipment = player.equipment

        slotMap.forEach { slot ->
            val item = equipment.getItem(slot.key)
            val attributes = item.getCustomAttributes()
                ?.filter { it.type == CustomAttributeTypes.SUMMON_SUPPORT_WOLF }

            spawnWolves(player, attributes, slot.value)
        }
    }

    private fun spawnWolves(player: Player, attributes: List<CustomAttribute>?, slot: SlotType) {
        if (attributes.isNullOrEmpty()) return

        for (attribute in attributes) {
            val wolf = player.world.spawnEntity(player.location, EntityType.WOLF, false) as Wolf
            wolf.owner = player

            val supportWolfTypeString = attribute.attributeRolls.getFirstAsString()
            val supportWolfType = SupportWolfType.fromString(supportWolfTypeString)
            supportWolfType.updateWolf(wolf)
            wolf.setActiveSupportWolf(ActiveSupportWolf(supportWolfType, slot))

            val strengthAmplifier = attribute.attributeRolls.getSecondAsInt() - 1
            val strengthEffect = PotionEffect(PotionEffectType.STRENGTH, PotionEffect.INFINITE_DURATION, strengthAmplifier, false, false)
            wolf.addPotionEffect(strengthEffect)
        }
    }

    private fun getPlayerWolfs(player: Player, world: World): List<Wolf> {
        return world.getEntitiesByClass(Wolf::class.java)
            .map { it as Wolf }
            .filter { it.owner?.uniqueId == player.uniqueId }
    }
}