package de.fuballer.mcendgame.main.component.item.custom.tool

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ToolMaterial
import net.minecraft.world.level.block.Block

object ToolMaterialUtil {
    fun of(
        incorrectBlocksForDrops: TagKey<Block>,
        durability: Int,
        speed: Float,
        attackDamageBonus: Float,
        enchantmentValue: Int,
        repairItems: TagKey<Item>
    ) = ToolMaterial(incorrectBlocksForDrops, durability, speed, attackDamageBonus, enchantmentValue, repairItems)
}