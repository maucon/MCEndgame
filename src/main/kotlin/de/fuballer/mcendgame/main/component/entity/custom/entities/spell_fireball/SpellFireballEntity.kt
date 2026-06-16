package de.fuballer.mcendgame.main.component.entity.custom.entities.spell_fireball

import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.level.Level

class SpellFireballEntity(
    type: EntityType<out Projectile>,
    level: Level,
) : Projectile(type, level) {
    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
    }
}