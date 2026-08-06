package de.fuballer.mcendgame.main

import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.entity.EntityTypes
import net.minecraft.world.level.block.Blocks

class MCEndgameGameTest {
    @GameTest
    fun test(helper: GameTestHelper) {
        helper.assertBlockPresent(Blocks.AIR, 0, 0, 0)

        helper.succeed()
    }

    @GameTest
    fun test2(helper: GameTestHelper) {
        helper.spawn(EntityTypes.GOAT, 0F, 0F, 0F)

        helper.succeed()
    }
}