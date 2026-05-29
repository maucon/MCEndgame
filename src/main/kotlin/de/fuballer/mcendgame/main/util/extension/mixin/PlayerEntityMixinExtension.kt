package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonLevelAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonExitAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityDungeonSeedAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityInsideDungeonAccessor
import de.fuballer.mcendgame.main.accessor.PlayerEntityMixinAccessor
import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import de.fuballer.mcendgame.main.component.dungeon.seed.PlayerDungeonSeed
import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import net.minecraft.entity.player.PlayerEntity

object PlayerEntityMixinExtension {
    fun PlayerEntity.getDungeonLevel(): PlayerDungeonLevel {
        val accessor = this as PlayerEntityDungeonLevelAccessor
        return accessor.`mcendgame$getDungeonLevel`()
    }

    fun PlayerEntity.setDungeonLevel(dungeonLevel: PlayerDungeonLevel) {
        val accessor = this as PlayerEntityDungeonLevelAccessor
        return accessor.`mcendgame$setDungeonLevel`(dungeonLevel)
    }

    fun PlayerEntity.getDungeonSeed(): PlayerDungeonSeed? {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$getDungeonSeed`()
    }

    fun PlayerEntity.setDungeonSeed(dungeonSeed: PlayerDungeonSeed?) {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$setDungeonSeed`(dungeonSeed)
    }

    fun PlayerEntity.clearDungeonSeed() {
        val accessor = this as PlayerEntityDungeonSeedAccessor
        return accessor.`mcendgame$setDungeonSeed`(null)
    }

    fun PlayerEntity.getAttackCooldownMultiplier(): Float {
        val accessor = this as PlayerEntityMixinAccessor
        return accessor.`mcendgame$getLastAttackCharge`()
    }

    fun PlayerEntity.wasLastAttackCritical(): Boolean {
        val accessor = this as PlayerEntityMixinAccessor
        return accessor.`mcendgame$getLastAttackWasCritical`()
    }

    fun PlayerEntity.isInsideDungeon(): Boolean {
        val accessor = this as PlayerEntityInsideDungeonAccessor
        return accessor.`mcendgame$isInsideDungeon`()
    }

    fun PlayerEntity.setInsideDungeon(insideDungeon: Boolean) {
        val accessor = this as PlayerEntityInsideDungeonAccessor
        return accessor.`mcendgame$setInsideDungeon`(insideDungeon)
    }

    fun PlayerEntity.getDungeonExitLocation(): TeleportLocation? {
        val accessor = this as PlayerEntityDungeonExitAccessor
        return accessor.`mcendgame$getDungeonExitLocation`()
    }

    fun PlayerEntity.setDungeonExitLocation(location: TeleportLocation?) {
        val accessor = this as PlayerEntityDungeonExitAccessor
        return accessor.`mcendgame$setDungeonExitLocation`(location)
    }

    fun PlayerEntity.clearDungeonExitLocation() {
        val accessor = this as PlayerEntityDungeonExitAccessor
        return accessor.`mcendgame$setDungeonExitLocation`(null)
    }
}