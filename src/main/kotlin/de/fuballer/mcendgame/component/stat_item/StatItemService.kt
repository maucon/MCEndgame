package de.fuballer.mcendgame.component.stat_item

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.equipment.ItemEnchantment
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

@Component
class StatItemService : Listener {
    private val random = Random()

    @EventHandler
    fun onEnchant(event: EnchantItemEvent) {
        val item = event.item
        val enchants = event.enchantsToAdd
        val damageAllTier = enchants[Enchantment.DAMAGE_ALL] ?: return

        item.addEnchantment(Enchantment.DAMAGE_ALL, damageAllTier)
        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        val item = event.result ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun onSmithingPrepare(event: PrepareSmithingEvent) {
        val item = event.result ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun onGrindstoneUse(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.GRINDSTONE || event.rawSlot != 2) return

        val item = inventory.getItem(2) ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }

    fun setCreatureEquipment(
        livingEntity: LivingEntity,
        mapTier: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
    ) {
        val equipment = livingEntity.equipment!!

        if (weapons) {
            createMainHandItem(mapTier, ranged)?.also {
                equipment.setItemInMainHand(it)
                equipment.itemInMainHandDropChance = 0f
            }
            createOffHandItem(mapTier)?.also {
                equipment.setItemInOffHand(it)
                equipment.itemInOffHandDropChance = 0f
            }
        }

        if (armor) {
            getSortableEquipment(mapTier, StatItemSettings.HELMETS)?.also {
                equipment.helmet = it
                equipment.helmetDropChance = 0f
            }
            getSortableEquipment(mapTier, StatItemSettings.CHESTPLATES)?.also {
                equipment.chestplate = it
                equipment.chestplateDropChance = 0f
            }
            getSortableEquipment(mapTier, StatItemSettings.LEGGINGS)?.also {
                equipment.leggings = it
                equipment.leggingsDropChance = 0f
            }
            getSortableEquipment(mapTier, StatItemSettings.BOOTS)?.also {
                equipment.boots = it
                equipment.bootsDropChance = 0f
            }
        }
    }

    private fun createMainHandItem(mapTier: Int, ranged: Boolean): ItemStack? {
        if (ranged) return createRangedMainHandItem(mapTier)

        val itemProbability = RandomUtil.pick(StatItemSettings.MAINHAND_PROBABILITIES).option ?: return null
        return getSortableEquipment(mapTier, itemProbability)
    }

    private fun createRangedMainHandItem(mapTier: Int): ItemStack? {
        val itemProbability = RandomUtil.pick(StatItemSettings.RANGED_MAINHAND_PROBABILITIES).option
        return getSortableEquipment(mapTier, itemProbability)
    }

    private fun createOffHandItem(mapTier: Int): ItemStack? {
        if (random.nextDouble() < StatItemSettings.OFFHAND_OTHER_OVER_MAINHAND_PROBABILITY) {
            return getUnsortableEquipment(mapTier, StatItemSettings.OTHER_ITEMS)
        }

        val itemProbability = RandomUtil.pick(StatItemSettings.MAINHAND_PROBABILITIES).option ?: return null
        return getSortableEquipment(mapTier, itemProbability)
    }

    private fun getSortableEquipment(
        mapTier: Int,
        equipmentProbabilities: List<SortableRandomOption<out Equipment?>>
    ): ItemStack? {
        val rolls = StatItemSettings.calculateEquipmentRollTries(mapTier)
        val equipment = RandomUtil.pick(equipmentProbabilities, rolls).option ?: return null
        return getEquipment(mapTier, equipment)
    }

    private fun getUnsortableEquipment(
        mapTier: Int,
        equipmentProbabilities: List<RandomOption<out Equipment>>
    ): ItemStack {
        val equipment = RandomUtil.pick(equipmentProbabilities).option
        return getEquipment(mapTier, equipment)
    }

    private fun getEquipment(
        mapTier: Int,
        equipment: Equipment
    ): ItemStack {
        val item = ItemStack(equipment.material)
        val itemMeta = item.itemMeta ?: return item

        enchantItem(mapTier, itemMeta, equipment.rollableEnchants)
        addCustomAttributes(mapTier, equipment, itemMeta)
        item.itemMeta = itemMeta

        ItemUtil.updateAttributesAndLore(item)

        return item
    }

    private fun enchantItem(
        mapTier: Int,
        itemMeta: ItemMeta,
        enchants: List<RandomOption<ItemEnchantment>>
    ) {
        repeat(StatItemSettings.calculateEnchantTries(mapTier)) {
            val itemEnchantment = RandomUtil.pick(enchants).option

            if (itemMeta.getEnchantLevel(itemEnchantment.enchantment) < itemEnchantment.level) {
                itemMeta.addEnchant(itemEnchantment.enchantment, itemEnchantment.level, true)
            }
        }
    }

    private fun addCustomAttributes(
        mapTier: Int,
        equipment: Equipment,
        itemMeta: ItemMeta
    ) {
        val statAmount = RandomUtil.pick(StatItemSettings.STAT_AMOUNTS, mapTier).option
        val rollableAttributesCopy = equipment.rollableAttributes.toMutableList()

        val pickedAttributes = mutableListOf<RollableAttribute>()
        repeat(statAmount) {
            if (rollableAttributesCopy.isEmpty()) return@repeat
            val pickedAttribute = RandomUtil.pick(rollableAttributesCopy)

            pickedAttributes.add(pickedAttribute.option)
            rollableAttributesCopy.remove(pickedAttribute)
        }

        val rolledAttributes = pickedAttributes.sortedBy { it.type.ordinal }
            .map { it.roll(mapTier) }

        PersistentDataUtil.setValue(itemMeta, DataTypeKeys.ATTRIBUTES, rolledAttributes)
    }
}