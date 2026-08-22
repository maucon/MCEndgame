package de.fuballer.mcendgame.test.main

import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Blocks
import org.slf4j.LoggerFactory

class MCEndgameGameTest {
    companion object {
        private val LOG = LoggerFactory.getLogger(this::class.java.simpleName)
    }

    @GameTest
    fun test(helper: GameTestHelper) {
        helper.assertBlockPresent(Blocks.AIR, 0, 0, 0)

        helper.succeed()
    }

    @GameTest
    fun testSpawnEntity(helper: GameTestHelper) {
        for (entityType in BuiltInRegistries.ENTITY_TYPE) {
            val id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
            if (id == Identifier.withDefaultNamespace("player")) continue

            LOG.info("Spawning {}", id)
            helper.spawn(entityType, 0, 0, 0)
        }

        helper.succeed()
    }

    @GameTest
    fun testDungeonOpenAndJoin(helper: GameTestHelper) {
        LOG.info("Running dungeon open & join flow")
        DungeonOpenAndJoinGameTest.testPlayerOpensAndJoinsDungeon(helper)
        LOG.info("Dungeon open & join flow finished successfully")
    }
}