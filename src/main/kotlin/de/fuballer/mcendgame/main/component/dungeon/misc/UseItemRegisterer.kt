package de.fuballer.mcendgame.main.component.dungeon.misc

import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.world.InteractionResult

@Injectable
class UseItemRegisterer {
    @Initializer
    fun init() {
        UseItemCallback.EVENT.register { player, world, hand ->
            if (!world.isDungeonWorld()) return@register InteractionResult.PASS
            if (player.isCreative) return@register InteractionResult.PASS

            val stack = player.getItemInHand(hand)
            if (stack.`is`(CustomTags.DUNGEON_DISABLED)) return@register InteractionResult.FAIL

            InteractionResult.PASS
        }
    }
}