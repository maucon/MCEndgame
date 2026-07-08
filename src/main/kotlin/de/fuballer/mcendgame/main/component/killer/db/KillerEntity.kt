package de.fuballer.mcendgame.main.component.killer.db

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.maucon.mauconframework.stereotype.Entity
import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.text.TextCodecs
import net.minecraft.util.Uuids
import java.util.*

data class KillerEntity(
    /** id of a player */
    override var id: UUID,
    val type: RegistryKey<EntityType<*>>,
    val displayName: Optional<Text>,
    val killerUUID: UUID,
    val equipment: Map<EquipmentSlot, ItemStack>,
    val statusEffects: List<StatusEffectInstance>,
) : Entity<UUID> {
    companion object {
        val EQUIPMENT_MAP_CODEC: Codec<Map<EquipmentSlot, ItemStack>> = Codec.unboundedMap(EquipmentSlot.CODEC, ItemStack.CODEC)

        val STATUS_EFFECTS_CODEC: Codec<List<StatusEffectInstance>> = Codec.list(StatusEffectInstance.CODEC)

        val CODEC: Codec<KillerEntity> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Uuids.CODEC.fieldOf("id").forGetter(KillerEntity::id),
                    RegistryKey.createCodec(RegistryKeys.ENTITY_TYPE).fieldOf("type").forGetter(KillerEntity::type),
                    Codec.optionalField("displayName", TextCodecs.CODEC, true).fieldOf("displayName").forGetter(KillerEntity::displayName),
                    Uuids.CODEC.fieldOf("killerUUID").forGetter(KillerEntity::killerUUID),
                    EQUIPMENT_MAP_CODEC.fieldOf("equipment").forGetter(KillerEntity::equipment),
                    STATUS_EFFECTS_CODEC.fieldOf("status_effects").forGetter(KillerEntity::statusEffects),
                ).apply(instance, ::KillerEntity)
            }

        val EQUIPMENT_SLOT_PACKET_CODEC: PacketCodec<ByteBuf, EquipmentSlot> =
            PacketCodecs.indexed(
                { id -> EquipmentSlot.entries[id] },
                { slot -> slot.ordinal }
            )

        val EQUIPMENT_MAP_PACKET_CODEC: PacketCodec<RegistryByteBuf, Map<EquipmentSlot, ItemStack>> =
            PacketCodecs.map(::Object2ObjectOpenHashMap, EQUIPMENT_SLOT_PACKET_CODEC, ItemStack.PACKET_CODEC)

        val STATUS_EFFECTS_PACKET_CODEC: PacketCodec<RegistryByteBuf, List<StatusEffectInstance>> =
            PacketCodecs.collection(::ArrayList, StatusEffectInstance.PACKET_CODEC)

        val PACKET_CODEC: PacketCodec<RegistryByteBuf, KillerEntity> = PacketCodec.tuple(
            Uuids.PACKET_CODEC, KillerEntity::id,
            RegistryKey.createPacketCodec(RegistryKeys.ENTITY_TYPE), KillerEntity::type,
            PacketCodecs.optional(TextCodecs.PACKET_CODEC), KillerEntity::displayName,
            Uuids.PACKET_CODEC, KillerEntity::killerUUID,
            EQUIPMENT_MAP_PACKET_CODEC, KillerEntity::equipment,
            STATUS_EFFECTS_PACKET_CODEC, KillerEntity::statusEffects,
            ::KillerEntity
        )

        fun of(
            killed: PlayerEntity,
            killer: LivingEntity,
        ): KillerEntity {
            val type = Registries.ENTITY_TYPE.getKey(killer.type).get()
            val name = Optional.ofNullable(killer.displayName)
            val equipment = mapOf(
                EquipmentSlot.HEAD to killer.getEquippedStack(EquipmentSlot.HEAD),
                EquipmentSlot.CHEST to killer.getEquippedStack(EquipmentSlot.CHEST),
                EquipmentSlot.LEGS to killer.getEquippedStack(EquipmentSlot.LEGS),
                EquipmentSlot.FEET to killer.getEquippedStack(EquipmentSlot.FEET),
                EquipmentSlot.MAINHAND to killer.getEquippedStack(EquipmentSlot.MAINHAND),
                EquipmentSlot.OFFHAND to killer.getEquippedStack(EquipmentSlot.OFFHAND),
            ).filter { !it.value.isEmpty }
            val effects = killer.statusEffects.toList()

            return KillerEntity(killed.uuid, type, name, killer.uuid, equipment, effects)
        }
    }
}