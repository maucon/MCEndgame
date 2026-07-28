package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver_wolf

import de.fuballer.mcendgame.main.component.entity.custom.CustomEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.level.Level

class BeastweaverWolfEntity(
    type: EntityType<out BeastweaverWolfEntity>,
    level: Level,
) : Wolf(type, level) {
    constructor(level: Level) : this(CustomEntities.BEASTWEAVER_WOLF, level)

    override fun baseTick() {
        super.baseTick()

        val serverLevel = level() as? ServerLevel ?: return
        if (owner?.isAlive != true) kill(serverLevel)
    }

    override fun isInvulnerableTo(level: ServerLevel, source: DamageSource): Boolean {
        if (source.entity == owner) return true
        return super.isInvulnerableTo(level, source)
    }
}