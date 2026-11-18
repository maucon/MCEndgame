package de.fuballer.mcendgame.client.component.custom_attribute

import de.fuballer.mcendgame.client.messaging.RenderItemTooltipCommand
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.ValueFormats
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeyword
import de.fuballer.mcendgame.main.messaging.misc.GetCustomAttributesTextsCommand
import de.fuballer.mcendgame.main.util.NumberUtil
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

private const val LANGUAGE_KEY_PREFIX = "attribute.mcendgame."
private const val DETAILED_KEY_SUFFIX = "detailed"
private val BOUNDS_COLOR = ChatFormatting.DARK_GRAY
private val TIER_COLOR = ChatFormatting.AQUA

/** Index of the first line below the item name in a tooltip. */
private const val FIRST_ATTRIBUTE_TOOLTIP_INDEX = 1

@Injectable
@Environment(EnvType.CLIENT)
class CustomAttributesRenderer {
    @CommandHandler
    fun on(cmd: RenderItemTooltipCommand) {
        val attributes = cmd.itemStack.getCustomAttributes()
        if (attributes.isEmpty()) return

        val detailed = Minecraft.getInstance().hasShiftDown()

        // Each line is inserted at the same index, so insert in reverse to keep the original order.
        attributes.reversed().forEach { attribute ->
            cmd.texts.add(FIRST_ATTRIBUTE_TOOLTIP_INDEX, buildAttributeLine(attribute, detailed))
        }
    }

    @CommandHandler
    fun on(cmd: GetCustomAttributesTextsCommand) {
        cmd.texts = cmd.attributes.map { buildAttributeLine(it, cmd.detailed) }
    }

    private fun buildAttributeLine(attribute: CustomAttribute, detailed: Boolean): Component {
        val type = attribute.type

        // Rolls whose sign is expressed by a keyword (e.g. "increased"/"reduced") are displayed with
        // the sign flipped to positive, and the keyword carries the sign instead.
        val signFlipped = attribute.rolls.mapIndexed { i, roll ->
            type.signBasedKeywords.getOrNull(i) != null && roll.isNegative()
        }
        val displayRolls = attribute.rolls.mapIndexed { i, roll ->
            if (signFlipped[i]) roll.getSignFlipped() else roll
        }

        val rollTexts =
            if (detailed) buildDetailedRollTexts(type, attribute.rolls, displayRolls)
            else buildSimpleRollTexts(type, displayRolls)
        val arguments = insertSignKeywords(rollTexts, type.signBasedKeywords, signFlipped)

        val translationKey = getTranslationKey(type, detailed)
        val line = Component.translatable(translationKey, *arguments.toTypedArray())
            .withStyle(type.color)

        return if (detailed) line.append(buildTierText(attribute.tier)) else line
    }

    private fun getTranslationKey(type: AttributeType, detailed: Boolean): String {
        val useDetailedKey = detailed && type.hasDetailedKey
        val key = if (useDetailedKey) "${type.key}.$DETAILED_KEY_SUFFIX" else type.key
        return "$LANGUAGE_KEY_PREFIX$key"
    }

    /** Only the rolled values, colored by their dominant enhancement (if any). */
    private fun buildSimpleRollTexts(
        type: AttributeType,
        displayRolls: List<AttributeRoll<*>>,
    ): List<Component> {
        val formatted = type.formatRolls(displayRolls)
        return formatted.mapIndexed { i, text ->
            val enhancementColor = displayRolls[i].getDominantEnhancementColor()
            Component.literal(text).apply { enhancementColor?.let { withStyle(it) } }
        }
    }

    /** Base value (without enhancements), followed by its bounds and the enhancement percentages. */
    private fun buildDetailedRollTexts(
        type: AttributeType,
        originalRolls: List<AttributeRoll<*>>,
        displayRolls: List<AttributeRoll<*>>,
    ): List<Component> {
        val baseValues = type.formatRolls(displayRolls.map { it.getWithoutEnhancement() })
        val bounds = type.formatBounds(displayRolls.map { it.bounds })

        return baseValues.mapIndexed { i, baseValue ->
            Component.literal(baseValue)
                .withStyle(type.color)
                .append(Component.literal(bounds[i]).withStyle(BOUNDS_COLOR))
                .append(buildEnhancementText(originalRolls[i]))
        }
    }

    private fun buildEnhancementText(roll: AttributeRoll<*>): Component {
        val text = Component.empty()
        roll.enhancements.forEach { (enhancementType, value) ->
            val percent = ValueFormats.formatDoubleSigned(value * 100)
            text.append(Component.literal("($percent%)").withStyle(enhancementType.color))
        }
        return text
    }

    /** Places each keyword directly after the roll at the same index. */
    private fun insertSignKeywords(
        rollTexts: List<Component>,
        keywords: List<SignBasedKeyword?>,
        signFlipped: List<Boolean>,
    ): List<Component> =
        rollTexts.flatMapIndexed { i, rollText ->
            val keyword = keywords.getOrNull(i)
                ?: return@flatMapIndexed listOf(rollText)

            val keywordText = if (signFlipped[i]) keyword.negative else keyword.positive
            listOf(rollText, keywordText)
        }

    private fun buildTierText(tier: Int) =
        Component.literal(" ${NumberUtil.toRomanNumeral(tier)}").withStyle(TIER_COLOR)
}