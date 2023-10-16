package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.generation.data.LayoutTile
import de.fuballer.mcendgame.component.remaining.RemainingService
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.helper.WorldHelper
import de.fuballer.mcendgame.random.RandomPick
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.inventory.EntityEquipment
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.awt.Point
import java.util.*
import kotlin.math.ceil

class EnemyGenerationService(
    private val statItemService: StatItemService,
    private val remainingService: RemainingService
) : Service {
    private val random = Random()

    fun summonMonsters(
        layoutTiles: Array<Array<LayoutTile>>,
        startPoint: Point,
        mapTier: Int,
        world: World
    ) {
        for (x in layoutTiles.indices) {
            for (y in layoutTiles[0].indices) {

                if (startPoint.x == x && startPoint.y == y) continue

                Bukkit.getScheduler().runTask(
                    MCEndgame.PLUGIN,
                    Runnable {
                        val mobCount = EnemyGenerationSettings.calculateMobCount(random)
                        spawnMobs(mobCount, -x * 16.0 - 8, -y * 16.0 - 8, mapTier, world)
                    }
                )
            }
        }
    }

    private fun spawnMobs(
        amount: Int,
        x: Double,
        z: Double,
        mapTier: Int,
        world: World
    ) {
        for (i in 0 until amount) {
            val entityType = RandomPick.pick(EnemyGenerationSettings.DUNGEON_MOBS).option
            val entity = world.spawnEntity(
                Location(
                    world,
                    x + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1),
                    DungeonGenerationSettings.MOB_Y_POS,
                    z + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1)
                ),
                entityType.type
            ) as Creature

            if (entityType.customName != null) {
                entity.customName = entityType.customName
                entity.isCustomNameVisible = false
            }
            entity.removeWhenFarAway = false

            statItemService.setCreatureEquipment(entity, mapTier, entityType.canHaveWeapons, entityType.isRanged, entityType.canHaveArmor)

            if (!entityType.dropBaseLoot) {
                entity.persistentDataContainer.set(Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN, false)
            }
            entity.persistentDataContainer.set(Keys.MAP_TIER, PersistentDataType.INTEGER, mapTier)

            addEffectUntilLoad(entity)
            addTemporarySlowfalling(entity)
            addEffectsToEntity(entity, mapTier)
            //applyNamePrefix(entity)
        }

        remainingService.addMobs(world, amount)
    }

    fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
        if (event.cause != EntityPotionEffectEvent.Cause.EXPIRATION) return
        val effect = event.oldEffect ?: return
        if (effect.type != PotionEffectType.LUCK) return

        val entity = event.entity as? LivingEntity ?: return
        if (WorldHelper.isNotDungeonWorld(entity.world)) return

        entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    }

    private fun addEffectUntilLoad(entity: Creature) {
        val effect = PotionEffect(PotionEffectType.LUCK, 1, 0, false, false)
        entity.addPotionEffect(effect)
    }

    private fun addTemporarySlowfalling(entity: Creature) {
        val effect = PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, false, false)
        entity.addPotionEffect(effect)
    }

    fun addEffectsToEntity(
        entity: Creature,
        mapTier: Int
    ) {
        val potionEffects = listOfNotNull(
            RandomPick.pick(EnemyGenerationSettings.RESISTANCE_EFFECTS, mapTier).option,
            RandomPick.pick(EnemyGenerationSettings.SPEED_EFFECTS, mapTier).option,
            RandomPick.pick(EnemyGenerationSettings.FIRE_RESISTANCE_EFFECT, mapTier).option,
            RandomPick.pick(EnemyGenerationSettings.INVISIBILITY_EFFECT).option,
        ).map { it.getPotionEffect() }

        entity.addPotionEffects(potionEffects)
        addStrengthToEntity(entity, mapTier)
    }

    private fun addStrengthToEntity(
        entity: Creature,
        mapTier: Int
    ) {
        val strengthAmplifier = EnemyGenerationSettings.calculateStrengthAmplifier(random, mapTier)
        if (strengthAmplifier < 0) return

        val potionEffect = PotionEffect(PotionEffectType.INCREASE_DAMAGE, Int.MAX_VALUE, strengthAmplifier, false, false)
        entity.addPotionEffect(potionEffect)
    }

    private fun applyNamePrefix(creature: Creature) {
        val isMelee = isMelee(creature)
        val damage = getDamage(creature, isMelee)
        val prefix = EnemyGenerationSettings.calculateMobPrefix(damage) ?: return

        creature.removePotionEffect(PotionEffectType.INVISIBILITY)
        creature.customName = "$prefix ${creature.name}"
        creature.isCustomNameVisible = true
    }

    private fun isMelee(creature: Creature): Boolean {
        val type = creature.type
        if (!EnemyGenerationSettings.RANGED_MOBS.containsKey(type)) return true

        val equipment = creature.equipment ?: return true
        return equipment.itemInMainHand.type != Material.BOW
    }

    private fun getDamage(
        creature: Creature,
        melee: Boolean
    ): Double {
        val type = creature.type

        val difficultyBaseStats = (if (melee) EnemyGenerationSettings.MELEE_MOBS[type] else EnemyGenerationSettings.RANGED_MOBS[type])
            ?: return 0.0

        var damage = when (creature.world.difficulty) {
            Difficulty.EASY -> difficultyBaseStats.dmgEasy
            Difficulty.NORMAL -> difficultyBaseStats.dmgNormal
            else -> difficultyBaseStats.dmgHard
        }

        val equipment = creature.equipment
        if (equipment != null) damage = applyWeaponDamage(damage, equipment, melee)
        if (melee) damage = applyStrength(damage, creature)

        return damage
    }

    private fun applyStrength(
        damage: Double,
        creature: Creature
    ): Double {
        val effect = creature.getPotionEffect(PotionEffectType.INCREASE_DAMAGE) ?: return damage
        return damage + 3 * (1 + effect.amplifier)
    }

    private fun applyWeaponDamage(
        damage: Double,
        equipment: EntityEquipment,
        melee: Boolean
    ): Double {
        var calcDamage = damage
        val mainHand = equipment.itemInMainHand

        if (melee) {
            if (mainHand.containsEnchantment(Enchantment.DAMAGE_ALL)) calcDamage += 0.5 + 0.5 * mainHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL)
            calcDamage += getItemAddedDamage(mainHand)

            val offHand = equipment.itemInOffHand
            if (offHand.type != Material.SHIELD) return damage
            getItemAddedDamage(offHand)
        } else {
            if (mainHand.containsEnchantment(Enchantment.ARROW_DAMAGE))
                calcDamage = ceil(damage * (1.25 + 0.25 * mainHand.getEnchantmentLevel(Enchantment.ARROW_DAMAGE)))
        }

        return calcDamage
    }

    private fun getItemAddedDamage(item: ItemStack): Double {
        var damage = 0.0

        val meta = item.itemMeta ?: return damage
        if (!meta.hasAttributeModifiers()) return damage
        val attributeModifiers = meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE) ?: return damage

        for (attributeModifier in attributeModifiers)
            damage += attributeModifier.amount

        return damage
    }
}