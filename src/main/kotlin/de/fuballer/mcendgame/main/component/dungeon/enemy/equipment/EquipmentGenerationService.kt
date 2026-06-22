package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment

import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.attributes.AttributeService
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.data.EquipmentTag
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.enchantment.EnchantmentService
import de.fuballer.mcendgame.main.component.entity.EnemyEquipmentClass
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonGenerateEnemiesCommand
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.fuballer.mcendgame.main.util.random.SortableRandomOption
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.equipment.trim.ArmorTrim
import kotlin.random.Random

@Injectable
class EquipmentGenerationService(
    private val enchantmentService: EnchantmentService,
    private val attributeService: AttributeService,
) {
    fun generate(
        entity: LivingEntity,
        entityEquipmentClass: EnemyEquipmentClass,
        level: Int,
        server: MinecraftServer,
        isLootGoblin: Boolean,
        random: Random,
        generateEnemiesCommand: DungeonGenerateEnemiesCommand,
    ) {
        val equipmentData = EquipmentGenerationData(
            generateEnemiesCommand.uniqueEquipmentProbability,
            isLootGoblin && generateEnemiesCommand.lootGoblinLuckyAttributes,
            generateEnemiesCommand.additionalAttributeProbabilities,
            if (isLootGoblin) getArmorTrim(server, random) else null
        )

        if (entityEquipmentClass.isNot(EnemyEquipmentClass.NO_WEAPONS)) {
            createEquipment(level, EquipmentSlot.MAINHAND, entityEquipmentClass, server, random, equipmentData, entityEquipmentClass.isOrIncludes(EnemyEquipmentClass.RANGED))?.also {
                entity.setItemSlot(EquipmentSlot.MAINHAND, it)
            }
            createEquipment(level, EquipmentSlot.OFFHAND, entityEquipmentClass, server, random, equipmentData)?.also {
                entity.setItemSlot(EquipmentSlot.OFFHAND, it)
            }
        }

        if (entityEquipmentClass.isOrIncludes(EnemyEquipmentClass.NO_ARMOR)) return

        createEquipment(level, EquipmentSlot.HEAD, entityEquipmentClass, server, random, equipmentData)?.also {
            entity.setItemSlot(EquipmentSlot.HEAD, it)
        }
        createEquipment(level, EquipmentSlot.CHEST, entityEquipmentClass, server, random, equipmentData)?.also {
            entity.setItemSlot(EquipmentSlot.CHEST, it)
        }
        createEquipment(level, EquipmentSlot.LEGS, entityEquipmentClass, server, random, equipmentData)?.also {
            entity.setItemSlot(EquipmentSlot.LEGS, it)
        }
        createEquipment(level, EquipmentSlot.FEET, entityEquipmentClass, server, random, equipmentData)?.also {
            entity.setItemSlot(EquipmentSlot.FEET, it)
        }
    }

    private fun createEquipment(
        level: Int,
        slot: EquipmentSlot,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
        isRanged: Boolean = false,
    ): ItemStack? {
        if (random.nextDouble() <= data.uniqueProbability) {
            return createUniqueEquipment(level, slot, server, random, isRanged)
        }

        return when (slot) {
            EquipmentSlot.MAINHAND -> createMainHandItem(level, entityEquipmentClass, server, random, data)
            EquipmentSlot.OFFHAND -> createOffHandItem(level, entityEquipmentClass, server, random, data)
            else -> createArmorEquipment(level, slot, entityEquipmentClass, server, random, data)
        }
    }

    private fun createUniqueEquipment(
        level: Int,
        slot: EquipmentSlot,
        server: MinecraftServer,
        random: Random,
        isRanged: Boolean,
    ): ItemStack? {
        val equipment = (if (isRanged && slot == EquipmentSlot.MAINHAND) EquipmentGenerationSettings.getRandomUniqueEquipment(slot, EquipmentTag.RANGED, true, random)
        else EquipmentGenerationSettings.getRandomUniqueEquipment(slot, random = random)) ?: return null

        val item = equipment.item
        val itemStack = if (item is UniqueAttributesItemInterface) item.getRolledStack(item) else ItemStack(item)

        enchantmentService.enchantItem(itemStack, equipment.rollableEnchants, level, server, random)

        return itemStack
    }

    private fun createMainHandItem(
        level: Int,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack? {
        val options = if (entityEquipmentClass.isOrIncludes(EnemyEquipmentClass.RANGED)) {
            RandomUtil.pickOne(EquipmentGenerationSettings.RANGED_MAINHAND_PROBABILITIES, random).option
        } else {
            RandomUtil.pickOne(EquipmentGenerationSettings.MAINHAND_WEAPON_PROBABILITIES, random).option
        } ?: return null

        return createEquipmentSortable(level, options, entityEquipmentClass, server, random, data)
    }

    private fun createOffHandItem(
        level: Int,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack? {
        if (random.nextDouble() < EquipmentGenerationSettings.OFFHAND_ITEM_PROBABILITY) {
            return createEquipment(level, EquipmentGenerationSettings.OFFHAND_ITEMS, entityEquipmentClass, server, random, data)
        }

        val options = RandomUtil.pickOne(EquipmentGenerationSettings.OFFHAND_WEAPON_PROBABILITIES, random).option ?: return null
        return createEquipmentSortable(level, options, entityEquipmentClass, server, random, data)
    }

    private fun createArmorEquipment(
        level: Int,
        slot: EquipmentSlot,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack? {
        val equipmentOptions = EquipmentGenerationSettings.ARMORSLOT_EQUIPMENT_MAP[slot] ?: return null
        val stack = createEquipmentSortable(level, equipmentOptions, entityEquipmentClass, server, random, data) ?: return null

        stack.set(DataComponents.TRIM, data.armorTrim)
        return stack
    }

    private fun createEquipmentSortable(
        level: Int,
        equipmentOptions: List<SortableRandomOption<out Equipment?>>,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack? {
        val rolls = EquipmentGenerationSettings.calculateEquipmentRollTries(level)
        val equipment = RandomUtil.pickBestOf(equipmentOptions, rolls, random).option ?: return null

        return createEquipment(level, equipment, entityEquipmentClass, server, random, data)
    }

    private fun createEquipment(
        level: Int,
        equipmentOptions: List<RandomOption<out Equipment?>>,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack? {
        val equipment = RandomUtil.pickOne(equipmentOptions, random).option ?: return null

        return createEquipment(level, equipment, entityEquipmentClass, server, random, data)
    }

    private fun createEquipment(
        level: Int,
        equipment: Equipment,
        entityEquipmentClass: EnemyEquipmentClass,
        server: MinecraftServer,
        random: Random,
        data: EquipmentGenerationData,
    ): ItemStack {
        val item = equipment.item
        val itemStack = ItemStack(item)

        enchantmentService.enchantItem(itemStack, equipment.rollableEnchants, level, server, random)
        attributeService.applyAttributes(itemStack, equipment.rollableCustomAttributes, entityEquipmentClass, level, random, equipment.slot, data)

        return itemStack
    }

    private fun getArmorTrim(
        server: MinecraftServer,
        random: Random
    ): ArmorTrim {
        val materialRegistry = server.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL)
        val patternRegistry = server.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN)

        val materialKey = RandomUtil.pickOne(EquipmentGenerationSettings.LOOT_GOBLIN_ARMOR_TRIM_MATERIALS, random).option
        val material = materialRegistry.getOrThrow(materialKey)
        val patternKey = RandomUtil.pickOne(EquipmentGenerationSettings.LOOT_GOBLIN_ARMOR_TRIM_PATTERNS, random).option
        val pattern = patternRegistry.getOrThrow(patternKey)

        return ArmorTrim(material, pattern)
    }
}