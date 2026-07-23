package de.fuballer.mcendgame.main.component.item.custom.totem

import de.fuballer.mcendgame.main.component.item.custom.totem.item.*
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object TotemItems {
    // basic
    val TOTEM_OF_BASTION = TotemItemRegistry.registerTotemItem(::TotemOfBastionItem, TotemItemIds.TOTEM_OF_BASTION)
    val TOTEM_OF_FORCE = TotemItemRegistry.registerTotemItem(::TotemOfForceItem, TotemItemIds.TOTEM_OF_FORCE)
    val TOTEM_OF_FORTRESS = TotemItemRegistry.registerTotemItem(::TotemOfFortressItem, TotemItemIds.TOTEM_OF_FORTRESS)
    val TOTEM_OF_FRENZY = TotemItemRegistry.registerTotemItem(::TotemOfFrenzyItem, TotemItemIds.TOTEM_OF_FRENZY)
    val TOTEM_OF_GRACE = TotemItemRegistry.registerTotemItem(::TotemOfGraceItem, TotemItemIds.TOTEM_OF_GRACE)
    val TOTEM_OF_IMPACT = TotemItemRegistry.registerTotemItem(::TotemOfImpactItem, TotemItemIds.TOTEM_OF_IMPACT)
    val TOTEM_OF_SWIFTNESS = TotemItemRegistry.registerTotemItem(::TotemOfSwiftnessItem, TotemItemIds.TOTEM_OF_SWIFTNESS)
    val TOTEM_OF_THICKNESS = TotemItemRegistry.registerTotemItem(::TotemOfThicknessItem, TotemItemIds.TOTEM_OF_THICKNESS)
    val TOTEM_OF_VANGUARD = TotemItemRegistry.registerTotemItem(::TotemOfVanguardItem, TotemItemIds.TOTEM_OF_VANGUARD)
    val TOTEM_OF_RENEWAL = TotemItemRegistry.registerTotemItem(::TotemOfRenewalItem, TotemItemIds.TOTEM_OF_RENEWAL)
    val TOTEM_OF_TEMPEST = TotemItemRegistry.registerTotemItem(::TotemOfTempestItem, TotemItemIds.TOTEM_OF_TEMPEST)
    val TOTEM_OF_DISPELLING = TotemItemRegistry.registerTotemItem(::TotemOfDispellingItem, TotemItemIds.TOTEM_OF_DISPELLING)

    // effect
    val TOTEM_OF_DEFIANCE = TotemItemRegistry.registerTotemItem(::TotemOfDefianceItem, TotemItemIds.TOTEM_OF_DEFIANCE)
    val TOTEM_OF_RECOVERY = TotemItemRegistry.registerTotemItem(::TotemOfRecoveryItem, TotemItemIds.TOTEM_OF_RECOVERY)
    val TOTEM_OF_RIME = TotemItemRegistry.registerTotemItem(::TotemOfRimeItem, TotemItemIds.TOTEM_OF_RIME)
    val TOTEM_OF_ONSLAUGHT = TotemItemRegistry.registerTotemItem(::TotemOfOnslaughtItem, TotemItemIds.TOTEM_OF_ONSLAUGHT)
    val TOTEM_OF_RESILIENCE = TotemItemRegistry.registerTotemItem(::TotemOfResilienceItem, TotemItemIds.TOTEM_OF_RESILIENCE)
    val TOTEM_OF_FURY = TotemItemRegistry.registerTotemItem(::TotemOfFuryItem, TotemItemIds.TOTEM_OF_FURY)

    // ultimate
    val TOTEM_OF_VOLLEY = TotemItemRegistry.registerTotemItem(::TotemOfVolleyItem, TotemItemIds.TOTEM_OF_VOLLEY)
    val TOTEM_OF_REACH = TotemItemRegistry.registerTotemItem(::TotemOfReachItem, TotemItemIds.TOTEM_OF_REACH)
    val TOTEM_OF_GIGANTISM = TotemItemRegistry.registerTotemItem(::TotemOfGigantismItem, TotemItemIds.TOTEM_OF_GIGANTISM)
    val TOTEM_OF_RESTORATION = TotemItemRegistry.registerTotemItem(::TotemOfRestorationItem, TotemItemIds.TOTEM_OF_RESTORATION)
}