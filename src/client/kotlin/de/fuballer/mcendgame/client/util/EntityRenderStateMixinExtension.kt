package de.fuballer.mcendgame.client.util

import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateAccessor
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateGhostlyAccessor
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateIsolatedAccessor
import de.fuballer.mcendgame.client.accessor.LivingEntityRenderStateWebbedAccessor
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState

object EntityRenderStateMixinExtension {
    fun LivingEntityRenderState.setWebbed(webbed: Boolean = true) {
        val accessor = this as LivingEntityRenderStateWebbedAccessor
        accessor.`mcendgame$setWebbed`(webbed)
    }

    fun LivingEntityRenderState.isWebbed(): Boolean {
        val accessor = this as LivingEntityRenderStateWebbedAccessor
        return accessor.`mcendgame$isWebbed`()
    }

    fun LivingEntityRenderState.setIsolated(isolated: Boolean = true) {
        val accessor = this as LivingEntityRenderStateIsolatedAccessor
        accessor.`mcendgame$setIsolated`(isolated)
    }

    fun LivingEntityRenderState.isIsolated(): Boolean {
        val accessor = this as LivingEntityRenderStateIsolatedAccessor
        return accessor.`mcendgame$isIsolated`()
    }

    fun LivingEntityRenderState.setHealth(health: Float) {
        val accessor = this as LivingEntityRenderStateAccessor
        accessor.`mcendgame$setHealth`(health)
    }

    fun LivingEntityRenderState.getHealth(): Float {
        val accessor = this as LivingEntityRenderStateAccessor
        return accessor.`mcendgame$getHealth`()
    }

    fun LivingEntityRenderState.setMaxHealth(maxHealth: Float) {
        val accessor = this as LivingEntityRenderStateAccessor
        accessor.`mcendgame$setMaxHealth`(maxHealth)
    }

    fun LivingEntityRenderState.getMaxHealth(): Float {
        val accessor = this as LivingEntityRenderStateAccessor
        return accessor.`mcendgame$getMaxHealth`()
    }

    fun LivingEntityRenderState.setLowHealthTicks(ticks: Int) {
        val accessor = this as LivingEntityRenderStateAccessor
        accessor.`mcendgame$setLowHealthTicks20`(ticks)
    }

    fun LivingEntityRenderState.getLowHealthTicks(): Int {
        val accessor = this as LivingEntityRenderStateAccessor
        return accessor.`mcendgame$getLowHealthTicks20`()
    }

    fun LivingEntityRenderState.setGhostly(ghostly: Boolean = true) = (this as LivingEntityRenderStateGhostlyAccessor).`mcendgame$setGhostly`(ghostly)

    fun LivingEntityRenderState.isGhostly() = (this as LivingEntityRenderStateGhostlyAccessor).`mcendgame$isGhostly`()
}