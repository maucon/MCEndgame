package de.fuballer.mcendgame.main.component.totem

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.asDoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemVanillaAttributesEntity
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemVanillaAttributesRepository
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemsEntity
import de.fuballer.mcendgame.main.component.totem.db.PlayerTotemsRepository
import de.fuballer.mcendgame.main.messaging.misc.CollectCustomAttributesCommand
import de.fuballer.mcendgame.main.messaging.misc.PlayerAfterDimensionChangeEvent
import de.fuballer.mcendgame.main.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.event.EventSubscriber
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.Container
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player

@Injectable
class TotemService(
    private val playerTotemsRepository: PlayerTotemsRepository,
    private val playerTotemVanillaAttributesRepository: PlayerTotemVanillaAttributesRepository,
) {
    fun openInventory(player: Player) {
        val playerTotems = getPlayerTotems(player)

        val screenHandlerFactory = SimpleMenuProvider({ syncId, inventory, _ ->
            TotemScreenHandler(syncId, inventory, playerTotems, this)
        }, Component.translatable("container.mcendgame.totem.title"))

        player.openMenu(screenHandlerFactory)
    }

    private fun getPlayerTotems(player: Player) = playerTotemsRepository.findById(player.uuid)?.totems ?: listOf()

    fun savePlayerTotems(player: Player, inventory: Container) {
        val entity = PlayerTotemsEntity(player.uuid, inventory.toList())
        playerTotemsRepository.save(entity)
    }

    @CommandHandler
    fun on(cmd: CollectCustomAttributesCommand) {
        val player = cmd.entity as? Player ?: return
        if (!player.level().isDungeonWorld()) return

        val attributes = getPlayerTotemAttributes(player)
        cmd.customAttributes.addAll(attributes)
    }

    private fun getPlayerTotemAttributes(player: Player) = getPlayerTotems(player).flatMap { it.getCustomAttributes() }

    @EventSubscriber(sync = true)
    fun on(event: PlayerAfterDimensionChangeEvent) {
        val player = event.player
        removeTotemVanillaAttributes(player)
        if (event.newWorld.isDungeonWorld()) addTotemVanillaAttributes(player)
    }

    private fun removeTotemVanillaAttributes(player: Player) {
        val entity = playerTotemVanillaAttributesRepository.findById(player.uuid) ?: return
        entity.attributes.forEach { (type, identifier) ->
            val instance = player.getAttribute(type)
            instance?.removeModifier(identifier)
        }
    }

    private fun addTotemVanillaAttributes(player: Player) {
        val attributes = mutableListOf<Pair<Holder<Attribute>, Identifier>>()

        getPlayerTotemAttributes(player)
            .filter { it.type is VanillaAttributeType }
            .forEach {
                val vanillaAttributeType = it.type as VanillaAttributeType
                val identifier = IdentifierUtil.defaultRandom()
                val modifier = AttributeModifier(identifier, it.rolls[0].asDoubleRoll().getValue(), vanillaAttributeType.scaleType)

                val type = vanillaAttributeType.attribute
                val instance = player.getAttribute(type)
                instance?.addTransientModifier(modifier)

                attributes.add(Pair(type, identifier))
            }

        val entity = PlayerTotemVanillaAttributesEntity(player.uuid, attributes)
        playerTotemVanillaAttributesRepository.save(entity)
    }
}