package de.fuballer.mcendgame.main.component.dungeon.loot

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import kotlin.random.Random

object LootSettings {
    const val ITEMS_DROP_PROBABILITY = 0.039

    const val ITEMS_DROP_PROBABILITY_DIAMOND = 0.015

    const val ITEMS_DROP_PROBABILITY_NETHERITE = 0.009

    fun calculateMagicFindDropProbabilityFactor(magicFind: Int) = 1 + magicFind / 100.0

    val ASPECTS = listOf(
        RandomOption(100, AspectItems.ASPECT_OF_HORDES),
        RandomOption(80, AspectItems.ASPECT_OF_TYRANNY),
        RandomOption(80, AspectItems.ASPECT_OF_DOMINION),
        RandomOption(100, AspectItems.ASPECT_OF_IMPATIENCE),
        RandomOption(50, AspectItems.ASPECT_OF_ZEAL),
        RandomOption(80, AspectItems.ASPECT_OF_CURIO),
        RandomOption(100, AspectItems.ASPECT_OF_GREED),
        RandomOption(50, AspectItems.ASPECT_OF_FORTUNE),
        //RandomOption(5, AspectItems.ASPECT_OF_GHOSTS), doesn't drop default [AspectOfGhostsService]
        RandomOption(75, AspectItems.ASPECT_OF_FORTITUDE),
        RandomOption(75, AspectItems.ASPECT_OF_SAVAGERY),
        RandomOption(15, AspectItems.ASPECT_OF_EMINENCE),
        RandomOption(50, AspectItems.ASPECT_OF_ANCESTORS),
        RandomOption(50, AspectItems.ASPECT_OF_DUALITY),
    )

    fun getBossBaseCrystalCount(dungeonLevel: Int) = dungeonLevel / 7.0 + Random.nextDouble(dungeonLevel / 5.0)

    val CRYSTALS = listOf(
        LevelRestrictedRandomOption(20, tier = 0, requiredLevel = 0, CrystalItems.CALIBRATION_CRYSTAL),
        LevelRestrictedRandomOption(10, tier = 0, requiredLevel = 2, CrystalItems.PERMUTATION_CRYSTAL),
        LevelRestrictedRandomOption(5, tier = 0, requiredLevel = 4, CrystalItems.REFORGE_CRYSTAL),
        LevelRestrictedRandomOption(3, tier = 0, requiredLevel = 6, CrystalItems.CORRUPTION_CRYSTAL),
        LevelRestrictedRandomOption(2, tier = 0, requiredLevel = 8, CrystalItems.SACRIFICIAL_CRYSTAL),
    )

    // unaffected by increased loot
    val BOSS_UNIQUES = mapOf(
        CustomEntities.BEAKBURN to mapOf(
            CustomArmorItems.EMBERREIGN to 0.05,
        ),
        CustomEntities.BONECRUSHER to mapOf(
            CustomToolItems.GRAVEBREAKER to 0.05,
        ),
        CustomEntities.ELF_DUELIST to mapOf(
            CustomArmorItems.WINDSTRIDER to 0.05,
        ),
        CustomEntities.ARACHNE to mapOf(
            CustomArmorItems.BROODMOTHER to 0.05,
        ),
    )
}