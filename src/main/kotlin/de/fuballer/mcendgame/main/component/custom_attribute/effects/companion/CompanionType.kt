package de.fuballer.mcendgame.main.component.custom_attribute.effects.companion

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asIntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asStringRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.effects.companion.wolf_companion.WolfCompanionType
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.entity.custom.entities.spiderling.SpiderlingEntity
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addAllyAuraStatusEffect
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.addEnemyAuraStatusEffect
import de.fuballer.mcendgame.main.util.extension.mixin.WolfMixinExtension.setCollarColor
import de.fuballer.mcendgame.main.util.extension.mixin.WolfMixinExtension.setVariant
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.passive.WolfEntity
import net.minecraft.registry.RegistryKeys

enum class CompanionType(
    val entityType: EntityType<out TameableEntity>,
    val entityClass: Class<out TameableEntity>,
    val attribute: CustomAttributeType,
    val getCount: (CustomAttribute) -> Int = { 1 },
    val applyOther: (TameableEntity, CustomAttribute) -> Unit = { _, _ -> },
) {
    WOLF(
        EntityType.WOLF,
        WolfEntity::class.java,
        CustomAttributeTypes.WOLF_COMPANION,
        applyOther = applyOther@{ wolf, attribute ->
            if (wolf !is WolfEntity) return@applyOther
            val type = WolfCompanionType.getByName(attribute.rolls[0].asStringRoll().getValue()) ?: return@applyOther

            for (effect in type.allyAuraStatusEffects) {
                wolf.addAllyAuraStatusEffect(effect)
            }
            for (effect in type.enemyAuraStatusEffects) {
                wolf.addEnemyAuraStatusEffect(effect)
            }
            for (effectType in type.selfEffects.keys) {
                wolf.addStatusEffect(
                    StatusEffectInstance(
                        effectType,
                        StatusEffectInstance.INFINITE,
                        type.selfEffects[effectType] ?: 0,
                        true,
                        true
                    )
                )
            }
            type.applyExtras(wolf)

            val registry = wolf.entityWorld.registryManager.getWrapperOrThrow(RegistryKeys.WOLF_VARIANT)
            val variantEntry = registry.getOrThrow(type.variant)
            wolf.variant = variantEntry
            wolf.setCollarColor(type.color)

            wolf.getAttributeInstance(EntityAttributes.GENERIC_SCALE)?.baseValue = type.scale
        },
    ),
    SPIDERLING(
        CustomEntities.SPIDERLING,
        SpiderlingEntity::class.java,
        CustomAttributeTypes.SPIDERLING_COMPANIONS,
        getCount = { attribute -> attribute.rolls[0].asIntRoll().getValue() },
    ),
}