package de.fuballer.mcendgame.main.component.item_filter.db

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.maucon.mauconframework.stereotype.Entity
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.*

data class ItemFilterEntity(
    /** id of a player */
    override var id: UUID,
    var items: MutableSet<Item>
) : Entity<UUID> {
    companion object {
        val CODEC: Codec<ItemFilterEntity> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    UUIDUtil.AUTHLIB_CODEC.fieldOf("id").forGetter(ItemFilterEntity::id),
                    Identifier.CODEC.listOf().fieldOf("items")
                        .forGetter { entity ->
                            entity.items
                                .filter { it != Items.AIR }
                                .map { BuiltInRegistries.ITEM.getKey(it) }
                        }
                ).apply(instance) { id, itemIds ->
                    val items = itemIds
                        .map { BuiltInRegistries.ITEM.getValue(it) }
                        .filter { it != Items.AIR }
                        .toMutableSet()

                    ItemFilterEntity(id, items)
                }
            }
    }
}