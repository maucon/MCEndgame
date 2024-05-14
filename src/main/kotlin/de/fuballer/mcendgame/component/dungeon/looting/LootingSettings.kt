package de.fuballer.mcendgame.component.dungeon.looting

import com.google.common.math.IntMath.pow
import de.fuballer.mcendgame.component.crafting.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.crafting.imitation.ImitationSettings
import de.fuballer.mcendgame.component.crafting.refinement.RefinementSettings
import de.fuballer.mcendgame.component.crafting.reshaping.ReshapingSettings
import de.fuballer.mcendgame.component.crafting.transfiguration.TransfigurationSettings
import de.fuballer.mcendgame.component.item.custom_item.types.*
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption

object LootingSettings {
    const val ITEMS_DROP_CHANCE = 0.015f
    const val ITEMS_DROP_CHANCE_PER_LOOTING = 0.008f
    const val ITEMS_DROP_CHANCE_DIAMOND = 0.004f
    const val ITEMS_DROP_CHANCE_DIAMOND_PER_LOOTING = 0.002f
    const val ITEMS_DROP_CHANCE_NETHERITE = 0f
    const val ITEMS_DROP_CHANCE_NETHERITE_PER_LOOTING = 0.0015f

    const val GEAR_DROP_CHANCE_MULTIPLIER_PER_KILLSTREAK = 1.0 / 150

    fun getCustomItemDropChance(mapTier: Int) = 0.000004 * pow(mapTier, 2) + 0.00003 * mapTier + 0.0003

    val CUSTOM_ITEM_OPTIONS = listOf(
        RandomOption(1, ArcheryAnnexItemType),
        RandomOption(1, ArrowfallItemType),
        RandomOption(1, BitterfrostItemType),
        RandomOption(1, FatesplitterItemType),
        RandomOption(1, GeistergaloschenItemType),
        RandomOption(1, HeadhuntersHaremType),
        RandomOption(1, LifewardAegisItemType),
        RandomOption(1, ShrinkshadowItemType),
        RandomOption(1, StormfeatherItemType),
        RandomOption(1, TitansEmbraceItemType),
        RandomOption(1, TwinfireItemType),
        RandomOption(1, VitalitySurgeItemType),
    )

    fun getBossOrbAmount(mapTier: Int) = 0.5 + 0.05 * (mapTier - 1)

    val BOSS_ORBS = listOf(
        RandomOption(1, ImitationSettings.getImitationItem()),
        RandomOption(5, CorruptionSettings.getDoubleCorruptionItem()),
        RandomOption(5, ReshapingSettings.getReshapingItem()),
        RandomOption(10, RefinementSettings.getRefinementItem()),
        RandomOption(15, TransfigurationSettings.getTransfigurationItem()),
        RandomOption(20, CorruptionSettings.getCorruptionItem()),
    )

    const val TOTEM_DROP_CHANCE = 0.00133

    val TOTEM_TIERS = listOf(
        SortableRandomOption(1000, 0, TotemTier.COMMON),
        SortableRandomOption(200, 1, TotemTier.UNCOMMON),
        SortableRandomOption(25, 2, TotemTier.RARE),
        SortableRandomOption(3, 3, TotemTier.LEGENDARY)
    )

    val TOTEM_TYPES = TotemType.REGISTRY
        .map { RandomOption(1, it.value) }
}