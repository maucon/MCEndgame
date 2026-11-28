package de.fuballer.mcendgame.main.component.migration.legacy

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

@Injectable
object LegacyItems {
    val ASPECT_OF_THE_GROVE: Item = RegistryUtil.registerItem(
        ::LegacyAspectOfTheGrove,
        Item.Properties().rarity(Rarity.UNCOMMON),
        RegistryKeyUtil.createItemKey(LegacyAspectOfTheGrove.ID)
    )
}