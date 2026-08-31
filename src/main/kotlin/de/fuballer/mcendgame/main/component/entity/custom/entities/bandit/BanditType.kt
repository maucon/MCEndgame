package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.equipment.trim.TrimMaterials
import net.minecraft.world.item.equipment.trim.TrimPatterns

private const val TRANSLATABLE_BASE_KEY = "entity.mcendgame.bandit."

enum class BanditType(
    val customName: Component,
    val modelType: PlayerModelType,
    val texture: Identifier,
    val equipment: List<BanditItemStack>,
    val jumpWhileTravel: Boolean = true,
    val jumpAttack: Boolean = false,
) {
    RUSK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "drenn"),
        PlayerModelType.WIDE,
        Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, ItemStack(Items.NETHERITE_AXE)),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance),
        ),
        jumpAttack = true,
    ),
    NESSA(
        Component.translatable(TRANSLATABLE_BASE_KEY + "nessa"),
        PlayerModelType.SLIM,
        Identifier.withDefaultNamespace("textures/entity/player/slim/alex.png"),
        listOf(
            BanditItemStack(EquipmentSlot.MAINHAND, CustomToolItems.RADIANT_DAWN.defaultInstance),
            BanditItemStack(EquipmentSlot.CHEST, CustomArmorItems.BOUND_ABYSS.defaultInstance),
            BanditItemStack(EquipmentSlot.LEGS, ItemStack(Items.NETHERITE_LEGGINGS)),
            BanditItemStack(EquipmentSlot.FEET, ItemStack(Items.NETHERITE_BOOTS), TrimMaterials.GOLD, TrimPatterns.WARD)
        ),
    );

    fun equip(bandit: BanditEntity) {
        equipment.forEach { it.equip(bandit) }
    }

    companion object {
        val DEFAULT = RUSK
    }
}