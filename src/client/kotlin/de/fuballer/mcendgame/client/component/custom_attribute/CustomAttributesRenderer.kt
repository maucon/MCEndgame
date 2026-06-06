package de.fuballer.mcendgame.client.component.custom_attribute

import de.fuballer.mcendgame.client.messaging.RenderItemTooltipCommand
import de.fuballer.mcendgame.main.component.custom_attribute.AttributeFormats
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeRoll
import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeType
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
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
private val ATTRIBUTE_LINE_COLOR = ChatFormatting.BLUE
private val ATTRIBUTE_LINE_DETAILS_COLOR = ChatFormatting.DARK_GRAY
private val TIER_DETAIL_COLOR = ChatFormatting.AQUA

@Injectable
@Environment(EnvType.CLIENT)
class CustomAttributesRenderer {
    @CommandHandler
    fun on(cmd: RenderItemTooltipCommand) {
        val customAttributes = cmd.itemStack.getCustomAttributes()
        if (customAttributes.isEmpty()) return

        customAttributes
            .reversed()
            .map { attribute -> buildAttributeLine(attribute, Minecraft.getInstance().hasShiftDown()) }
            .forEach { cmd.texts.add(1, it) }
    }

    @CommandHandler
    fun on(cmd: GetCustomAttributesTextsCommand) {
        cmd.texts = cmd.attributes.map { buildAttributeLine(it, cmd.detailed) }
    }

    private fun buildAttributeLine(attribute: CustomAttribute, detailed: Boolean): Component {
        val type = attribute.type
        val rolls = attribute.rolls
        val signBasedKeywords = type.signBasedKeywords
        val flipSign = signBasedKeywords.zip(rolls).map { it.first != null && it.second.isNegative() }

        val flippedRolls = flipRollSigns(rolls, flipSign)
        if (!detailed) {
            val dominantEnhancementColors = flippedRolls.map { it.getDominantEnhancementColor() }
            val rollTexts = type.formatRolls(flippedRolls).mapIndexed { i, formattedRoll ->
                Component.literal(formattedRoll).apply { dominantEnhancementColors[i]?.let { withStyle(it) } }
            }
            val array = getWithSignBasedKeywords(rollTexts, signBasedKeywords, flipSign).toTypedArray()
            return Component.translatable("$LANGUAGE_KEY_PREFIX${type.key}", *array)
                .withStyle(ATTRIBUTE_LINE_COLOR)
        }

        val notEnhancedRolls = flippedRolls.map(AttributeRoll<*>::getWithoutEnhancement)
        val formattedRolls = type.formatRolls(notEnhancedRolls)
        val boundsTexts = getFormattedBoundsTexts(type, flippedRolls)
        val enhancementTexts = getFormattedEnhancementTexts(rolls)
        val formattedRollsWithBounds = formattedRolls.indices.map { i ->
            Component.literal(formattedRolls[i])
                .withStyle(ATTRIBUTE_LINE_COLOR)
                .append(boundsTexts[i])
                .append(enhancementTexts[i])
        }
        val array = getWithSignBasedKeywords(formattedRollsWithBounds, signBasedKeywords, flipSign).toTypedArray()

        return Component.translatable("$LANGUAGE_KEY_PREFIX${type.key}", *array)
            .withStyle(ATTRIBUTE_LINE_COLOR)
            .append(getFormattedTier(attribute.tier))
    }

    private fun getFormattedBoundsTexts(
        type: AttributeType,
        rolls: List<AttributeRoll<*>>,
    ) = type.formatBounds(rolls.map { it.bounds }).map { Component.literal(it).withStyle(ATTRIBUTE_LINE_DETAILS_COLOR) }

    private fun getFormattedEnhancementTexts(rolls: List<AttributeRoll<*>>) =
        rolls.map { roll ->
            roll.enhancements.entries.map { (type, value) ->
                val formattedValue = AttributeFormats.formatDoubleSigned(value * 100)
                Component.literal("($formattedValue%)").withStyle(type.color)
            }.reduceOrNull { first, second ->
                first.append(second)
            } ?: Component.empty()
        }

    private fun flipRollSigns(rolls: List<AttributeRoll<*>>, flip: List<Boolean>) =
        rolls.mapIndexed { i, roll ->
            if (flip.getOrNull(i) != true) roll
            else roll.getSignFlipped()
        }

    private fun getWithSignBasedKeywords(
        rolls: List<Component>,
        keywords: List<SignBasedKeyword?>,
        isNegative: List<Boolean>,
    ): List<Component> {
        if (keywords.isEmpty()) return rolls

        val insertedList = rolls.toMutableList()
        var insertedCount = 0
        keywords.forEachIndexed { i, keyword ->
            if (keyword == null) return@forEachIndexed

            val text = if (isNegative[i]) keyword.negative else keyword.positive
            val index = i + insertedCount + 1
            insertedList.add(index, text)

            insertedCount++
        }

        return insertedList
    }

    private fun getFormattedTier(tier: Int) = Component.literal(" ${NumberUtil.toRomanNumeral(tier)}")
        .withStyle(TIER_DETAIL_COLOR)
}