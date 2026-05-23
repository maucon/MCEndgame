package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect.PotionEffect
import de.fuballer.mcendgame.main.component.entity.custom.entities.training_dummy.TrainingDummyEntity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ItemStack

data class TrainingDummyLoadout(
    val items: Map<EquipmentSlot, ItemStack> = mapOf(),
    val effects: List<PotionEffect> = listOf(),
) {
    fun apply(dummy: TrainingDummyEntity) {
        items.forEach {
            dummy.equipStack(it.key, it.value.copy())
        }
        effects.forEach {
            dummy.addStatusEffect(it.getEffectInstance())
        }
    }
}