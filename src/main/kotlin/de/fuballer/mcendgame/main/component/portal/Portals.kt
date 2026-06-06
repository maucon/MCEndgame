package de.fuballer.mcendgame.main.component.portal

import de.fuballer.mcendgame.main.component.portal.teleport.TeleportLocation
import de.fuballer.mcendgame.main.component.portal.type.DefaultPortalType
import de.fuballer.mcendgame.main.component.portal.type.PortalType
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.animal.equine.AbstractHorse.createLivingAttributes
import net.minecraft.world.phys.Vec3

@Injectable
object Portals {
    val ENTITY_TYPE = RegistryUtil.registerEntity(
        "portal",
        EntityType.Builder.of(::PortalEntity, MobCategory.MISC)
            .sized(PortalSettings.DEFAULT_HITBOX_WIDTH, PortalSettings.DEFAULT_HITBOX_HEIGHT)
    )

    @Initializer
    fun register() {
        FabricDefaultAttributeRegistry.register(ENTITY_TYPE, createLivingAttributes())
    }

    fun spawn(
        world: ServerLevel,
        pos: Vec3,
        teleportLocation: TeleportLocation,
        lookAt: Vec3 = Vec3.ZERO,
        rotation: Float? = null,
        singleUse: Boolean = false,
        type: PortalType = DefaultPortalType()
    ): PortalEntity {
        val consumer = { entity: PortalEntity ->
            entity.entityData.set(PortalEntity.TYPE, type.getId())
            entity.setPos(pos)
            entity.lookAt(EntityAnchorArgument.Anchor.FEET, lookAt)
            rotation?.let { entity.forceSetRotation(it, false, 0f, false) }
            entity.singleUse = singleUse
            entity.type = type
            entity.teleportLocation = teleportLocation
        }
        return ENTITY_TYPE.spawn(world, consumer, BlockPos.containing(pos.x, pos.y, pos.z), EntitySpawnReason.LOAD, false, false)!!
    }
}