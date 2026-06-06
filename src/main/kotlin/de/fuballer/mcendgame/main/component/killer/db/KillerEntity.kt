package de.fuballer.mcendgame.main.component.killer.db

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.maucon.mauconframework.stereotype.Entity
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.*

data class KillerEntity(
    /** id of a player */
    override var id: UUID,
    val type: ResourceKey<EntityType<*>>,
    val displayName: Optional<Component>,
    val killerUUID: UUID,
    val equipment: Map<EquipmentSlot, ItemStack>,
    val statusEffects: List<MobEffectInstance>,
) : Entity<UUID> {
    companion object {
        val EQUIPMENT_MAP_CODEC: Codec<Map<EquipmentSlot, ItemStack>> = Codec.unboundedMap(EquipmentSlot.CODEC, ItemStack.CODEC)

        val STATUS_EFFECTS_CODEC: Codec<List<MobEffectInstance>> = Codec.list(MobEffectInstance.CODEC)

        val CODEC: Codec<KillerEntity> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("id").forGetter(KillerEntity::id),
                    ResourceKey.codec(Registries.ENTITY_TYPE).fieldOf("type").forGetter(KillerEntity::type),
                    Codec.optionalField("displayName", ComponentSerialization.CODEC, true).fieldOf("displayName").forGetter(KillerEntity::displayName),
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("killerUUID").forGetter(KillerEntity::killerUUID),
                    EQUIPMENT_MAP_CODEC.fieldOf("equipment").forGetter(KillerEntity::equipment),
                    STATUS_EFFECTS_CODEC.fieldOf("status_effects").forGetter(KillerEntity::statusEffects),
                ).apply(instance, ::KillerEntity)
            }

        val EQUIPMENT_MAP_PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, Map<EquipmentSlot, ItemStack>> =
            ByteBufCodecs.map(::Object2ObjectOpenHashMap, EquipmentSlot.STREAM_CODEC, ItemStack.STREAM_CODEC)

        val STATUS_EFFECTS_PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, List<MobEffectInstance>> =
            ByteBufCodecs.collection(::ArrayList, MobEffectInstance.STREAM_CODEC)

        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, KillerEntity> = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, KillerEntity::id,
            ResourceKey.streamCodec(Registries.ENTITY_TYPE), KillerEntity::type,
            ByteBufCodecs.optional(ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC), KillerEntity::displayName,
            UUIDUtil.STREAM_CODEC, KillerEntity::killerUUID,
            EQUIPMENT_MAP_PACKET_CODEC, KillerEntity::equipment,
            STATUS_EFFECTS_PACKET_CODEC, KillerEntity::statusEffects,
            ::KillerEntity
        )

        fun of(
            killed: Player,
            killer: LivingEntity,
        ): KillerEntity {
            val type = BuiltInRegistries.ENTITY_TYPE.getResourceKey(killer.type).get()
            val name = Optional.ofNullable(killer.displayName)
            val equipment = mapOf(
                EquipmentSlot.HEAD to killer.getItemBySlot(EquipmentSlot.HEAD),
                EquipmentSlot.CHEST to killer.getItemBySlot(EquipmentSlot.CHEST),
                EquipmentSlot.LEGS to killer.getItemBySlot(EquipmentSlot.LEGS),
                EquipmentSlot.FEET to killer.getItemBySlot(EquipmentSlot.FEET),
                EquipmentSlot.MAINHAND to killer.getItemBySlot(EquipmentSlot.MAINHAND),
                EquipmentSlot.OFFHAND to killer.getItemBySlot(EquipmentSlot.OFFHAND),
            ).filter { !it.value.isEmpty }
            val effects = killer.activeEffects.toList()

            return KillerEntity(killed.uuid, type, name, killer.uuid, equipment, effects)
        }
    }
}