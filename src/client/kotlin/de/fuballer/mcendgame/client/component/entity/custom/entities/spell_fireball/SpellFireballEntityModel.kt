package de.fuballer.mcendgame.client.component.entity.custom.entities.spell_fireball

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class SpellFireballEntityModel(
    modelPart: ModelPart,
) : EntityModel<SpellFireballRenderState>(modelPart) {
    val fireball: ModelPart = root.getChild("fireball")

    companion object {
        val SPELL_FIREBALL = ModelLayerLocation(IdentifierUtil.default("spell_fireball"), "main")
        val SPELL_FIREBALL_OUTER = ModelLayerLocation(IdentifierUtil.default("spell_fireball_outer"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            modelPartData.addOrReplaceChild(
                "fireball",
                CubeListBuilder.create().texOffs(4, 13).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, CubeDeformation(0.0F))
                    .texOffs(7, 22).addBox(-1.5F, -1.5F, 2.0F, 3.0F, 3.0F, 2.0F, CubeDeformation(0.0F))
                    .texOffs(8, 28).addBox(-1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 2.0F, CubeDeformation(0.0F))
                    .texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation(-0.75F)),
                PartPose.offset(0.0F, -3.0F, 0.0F),
            )
            return LayerDefinition.create(modelData, 32, 32)
        }

        fun getOuterTextureModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            modelPartData.addOrReplaceChild(
                "fireball",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, CubeDeformation(-0.75F)),
                PartPose.offset(0.0F, -3.0F, 0.0F),
            )
            return LayerDefinition.create(modelData, 32, 32)
        }
    }

    override fun setupAnim(
        state: SpellFireballRenderState,
    ) {
        super.setupAnim(state)
        fireball.zRot += state.ageInTicks * 0.12F
    }
}