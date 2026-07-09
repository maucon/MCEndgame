package de.fuballer.mcendgame.main.component.item.custom.tool

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.tool.item.*
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.MaceItem
import net.minecraft.world.item.component.BlocksAttacks
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.component.Weapon
import java.util.*

@Injectable
object CustomToolItems {
    val BLOODHARVEST = UniqueItemRegistry.registerToolItem(
        ::Bloodharvest,
        Item.Properties().sword(CustomToolMaterials.BLOODHARVEST, 7F, -2.4F),
        CustomToolItemIds.BLOODHARVEST
    )
    val TWINFIRE = UniqueItemRegistry.registerToolItem(
        ::Twinfire,
        Item.Properties().sword(CustomToolMaterials.TWINFIRE, 7F, -2.4F),
        CustomToolItemIds.TWINFIRE
    )
    val FATESPLITTER = UniqueItemRegistry.registerToolItem(
        ::Fatesplitter,
        Item.Properties().axe(CustomToolMaterials.FATESPLITTER, 9F, -3F),
        CustomToolItemIds.FATESPLITTER
    )
    val SERPENTS_FANG = UniqueItemRegistry.registerToolItem(
        ::SerpentsFang,
        Item.Properties().sword(CustomToolMaterials.SERPENTS_FANG, 5F, -2.2F),
        CustomToolItemIds.SERPENTS_FANG
    )
    val NIGHTREAVER = UniqueItemRegistry.registerToolItem(
        ::Nightreaver,
        Item.Properties().sword(CustomToolMaterials.NIGHTREAVER, 5F, -2F),
        CustomToolItemIds.NIGHTREAVER
    )
    val RADIANT_DAWN = UniqueItemRegistry.registerToolItem(
        ::RadiantDawn,
        Item.Properties().sword(CustomToolMaterials.RADIANT_DAWN, 7F, -3.2F),
        CustomToolItemIds.RADIANT_DAWN
    )
    val WINDSTRING = UniqueItemRegistry.registerToolItem(
        ::Windstring,
        Item.Properties().durability(500),
        CustomToolItemIds.WINDSTRING
    )
    val HAILSTORM = UniqueItemRegistry.registerToolItem(
        ::Hailstorm,
        Item.Properties().durability(500),
        CustomToolItemIds.HAILSTORM
    )
    val DUSK_PIERCER = UniqueItemRegistry.registerToolItem(
        ::DuskPiercer,
        Item.Properties().durability(500),
        CustomToolItemIds.DUSK_PIERCER
    )
    val GRUDGEBEARER = UniqueItemRegistry.registerToolItem(
        ::Grudgebearer,
        Item.Properties()
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .durability(336)
            .delayedComponent(DataComponents.BLOCKS_ATTACKS) { context ->
                BlocksAttacks(
                    0.25F,
                    1.0F,
                    listOf(BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                    Optional.of(SoundEvents.SHIELD_BLOCK),
                    Optional.of(SoundEvents.SHIELD_BREAK)
                )
            }
            .component(
                DataComponents.USE_COOLDOWN,
                UseCooldown(0F, Optional.of(Identifier.fromNamespaceAndPath("minecraft", "shield")))
            )
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK),
        CustomToolItemIds.GRUDGEBEARER
    )
    val GRAVEBREAKER = UniqueItemRegistry.registerToolItem(
        ::Gravebreaker,
        Item.Properties()
            .durability(500)
            .component(DataComponents.TOOL, MaceItem.createToolProperties())
            .repairable(ItemTags.STONE_TOOL_MATERIALS)
            .attributes(Gravebreaker.createAttributeModifiers())
            .enchantable(15)
            .component(DataComponents.WEAPON, Weapon(1)),
        CustomToolItemIds.GRAVEBREAKER
    )
}