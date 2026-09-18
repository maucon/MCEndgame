package de.fuballer.mcendgame.main.component.analytics

import com.google.gson.JsonElement
import com.mojang.serialization.JsonOps
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.data_component_type.CustomDataComponentType
import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.RolledScarredOneEffect
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.migration.MigrationService
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemsRepository
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getCreationTime
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonSeed
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object AnalyticsUtil {
    const val UNKNOWN = "unknown"

    fun toPayloadItem(itemStack: ItemStack): PayloadItem? {
        if (itemStack.isEmpty) return null
        val id = BuiltInRegistries.ITEM.getKey(itemStack.item).toString()
        val enchants = itemStack.get(DataComponents.ENCHANTMENTS)
            ?.entrySet()
            ?.associate {
                it.key.unwrapKey().map { k -> k.identifier().toString() }.orElse(UNKNOWN)!! to it.intValue
            }
            ?: emptyMap()
        val customAttributes = CustomAttribute.CODEC.listOf()
            .encodeStart(JsonOps.INSTANCE, itemStack.getCustomAttributes())
            .getOrThrow()
            .asJsonArray
            .also { array -> array.forEach { it.asJsonObject.remove("id") } }
        return PayloadItem(id, enchants, customAttributes)
    }

    fun getArmorItems(entity: LivingEntity): List<PayloadItem?> = listOf(
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.HEAD)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.CHEST)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.LEGS)),
        toPayloadItem(entity.getItemBySlot(EquipmentSlot.FEET)),
    )

    fun getMainhandItems(entity: LivingEntity) = toPayloadItem(entity.getItemBySlot(EquipmentSlot.MAINHAND))
    fun getOffhandItems(entity: LivingEntity) = toPayloadItem(entity.getItemBySlot(EquipmentSlot.OFFHAND))

    fun getHotbarItems(player: Player) = (0..8).map { toPayloadItem(player.inventory.getItem(it)) }

    fun getActiveEffects(entity: LivingEntity) = entity.activeEffects.associate {
        it.effect.unwrapKey().map { k -> k.identifier().toString() }.orElse(UNKNOWN)!! to it.amplifier
    }

    private fun getPlayerTotems(
        player: Player,
        playerTotemsRepository: PlayerTotemsRepository
    ): List<TotemPayload> {
        val totems = playerTotemsRepository.findById(player.uuid)?.totems ?: listOf()
        return totems.map { itemStack ->
            val totemItem = itemStack.item as? TotemItem ?: return@map null

            // Backfill tier data component for totems saved before it existed
            MigrationService.migrateTotemTier(itemStack, player)

            val id = BuiltInRegistries.ITEM.getKey(totemItem).toString()
            val tier = itemStack.get(CustomDataComponentType.TOTEM_TIER) ?: return@map null
            val type = totemItem.type.toString()

            TotemPayload(id, tier, type)
        }.filterNotNull()
    }

    fun getPlayerLoadoutData(
        player: Player,
        playerTotemsRepository: PlayerTotemsRepository
    ) = PlayerLoadoutPayload(
        armor = getArmorItems(player),
        mainhand = getMainhandItems(player),
        offhand = getOffhandItems(player),
        hotbar = getHotbarItems(player),
        effects = getActiveEffects(player),
        totems = getPlayerTotems(player, playerTotemsRepository),
        gamemode = player.gameMode()?.name ?: UNKNOWN,
    )

    fun getEntityLoadoutData(killer: LivingEntity) = EntityLoadoutPayload(
        entity = BuiltInRegistries.ENTITY_TYPE.getKey(killer.type).toString(),
        armor = getArmorItems(killer),
        mainhand = getMainhandItems(killer),
        offhand = getOffhandItems(killer),
        effects = getActiveEffects(killer),
    )

    fun getDungeonData(dungeonLevel: ServerLevel) =
        DungeonDataPayload(
            seed = dungeonLevel.getDungeonSeed(),
            dungeonLevel = dungeonLevel.getDungeonLevel(),
            aspects = getAspectItems(dungeonLevel),
            timeSinceCreation = dungeonLevel.gameTime - dungeonLevel.getCreationTime(),
        )

    fun getAspectItems(dungeonLevel: ServerLevel) = dungeonLevel.getDungeonAspects()
        .flatMap { (item, count) -> List(count) { item } }
        .map { BuiltInRegistries.ITEM.getKey(it).toString() }

    fun getScarredOneEffectsJson(effects: List<RolledScarredOneEffect>): JsonElement = RolledScarredOneEffect.LIST_CODEC
        .encodeStart(JsonOps.INSTANCE, effects)
        .getOrThrow()
        .asJsonArray
        .also { array ->
            array.forEach {
                it.asJsonObject.getAsJsonObject("attribute").remove("id")
            }
        }
}