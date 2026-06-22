package de.fuballer.mcendgame.main.component.item.equipment.tool

import de.fuballer.mcendgame.main.component.item.custom.misc.CustomMiscItems
import de.fuballer.mcendgame.main.component.item.equipment.Equipment
import de.fuballer.mcendgame.main.component.item.equipment.data.TieredRollableCustomAttribute
import de.fuballer.mcendgame.main.component.item.equipment.enchantment.EquipmentEnchantment
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item

enum class Horn(
    override val item: Item,
) : Equipment {
    VERDANT_ECHO(
        CustomMiscItems.VERDANT_ECHO,
    ),
    MOLTEN_ROAR(
        CustomMiscItems.MOLTEN_ROAR,
    ),
    FRIGID_CRY(
        CustomMiscItems.FRIGID_CRY,
    );

    override val slot = EquipmentSlotGroup.HAND

    override val rollableCustomAttributes: List<RandomOption<TieredRollableCustomAttribute>> = listOf()

    override val rollableEnchants: List<RandomOption<EquipmentEnchantment>> = listOf()
}