package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver_wolf

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.wolf.Wolf
import net.minecraft.world.level.Level

class BeastweaverWolfEntity(
    type: EntityType<out BeastweaverWolfEntity>,
    level: Level,
) : Wolf(type, level) {
}