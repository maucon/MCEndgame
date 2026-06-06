package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect.PotionEffect
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack

data class TrainingDummyLoadout(
    val items: Map<EquipmentSlot, ItemStack> = mapOf(),
    val effects: List<PotionEffect> = listOf(),
) {
    fun apply(dummy: TrainingDummyEntity) {
        items.forEach {
            dummy.setItemSlot(it.key, it.value)
        }
        effects.forEach {
            dummy.addEffect(it.getEffectInstance())
        }
    }
}