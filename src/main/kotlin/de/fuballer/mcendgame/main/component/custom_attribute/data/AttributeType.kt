package de.fuballer.mcendgame.main.component.custom_attribute.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinity
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeyword
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.CodecUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier

private val DEFAULT_COLOR = ChatFormatting.BLUE

sealed class AttributeType(
    /** Translation key suffix, see `attribute.mcendgame.<key>`. */
    val key: String,
    /** How the rolled values and their bounds are displayed. */
    val format: AttributeFormat,
    val affinities: List<AttributeAffinity>,
    /**
     * Entry `i` belongs to value `i` (null = no keyword for that value).
     * If present, the value is displayed without its sign and the keyword
     * (e.g. "increased"/"reduced") is inserted after it instead.
     */
    val signBasedKeywords: List<SignBasedKeyword?>,
    val color: ChatFormatting,
    /** If true, the detailed tooltip (shift) uses the translation `<key>.detailed`. */
    val hasDetailedKey: Boolean,
) {
    fun formatRolls(rolls: List<AttributeRoll<*>>): List<String> = format.formatRolls(rolls)

    fun formatBounds(bounds: List<AttributeBounds<*>>): List<String> = format.formatBounds(bounds)

    companion object {
        val CODEC: Codec<AttributeType> = CodecUtil.ofTwo(VanillaAttributeType.CODEC, CustomAttributeType.CODEC)
    }
}

class VanillaAttributeType(
    val attribute: Holder<Attribute>,
    val scaleType: AttributeModifier.Operation,
    key: String,
    format: AttributeFormat,
    affinities: List<AttributeAffinity>,
    signBasedKeywords: List<SignBasedKeyword?> = emptyList(),
) : AttributeType(
    key = key,
    format = format,
    affinities = affinities,
    signBasedKeywords = signBasedKeywords,
    color = DEFAULT_COLOR,
    hasDetailedKey = false,
) {
    companion object {
        val CODEC: Codec<VanillaAttributeType> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("vkey").forGetter(VanillaAttributeType::key)
                ).apply(instance, VanillaAttributeTypes::getByKey)
            }
    }
}

class CustomAttributeType(
    key: String,
    format: AttributeFormat,
    affinities: List<AttributeAffinity>,
    signBasedKeywords: List<SignBasedKeyword?> = emptyList(),
    color: ChatFormatting = DEFAULT_COLOR,
    hasDetailedKey: Boolean = false,
) : AttributeType(key, format, affinities, signBasedKeywords, color, hasDetailedKey) {
    companion object {
        val CODEC: Codec<CustomAttributeType> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("ckey").forGetter(CustomAttributeType::key)
                ).apply(instance, CustomAttributeTypes::getByKey)
            }
    }
}