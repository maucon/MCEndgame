package de.fuballer.mcendgame.component.dungeon.type

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.bogged.BoggedEntityType
import de.fuballer.mcendgame.component.custom_entity.types.buff_cow.BuffCowEntityType
import de.fuballer.mcendgame.component.custom_entity.types.cerberus.CerberusEntityType
import de.fuballer.mcendgame.component.custom_entity.types.demonic_golem.DemonicGolemEntityType
import de.fuballer.mcendgame.component.custom_entity.types.elf_duelist.ElfDuelistEntityType
import de.fuballer.mcendgame.component.custom_entity.types.husk.HuskEntityType
import de.fuballer.mcendgame.component.custom_entity.types.melee_skeleton.MeleeSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.minotaur.MinotaurEntityType
import de.fuballer.mcendgame.component.custom_entity.types.necromancer.NecromancerEntityType
import de.fuballer.mcendgame.component.custom_entity.types.piglin_brute.PiglinBruteEntityType
import de.fuballer.mcendgame.component.custom_entity.types.reaper.ReaperEntityType
import de.fuballer.mcendgame.component.custom_entity.types.skeleton.SkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.stray.StrayEntityType
import de.fuballer.mcendgame.component.custom_entity.types.wendigo.WendigoEntityType
import de.fuballer.mcendgame.component.custom_entity.types.witch.WitchEntityType
import de.fuballer.mcendgame.component.custom_entity.types.wither_skeleton.WitherSkeletonEntityType
import de.fuballer.mcendgame.component.custom_entity.types.zombie.ZombieEntityType
import de.fuballer.mcendgame.component.dungeon.generation.DungeonMapType
import de.fuballer.mcendgame.component.dungeon.type.data.RolledDungeonType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import kotlin.random.Random

enum class DungeonType(
    private val mapTypes: List<RandomOption<DungeonMapType>>,
    private val entityTypes: List<RandomOption<CustomEntityType>>,
    private val bossEntityTypes: List<RandomOption<CustomEntityType>>
) {
    STRONGHOLD(
        listOf(
            RandomOption(1, DungeonMapType.STRONGHOLD),
        ),
        listOf(
            RandomOption(50, ZombieEntityType),
            RandomOption(25, SkeletonEntityType),
            RandomOption(30, HuskEntityType),
            RandomOption(20, StrayEntityType),
            RandomOption(20, MeleeSkeletonEntityType),
            RandomOption(20, PiglinBruteEntityType),
            RandomOption(10, WitherSkeletonEntityType),
            RandomOption(7, BoggedEntityType),
            RandomOption(5, WitchEntityType),
        ),
        listOf(
            RandomOption(100, ElfDuelistEntityType),
            RandomOption(100, ReaperEntityType),
            RandomOption(100, NecromancerEntityType),
            RandomOption(100, DemonicGolemEntityType),
            RandomOption(100, MinotaurEntityType),
            RandomOption(100, CerberusEntityType),
            RandomOption(100, WendigoEntityType),
            RandomOption(1, BuffCowEntityType),
        )
    );

    fun roll(random: Random): RolledDungeonType {
        require(bossEntityTypes.size >= 3) { "there must be at least 3 boss entity types" }

        return RolledDungeonType(
            RandomUtil.pick(mapTypes, random).option,
            entityTypes,
            RandomUtil.pick(bossEntityTypes, random, 3)
        )
    }
}