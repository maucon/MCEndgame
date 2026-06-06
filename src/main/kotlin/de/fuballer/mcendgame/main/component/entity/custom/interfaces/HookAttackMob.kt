package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import de.fuballer.mcendgame.main.component.entity.custom.networking.EntityHookEntityPayload
import de.fuballer.mcendgame.main.util.extension.EntityExtension.setAndSyncVelocity
import net.fabricmc.fabric.api.networking.v1.PlayerLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import java.util.*

interface HookAttackMob {
    val hooker: Entity

    val hookPullCount: Int
    val hookPullInterval: Int
    val hookPullStrength: Double
    val hookPullAdditionalY: Double
    val hookedEntityIds: MutableList<Int> // client-side
    val hookedEntityUuidMap: MutableMap<UUID, Pair<Int, Int>> // server-side, Pair<PullCount, PullCooldown>

    fun shootHookAt(target: LivingEntity)

    fun addHookedEntity(hookedUuid: UUID) {
        if (hookedEntityUuidMap.containsKey(hookedUuid)) {
            hookedEntityUuidMap[hookedUuid] = Pair(0, hookedEntityUuidMap[hookedUuid]?.second ?: 0)
            return
        }
        hookedEntityUuidMap[hookedUuid] = Pair(0, 0)

        val serverWorld = hooker.level() as? ServerLevel ?: return

        val hookedEntity = serverWorld.getEntity(hookedUuid) ?: return
        for (player in PlayerLookup.tracking(serverWorld, hookedEntity.blockPosition())) {
            ServerPlayNetworking.send(player, EntityHookEntityPayload(hooker.id, hookedEntity.id, false))
        }
    }

    fun removeHookedEntity(hookedUuid: UUID) {
        hookedEntityUuidMap.remove(hookedUuid)

        val serverWorld = hooker.level() as? ServerLevel ?: return
        val hookedEntity = serverWorld.getEntity(hookedUuid) ?: return

        for (player in PlayerLookup.tracking(serverWorld, hookedEntity.blockPosition())) {
            ServerPlayNetworking.send(player, EntityHookEntityPayload(hooker.id, hookedEntity.id, true))
        }
    }

    fun addHookedEntity(hookedId: Int) {
        hookedEntityIds.add(hookedId)
    }

    fun removeHookedEntity(hookedId: Int) {
        hookedEntityIds.remove(hookedId)
    }

    fun tickHooks() {
        val world = hooker.level() as? ServerLevel ?: return
        updateHookedEntities(world)
    }

    fun updateHookedEntities(
        world: ServerLevel
    ) {
        val toRemove = mutableListOf<UUID>()

        for (hookedUuid in hookedEntityUuidMap.keys) {
            val hookedEntity = world.getEntity(hookedUuid)
            if (hookedEntity == null || !hookedEntity.isAlive) {
                toRemove.add(hookedUuid)
                continue
            }

            var (pullCount, pullCooldown) = hookedEntityUuidMap[hookedUuid] ?: Pair(0, 0)
            if (++pullCooldown >= hookPullInterval) {
                pullCooldown = 0

                if (pullCount++ > hookPullCount) {
                    toRemove.add(hookedUuid)
                    continue
                }

                pullHookedEntity(hookedEntity)
            }

            hookedEntityUuidMap[hookedUuid] = Pair(pullCount, pullCooldown)
        }

        for (entityUuid in toRemove) {
            removeHookedEntity(entityUuid)
        }
    }

    fun pullHookedEntity(
        hooked: Entity
    ) {
        val direction = hooker.position().subtract(hooked.position())
        if (direction.lengthSqr() < 1e-6) return
        val normalizedDirection = direction.normalize()

        val baseVelocity = normalizedDirection.scale(hookPullStrength)
        val finalVelocity = baseVelocity.add(0.0, hookPullAdditionalY, 0.0)

        hooked.setAndSyncVelocity(finalVelocity)
    }
}