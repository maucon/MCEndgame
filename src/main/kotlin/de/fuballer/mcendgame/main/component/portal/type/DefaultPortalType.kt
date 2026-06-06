package de.fuballer.mcendgame.main.component.portal.type

import de.fuballer.mcendgame.main.component.portal.PortalEntity

class DefaultPortalType : PortalType() {
    override fun getId() = "default"

    override fun tickAnimation(portalEntity: PortalEntity) {
        idleAnimationState.startIfStopped(portalEntity.tickCount)
    }
}