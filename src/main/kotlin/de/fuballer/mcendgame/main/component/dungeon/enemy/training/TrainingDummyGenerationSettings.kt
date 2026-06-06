package de.fuballer.mcendgame.main.component.dungeon.enemy.training

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.setCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect.PotionEffect
import net.minecraft.core.registries.Registries
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments

object TrainingDummyGenerationSettings {
    val LOADOUT_ORDER = listOf(
        0,
        0,
        0,
        2,
        3,
        4,
        0,
        1,
    )

    private var LOADOUTS: List<TrainingDummyLoadout>? = null

    private fun populateLoadouts(server: MinecraftServer) {
        val enchantmentRegistry = server.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
        val protectionEntry = enchantmentRegistry.getOrThrow(Enchantments.PROTECTION)

        LOADOUTS = listOf(
            // Default
            TrainingDummyLoadout(),

            // Iron + Protection II
            TrainingDummyLoadout(
                mapOf(
                    EquipmentSlot.HEAD to ItemStack(Items.IRON_HELMET).apply {
                        enchant(protectionEntry, 2)
                    },
                    EquipmentSlot.CHEST to ItemStack(Items.IRON_CHESTPLATE).apply {
                        enchant(protectionEntry, 2)
                    },
                    EquipmentSlot.LEGS to ItemStack(Items.IRON_LEGGINGS).apply {
                        enchant(protectionEntry, 2)
                    },
                    EquipmentSlot.FEET to ItemStack(Items.IRON_BOOTS).apply {
                        enchant(protectionEntry, 2)
                    },
                )
            ),

            // Diamond + Protection III
            TrainingDummyLoadout(
                mapOf(
                    EquipmentSlot.HEAD to ItemStack(Items.DIAMOND_HELMET).apply {
                        enchant(protectionEntry, 3)
                    },
                    EquipmentSlot.CHEST to ItemStack(Items.DIAMOND_CHESTPLATE).apply {
                        enchant(protectionEntry, 3)
                    },
                    EquipmentSlot.LEGS to ItemStack(Items.DIAMOND_LEGGINGS).apply {
                        enchant(protectionEntry, 3)
                    },
                    EquipmentSlot.FEET to ItemStack(Items.DIAMOND_BOOTS).apply {
                        enchant(protectionEntry, 3)
                    },
                )
            ),

            // Netherite + Protection IV + Resistance 1
            TrainingDummyLoadout(
                mapOf(
                    EquipmentSlot.HEAD to ItemStack(Items.NETHERITE_HELMET).apply {
                        enchant(protectionEntry, 4)
                    },
                    EquipmentSlot.CHEST to ItemStack(Items.NETHERITE_CHESTPLATE).apply {
                        enchant(protectionEntry, 4)
                    },
                    EquipmentSlot.LEGS to ItemStack(Items.NETHERITE_LEGGINGS).apply {
                        enchant(protectionEntry, 4)
                    },
                    EquipmentSlot.FEET to ItemStack(Items.NETHERITE_BOOTS).apply {
                        enchant(protectionEntry, 4)
                    },
                ),
                listOf(
                    PotionEffect.RESISTANCE_1,
                )
            ),

            // Netherite + Protection IV + Max Attributes + Resistance 2
            TrainingDummyLoadout(
                mapOf(
                    EquipmentSlot.HEAD to ItemStack(Items.NETHERITE_HELMET).apply {
                        enchant(protectionEntry, 4)
                        setCustomAttributes(
                            listOf(
                                CustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleRoll(DoubleBounds(1.5))),
                                CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(2.0))),
                                CustomAttribute(CustomAttributeTypes.WARD, 0, DoubleRoll(DoubleBounds(2.0))),
                            ),
                            EquipmentSlotGroup.ANY,
                        )
                    },
                    EquipmentSlot.CHEST to ItemStack(Items.NETHERITE_CHESTPLATE).apply {
                        enchant(protectionEntry, 4)
                        setCustomAttributes(
                            listOf(
                                CustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleRoll(DoubleBounds(2.0))),
                                CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(3.0))),
                                CustomAttribute(CustomAttributeTypes.WARD, 0, DoubleRoll(DoubleBounds(3.0))),
                            ),
                            EquipmentSlotGroup.ANY,
                        )
                    },
                    EquipmentSlot.LEGS to ItemStack(Items.NETHERITE_LEGGINGS).apply {
                        enchant(protectionEntry, 4)
                        setCustomAttributes(
                            listOf(
                                CustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleRoll(DoubleBounds(1.75))),
                                CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(2.5))),
                                CustomAttribute(CustomAttributeTypes.WARD, 0, DoubleRoll(DoubleBounds(2.5))),
                            ),
                            EquipmentSlotGroup.ANY,
                        )
                    },
                    EquipmentSlot.FEET to ItemStack(Items.NETHERITE_BOOTS).apply {
                        enchant(protectionEntry, 4)
                        setCustomAttributes(
                            listOf(
                                CustomAttribute(VanillaAttributeTypes.ARMOR, 0, DoubleRoll(DoubleBounds(1.5))),
                                CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(2.0))),
                                CustomAttribute(CustomAttributeTypes.WARD, 0, DoubleRoll(DoubleBounds(2.0))),
                            ),
                            EquipmentSlotGroup.ANY,
                        )
                    },
                ),
                listOf(
                    PotionEffect.RESISTANCE_2,
                )
            ),
        )
    }

    fun getLoadout(index: Int, server: MinecraftServer): TrainingDummyLoadout {
        if (LOADOUTS == null) populateLoadouts(server)
        return LOADOUTS!!.getOrElse(LOADOUT_ORDER[index]) { LOADOUTS!![0] }
    }
}