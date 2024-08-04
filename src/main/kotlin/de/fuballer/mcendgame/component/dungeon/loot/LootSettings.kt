package de.fuballer.mcendgame.component.dungeon.loot

import com.google.common.math.IntMath.pow
import de.fuballer.mcendgame.component.crafting.corruption.CorruptionSettings
import de.fuballer.mcendgame.component.crafting.duplication.DuplicationSettings
import de.fuballer.mcendgame.component.crafting.reforge.ReforgeSettings
import de.fuballer.mcendgame.component.crafting.roll_randomization.RollRandomizationSettings
import de.fuballer.mcendgame.component.crafting.roll_sacrifice.RollSacrificeSettings
import de.fuballer.mcendgame.component.crafting.roll_shuffle.RollShuffleSettings
import de.fuballer.mcendgame.component.item.custom_item.types.*
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.inventory.ItemStack

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

    private val BASE_BOSS_LOOT = listOf(
        RandomOption(1, DuplicationSettings.getItem()),
        RandomOption(12, RollSacrificeSettings.getItem()),
        RandomOption(15, RollShuffleSettings.getItem()),
        RandomOption(20, CorruptionSettings.getCorruptItem()),
    )

    fun getBossLootOptions(mapTier: Int): List<RandomOption<ItemStack>> {
        val baseLoot = BASE_BOSS_LOOT.toMutableList()

        if (mapTier >= 5) {
            baseLoot.add(RandomOption(5, CorruptionSettings.getDoubleCorruptItem()))
            baseLoot.add(RandomOption(5, ReforgeSettings.getItem()))
        }
        if (mapTier >= 10) {
            baseLoot.add(RandomOption(10, RollRandomizationSettings.getItem()))
        }

        return baseLoot
    }

    val TOTEM_TIERS = listOf(
        SortableRandomOption(1000, 0, TotemTier.COMMON),
        SortableRandomOption(200, 1, TotemTier.UNCOMMON),
        SortableRandomOption(25, 2, TotemTier.RARE),
        SortableRandomOption(3, 3, TotemTier.LEGENDARY)
    )

    val TOTEM_TYPES = TotemType.REGISTRY
        .map { RandomOption(1, it.value) }
}