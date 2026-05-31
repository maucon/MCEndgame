package de.fuballer.mcendgame.main.util.extension.mixin

import de.fuballer.mcendgame.main.accessor.*
import de.fuballer.mcendgame.main.component.custom_attribute.effects.data.AuraStatusEffect
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import de.fuballer.mcendgame.main.mixin.living_entity.LivingEntityAccessor
import de.fuballer.mcendgame.main.mixin.living_entity.LivingEntityLastDamageTimeAccessorMixin
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3i
import java.util.*

object EntityMixinExtension {
    fun LivingEntity.addTemporaryAttributeModifier(
        type: RegistryEntry<EntityAttribute?>?,
        identifier: Identifier?,
        ticks: Int,
        value: Double,
        operation: EntityAttributeModifier.Operation?
    ) {
        val accessor = this as LivingEntityTemporaryAttributeModifierAccessor
        accessor.`mcendgame$addTemporaryAttributeModifier`(type, identifier, ticks, value, operation)
    }

    fun LivingEntity.updateDodged() {
        val accessor = this as LivingEntityDodgedRecentlyAccessor
        return accessor.`mcendgame$updateDodge`()
    }

    fun LivingEntity.hasDodged(ticks: Int): Boolean {
        val accessor = this as LivingEntityDodgedRecentlyAccessor
        return accessor.`mcendgame$hasDodged`(ticks)
    }

    fun LivingEntity.setDungeonEnemy(enemy: Boolean = true) {
        val accessor = this as LivingEntityDungeonEnemyAccessor
        return accessor.`mcendgame$setDungeonEnemy`(enemy)
    }

    fun LivingEntity.isDungeonEnemy(): Boolean {
        val enemyAccessor = this as LivingEntityDungeonEnemyAccessor
        return enemyAccessor.`mcendgame$isDungeonEnemy`()
    }

    fun LivingEntity.setDropsAspectOfGhosts(drops: Boolean = true) {
        val accessor = this as LivingEntityDungeonEnemyAccessor
        return accessor.`mcendgame$setDropsAspectOfGhosts`(drops)
    }

    fun LivingEntity.dropsAspectOfGhosts(): Boolean {
        val enemyAccessor = this as LivingEntityDungeonEnemyAccessor
        return enemyAccessor.`mcendgame$dropsAspectOfGhosts`()
    }

    fun LivingEntity.setDungeonBoss(dungeonBoss: Boolean = true) {
        val accessor = this as MobEntityDungeonBossAccessor
        return accessor.`mcendgame$setDungeonBoss`(dungeonBoss)
    }

    fun LivingEntity.isDungeonBoss(): Boolean {
        val bossAccessor = this as? MobEntityDungeonBossAccessor ?: return false
        return bossAccessor.`mcendgame$isDungeonBoss`()
    }

    fun LivingEntity.setCompanion() {
        val accessor = this as LivingEntityCompanionAccessor
        accessor.`mcendgame$setCompanion`()
    }

    fun LivingEntity.isCompanion(): Boolean {
        val accessor = this as LivingEntityCompanionAccessor
        return accessor.`mcendgame$isCompanion`()
    }

    fun LivingEntity.addEnemyAuraStatusEffect(effect: AuraStatusEffect) {
        val accessor = this as LivingEntityAuraAccessor
        accessor.`mcendgame$addEnemyAuraStatusEffect`(effect)
    }

    fun LivingEntity.addAllyAuraStatusEffect(effect: AuraStatusEffect) {
        val accessor = this as LivingEntityAuraAccessor
        accessor.`mcendgame$addAllyAuraStatusEffect`(effect)
    }

    fun LivingEntity.setVisualFire(visualFire: Boolean = true) {
        val accessor = this as LivingEntityVisualFireAccessor
        return accessor.`mcendgame$setVisualFire`(visualFire)
    }

    fun LivingEntity.hasVisualFire(): Boolean {
        val accessor = this as LivingEntityVisualFireAccessor
        return accessor.`mcendgame$hasVisualFire`()
    }

    fun LivingEntity.setWebbed(webbed: Boolean = true) {
        val accessor = this as LivingEntityWebbedAccessor
        return accessor.`mcendgame$setWebbed`(webbed)
    }

    fun LivingEntity.isWebbed(): Boolean {
        val accessor = this as LivingEntityWebbedAccessor
        return accessor.`mcendgame$isWebbed`()
    }

    fun LivingEntity.setElite(elite: Boolean = true) {
        val accessor = this as LivingEntityEliteAccessor
        return accessor.`mcendgame$setElite`(elite)
    }

    fun LivingEntity.isElite(): Boolean {
        val accessor = this as LivingEntityEliteAccessor
        return accessor.`mcendgame$isElite`()
    }

    fun LivingEntity.setLootGoblin(lootGoblin: Boolean = true) {
        val accessor = this as LivingEntityLootGoblinAccessor
        return accessor.`mcendgame$setLootGoblin`(lootGoblin)
    }

    fun LivingEntity.isLootGoblin(): Boolean {
        val accessor = this as LivingEntityLootGoblinAccessor
        return accessor.`mcendgame$isLootGoblin`()
    }

    fun LivingEntity.getLastDamageTime(): Long {
        val accessor = this as LivingEntityLastDamageTimeAccessorMixin
        return accessor.lastDamageTime
    }

    fun LivingEntity.setDungeonBossSpawnPosition(spawnPosition: SpawnPosition) {
        val accessor = this as MobEntityDungeonBossAccessor
        return accessor.`mcendgame$setSpawnPosition`(spawnPosition)
    }

    fun LivingEntity.getDungeonBossSpawnPosition(): SpawnPosition {
        val accessor = this as MobEntityDungeonBossAccessor
        return accessor.`mcendgame$getSpawnPosition`() ?: SpawnPosition(Vec3i.ZERO)
    }

    fun Entity.setForcedGlowColor(color: Int) {
        val accessor = this as EntityForcedGlowColorAccessor
        accessor.`mcendgame$setForcedGlowColor`(color)
    }

    fun LivingEntity.getLinkedBy(): HashSet<UUID> = (this as LivingEntityLinkAttributeAccessor).`mcendgame$getLinkedBy`()

    fun LivingEntity.getLinkedEntities(): Map<UUID, Long> = (this as LivingEntityLinkAttributeAccessor).`mcendgame$getLinkedEntities`()

    fun LivingEntity.resetWorldAttributesUpdate() {
        val accessor = this as LivingEntityWorldAttributesAccessor
        accessor.`mcendgame$resetWorldAttributesUpdate`()
    }

    fun LivingEntity.getHitbox(): Box = (this as LivingEntityAccessor).`mcendgame$invokeGetHitbox`()
}
