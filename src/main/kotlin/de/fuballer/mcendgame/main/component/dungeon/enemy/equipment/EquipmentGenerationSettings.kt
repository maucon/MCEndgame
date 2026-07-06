package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment

import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.data.EquipmentTag
import de.fuballer.mcendgame.main.component.dungeon.enemy.equipment.data.TaggedEquipment
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.armor.Boots
import de.fuballer.mcendgame.main.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.main.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.main.component.item.equipment.armor.Leggings
import de.fuballer.mcendgame.main.component.item.equipment.tool.*
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import de.fuballer.mcendgame.main.util.random.SortableRandomOption
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.trim.ArmorTrimMaterial
import net.minecraft.item.trim.ArmorTrimMaterials
import net.minecraft.item.trim.ArmorTrimPattern
import net.minecraft.item.trim.ArmorTrimPatterns
import net.minecraft.registry.RegistryKey
import kotlin.random.Random

object EquipmentGenerationSettings {
    private const val EQUIPMENT_ROLL_TRIES_PER_TIER = 0.5
    fun calculateEquipmentRollTries(mapTier: Int) = 1 + (mapTier * EQUIPMENT_ROLL_TRIES_PER_TIER).toInt()

    fun getUniqueEquipmentBaseProbability(level: Int) = 0.0005 + (level * 0.0000125)

    val UNIQUE_EQUIPMENT = listOf(
        RandomOption(100, TaggedEquipment.forBothHands(Sword.TWINFIRE)),
        RandomOption(100, TaggedEquipment.forBothHands(Sword.BLOODHARVEST)),
        RandomOption(100, TaggedEquipment.forBothHands(Sword.SERPENTS_FANG)),
        RandomOption(100, TaggedEquipment.forBothHands(Sword.NIGHTREAVER)),
        RandomOption(100, TaggedEquipment.forBothHands(Sword.RADIANT_DAWN)),
        RandomOption(100, TaggedEquipment.forBothHands(Axe.FATESPLITTER)),
        //RandomOption(100, TaggedEquipment.forBothHands(Mace.GRAVEBREAKER)), drops from bonecrusher boss

        RandomOption(100, TaggedEquipment(Helmet.ICEBORNE, EquipmentSlot.HEAD)),
        RandomOption(100, TaggedEquipment(Helmet.EMBERCHANT, EquipmentSlot.HEAD)),
        RandomOption(100, TaggedEquipment(Helmet.DRUIDS_HELMET, EquipmentSlot.HEAD)),
        RandomOption(100, TaggedEquipment(Helmet.WITHER_ROSE_HELMET, EquipmentSlot.HEAD)),
        RandomOption(100, TaggedEquipment(Helmet.SUEDE_HELMET, EquipmentSlot.HEAD)),
        RandomOption(100, TaggedEquipment(Helmet.ABYSSAL_MASK, EquipmentSlot.HEAD)),

        RandomOption(100, TaggedEquipment(Chestplate.BOUND_ABYSS, EquipmentSlot.CHEST)),
        RandomOption(100, TaggedEquipment(Chestplate.DRUIDS_CHESTPLATE, EquipmentSlot.CHEST)),
        RandomOption(100, TaggedEquipment(Chestplate.WITHER_ROSE_CHESTPLATE, EquipmentSlot.CHEST)),
        RandomOption(100, TaggedEquipment(Chestplate.SUEDE_CHESTPLATE, EquipmentSlot.CHEST)),
        RandomOption(100, TaggedEquipment(Chestplate.VOIDWEAVER, EquipmentSlot.CHEST)),
        //RandomOption(100, TaggedEquipment(Chestplate.BROODMOTHER, EquipmentSlot.CHEST)), drops from arachne boss

        RandomOption(100, TaggedEquipment(Leggings.LAMIAS_GIFT, EquipmentSlot.LEGS)),
        RandomOption(100, TaggedEquipment(Leggings.DRUIDS_LEGGINGS, EquipmentSlot.LEGS)),
        RandomOption(100, TaggedEquipment(Leggings.WITHER_ROSE_LEGGINGS, EquipmentSlot.LEGS)),
        RandomOption(100, TaggedEquipment(Leggings.SUEDE_LEGGINGS, EquipmentSlot.LEGS)),
        RandomOption(100, TaggedEquipment(Leggings.STONEWARD, EquipmentSlot.LEGS)),
        RandomOption(100, TaggedEquipment(Leggings.GILDED_TEMPEST, EquipmentSlot.LEGS)),
        //RandomOption(100, TaggedEquipment(Leggings.WINDSTRIDER, EquipmentSlot.LEGS)), drops from elf duelist boss

        RandomOption(100, TaggedEquipment(Boots.DRUIDS_BOOTS, EquipmentSlot.FEET)),
        RandomOption(100, TaggedEquipment(Boots.WITHER_ROSE_BOOTS, EquipmentSlot.FEET)),
        RandomOption(100, TaggedEquipment(Boots.SUEDE_BOOTS, EquipmentSlot.FEET)),
        RandomOption(100, TaggedEquipment(Boots.MOONSHADOW, EquipmentSlot.FEET)),
        //RandomOption(100, TaggedEquipment(Boots.EMBERREIGN, EquipmentSlot.FEET)), drops from beakburn boss
        //RandomOption(100, TaggedEquipment(Boots.GEISTERGALOSCHEN, EquipmentSlot.FEET)), drops from final boss with aspect of ghosts

        RandomOption(100, TaggedEquipment.forRangedWeapon(Bow.WINDSTRING)),
        RandomOption(100, TaggedEquipment.forRangedWeapon(Bow.HAILSTORM)),
        RandomOption(100, TaggedEquipment.forRangedWeapon(Bow.DUSK_PIERCER)),

        RandomOption(100, TaggedEquipment(Horn.VERDANT_ECHO, EquipmentSlot.OFFHAND)),
        RandomOption(100, TaggedEquipment(Horn.MOLTEN_ROAR, EquipmentSlot.OFFHAND)),
        RandomOption(100, TaggedEquipment(Horn.FRIGID_CRY, EquipmentSlot.OFFHAND)),

        RandomOption(100, TaggedEquipment(Shield.GRUDGEBEARER, EquipmentSlot.OFFHAND)),
    )

