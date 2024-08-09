package de.fuballer.mcendgame.component.item.attribute.effects.summon_support_wolf

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType
import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getFirstAsString
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.getSecondAsInt
import de.fuballer.mcendgame.util.extension.EntityExtension.getActiveSupportWolf
import de.fuballer.mcendgame.util.extension.EntityExtension.setActiveSupportWolf
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.LivingEntityExtension.getCustomAttributes
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Wolf
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPortalEvent
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.vehicle.VehicleEnterEvent
import org.bukkit.event.world.EntitiesUnloadEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.pow

private val SLOWNESS_EFFECT = PotionEffect(PotionEffectType.SLOWNESS, 100, 1, true, false)
private val WEAKNESS_EFFECT = PotionEffect(PotionEffectType.WEAKNESS, 100, 1, true, false)
private val SPEED_EFFECT = PotionEffect(PotionEffectType.SPEED, 200, 1, true, false)
private const val LIFE_STEAL_ON_HIT = 1.0
private const val LIFE_STEAL_ON_KILL = 2.0
private const val PLAYER_INC_DAMAGE = 0.1
private const val WOLF_MORE_DAMAGE = 0.25

private val slotMap = mapOf(
    EquipmentSlot.HEAD to SlotType.HEAD,
    EquipmentSlot.CHEST to SlotType.CHEST,
    EquipmentSlot.LEGS to SlotType.LEGS,
    EquipmentSlot.FEET to SlotType.FEET,
)

@Service
class SummonSupportWolfEffectService : Listener {
    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerQuitEvent) {
        removeWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerChangedWorldEvent) {
        removeWolves(event.player, event.from)
        removeWolves(event.player)
        spawnWolves(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerArmorChangeEvent) {
        if (event.newItem.getCustomAttributes() == event.oldItem.getCustomAttributes()) return // item durability change

        val player = event.player
        val newAttributes = event.newItem.getCustomAttributes()
            ?.filter { it.type == CustomAttributeTypes.SUMMON_SUPPORT_WOLF }

        removeWolves(player, event.slotType)
        spawnWolves(player, newAttributes, event.slotType)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityPortalEvent) {
        val wolf = event.entity as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: VehicleEnterEvent) {
        val wolf = event.entered as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerInteractEntityEvent) {
        val wolf = event.rightClicked as? Wolf ?: return
        if (wolf.getActiveSupportWolf() == null) return

        event.cancel()
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

    @EventHandler(ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        val damager = event.damager
        val player = damager as? Player ?: (damager as? Wolf ?: return).owner as? Player ?: return

        val supportWolfAttributes = player.getCustomAttributes()[CustomAttributeTypes.SUMMON_SUPPORT_WOLF] ?: return
        val incitingWolfAmount = supportWolfAttributes.filter { SupportWolfType.getByString(it.attributeRolls.getFirstAsString()) == SupportWolfType.INCITING }.size
        if (incitingWolfAmount == 0) return

        if (damager is Player) {
            event.increasedDamage.add(PLAYER_INC_DAMAGE * incitingWolfAmount)
            return
        }
        event.moreDamage.add(1 - (1 + WOLF_MORE_DAMAGE).pow(incitingWolfAmount))
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        val target = event.entity as? LivingEntity ?: return
        val wolf = event.damager as? Wolf ?: return

        val supportWolfType = wolf.getActiveSupportWolf()?.type ?: return
        if (supportWolfType == SupportWolfType.INCITING) return

        when (supportWolfType) {
            SupportWolfType.SLOWING -> onLivingEntityDamagedBySlowingWolf(target)
            SupportWolfType.WEAKENING -> onLivingEntityDamagedByWeakeningWolf(target)
            SupportWolfType.LIFE_STEALING -> onLivingEntityDamagedByLifeStealingWolf(target, wolf)
            SupportWolfType.HASTING -> onLivingEntityDamagedByHastingWolf(wolf)
            else -> return
        }
    }

    private fun onLivingEntityDamagedBySlowingWolf(target: LivingEntity) {
        if (target.isDead) return
        target.addPotionEffect(SLOWNESS_EFFECT)
    }

    private fun onLivingEntityDamagedByWeakeningWolf(target: LivingEntity) {
        if (target.isDead) return
        target.addPotionEffect(WEAKNESS_EFFECT)
    }

    private fun onLivingEntityDamagedByLifeStealingWolf(target: LivingEntity, wolf: Wolf) {
        val player = wolf.owner as? Player ?: return
        val healAmount = if (target.isDead) LIFE_STEAL_ON_KILL else LIFE_STEAL_ON_HIT
        player.heal(healAmount)
    }

    private fun onLivingEntityDamagedByHastingWolf(wolf: Wolf) {
        val player = wolf.owner as? Player ?: return
        player.addPotionEffect(SPEED_EFFECT)

        val world = player.world
        world.getEntitiesByClass(Wolf::class.java)
            .filter { it.owner == player }
            .forEach { it.addPotionEffect(SPEED_EFFECT) }
    }

    private fun spawnWolves(player: Player, world: World? = null) {
        val equipment = player.equipment

        slotMap.forEach { slot ->
            val item = equipment.getItem(slot.key)
            val attributes = item.getCustomAttributes()
                ?.filter { it.type == CustomAttributeTypes.SUMMON_SUPPORT_WOLF }

            spawnWolves(player, attributes, slot.value, world)
        }
    }

    private fun spawnWolves(player: Player, attributes: List<CustomAttribute>?, slot: SlotType, world: World? = null) {
        if (attributes.isNullOrEmpty()) return

        val actualWorld = world ?: player.world
        for (attribute in attributes) {
            val wolf = actualWorld.spawnEntity(player.location, EntityType.WOLF, false) as Wolf
            wolf.owner = player

            val supportWolfType = SupportWolfType.getByString(attribute.attributeRolls.getFirstAsString())
            supportWolfType.updateWolf(wolf)
            wolf.setActiveSupportWolf(ActiveSupportWolf(supportWolfType, slot))

            val strengthEffect = PotionEffect(PotionEffectType.STRENGTH, Int.MAX_VALUE, attribute.attributeRolls.getSecondAsInt() - 1, false, false)
            wolf.addPotionEffect(strengthEffect)

            wolf.isInvulnerable = true
        }
    }

    private fun removeWolves(player: Player, world: World? = null) {
        getPlayerWolfs(player, world)
            .filter { it.getActiveSupportWolf() != null }
            .forEach { it.remove() }
    }

    private fun removeWolves(player: Player, slot: SlotType, world: World? = null) {
        getPlayerWolfs(player, world)
            .filter { it.getActiveSupportWolf()?.slot == slot }
            .forEach { it.remove() }
    }

    private fun getPlayerWolfs(player: Player, world: World?): List<Wolf> {
        val actualWorld = world ?: player.world
        return actualWorld.getEntitiesByClass(Wolf::class.java)
            .map { it as Wolf }
            .filter { it.owner?.uniqueId == player.uniqueId }
    }
}