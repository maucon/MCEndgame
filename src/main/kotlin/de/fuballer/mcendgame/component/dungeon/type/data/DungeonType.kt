package de.fuballer.mcendgame.component.dungeon.type.data

import de.fuballer.mcendgame.component.dungeon.boss.data.BossType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
    private val bossType: List<RandomOption<BossType>>
) {
    HELL(
        listOf(
            RandomOption(1, DungeonMapType.MINE),
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(2, CustomEntityType.WITHER_SKELETON),
            RandomOption(5, CustomEntityType.SUCCUBUS),
            RandomOption(5, CustomEntityType.INCUBUS),
        ),
        listOf(RandomOption(1, BossType.CERBERUS))
    ),
    UNDEAD(
        listOf(
            RandomOption(1, DungeonMapType.MINE),
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(2, CustomEntityType.WITHER_SKELETON),
            RandomOption(5, CustomEntityType.ZOMBIE),
            RandomOption(5, CustomEntityType.SKELETON),
            RandomOption(5, CustomEntityType.STRAY),
            RandomOption(5, CustomEntityType.NECROMANCER),
            RandomOption(5, CustomEntityType.HUSK),
            RandomOption(5, CustomEntityType.REAPER)
        ),
        listOf(RandomOption(1, BossType.CERBERUS))
    ),
    MONSTER(
        listOf(
            RandomOption(1, DungeonMapType.CATACOMBS),
            RandomOption(1, DungeonMapType.CATACOMBS_ALTERNATIVE)
        ),
        listOf(
            RandomOption(2, CustomEntityType.CREEPER),
            RandomOption(5, CustomEntityType.STALKER)
        ),
        listOf(RandomOption(1, BossType.DEMONIC_GOLEM))
    ),
    MYTHICAL(
        listOf(
            RandomOption(1, DungeonMapType.ICE_CAVE),
            RandomOption(1, DungeonMapType.LUSH_CAVE)
        ),
        listOf(
            RandomOption(2, CustomEntityType.CYCLOPS),
            RandomOption(5, CustomEntityType.WITCH),
            RandomOption(5, CustomEntityType.HARPY),
            RandomOption(5, CustomEntityType.NAGA)
        ),
        listOf(RandomOption(1, BossType.MINOTAUR))
    );

    fun roll() = RolledDungeonType(
        RandomUtil.pick(mapTypes).option,
        entityTypes,
        RandomUtil.pick(bossType).option
    )
}
