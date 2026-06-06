package de.fuballer.mcendgame.client.component.entity.custom.entities.elf_duelist

import com.geckolib.model.GeoModel
import com.geckolib.renderer.base.GeoRenderState
import de.fuballer.mcendgame.main.component.entity.custom.entities.elf_duelist.ElfDuelistEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil

class ElfDuelistModel : GeoModel<ElfDuelistEntity>() {
    companion object {
        val MODEL_IDENTIFIER = IdentifierUtil.default("entity/elf_duelist")
        val TEXTURE_IDENTIFIER = IdentifierUtil.default("textures/entity/elf_duelist/elf_duelist.png")
        val ANIMATION_IDENTIFIER = IdentifierUtil.default("entity/elf_duelist")
    }

    override fun getModelResource(renderState: GeoRenderState) = MODEL_IDENTIFIER

    override fun getTextureResource(renderState: GeoRenderState) = TEXTURE_IDENTIFIER

    override fun getAnimationResource(entity: ElfDuelistEntity) = ANIMATION_IDENTIFIER
}