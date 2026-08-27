package de.fuballer.mcendgame.main.component.entity.custom.entities.bandit

import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.PlayerModelType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

private const val TRANSLATABLE_BASE_KEY = "entity.mcendgame.bandit."

enum class BanditType(
    val customName: Component,
    val modelType: PlayerModelType,
    val texture: Identifier,
    val equipment: Map<EquipmentSlot, ItemStack>,
) {
    RUSK(
        Component.translatable(TRANSLATABLE_BASE_KEY + "drenn"),
        PlayerModelType.WIDE,
        IdentifierUtil.default("textures/entity/bandit/gommehd.png"),
        mapOf(
            EquipmentSlot.MAINHAND to ItemStack(Items.NETHERITE_AXE),
            EquipmentSlot.CHEST to CustomArmorItems.WITHER_ROSE_CHESTPLATE.defaultInstance,
        )
    );

    fun equip(bandit: BanditEntity) {
        equipment.forEach { (slot, stack) -> bandit.setItemSlot(slot, stack.copy()) }
    }

    companion object {
        val DEFAULT = RUSK
    }
}