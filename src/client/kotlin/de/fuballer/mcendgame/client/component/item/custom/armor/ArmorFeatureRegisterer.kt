package de.fuballer.mcendgame.client.component.item.custom.armor

import de.fuballer.mcendgame.client.messaging.RegisterLivingEntityFeatureRendererCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.state.HumanoidRenderState

@Injectable
class ArmorFeatureRegisterer {
    @CommandHandler
    fun on(cmd: RegisterLivingEntityFeatureRendererCommand) {
        val renderer = cmd.entityRenderer
        if (renderer.model !is HumanoidModel) return

        @Suppress("UNCHECKED_CAST")
        val bipedEntityRenderer = renderer as RenderLayerParent<HumanoidRenderState, HumanoidModel<HumanoidRenderState>>

        val customHumanoidArmorFeatureRenderer = CustomHumanoidArmorFeatureRenderer(bipedEntityRenderer, cmd.context)
        cmd.registrationHelper.register(customHumanoidArmorFeatureRenderer)
    }
}