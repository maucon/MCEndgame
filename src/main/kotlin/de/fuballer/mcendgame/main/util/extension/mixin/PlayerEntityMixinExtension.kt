package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonLevelAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonSeedAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityInsideDungeonAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityMixinAccessor
import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import de.fuballer.mcendgame.main.component.dungeon.seed.PlayerDungeonSeed
import net.minecraft.world.entity.player.Player

object PlayerEntityMixinExtension {
    fun Player.getDungeonLevel(): PlayerDungeonLevel {
        val accessor = this as PlayerEntityDungeonLevelAccessor
        return accessor.`mcendgame$getDungeonLevel`()
    }

    fun Player.setDungeonLevel(dungeonLevel: PlayerDungeonLevel) {
        val accessor = this as PlayerEntityDungeonLevelAccessor
        return accessor.`mcendgame$setDungeonLevel`(dungeonLevel)
    }

    fun Player.getDungeonSeed(): PlayerDungeonSeed? {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$getDungeonSeed`()
    }

    fun Player.setDungeonSeed(dungeonSeed: PlayerDungeonSeed?) {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$setDungeonSeed`(dungeonSeed)
    }

    fun Player.clearDungeonSeed() {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$setDungeonSeed`(null)
    }

    fun Player.getAttackCooldownMultiplier(): Float {
        val accessor = this as PlayerEntityMixinAccessor
        return accessor.`mcendgame$getLastAttackCharge`()
    }

    fun Player.wasLastAttackCritical(): Boolean {
        val accessor = this as PlayerEntityMixinAccessor
        return accessor.`mcendgame$getLastAttackWasCritical`()
    }

    fun Player.isInsideDungeon(): Boolean {
        val accessor = this as PlayerEntityInsideDungeonAccessor
        return accessor.`mcendgame$isInsideDungeon`()
    }

    fun Player.setInsideDungeon(insideDungeon: Boolean) {
        val accessor = this as PlayerEntityInsideDungeonAccessor
        return accessor.`mcendgame$setInsideDungeon`(insideDungeon)
    }
}