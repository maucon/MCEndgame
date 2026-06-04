package de.fuballer.mcendgame.main.component.custom_attribute.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.custom_attribute.affinity.AttributeAffinity
import de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword.SignBasedKeyword
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.CodecUtil
import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier

sealed class AttributeType(
    val key: String,
    val formatRolls: (List<AttributeRoll<*>>) -> List<String>,
    val formatBounds: (List<AttributeBounds<*>>) -> List<String>,
    val affinities: List<AttributeAffinity>,
    val signBasedKeywords: List<SignBasedKeyword?>,
) {
    companion object {
        val CODEC: Codec<AttributeType> = CodecUtil.ofTwo(VanillaAttributeType.CODEC, CustomAttributeType.CODEC)
    }
}

class VanillaAttributeType(
    val attribute: Holder<Attribute>,
    val scaleType: AttributeModifier.Operation,
    key: String,
    formatRolls: (List<AttributeRoll<*>>) -> List<String>,
    formatBounds: (List<AttributeBounds<*>>) -> List<String>,
    affinities: List<AttributeAffinity>,
    signBasedKeywords: List<SignBasedKeyword?> = listOf<SignBasedKeyword>(),
) : AttributeType(key, formatRolls, formatBounds, affinities, signBasedKeywords) {
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
    formatRolls: (List<AttributeRoll<*>>) -> List<String>,
    formatBounds: (List<AttributeBounds<*>>) -> List<String>,
    affinities: List<AttributeAffinity>,
    signBasedKeywords: List<SignBasedKeyword?> = listOf<SignBasedKeyword>(),
) : AttributeType(key, formatRolls, formatBounds, affinities, signBasedKeywords) {
    companion object {
        val CODEC: Codec<CustomAttributeType> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.STRING.fieldOf("ckey").forGetter(CustomAttributeType::key)
                ).apply(instance, CustomAttributeTypes::getByKey)
            }
    }
}