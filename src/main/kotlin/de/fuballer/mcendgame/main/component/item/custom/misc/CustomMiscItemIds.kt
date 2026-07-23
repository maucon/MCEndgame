package de.fuballer.mcendgame.main.component.item.custom.misc

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CustomMiscItemIds {
    val VERDANT_ECHO = RegistryKeyUtil.createItemKey("verdant_echo")
    val MOLTEN_ROAR = RegistryKeyUtil.createItemKey("molten_roar")
    val FRIGID_CRY = RegistryKeyUtil.createItemKey("frigid_cry")
}