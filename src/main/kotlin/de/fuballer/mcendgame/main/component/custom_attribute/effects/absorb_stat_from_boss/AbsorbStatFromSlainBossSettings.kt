package de.fuballer.mcendgame.main.component.custom_attribute.effects.absorb_stat_from_boss

import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlotGroup

object AbsorbStatFromSlainBossSettings {
    private val BOSS_ATTRIBUTES = mapOf(
        CustomEntities.ELF_DUELIST to listOf(
            RollableCustomAttribute(VanillaAttributeTypes.INCREASED_JUMP_STRENGTH, 0, DoubleBounds(0.15, 0.25)),
            RollableCustomAttribute(CustomAttributeTypes.INCREASED_JUMP_STRENGTH_MODIFIERS_GRANT_CRITICAL_DAMAGE_MULTIPLIER, 0, DoubleBounds(0.8, 1.0)),
        ),
        CustomEntities.BEAKBURN to listOf(
            RollableCustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, 0, DoubleBounds(1.5, 2.0)),
            RollableCustomAttribute(VanillaAttributeTypes.ATTACK_DAMAGE, 0, DoubleBounds(1.5, 2.0)),
        ),
        CustomEntities.BONECRUSHER to listOf(
            RollableCustomAttribute(VanillaAttributeTypes.MORE_ATTACK_SPEED, 0, DoubleBounds(-0.2, -0.15)),
            RollableCustomAttribute(VanillaAttributeTypes.MORE_ATTACK_DAMAGE, 0, DoubleBounds(0.25, 0.35)),
        ),
        CustomEntities.ARACHNE to listOf(
            RollableCustomAttribute(VanillaAttributeTypes.INCREASED_MOVEMENT_SPEED, 0, DoubleBounds(0.14, 0.18)),
        ),
        CustomEntities.BEASTWEAVER to listOf(
            RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_PER_MISSING_HEART, 0, DoubleBounds(0.01, 0.015)),
            RollableCustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN_PER_MISSING_HEART, 0, DoubleBounds(-0.012, -0.009)),
        ),
        CustomEntities.THAR_ZUL to listOf(
            // TODO inc/more dmg with sweep attacks (also to main target)
        ),
        CustomEntities.THAL_VEYRA to listOf(
            RollableCustomAttribute(CustomAttributeTypes.SPELL_IGNITE, 0),
            RollableCustomAttribute(CustomAttributeTypes.INCREASED_SPELL_DAMAGE, 0, DoubleBounds(0.10, 0.15)),
        ),
    )

    fun getAttributeForBoss(type: EntityType<*>, slot: EquipmentSlotGroup) = BOSS_ATTRIBUTES[type]?.map { it.roll(slot) }

    val ATTRIBUTE_ABSORBED_MESSAGE: Component =
        Component.translatable("text.mcendgame.attribute_absorbed").withStyle { style -> style.withBold(false).withColor(ChatFormatting.LIGHT_PURPLE) }
    val ATTRIBUTE_ALREADY_ABSORBED_MESSAGE: Component =
        Component.translatable("text.mcendgame.cannot_absorb_attribute").withStyle { style -> style.withBold(false).withColor(ChatFormatting.GRAY) }
}