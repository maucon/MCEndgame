package de.fuballer.mcendgame.component.dungeon.looting

import de.fuballer.mcendgame.component.item.custom_item.types.*
import de.fuballer.mcendgame.util.random.RandomOption

object LootingSettings {
    const val ITEMS_DROP_CHANCE = 0.015f
    const val ITEMS_DROP_CHANCE_PER_LOOTING = 0.008f
    const val ITEMS_DROP_CHANCE_DIAMOND = 0.004f
    const val ITEMS_DROP_CHANCE_DIAMOND_PER_LOOTING = 0.002f
    const val ITEMS_DROP_CHANCE_NETHERITE = 0f
    const val ITEMS_DROP_CHANCE_NETHERITE_PER_LOOTING = 0.0015f

    private const val CUSTOM_ITEM_BASE_CHANCE = 0.01
    private const val CUSTOM_ITEM_CHANCE_INCREASE = 0.002
    fun getCustomItemDropChance(mapTier: Int) = CUSTOM_ITEM_BASE_CHANCE + CUSTOM_ITEM_CHANCE_INCREASE * mapTier

    val CUSTOM_ITEM_OPTIONS = listOf(
        RandomOption(1, ArcheryAnnexItemType),
        RandomOption(1, ArrowfallItemType),
        RandomOption(1, BitterfrostItemType),
        RandomOption(1, FatesplitterItemType),
        RandomOption(1, GeistergaloschenItemType),
        RandomOption(1, HeadhuntersHaremType),
        RandomOption(1, LifewardAegisItemType),
        RandomOption(1, ShrinkshadowItemType),
        RandomOption(1, TitansEmbraceItemType),
        RandomOption(1, TwinfireItemType),
        RandomOption(1, VitalitySurgeItemType),
    )
}