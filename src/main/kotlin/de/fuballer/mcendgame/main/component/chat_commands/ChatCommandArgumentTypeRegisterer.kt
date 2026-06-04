package de.fuballer.mcendgame.main.component.chat_commands

import de.fuballer.mcendgame.main.component.item.custom.command.UniqueItemArgumentType
import de.fuballer.mcendgame.main.component.item.custom.totem.command.TotemItemArgumentType
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry
import net.minecraft.commands.synchronization.SingletonArgumentInfo

@Injectable
object ChatCommandArgumentTypeRegisterer {
    @Initializer
    fun register() {
        ArgumentTypeRegistry.registerArgumentType(
            IdentifierUtil.default("unique_item"),
            UniqueItemArgumentType::class.java,
            SingletonArgumentInfo.contextFree(::UniqueItemArgumentType)
        )
        ArgumentTypeRegistry.registerArgumentType(
            IdentifierUtil.default("totem_item"),
            TotemItemArgumentType::class.java,
            SingletonArgumentInfo.contextFree(::TotemItemArgumentType)
        )
    }
}