package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.ScarredOneEffectTargetGroup
import de.fuballer.mcendgame.main.messaging.misc.GetCustomAttributesTextsCommand
import de.maucon.mauconframework.command.CommandGateway
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class RolledScarredOneEffect(
    val attribute: CustomAttribute,
    val targets: ScarredOneEffectTargetGroup,
) {
    companion object {
        val CODEC: Codec<RolledScarredOneEffect> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    CustomAttribute.CODEC.fieldOf("attribute").forGetter(RolledScarredOneEffect::attribute),

                    Codec.INT.xmap(
                        { index -> ScarredOneEffectTargetGroup.entries.getOrNull(index) ?: ScarredOneEffectTargetGroup.ALLIES },
                        { it.ordinal }
                    ).fieldOf("targets").forGetter(RolledScarredOneEffect::targets)
                ).apply(instance, ::RolledScarredOneEffect)
            }
        val LIST_CODEC: Codec<List<RolledScarredOneEffect>> = Codec.list(CODEC)

        val PACKET_CODEC: StreamCodec<ByteBuf, RolledScarredOneEffect> = ByteBufCodecs.fromCodec(CODEC)
    }

    fun getAttribute(): Component = CommandGateway.apply(GetCustomAttributesTextsCommand(attribute)).texts.firstOrNull() ?: Component.empty()
}