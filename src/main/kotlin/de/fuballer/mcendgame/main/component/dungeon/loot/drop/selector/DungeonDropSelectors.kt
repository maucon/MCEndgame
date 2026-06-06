package de.fuballer.mcendgame.main.component.dungeon.loot.drop.selector

import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object DungeonDropSelectors {
    val PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { _, itemEntity -> itemEntity.owner is Player }

    val UNIQUE: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> itemStack.item is UniqueAttributesItemInterface }
    val UNIQUE_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> UNIQUE(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }

    val ASPECT: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> itemStack.item is AspectItem }
    val ASPECT_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> ASPECT(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }

    val CRYSTAL: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> itemStack.item is CrystalItem }
    val CRYSTAL_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> CRYSTAL(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }

    val TOTEM_BASIC: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> (itemStack.item as? TotemItem)?.type == TotemType.BASIC }
    val TOTEM_BASIC_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> TOTEM_BASIC(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }
    val TOTEM_EFFECT: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> (itemStack.item as? TotemItem)?.type == TotemType.EFFECT }
    val TOTEM_EFFECT_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> TOTEM_EFFECT(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }
    val TOTEM_ULTIMATE: (ItemStack, ItemEntity) -> Boolean = { itemStack, _ -> (itemStack.item as? TotemItem)?.type == TotemType.ULTIMATE }
    val TOTEM_ULTIMATE_PLAYER_DROPPED: (ItemStack, ItemEntity) -> Boolean = { itemStack, itemEntity -> TOTEM_ULTIMATE(itemStack, itemEntity) && PLAYER_DROPPED(itemStack, itemEntity) }
}