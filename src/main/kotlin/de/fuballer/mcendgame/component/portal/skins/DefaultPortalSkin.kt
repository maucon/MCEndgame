package de.fuballer.mcendgame.component.portal.skins

import de.fuballer.mcendgame.util.SchedulingUtil
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class DefaultPortalSkin : PortalSkin {
    private var taskId = -1
    private lateinit var particleCoordinates: List<Vector>
    private lateinit var world: World

    override fun prepare(location: Location) {
        world = location.world!!

        val width = 0.75 / 2
        val height = 1.25 / 2
        val particles = 12
        val increment = (2 * Math.PI) / particles

        particleCoordinates = (0 until particles)
            .map {
                val angle = it * increment
                val offset = Vector(width * cos(angle), height * sin(angle) + 0.75, 0.0)

                offset.rotateAroundY(-Math.toRadians(location.yaw.toDouble()) )

                Vector(location.x + offset.x, location.y + offset.y, location.z + offset.z)
            }
    }

    override fun play() {
        taskId = SchedulingUtil.scheduleSyncRepeatingTask(0, 1) {
            particleCoordinates.forEach {
                world.spawnParticle(
                    Particle.PORTAL,
                    it.x, it.y, it.z,
                    1,
                    0.0, 0.1, 0.0,
                    0.1
                )
            }
        }
    }

    override fun cancel() {
        SchedulingUtil.cancelTask(taskId)
    }
}