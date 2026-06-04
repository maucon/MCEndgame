package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem

@Injectable
class DungeonBlocksService {
    @Initializer
    fun onBlockUse() {
        UseBlockCallback.EVENT.register { player, world, hand, hitResult ->
            if (!world.isDungeonWorld()) return@register InteractionResult.PASS
            if (player.isCreative) return@register InteractionResult.PASS

            val stack = player.getItemInHand(hand)
            if (stack.item is BlockItem) return@register InteractionResult.FAIL

            if (!world.getBlockState(hitResult.blockPos).`is`(CustomTags.DUNGEON_INTERACTABLE)) return@register InteractionResult.TRY_WITH_EMPTY_HAND

            InteractionResult.PASS
        }
    }

    @Initializer
    fun onBlockBreak() {
        PlayerBlockBreakEvents.BEFORE.register { world, player, _, blockState, _ ->
            if (!world.isDungeonWorld()) return@register true
            if (player.isCreative) return@register true

            blockState.`is`(CustomTags.DUNGEON_BREAKABLE)
        }
    }
}