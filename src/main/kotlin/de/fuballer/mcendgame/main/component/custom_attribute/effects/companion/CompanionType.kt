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
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.animal.wolf.Wolf

enum class CompanionType(
    val entityType: EntityType<out TamableAnimal>,
    val entityClass: Class<out TamableAnimal>,
    val attribute: CustomAttributeType,
    val getCount: (CustomAttribute) -> Int = { 1 },
    val applyOther: (TamableAnimal, CustomAttribute) -> Unit = { _, _ -> },
) {
    WOLF(
        EntityType.WOLF,
        Wolf::class.java,
        CustomAttributeTypes.WOLF_COMPANION,
        applyOther = applyOther@{ wolf, attribute ->
            if (wolf !is Wolf) return@applyOther
            val type = WolfCompanionType.getByName(attribute.rolls[0].asStringRoll().getValue()) ?: return@applyOther

            for (effect in type.allyAuraStatusEffects) {
                wolf.addAllyAuraStatusEffect(effect)
            }
            for (effect in type.enemyAuraStatusEffects) {
                wolf.addEnemyAuraStatusEffect(effect)
            }
            for (effectType in type.selfEffects.keys) {
                wolf.addEffect(
                    MobEffectInstance(
                        effectType,
                        MobEffectInstance.INFINITE_DURATION,
                        type.selfEffects[effectType] ?: 0,
                        true,
                        true
                    )
                )
            }
            type.applyExtras(wolf)

            val registry = wolf.level().registryAccess().lookupOrThrow(Registries.WOLF_VARIANT)
            val variantEntry = registry.getOrThrow(type.variant)
            wolf.setVariant(variantEntry)
            wolf.setCollarColor(type.color)

            wolf.getAttribute(Attributes.SCALE)?.baseValue = type.scale
        },
    ),
    SPIDERLING(
        CustomEntities.SPIDERLING,
        SpiderlingEntity::class.java,
        CustomAttributeTypes.SPIDERLING_COMPANIONS,
        getCount = { attribute -> attribute.rolls[0].asIntRoll().getValue() },
    ),
}