package de.fuballer.mcendgame.main.component.item.custom.tool.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesSwordItem
import de.fuballer.mcendgame.main.component.item.custom.tool.BloodHarvestToolMaterial
import net.minecraft.block.Block
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey

class Bloodharvest(
    settings: Settings,
) : UniqueAttributesSwordItem(BloodHarvestToolMaterial, settings) {
    override fun getCustomAttributes() = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.0, 3.0)),
        RollableCustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleBounds(0.2, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.MORE_ATTACK_KNOCKBACK, 0, DoubleBounds(-0.5, -0.5)),
    )

    override fun getAttributeModifierSlot() = AttributeModifierSlot.MAINHAND
}