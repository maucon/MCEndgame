package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.item.BlockItem
import net.minecraft.util.ActionResult

@Injectable
class DungeonBlocksService {
    @Initializer
    fun onBlockUse() {
        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            if (!world.isDungeonWorld()) return@register ActionResult.PASS
            if (player.isCreative) return@register ActionResult.PASS

            val stack = player.getStackInHand(hand)
            if (stack.item is BlockItem) return@register ActionResult.FAIL

            if (!world.getBlockState(hitResult.blockPos).isIn(CustomTags.DUNGEON_INTERACTABLE)) return@register ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION

            ActionResult.PASS
        }
    }

    @Initializer
    fun onBlockBreak() {
        PlayerBlockBreakEvents.BEFORE.register { world, player, _, blockState, _ ->
            if (!world.isDungeonWorld()) return@register true
            if (player.isCreative) return@register true

            blockState.isIn(CustomTags.DUNGEON_BREAKABLE)
        }
    }
}