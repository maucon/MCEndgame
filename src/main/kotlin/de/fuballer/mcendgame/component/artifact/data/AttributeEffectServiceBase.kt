package de.fuballer.mcendgame.component.artifact.data

import de.fuballer.mcendgame.event.PlayerDungeonJoinEvent
import de.fuballer.mcendgame.event.PlayerDungeonLeaveEvent
import de.fuballer.mcendgame.util.extension.AttributeInstanceExtension.findModifierByName
import de.fuballer.mcendgame.util.extension.PlayerExtension.getHighestArtifactTier
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

open class AttributeEffectServiceBase(
    private val artifactType: ArtifactType,
    private val attributeType: Attribute,
    private val attributeModifierOperation: Operation
) : Listener {
    private val modifierName = artifactType.key.toString()

    @EventHandler
    fun on(event: PlayerDungeonJoinEvent) {
        val tier = event.player.getHighestArtifactTier(artifactType) ?: return

        val (value) = artifactType.getValues(tier)
        val modifier = AttributeModifier(modifierName, value, attributeModifierOperation)

        val attribute = event.player.getAttribute(attributeType) ?: return
        if (attribute.findModifierByName(modifierName) != null) return

        attribute.addModifier(modifier)
    }

    @EventHandler
    fun on(event: PlayerDungeonLeaveEvent) {
        val attribute = event.player.getAttribute(attributeType) ?: return
        val modifier = attribute.findModifierByName(modifierName) ?: return

        attribute.removeModifier(modifier)
    }
}