package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine

import kotlin.random.Random

data class BeastweaverVineSwayData(
    val speedX: Double = Random.nextDouble(0.06, 0.1),
    val speedY: Double = Random.nextDouble(0.06, 0.1),
    val timeOffsetX: Double = Random.nextDouble(0.0, 2 * Math.PI),
    val timeOffsetY: Double = Random.nextDouble(0.0, 2 * Math.PI),
    val strengthX: Double = Random.nextDouble(3.5, 5.5),
    val strengthY: Double = Random.nextDouble(3.5, 5.5),
)