    fun getRandomUniqueEquipment(
        slot: EquipmentSlot? = null,
        tag: EquipmentTag,
        tagExactMatch: Boolean = true,
        random: Random = Random,
    ) = getRandomUniqueEquipment(slot, setOf(tag), tagExactMatch, random)

    fun getRandomUniqueEquipment(
        slot: EquipmentSlot? = null,
        tags: Set<EquipmentTag> = setOf(),
        tagsExactMatch: Boolean = true,
        random: Random = Random,
    ): Equipment? {
        val options = UNIQUE_EQUIPMENT.toMutableList()
        if (slot != null) options.removeAll { !it.option.slots.contains(slot) }

        if (tagsExactMatch) options.removeAll { it.option.tags != tags }
        else options.removeAll { !it.option.tags.containsAll(tags) }

        if (options.isEmpty()) return null
        return RandomUtil.pickOne(options, random).option.equipment
    }

    val HELMETS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Helmet.LEATHER),
        SortableRandomOption(500, 3, Helmet.GOLDEN),
        SortableRandomOption(500, 4, Helmet.CHAINMAIL),
        SortableRandomOption(500, 5, Helmet.IRON),
        SortableRandomOption(100, 6, Helmet.TURTLE),
        SortableRandomOption(800, 7, Helmet.DIAMOND),
        SortableRandomOption(500, 8, Helmet.NETHERITE),
    )
    val CHESTPLATES = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Chestplate.LEATHER),
        SortableRandomOption(500, 3, Chestplate.GOLDEN),
        SortableRandomOption(500, 4, Chestplate.CHAINMAIL),
        SortableRandomOption(500, 5, Chestplate.IRON),
        SortableRandomOption(800, 7, Chestplate.DIAMOND),
        SortableRandomOption(500, 8, Chestplate.NETHERITE)
    )
    val LEGGINGS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Leggings.LEATHER),
        SortableRandomOption(500, 3, Leggings.GOLDEN),
        SortableRandomOption(500, 4, Leggings.CHAINMAIL),
        SortableRandomOption(500, 5, Leggings.IRON),
        SortableRandomOption(800, 7, Leggings.DIAMOND),
        SortableRandomOption(500, 8, Leggings.NETHERITE)
    )
    val BOOTS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(500, 1, Boots.LEATHER),
        SortableRandomOption(500, 3, Boots.GOLDEN),
        SortableRandomOption(500, 4, Boots.CHAINMAIL),
        SortableRandomOption(500, 5, Boots.IRON),
        SortableRandomOption(800, 7, Boots.DIAMOND),
        SortableRandomOption(500, 8, Boots.NETHERITE)
    )

    val ARMORSLOT_EQUIPMENT_MAP = mapOf<EquipmentSlot, List<SortableRandomOption<out Equipment?>>>(
        EquipmentSlot.HEAD to HELMETS,
        EquipmentSlot.CHEST to CHESTPLATES,
        EquipmentSlot.LEGS to LEGGINGS,
        EquipmentSlot.FEET to BOOTS,
    )

    private val SWORDS = listOf(
        SortableRandomOption(500, 1, Sword.WOODEN),
        SortableRandomOption(500, 2, Sword.GOLDEN),
        SortableRandomOption(500, 3, Sword.STONE),
        SortableRandomOption(500, 4, Sword.COPPER),
        SortableRandomOption(500, 5, Sword.IRON),
        SortableRandomOption(800, 6, Sword.DIAMOND),
        SortableRandomOption(500, 7, Sword.NETHERITE),
    )
    private val AXES = listOf(
        SortableRandomOption(500, 1, Axe.WOODEN),
        SortableRandomOption(500, 2, Axe.GOLDEN),
        SortableRandomOption(500, 3, Axe.STONE),
        SortableRandomOption(500, 5, Axe.IRON),
        SortableRandomOption(800, 6, Axe.DIAMOND),
        SortableRandomOption(500, 7, Axe.NETHERITE)
    )
    private val PICKAXES = listOf(
        SortableRandomOption(500, 1, Pickaxe.WOODEN),
        SortableRandomOption(500, 2, Pickaxe.GOLDEN),
        SortableRandomOption(500, 3, Pickaxe.STONE),
        SortableRandomOption(500, 5, Pickaxe.IRON),
        SortableRandomOption(800, 6, Pickaxe.DIAMOND),
        SortableRandomOption(500, 7, Pickaxe.NETHERITE)
    )
    private val SHOVELS = listOf(
        SortableRandomOption(500, 1, Shovel.WOODEN),
        SortableRandomOption(500, 2, Shovel.GOLDEN),
        SortableRandomOption(500, 3, Shovel.STONE),
        SortableRandomOption(500, 5, Shovel.IRON),
        SortableRandomOption(800, 6, Shovel.DIAMOND),
        SortableRandomOption(500, 7, Shovel.NETHERITE),
    )
    private val HOES = listOf(
        SortableRandomOption(500, 1, Hoe.WOODEN),
        SortableRandomOption(500, 2, Hoe.GOLDEN),
        SortableRandomOption(500, 3, Hoe.STONE),
        SortableRandomOption(500, 5, Hoe.IRON),
        SortableRandomOption(800, 6, Hoe.DIAMOND),
        SortableRandomOption(500, 7, Hoe.NETHERITE),
    )
    private val BOWS = listOf(
        SortableRandomOption(100, 0, Bow.BOW)
    )
    private val SPECIAL_WEAPONS: List<SortableRandomOption<out Equipment>> = listOf(
        SortableRandomOption(100, 0, Miscellaneous.TRIDENT),
        SortableRandomOption(50, 0, Mace.MACE)
    )

    const val OFFHAND_ITEM_PROBABILITY = 0.35
    val MAINHAND_WEAPON_PROBABILITIES = listOf<RandomOption<List<SortableRandomOption<out Equipment>>?>>(
        RandomOption(12, SWORDS),
        RandomOption(10, AXES),
        RandomOption(6, PICKAXES),
        RandomOption(6, SHOVELS),
        RandomOption(6, HOES),
        RandomOption(1, SPECIAL_WEAPONS),
    )
    val OFFHAND_WEAPON_PROBABILITIES = listOf<RandomOption<List<SortableRandomOption<out Equipment>>?>>(
        RandomOption(12, null),
        RandomOption(12, SWORDS),
        RandomOption(10, AXES),
        RandomOption(6, PICKAXES),
        RandomOption(6, SHOVELS),
        RandomOption(6, HOES),
        RandomOption(1, SPECIAL_WEAPONS),
    )
    val RANGED_MAINHAND_PROBABILITIES = listOf<RandomOption<List<SortableRandomOption<out Equipment>>>>(
        RandomOption(100, BOWS),
    )
    val OFFHAND_ITEMS = listOf<RandomOption<out Equipment>>(
        RandomOption(1, Miscellaneous.FISHING_ROD),
        RandomOption(9, Shield.SHIELD),
    )

    val LOOT_GOBLIN_ARMOR_TRIM_MATERIALS = listOf(
        RandomOption(1, ArmorTrimMaterials.NETHERITE),
        RandomOption(3, ArmorTrimMaterials.DIAMOND),
        RandomOption(5, ArmorTrimMaterials.EMERALD),
        RandomOption(5, ArmorTrimMaterials.AMETHYST),
        RandomOption(5, ArmorTrimMaterials.QUARTZ),
        RandomOption(8, ArmorTrimMaterials.GOLD),
        RandomOption(8, ArmorTrimMaterials.IRON),
        RandomOption(8, ArmorTrimMaterials.COPPER),
        RandomOption(8, ArmorTrimMaterials.REDSTONE),
        RandomOption(8, ArmorTrimMaterials.LAPIS),
    )

    val LOOT_GOBLIN_ARMOR_TRIM_PATTERNS = listOf(
        RandomOption(1, ArmorTrimPatterns.SILENCE), //Ancient City chest 1.2%
        RandomOption(3, ArmorTrimPatterns.WARD),  //Ancient City chest 5%
        RandomOption(5, ArmorTrimPatterns.VEX), //Woodland Mansion chest 50%
        RandomOption(5, ArmorTrimPatterns.BOLT), //Vault 6.3%
        RandomOption(7, ArmorTrimPatterns.FLOW), //Ominous Vault 22.5%
        RandomOption(8, ArmorTrimPatterns.SNOUT), //Bastion Remnant chest 8.3%
        RandomOption(8, ArmorTrimPatterns.WILD), //Jungle Temple chest 33.3%
        RandomOption(10, ArmorTrimPatterns.EYE), //Stronghold altar chest 10%, library chest 100%
        RandomOption(15, ArmorTrimPatterns.SPIRE), //End city chest 6.7%
        RandomOption(15, ArmorTrimPatterns.TIDE), //Elder Guardian 20%
        RandomOption(15, ArmorTrimPatterns.RIB), //Nether Fortress chest 6.7%
        RandomOption(15, ArmorTrimPatterns.DUNE), //Desert Temple 14.3%
        RandomOption(15, ArmorTrimPatterns.WAYFINDER), //Suspicious gravel 8.3%
        RandomOption(15, ArmorTrimPatterns.SHAPER), //Suspicious gravel 8.3%
        RandomOption(15, ArmorTrimPatterns.RAISER), //Suspicious gravel 8.3%
        RandomOption(15, ArmorTrimPatterns.HOST), //Suspicious gravel 8.3%
        RandomOption(20, ArmorTrimPatterns.SENTRY), //Outpost chest 25%
        RandomOption(25, ArmorTrimPatterns.COAST), //Shipwreck chest 16.7%
    )
}