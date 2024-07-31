package de.fuballer.mcendgame.component.dungeon.loot

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

object LootSettings {
    const val ITEMS_DROP_CHANCE = 0.015
    const val ITEMS_DROP_CHANCE_PER_LOOTING = 0.008
    const val ITEMS_DROP_CHANCE_DIAMOND = 0.006
    const val ITEMS_DROP_CHANCE_DIAMOND_PER_LOOTING = 0.003
    const val ITEMS_DROP_CHANCE_NETHERITE = 0.003
    const val ITEMS_DROP_CHANCE_NETHERITE_PER_LOOTING = 0.002

    fun getCustomItemDropChance(mapTier: Int) = 0.000012 * pow(mapTier, 2) + 0.0001 * mapTier + 0.001

    val CUSTOM_ITEM_OPTIONS = listOf(
        RandomOption(1, AbyssalMaskItemType),
        RandomOption(1, ArcheryAnnexItemType),
        RandomOption(1, ArrowfallItemType),
        RandomOption(1, BitterfrostItemType),
        RandomOption(1, BloodlustItemType),
        RandomOption(1, ChaosguardItemType),
        RandomOption(1, FatesplitterItemType),
        RandomOption(1, GalestrideItemType),
        RandomOption(1, GeistergaloschenItemType),
        RandomOption(1, HeadhuntersHaremType),
        RandomOption(1, LifewardAegisItemType),
        RandomOption(1, MoonshadowItemType),
        RandomOption(1, SerpentsFangItemType),
        RandomOption(1, ShrinkshadowItemType),
        RandomOption(1, StonewardItemType),
        RandomOption(1, StormfeatherItemType),
        RandomOption(1, TitansEmbraceItemType),
        RandomOption(1, TwinfireItemType),
        RandomOption(1, TyrantsReachItemType),
        RandomOption(1, VitalitySurgeItemType),
        RandomOption(1, CrownOfConflictItemType),
        RandomOption(1, WingedFlightItemType),
    )

    fun getBossOrbAmount(mapTier: Int) = 0.5 + 0.05 * (mapTier - 1)

    val BOSS_ORBS = listOf(
        RandomOption(1, ImitationSettings.getImitationItem()),
        RandomOption(3, RefinementSettings.getRefinementItem()),
        RandomOption(5, CorruptionSettings.getDoubleCorruptionItem()),
        RandomOption(5, ReshapingSettings.getReshapingItem()),
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