package de.fuballer.mcendgame.main.component.item.custom.aspect.item.sigil.scorched

import de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin.thal_veyra.ThalVeyraEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin.thar_zul.TharZulEntity
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossKilledMessageCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossSpawnPortalCommand
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

@Injectable
object ScorchedSigilService {
    @CommandHandler
    fun onSpawnBossPortal(cmd: DungeonBossSpawnPortalCommand) {
        if (!cmd.aspects.containsKey(AspectItems.SCORCHED_SIGIL)) return
        cmd.shouldCreatePortal = false
    }

    @CommandHandler
    fun onSendDungeonBossKilledMessage(cmd: DungeonBossKilledMessageCommand) {
        if (!cmd.aspects.containsKey(AspectItems.SCORCHED_SIGIL)) return

        if (cmd.boss is TharZulEntity) {
            cmd.message = Component.translatable("text.mcendgame.dungeon.thar_zul_killed").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                .append(
                    if (cmd.final) Component.translatable("text.mcendgame.dungeon.demonkin_killed")
                        .withStyle { style -> style.withBold(false).withColor(ChatFormatting.GRAY) }
                    else Component.translatable("text.mcendgame.dungeon.thal_veyra_empower")
                        .withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) }
                )
            return
        }

        if (cmd.boss is ThalVeyraEntity) {
            cmd.message = Component.translatable("text.mcendgame.dungeon.thal_veyra_killed").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
                .append(
                    if (cmd.final) Component.translatable("text.mcendgame.dungeon.demonkin_killed")
                        .withStyle { style -> style.withBold(false).withColor(ChatFormatting.GRAY) }
                    else Component.translatable("text.mcendgame.dungeon.thar_zul_empower")
                        .withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) }
                )
            return
        }

        cmd.shouldSend = false
    }
}