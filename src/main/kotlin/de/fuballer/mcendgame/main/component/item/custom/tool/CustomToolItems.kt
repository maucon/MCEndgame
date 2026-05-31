package de.fuballer.mcendgame.main.component.item.custom.tool

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.tool.item.*
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.BlocksAttacksComponent
import net.minecraft.component.type.UseCooldownComponent
import net.minecraft.component.type.WeaponComponent
import net.minecraft.item.Item
import net.minecraft.item.MaceItem
import net.minecraft.registry.tag.DamageTypeTags
import net.minecraft.registry.tag.ItemTags
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import java.util.*

@Injectable
object CustomToolItems {
    val BLOODHARVEST = UniqueItemRegistry.registerToolItem(
        ::Bloodharvest,
        Item.Settings().sword(CustomToolMaterials.BLOODHARVEST, 7F, -2.4F),
        "bloodharvest"
    )
    val TWINFIRE = UniqueItemRegistry.registerToolItem(
        ::Twinfire,
        Item.Settings().sword(CustomToolMaterials.TWINFIRE, 7F, -2.4F),
        "twinfire"
    )
    val FATESPLITTER = UniqueItemRegistry.registerToolItem(
        ::Fatesplitter,
        Item.Settings().axe(CustomToolMaterials.FATESPLITTER, 9F, -3F),
        "fatesplitter"
    )
    val SERPENTS_FANG = UniqueItemRegistry.registerToolItem(
        ::SerpentsFang,
        Item.Settings().sword(CustomToolMaterials.SERPENTS_FANG, 5F, -2.2F),
        "serpents_fang"
    )
    val NIGHTREAVER = UniqueItemRegistry.registerToolItem(
        ::Nightreaver,
        Item.Settings().sword(CustomToolMaterials.NIGHTREAVER, 5F, -2F),
        "nightreaver"
    )
    val RADIANT_DAWN = UniqueItemRegistry.registerToolItem(
        ::RadiantDawn,
        Item.Settings().sword(CustomToolMaterials.RADIANT_DAWN, 7F, -3.2F),
        "radiant_dawn"
    )
    val WINDSTRING = UniqueItemRegistry.registerToolItem(
        ::Windstring,
        Item.Settings().maxDamage(500),
        "windstring"
    )
    val HAILSTORM = UniqueItemRegistry.registerToolItem(
        ::Hailstorm,
        Item.Settings().maxDamage(500),
        "hailstorm"
    )
    val DUSK_PIERCER = UniqueItemRegistry.registerToolItem(
        ::DuskPiercer,
        Item.Settings().maxDamage(500),
        "dusk_piercer"
    )
    val GRUDGEBEARER = UniqueItemRegistry.registerToolItem(
        ::Grudgebearer,
        Item.Settings()
            .maxDamage(336)
            .component(
                DataComponentTypes.BLOCKS_ATTACKS,
                BlocksAttacksComponent(
                    0.25F,
                    1.0F,
                    listOf(BlocksAttacksComponent.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    BlocksAttacksComponent.ItemDamage(3.0F, 1.0F, 1.0F),
                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                    Optional.of(SoundEvents.ITEM_SHIELD_BLOCK),
                    Optional.of(SoundEvents.ITEM_SHIELD_BREAK),
                )
            )
            .component(
                DataComponentTypes.USE_COOLDOWN,
                UseCooldownComponent(0F, Optional.of(Identifier.of("minecraft", "shield")))
            )
            .component(DataComponentTypes.BREAK_SOUND, SoundEvents.ITEM_SHIELD_BREAK),
        "grudgebearer"
    )
    val GRAVEBREAKER = UniqueItemRegistry.registerToolItem(
        ::Gravebreaker,
        Item.Settings()
            .maxDamage(500)
            .component(DataComponentTypes.TOOL, MaceItem.createToolComponent())
            .repairable(ItemTags.STONE_TOOL_MATERIALS)
            .attributeModifiers(Gravebreaker.createAttributeModifiers())
            .enchantable(15)
            .component(DataComponentTypes.WEAPON, WeaponComponent(1)),
        "gravebreaker"
    )
}