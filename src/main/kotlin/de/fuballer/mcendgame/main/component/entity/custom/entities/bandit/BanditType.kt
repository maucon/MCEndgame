package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.equipment.trim.TrimMaterials
import net.minecraft.world.item.equipment.trim.TrimPatterns
import kotlin.random.Random

private const val TRANSLATABLE_BASE_KEY = "entity.mcendgame.bandit."

enum class BanditType(
    val customName: Component,
    val modelType: PlayerModelType,
    val texture: Identifier,
    val equipment: List<BanditItemStack>,
    val jumpWhileTravel: Boolean = true,
    val jumpCritAttack: Boolean = false,
    val strafeBackAfterTargetHit: Boolean = true,
    val sideStrafeUpdateTime: Int = 10,
    val blockOnEnterDuel: Boolean = true,
    val blockAfterTargetHitProbability: Double = 0.5,
    val blockDuration: () -> Int = { Random.nextInt(20, 40) }
) {
    // bruiser
    RUSK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "drenn"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.FATESPLITTER.defaultInstance),
            BanditItemStack(EquipmentSlot.OFFHAND, CustomToolItems.GRUDGEBEARER.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET)),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.WITHER_ROSE_LEGGINGS.defaultInstance),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS)),
        ),
        jumpCritAttack = true,
    ),
    SLOANE(
        Component.translatable(TRANSLATABLE_BASE_KEY + "sloane"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_SWORD)),
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.NETHERITE_SWORD)),
        ),
    ),

    // assassin
    KEIR(
        Component.translatable(TRANSLATABLE_BASE_KEY + "keir"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
        ),
    ),
    NIAMH(
        Component.translatable(TRANSLATABLE_BASE_KEY + "niamh"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
        ),
    ),

    // tank
    HADRIK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "hadrik"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.SHIELD)),
        ),
    ),
    BRINA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "brina"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/brina.png"),
        listOf(
            BanditItemStack(EquipmentSlot.OFFHAND, ItemStack(Items.SHIELD)),
            BanditItemStack(EquipmentSlot.LEGS, CustomArmorItems.STONEWARD.defaultInstance),
        ),
    ),

    // healer
    EMRYS(
        Component.translatable(TRANSLATABLE_BASE_KEY + "emrys"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
        ),
    ),
    NESSA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "nessa"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.RADIANT_DAWN.defaultInstance),
            BanditItemStack(EquipmentSlot.HEAD, ItemStack(Items.NETHERITE_HELMET), TrimMaterials.GOLD, TrimPatterns.WARD),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.BOUND_ABYSS.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, ItemStack(Items.NETHERITE_LEGGINGS)),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS), TrimMaterials.GOLD, TrimPatterns.WARD),
        ),
    ),

    // archer
    LORCAN(
        Component.translatable(TRANSLATABLE_BASE_KEY + "lorcan"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
        ),
    ),
    YVRA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "yvra"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
        ),
    ),

    // mage
    CATHAL(
        Component.translatable(TRANSLATABLE_BASE_KEY + "cathal"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
        ),
    ),
    MAEVE(
        Component.translatable(TRANSLATABLE_BASE_KEY + "maeve"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
        ),
    ),

    // summoner / companion
    BRAKK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "brakk"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/drenn.png"),
        listOf(
            BanditItemStack(EquipmentSlot.FEET, CustomArmorItems.DRUIDS_BOOTS.defaultInstance),
        ),
    ),
    MORRIGAN(
        Component.translatable(TRANSLATABLE_BASE_KEY + "morrigan"),
        PlayerModelType.SLIM,
        IdentifierUtil.default("textures/entity/bandit/nessa.png"),
        listOf(
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.BROODMOTHER.defaultInstance),
        ),
    );

    fun equip(bandit: BanditEntity) {
        equipment.forEach { it.equip(bandit) }
    }

    companion object {
        val DEFAULT = RUSK
    }
}