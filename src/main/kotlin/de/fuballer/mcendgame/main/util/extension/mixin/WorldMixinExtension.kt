package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.DungeonWorldAccessor
import de.fuballer.mcendgame.main.accessor.WorldAttributesAccessor
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.world.VanillaTypeWorldAttributeInstance
import de.fuballer.mcendgame.main.component.world.WorldAttributeAction
import de.fuballer.mcendgame.main.messaging.dungeon.WorldAttributeChangedEvent
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.core.GlobalPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.function.Predicate

object WorldMixinExtension {
    fun ServerLevel.setTrainingDungeon() {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setTraining`()
    }

    fun ServerLevel.isTrainingDungeon(): Boolean {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$isTraining`()
    }

    fun ServerLevel.setDungeonCompleted(completed: Boolean = true) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setCompleted`(completed)
    }

    fun ServerLevel.isDungeonCompleted(): Boolean {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$isCompleted`()
    }

    fun ServerLevel.setDungeonSeed(dungeonSeed: Long) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setDungeonSeed`(dungeonSeed)
    }

    fun ServerLevel.getDungeonSeed(): Long {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getDungeonSeed`()
    }

    fun ServerLevel.setDungeonLevel(dungeonLevel: Int) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setLevel`(dungeonLevel)
    }

    fun ServerLevel.getDungeonLevel(): Int {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getLevel`()
    }

    fun ServerLevel.setCreationTime(time: Long) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setCreationTime`(time)
    }

    fun ServerLevel.getCreationTime(): Long {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getCreationTime`()
    }

    fun ServerLevel.setTotalBossCount(count: Int) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setTotalBossCount`(count)
    }

    fun ServerLevel.getTotalBossCount(): Int {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getTotalBossCount`()
    }

    fun ServerLevel.increaseBossesKilled() {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$increaseBossesKilled`()
    }

    fun ServerLevel.getBossesKilled(): Int {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getBossesKilled`()
    }

    fun ServerLevel.setOpener(opener: Player) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setOpener`(opener)
    }

    fun ServerLevel.getOpener(): Player {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getOpener`()
    }

    fun ServerLevel.setDungeonAspects(aspects: Map<AspectItem, Int>) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setAspects`(aspects)
    }

    fun ServerLevel.getDungeonAspects(): Map<AspectItem, Int> {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getAspects`()
    }

    fun ServerLevel.setDungeonType(type: DungeonType) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setDungeonType`(type)
    }

    fun ServerLevel.getDungeonType(): DungeonType {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getDungeonType`()
    }

    fun ServerLevel.setDungeonExitPos(pos: GlobalPos) {
        val accessor = this as DungeonWorldAccessor
        accessor.`mcendgame$setDungeonExitPos`(pos)
    }

    fun ServerLevel.getDungeonExitPos(): GlobalPos {
        val accessor = this as DungeonWorldAccessor
        return accessor.`mcendgame$getDungeonExitPos`()
    }

    fun ServerLevel.getAttributeUpdateCount(): Int {
        val accessor = this as WorldAttributesAccessor
        return accessor.`mcendgame$getAttributeUpdateCount`()
    }

    fun ServerLevel.addCustomAttribute(attribute: CustomAttribute, applies: Predicate<LivingEntity> = { true }) {
        val accessor = this as WorldAttributesAccessor
        accessor.`mcendgame$addCustomAttribute`(attribute, applies)

        EventGateway.publish(WorldAttributeChangedEvent(this, attribute, WorldAttributeAction.ADD))
    }

    fun ServerLevel.removeCustomAttribute(attribute: CustomAttribute, applies: Predicate<LivingEntity> = { true }) {
        val accessor = this as WorldAttributesAccessor
        accessor.`mcendgame$removeCustomAttribute`(attribute, applies)

        EventGateway.publish(WorldAttributeChangedEvent(this, attribute, WorldAttributeAction.REMOVE))
    }

    fun ServerLevel.getCustomTypeAttributes(entity: LivingEntity): List<CustomAttribute> {
        val accessor = this as WorldAttributesAccessor
        return accessor.`mcendgame$getCustomTypeAttributes`(entity)
    }

    fun ServerLevel.getVanillaTypeAttributesHistory(entity: LivingEntity): List<VanillaTypeWorldAttributeInstance> {
        val accessor = this as WorldAttributesAccessor
        return accessor.`mcendgame$getVanillaTypeAttributesHistory`(entity)
    }
}