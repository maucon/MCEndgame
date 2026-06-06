package de.fuballer.mcendgame.main.util.minecraft

import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import net.minecraft.resources.Identifier
import java.util.*

object IdentifierUtil {
    fun default(id: String): Identifier {
        return Identifier.fromNamespaceAndPath(MCEndgame.MOD_ID, id)
    }

    fun defaultJava(id: String): Identifier {
        return Identifier.fromNamespaceAndPath(MCEndgame.MOD_ID, id)
    }

    fun defaultRandom(): Identifier {
        return Identifier.fromNamespaceAndPath(MCEndgame.MOD_ID, UUID.randomUUID().toString())
    }

    fun defaultCustomAttribute(attribute: CustomAttribute): Identifier {
        return Identifier.fromNamespaceAndPath(MCEndgame.MOD_ID, attribute.id.toString())
    }
}