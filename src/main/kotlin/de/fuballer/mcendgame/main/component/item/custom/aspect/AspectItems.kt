package de.fuballer.mcendgame.main.component.item.custom.aspect

import de.fuballer.mcendgame.main.component.item.custom.aspect.item.ancestors.AspectOfAncestors
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.curio.AspectOfCurio
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.dominion.AspectOfDominion
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.duality.AspectOfDuality
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.eminence.AspectOfEminence
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.fortitude.AspectOfFortitude
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.fortune.AspectOfFortune
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.ghosts.AspectOfGhosts
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.greed.AspectOfGreed
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.grove.AspectOfTheGrove
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.hordes.AspectOfHordes
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.impatience.AspectOfImpatience
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.savagery.AspectOfSavagery
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.tyranny.AspectOfTyranny
import de.fuballer.mcendgame.main.component.item.custom.aspect.item.zeal.AspectOfZeal
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object AspectItems {
    val ASPECT_OF_TYRANNY = RegistryUtil.registerAspectItem(::AspectOfTyranny, AspectItemIds.ASPECT_OF_TYRANNY)
    val ASPECT_OF_GREED = RegistryUtil.registerAspectItem(::AspectOfGreed, AspectItemIds.ASPECT_OF_GREED)
    val ASPECT_OF_DOMINION = RegistryUtil.registerAspectItem(::AspectOfDominion, AspectItemIds.ASPECT_OF_DOMINION)
    val ASPECT_OF_IMPATIENCE = RegistryUtil.registerAspectItem(::AspectOfImpatience, AspectItemIds.ASPECT_OF_IMPATIENCE)
    val ASPECT_OF_HORDES = RegistryUtil.registerAspectItem(::AspectOfHordes, AspectItemIds.ASPECT_OF_HORDES)
    val ASPECT_OF_CURIO = RegistryUtil.registerAspectItem(::AspectOfCurio, AspectItemIds.ASPECT_OF_CURIO)
    val ASPECT_OF_FORTUNE = RegistryUtil.registerAspectItem(::AspectOfFortune, AspectItemIds.ASPECT_OF_FORTUNE)
    val ASPECT_OF_ZEAL = RegistryUtil.registerAspectItem(::AspectOfZeal, AspectItemIds.ASPECT_OF_ZEAL)
    val ASPECT_OF_GHOSTS = RegistryUtil.registerAspectItem(::AspectOfGhosts, AspectItemIds.ASPECT_OF_GHOSTS)
    val ASPECT_OF_FORTITUDE = RegistryUtil.registerAspectItem(::AspectOfFortitude, AspectItemIds.ASPECT_OF_FORTITUDE)
    val ASPECT_OF_SAVAGERY = RegistryUtil.registerAspectItem(::AspectOfSavagery, AspectItemIds.ASPECT_OF_SAVAGERY)
    val ASPECT_OF_EMINENCE = RegistryUtil.registerAspectItem(::AspectOfEminence, AspectItemIds.ASPECT_OF_EMINENCE)
    val ASPECT_OF_ANCESTORS = RegistryUtil.registerAspectItem(::AspectOfAncestors, AspectItemIds.ASPECT_OF_ANCESTORS)
    val ASPECT_OF_DUALITY = RegistryUtil.registerAspectItem(::AspectOfDuality, AspectItemIds.ASPECT_OF_DUALITY)
    val ASPECT_OF_THE_GROVE = RegistryUtil.registerAspectItem(::AspectOfTheGrove, AspectItemIds.ASPECT_OF_THE_GROVE)
}