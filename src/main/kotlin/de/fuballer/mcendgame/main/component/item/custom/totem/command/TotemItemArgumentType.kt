package de.fuballer.mcendgame.main.component.item.custom.totem.command

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.exceptions.CommandSyntaxException
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItemRegistry
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.item.Item

@Injectable
class TotemItemArgumentType : ArgumentType<Item> {
    override fun parse(reader: StringReader): Item {
        val string = reader.readString()

        return TotemItemRegistry.NAME_MAP[string]
            ?: throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("Invalid totem item name.")
    }
}