package de.fuballer.mcendgame.main.component.dungeon.type

import de.fuballer.mcendgame.main.component.biome.CustomBiomes
import de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect.PotionEffect
import de.fuballer.mcendgame.main.component.dungeon.generation.layout.DungeonLayoutType
import de.fuballer.mcendgame.main.component.dungeon.type.data.RolledDungeonType
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.component.entity.types.*
import de.fuballer.mcendgame.main.component.entity.types.boss.ArachneBossStats
import de.fuballer.mcendgame.main.component.entity.types.boss.BeakburnBossStats
import de.fuballer.mcendgame.main.component.entity.types.boss.BonecrusherBossStats
import de.fuballer.mcendgame.main.component.entity.types.boss.ElfDuelistBossStats
import de.fuballer.mcendgame.main.util.random.RandomOption
import de.fuballer.mcendgame.main.util.random.RandomUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import kotlin.random.Random

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonLayoutType>>,
    private val entityTypes: List<RandomOption<EntityTypeStats>>,
    private val bossEntityTypes: List<RandomOption<EntityTypeStats>>,
    val enemyCount: Int,
    val bossCount: Int,
    val biome: ResourceKey<Biome>,
    val applyMisc: (List<LivingEntity>) -> Unit = {},
) { // Note: dungeon seed uses ordinal to save dungeon type
    STRONGHOLD(
        listOf(
            RandomOption(1, DungeonLayoutType.STRONGHOLD),
        ),
        listOf(
            RandomOption(45, ZombieStats),
            RandomOption(18, HuskStats),
            RandomOption(15, SkeletonStats),
            RandomOption(18, MeleeSkeletonStats),
            RandomOption(8, StrayStats),
            RandomOption(8, BoggedStats),
            RandomOption(8, ParchedStats),
            RandomOption(3, WitherSkeletonStats),
        ),
        listOf(
            RandomOption(1, ArachneBossStats),
            RandomOption(1, BonecrusherBossStats),
            RandomOption(1, ElfDuelistBossStats),
            RandomOption(1, BeakburnBossStats),
        ),
        enemyCount = 125,
        bossCount = 3,
        biome = Biomes.PLAINS,
    ),
    NETHER(
        listOf(
            RandomOption(1, DungeonLayoutType.NETHER),
        ),
        listOf(
            RandomOption(30, ZombieStats),
            RandomOption(25, HuskStats),
            RandomOption(15, SkeletonStats),
            RandomOption(8, BoggedStats),
            RandomOption(15, ParchedStats),
            RandomOption(20, MeleeSkeletonStats),
            RandomOption(5, WitherSkeletonStats),
        ),
        listOf(
            RandomOption(1, ArachneBossStats),
            RandomOption(1, BonecrusherBossStats),
            RandomOption(1, ElfDuelistBossStats),
            RandomOption(1, BeakburnBossStats),
        ),
        enemyCount = 125,
        bossCount = 3,
        biome = Biomes.NETHER_WASTES,
        { enemies -> enemies.forEach { it.addEffect(PotionEffect.FIRE_RESISTANCE.getEffectInstance(false)) } },
    ),
    DESERT(
        listOf(
            RandomOption(1, DungeonLayoutType.DESERT),
        ),
        listOf(
            RandomOption(25, ZombieStats),
            RandomOption(30, HuskStats),
            RandomOption(10, SkeletonStats),
            RandomOption(8, BoggedStats),
            RandomOption(20, ParchedStats),
            RandomOption(20, MeleeSkeletonStats),
            RandomOption(5, WitherSkeletonStats),
        ),
        listOf(
            RandomOption(1, ArachneBossStats),
            RandomOption(1, BonecrusherBossStats),
            RandomOption(1, ElfDuelistBossStats),
            RandomOption(1, BeakburnBossStats),
        ),
        enemyCount = 125,
        bossCount = 3,
        biome = CustomBiomes.DESERT_DUNGEON,
    ),
    TRAINING(
        listOf(
            RandomOption(1, DungeonLayoutType.TRAINING),
        ),
        listOf(),
        listOf(),
        enemyCount = 0,
        bossCount = 0,
        biome = Biomes.PLAINS,
    );

    fun roll(random: Random): RolledDungeonType =
        RolledDungeonType(
            RandomUtil.pickOne(mapTypes, random).option,
            entityTypes,
            RandomUtil.pickRepeatIfNeeded(bossEntityTypes, random, bossCount),
            applyMisc,
        )

    fun getEntityTypes() = entityTypes.toMutableList()
}