package de.fuballer.mcendgame.client.component.item.custom

import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.PartDefinition

object ModelPartDataExtension {
    fun PartDefinition.createEmptyChild(name: String): PartDefinition {
        return addOrReplaceChild(name, CubeListBuilder.create(), PartPose.ZERO)
    }
}