package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.util.TypedActionResult

@Injectable
class UseItemRegisterer {
    @Initializer
    fun init() {
        UseItemCallback.EVENT.register { player, world, hand ->
            val stack = player.getStackInHand(hand)

            if (!world.isDungeonWorld()) return@register TypedActionResult.pass(stack)
            if (player.isCreative) return@register TypedActionResult.pass(stack)

            if (stack.isIn(CustomTags.DUNGEON_DISABLED)) return@register TypedActionResult.fail(stack)

            TypedActionResult.pass(stack)
        }
    }
}