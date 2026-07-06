package de.fuballer.mcendgame.main.component.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.component.portal.type.DefaultPortalType
import de.fuballer.mcendgame.main.component.portal.type.PortalType
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.command.argument.EntityAnchorArgumentType
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.passive.AbstractHorseEntity.createLivingAttributes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

@Injectable
object Portals {
    val ENTITY_TYPE = RegistryUtil.registerEntity(
        "portal",
        EntityType.Builder.create(::PortalEntity, SpawnGroup.MISC)
            .dimensions(PortalSettings.DEFAULT_HITBOX_WIDTH, PortalSettings.DEFAULT_HITBOX_HEIGHT)
    )

    @Initializer
    fun register() {
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createLivingAttributes())
    }

    fun spawn(
        world: ServerWorld,
        pos: Vec3d,
        teleportLocation: TeleportLocation,
        lookAt: Vec3d = Vec3d.ZERO,
        rotation: Float? = null,
        singleUse: Boolean = false,
        type: PortalType = DefaultPortalType()
    ): PortalEntity {
        val consumer = { entity: PortalEntity ->
            entity.dataTracker.set(PortalEntity.TYPE, type.getId())
            entity.setPosition(pos)
            entity.lookAt(EntityAnchorArgumentType.EntityAnchor.FEET, lookAt)
            rotation?.let { entity.rotate(it, false, 0f, false) }
            entity.singleUse = singleUse
            entity.type = type
            entity.teleportLocation = teleportLocation
        }
        return ENTITY_TYPE.spawn(world, consumer, BlockPos.ofFloored(pos.x, pos.y, pos.z), SpawnReason.NATURAL, false, false)!!
    }
}