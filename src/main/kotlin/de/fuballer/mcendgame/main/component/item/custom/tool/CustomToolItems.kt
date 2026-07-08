package de.fuballer.mcendgame.main.component.item.custom.tool

import de.fuballer.mcendgame.main.component.item.custom.UniqueItemRegistry
import de.fuballer.mcendgame.main.component.item.custom.tool.item.*
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.Item
import net.minecraft.item.MaceItem

@Injectable
object CustomToolItems {
    val BLOODHARVEST = UniqueItemRegistry.registerItem( // TODO AS -2.4F
        Bloodharvest(
            Item.Settings(),
        ),
        "bloodharvest"
    )
    val TWINFIRE = UniqueItemRegistry.registerItem( // TODO AS -2.4F
        Twinfire(
            Item.Settings(),
        ),
        "twinfire"
    )
    val FATESPLITTER = UniqueItemRegistry.registerItem( // TODO AS -3F
        Fatesplitter(
            Item.Settings(),
        ),
        "fatesplitter"
    )
    val SERPENTS_FANG = UniqueItemRegistry.registerItem( // TODO AS -2.2F
        SerpentsFang(
            Item.Settings(),
        ),
        "serpents_fang"
    )
    val NIGHTREAVER = UniqueItemRegistry.registerItem( // TODO AS -2F
        Nightreaver(
            Item.Settings(),
        ),
        "nightreaver"
    )
    val RADIANT_DAWN = UniqueItemRegistry.registerItem( // TODO AS -3.2F
        RadiantDawn(
            Item.Settings(),
        ),
        "radiant_dawn"
    )
    val WINDSTRING = UniqueItemRegistry.registerItem(
        Windstring(
            Item.Settings().maxDamage(500),
        ),
        "windstring"
    )
    val HAILSTORM = UniqueItemRegistry.registerItem(
        Hailstorm(
            Item.Settings().maxDamage(500),
        ),
        "hailstorm"
    )
    val DUSK_PIERCER = UniqueItemRegistry.registerItem(
        DuskPiercer(
            Item.Settings().maxDamage(500),
        ),
        "dusk_piercer"
    )
    val GRUDGEBEARER = UniqueItemRegistry.registerItem(
        Grudgebearer(
            Item.Settings().maxDamage(336),
//            .component(
//                DataComponentTypes.BLOCKS_ATTACKS,
//                BlocksAttacksComponent(
//                    0.25F,
//                    1.0F,
//                    listOf(BlocksAttacksComponent.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
//                    BlocksAttacksComponent.ItemDamage(3.0F, 1.0F, 1.0F),
//                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
//                    Optional.of(SoundEvents.ITEM_SHIELD_BLOCK),
//                    Optional.of(SoundEvents.ITEM_SHIELD_BREAK),
//                )
//            )
        ),
        "grudgebearer"
    )
    val GRAVEBREAKER = UniqueItemRegistry.registerItem(
        Gravebreaker(
            Item.Settings()
                .maxDamage(500)
                .component(DataComponentTypes.TOOL, MaceItem.createToolComponent())
                .attributeModifiers(Gravebreaker.createAttributeModifiers()),
            //.repairable(ItemTags.STONE_TOOL_MATERIALS)
            //.enchantable(15)
            //.component(DataComponentTypes.WEAPON, WeaponComponent(1)),
        ),
        "gravebreaker"
    )
}