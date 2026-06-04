package de.fuballer.mcendgame.main.component.world

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.world.entity.LivingEntity
import java.util.function.Predicate

data class VanillaTypeWorldAttributeInstance(
    val attribute: CustomAttribute,
    val applies: Predicate<LivingEntity>,
    val action: WorldAttributeAction,
    val update: Int,
)