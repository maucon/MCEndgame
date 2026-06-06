package de.fuballer.mcendgame.client.messaging

import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

@Injectable
object CommandMapper {
    @Initializer
    fun onItemTooltip() = ItemTooltipCallback.EVENT.register { itemStack: ItemStack, context: Item.TooltipContext, tooltipType: TooltipFlag, texts: MutableList<Component> ->
        val cmd = RenderItemTooltipCommand(itemStack, context, tooltipType, texts)
        CommandGateway.apply(cmd)
    }

    @Initializer
    fun onLivingEntityFeatureRendererRegistration() = LivingEntityRenderLayerRegistrationCallback.EVENT.register { type, renderer, registrationHelper, context ->
        val cmd = RegisterLivingEntityFeatureRendererCommand(type, renderer, registrationHelper, context)
        CommandGateway.apply(cmd)
    }

    @Initializer
    fun collectRenderSubmits() = LevelRenderEvents.COLLECT_SUBMITS.register { context ->
        val cmd = CollectRenderSubmitsCommand(context)
        CommandGateway.apply(cmd)
    }
}
