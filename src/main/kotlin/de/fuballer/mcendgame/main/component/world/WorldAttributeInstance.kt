package de.fuballer.mcendgame.main.component.world

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.world.entity.LivingEntity
import java.util.function.Predicate

data class WorldAttributeInstance(
    val attribute: CustomAttribute,
    val applies: Predicate<LivingEntity>,
)