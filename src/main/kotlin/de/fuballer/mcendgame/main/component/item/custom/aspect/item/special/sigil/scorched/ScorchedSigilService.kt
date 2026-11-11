package de.fuballer.mcendgame.main.component.item.custom.aspect.item.special.sigil.scorched

import de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin.thal_veyra.ThalVeyraEntity
import de.fuballer.mcendgame.main.component.entity.custom.entities.demonkin.thar_zul.TharZulEntity
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItems
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossDeathEvent
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossKilledMessageCommand
import de.fuballer.mcendgame.main.messaging.dungeon.DungeonBossSpawnPortalCommand
import de.fuballer.mcendgame.main.util.extension.EntityExtension.getTotalCustomAttributeLootMultiplier
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonLevel
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import kotlin.random.Random

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

    @EventSubscriber(sync = true)
    fun onDungeonBossDeath(event: DungeonBossDeathEvent) {
        if (!event.final) return
        val serverLevel = event.world as? ServerLevel ?: return
        if (!serverLevel.getDungeonAspects().containsKey(AspectItems.SCORCHED_SIGIL)) return

        val probability = serverLevel.getDungeonLevel() / 10.0 * event.bossEntity.getTotalCustomAttributeLootMultiplier()
        val count = probability.toInt() + if (Random.nextDouble() < probability % 1) 1 else 0

        val stack = ItemStack(AspectItems.DEMONIC_KEY, count)
        event.bossEntity.spawnAtLocation(serverLevel, stack)
    }
}