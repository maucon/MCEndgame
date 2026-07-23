package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.util.minecraft.RegistryKeyUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object TotemItemIds {
    // basic
    val TOTEM_OF_BASTION = RegistryKeyUtil.createItemKey("totem_of_bastion")
    val TOTEM_OF_FORCE = RegistryKeyUtil.createItemKey("totem_of_force")
    val TOTEM_OF_FORTRESS = RegistryKeyUtil.createItemKey("totem_of_fortress")
    val TOTEM_OF_FRENZY = RegistryKeyUtil.createItemKey("totem_of_frenzy")
    val TOTEM_OF_GRACE = RegistryKeyUtil.createItemKey("totem_of_grace")
    val TOTEM_OF_IMPACT = RegistryKeyUtil.createItemKey("totem_of_impact")
    val TOTEM_OF_SWIFTNESS = RegistryKeyUtil.createItemKey("totem_of_swiftness")
    val TOTEM_OF_THICKNESS = RegistryKeyUtil.createItemKey("totem_of_thickness")
    val TOTEM_OF_VANGUARD = RegistryKeyUtil.createItemKey("totem_of_vanguard")
    val TOTEM_OF_RENEWAL = RegistryKeyUtil.createItemKey("totem_of_renewal")
    val TOTEM_OF_TEMPEST = RegistryKeyUtil.createItemKey("totem_of_tempest")
    val TOTEM_OF_DISPELLING = RegistryKeyUtil.createItemKey("totem_of_dispelling")

    // effect
    val TOTEM_OF_DEFIANCE = RegistryKeyUtil.createItemKey("totem_of_defiance")
    val TOTEM_OF_RECOVERY = RegistryKeyUtil.createItemKey("totem_of_recovery")
    val TOTEM_OF_RIME = RegistryKeyUtil.createItemKey("totem_of_rime")
    val TOTEM_OF_ONSLAUGHT = RegistryKeyUtil.createItemKey("totem_of_onslaught")
    val TOTEM_OF_RESILIENCE = RegistryKeyUtil.createItemKey("totem_of_resilience")
    val TOTEM_OF_FURY = RegistryKeyUtil.createItemKey("totem_of_fury")

    // ultimate
    val TOTEM_OF_VOLLEY = RegistryKeyUtil.createItemKey("totem_of_volley")
    val TOTEM_OF_REACH = RegistryKeyUtil.createItemKey("totem_of_reach")
    val TOTEM_OF_GIGANTISM = RegistryKeyUtil.createItemKey("totem_of_gigantism")
    val TOTEM_OF_RESTORATION = RegistryKeyUtil.createItemKey("totem_of_restoration")
}