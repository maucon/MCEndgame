package de.fuballer.mcendgame.main.component.dungeon.loot

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import de.fuballer.mcendgame.main.component.item.custom.armor.CustomArmorItems
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItems
import de.fuballer.mcendgame.main.component.item.custom.misc.CustomMiscItems
import de.fuballer.mcendgame.main.component.item.custom.tool.CustomToolItems
import de.fuballer.mcendgame.main.util.random.LevelRestrictedRandomOption
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.item.Item
import kotlin.random.Random

object LootSettings {
    const val ITEMS_DROP_PROBABILITY = 0.039

    const val ITEMS_DROP_PROBABILITY_DIAMOND = 0.015

    const val ITEMS_DROP_PROBABILITY_NETHERITE = 0.009

    fun calculateMagicFindDropProbabilityFactor(magicFind: Int) = 1 + magicFind / 100.0

    val ASPECTS = listOf(
        RandomOption(100, AspectItems.ASPECT_OF_HORDES),
        RandomOption(80, AspectItems.ASPECT_OF_TYRANNY),
        RandomOption(15, AspectItems.ASPECT_OF_ASCENSION),
        RandomOption(80, AspectItems.ASPECT_OF_DOMINION),
        RandomOption(100, AspectItems.ASPECT_OF_IMPATIENCE),
        RandomOption(50, AspectItems.ASPECT_OF_ZEAL),
        RandomOption(30, AspectItems.ASPECT_OF_CURIO),
        RandomOption(100, AspectItems.ASPECT_OF_GREED),
        RandomOption(35, AspectItems.ASPECT_OF_FORTUNE),
        //RandomOption(1, AspectItems.ASPECT_OF_GHOSTS), doesn't drop default [AspectOfGhostsService]
        RandomOption(75, AspectItems.ASPECT_OF_FORTITUDE),
        RandomOption(75, AspectItems.ASPECT_OF_SAVAGERY),
        RandomOption(8, AspectItems.ASPECT_OF_EMINENCE),
        RandomOption(40, AspectItems.ASPECT_OF_ANCESTORS),
        RandomOption(70, AspectItems.ASPECT_OF_DUALITY),
        //RandomOption(1, AspectItems.ASPECT_OF_THE_GROVE), doesn't drop default [AspectOfTheGroveService]
        RandomOption(80, AspectItems.ASPECT_OF_OUTLAWS),
        RandomOption(15, AspectItems.ASPECT_OF_KIN),
    )

    fun getBossBaseCrystalCount(dungeonLevel: Int) = dungeonLevel / 7.0 + Random.nextDouble(dungeonLevel / 5.0)

    val CRYSTALS = listOf(
        LevelRestrictedRandomOption(20, tier = 0, requiredLevel = 0, CrystalItems.CALIBRATION_CRYSTAL),
        LevelRestrictedRandomOption(10, tier = 0, requiredLevel = 2, CrystalItems.PERMUTATION_CRYSTAL),
        LevelRestrictedRandomOption(5, tier = 0, requiredLevel = 4, CrystalItems.REFORGE_CRYSTAL),
        LevelRestrictedRandomOption(3, tier = 0, requiredLevel = 6, CrystalItems.CORRUPTION_CRYSTAL),
        LevelRestrictedRandomOption(2, tier = 0, requiredLevel = 8, CrystalItems.SACRIFICIAL_CRYSTAL),
        //LevelRestrictedRandomOption(1, tier = 0, requiredLevel = 10, CrystalItems.IMITATION_CRYSTAL), drops from beastweaver boss
    )

    // unaffected by increased loot
    val BOSS_UNIQUES = mapOf(
        CustomEntities.BEAKBURN to BossUniqueItemDrops(
            probability = 0.05,
            RandomOption(1, Pair(CustomArmorItems.EMBERREIGN, 1)),
        ),
        CustomEntities.BONECRUSHER to BossUniqueItemDrops(
            probability = 0.05,
            RandomOption(1, Pair(CustomToolItems.GRAVEBREAKER, 1)),
        ),
        CustomEntities.ELF_DUELIST to BossUniqueItemDrops(
            probability = 0.05,
            RandomOption(1, Pair(CustomArmorItems.WINDSTRIDER, 1)),
        ),
        CustomEntities.ARACHNE to BossUniqueItemDrops(
            probability = 0.05,
            RandomOption(1, Pair(CustomArmorItems.BROODMOTHER, 1)),
        ),
        CustomEntities.BEASTWEAVER to BossUniqueItemDrops(
            probability = 1.0,
            RandomOption(3, Pair(CustomArmorItems.CROWN_OF_THE_STAG, 1)),
            RandomOption(1, Pair(CustomArmorItems.SKIN_OF_THE_RHINO, 1)),
            RandomOption(1, Pair(CustomToolItems.CLAWS_OF_THE_BEAR, 2)),
            RandomOption(1, Pair(CustomMiscItems.HOWL_OF_THE_WOLF, 1))
        ),
    )

    data class BossUniqueItemDrops(
        val probability: Double,
        val items: List<RandomOption<Pair<Item, Int>>>,
    ) {
        constructor(
            probability: Double,
            vararg items: RandomOption<Pair<Item, Int>>,
        ) : this(probability, items.toList())
    }

    fun getBanditCrystalCount(dungeonLevel: Int): Int {
        val exactBonus = dungeonLevel / 15.0
        val guaranteedBonus = exactBonus.toInt()
        val fractionalChance = exactBonus - guaranteedBonus

        return 1 + guaranteedBonus +
                if (Random.nextDouble() < fractionalChance) 1 else 0
    }
}