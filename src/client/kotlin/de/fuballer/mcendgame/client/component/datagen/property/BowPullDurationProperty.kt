package de.fuballer.mcendgame.client.component.datagen.property

import com.mojang.serialization.MapCodec
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getBowFullPullTicks
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty
import net.minecraft.client.renderer.item.properties.numeric.UseDuration
import net.minecraft.world.entity.ItemOwner
import net.minecraft.world.item.ItemStack
import kotlin.math.min

class BowPullDurationProperty() : RangeSelectItemModelProperty {
    companion object {
        val CODEC: MapCodec<BowPullDurationProperty> = MapCodec.unit(BowPullDurationProperty())
    }

    override fun type(): MapCodec<BowPullDurationProperty> = CODEC

    override fun get(stack: ItemStack, world: ClientLevel?, context: ItemOwner?, seed: Int): Float {
        val holder = context?.asLivingEntity() ?: return 0F
        if (holder.useItem != stack) return 0F
        val ticksUsed = UseDuration.useDuration(stack, holder)
        val maxTicks = holder.getBowFullPullTicks()
        return min(ticksUsed.toFloat() / maxTicks, 1F)
    }
}