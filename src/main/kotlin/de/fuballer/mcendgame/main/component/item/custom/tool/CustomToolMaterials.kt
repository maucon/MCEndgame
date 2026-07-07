package de.fuballer.mcendgame.main.component.item.custom.tool

import net.minecraft.block.Block
import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.TagKey

object BloodHarvestToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 7F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}

object TwinfireToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 7.0F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}

object FatesplitterToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 9F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}

object SerpentsFangToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 5F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}

object NightreaverToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 5F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}

object RadiantDawnToolMaterial : ToolMaterial {
    override fun getDurability() = 2031
    override fun getMiningSpeedMultiplier() = 9F
    override fun getAttackDamage() = 7F
    override fun getInverseTag(): TagKey<Block> = BlockTags.INCORRECT_FOR_NETHERITE_TOOL
    override fun getEnchantability() = 22
    override fun getRepairIngredient(): Ingredient = Ingredient.ofItems(Items.NETHERITE_INGOT)
}