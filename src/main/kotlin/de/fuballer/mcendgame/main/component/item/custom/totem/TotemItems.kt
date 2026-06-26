package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.component.item.custom.totem.item.*
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object TotemItems {
    // basic
    val TOTEM_OF_BASTION = TotemItemRegistry.registerTotemItem(::TotemOfBastionItem, "totem_of_bastion")
    val TOTEM_OF_FORCE = TotemItemRegistry.registerTotemItem(::TotemOfForceItem, "totem_of_force")
    val TOTEM_OF_FORTRESS = TotemItemRegistry.registerTotemItem(::TotemOfFortressItem, "totem_of_fortress")
    val TOTEM_OF_FRENZY = TotemItemRegistry.registerTotemItem(::TotemOfFrenzyItem, "totem_of_frenzy")
    val TOTEM_OF_GRACE = TotemItemRegistry.registerTotemItem(::TotemOfGraceItem, "totem_of_grace")
    val TOTEM_OF_IMPACT = TotemItemRegistry.registerTotemItem(::TotemOfImpactItem, "totem_of_impact")
    val TOTEM_OF_SWIFTNESS = TotemItemRegistry.registerTotemItem(::TotemOfSwiftnessItem, "totem_of_swiftness")
    val TOTEM_OF_THICKNESS = TotemItemRegistry.registerTotemItem(::TotemOfThicknessItem, "totem_of_thickness")
    val TOTEM_OF_VANGUARD = TotemItemRegistry.registerTotemItem(::TotemOfVanguardItem, "totem_of_vanguard")
    val TOTEM_OF_RENEWAL = TotemItemRegistry.registerTotemItem(::TotemOfRenewalItem, "totem_of_renewal")
    val TOTEM_OF_TEMPEST = TotemItemRegistry.registerTotemItem(::TotemOfTempestItem, "totem_of_tempest")
    val TOTEM_OF_DISPELLING = TotemItemRegistry.registerTotemItem(::TotemOfDispellingItem, "totem_of_dispelling")

    // effect
    val TOTEM_OF_DEFIANCE = TotemItemRegistry.registerTotemItem(::TotemOfDefianceItem, "totem_of_defiance")
    val TOTEM_OF_RECOVERY = TotemItemRegistry.registerTotemItem(::TotemOfRecoveryItem, "totem_of_recovery")
    val TOTEM_OF_RIME = TotemItemRegistry.registerTotemItem(::TotemOfRimeItem, "totem_of_rime")
    val TOTEM_OF_ONSLAUGHT = TotemItemRegistry.registerTotemItem(::TotemOfOnslaughtItem, "totem_of_onslaught")
    val TOTEM_OF_RESILIENCE = TotemItemRegistry.registerTotemItem(::TotemOfResilienceItem, "totem_of_resilience")
    val TOTEM_OF_FURY = TotemItemRegistry.registerTotemItem(::TotemOfFuryItem, "totem_of_fury")

    // ultimate
    val TOTEM_OF_VOLLEY = TotemItemRegistry.registerTotemItem(::TotemOfVolleyItem, "totem_of_volley")
    val TOTEM_OF_REACH = TotemItemRegistry.registerTotemItem(::TotemOfReachItem, "totem_of_reach")
    val TOTEM_OF_GIGANTISM = TotemItemRegistry.registerTotemItem(::TotemOfGigantismItem, "totem_of_gigantism")
    val TOTEM_OF_RESTORATION = TotemItemRegistry.registerTotemItem(::TotemOfRestorationItem, "totem_of_restoration")
}