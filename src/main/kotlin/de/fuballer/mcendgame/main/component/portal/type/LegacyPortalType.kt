package de.fuballer.mcendgame.main.component.portal.type

import de.fuballer.mcendgame.main.component.portal.PortalEntity

class LegacyPortalType : PortalType() {
    companion object {
        const val ID = "legacy"
    }

    override fun getId() = ID

    override fun tickAnimation(portalEntity: PortalEntity) {
        idleAnimationState.startIfStopped(portalEntity.tickCount)
    }
}