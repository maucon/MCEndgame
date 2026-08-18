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
    val ASPECT_OF_TYRANNY = RegistryUtil.registerAspectItem(::AspectOfTyranny, "aspect_of_tyranny")
    val ASPECT_OF_GREED = RegistryUtil.registerAspectItem(::AspectOfGreed, "aspect_of_greed")
    val ASPECT_OF_DOMINION = RegistryUtil.registerAspectItem(::AspectOfDominion, "aspect_of_dominion")
    val ASPECT_OF_IMPATIENCE = RegistryUtil.registerAspectItem(::AspectOfImpatience, "aspect_of_impatience")
    val ASPECT_OF_HORDES = RegistryUtil.registerAspectItem(::AspectOfHordes, "aspect_of_hordes")
    val ASPECT_OF_CURIO = RegistryUtil.registerAspectItem(::AspectOfCurio, "aspect_of_curio")
    val ASPECT_OF_FORTUNE = RegistryUtil.registerAspectItem(::AspectOfFortune, "aspect_of_fortune")
    val ASPECT_OF_ZEAL = RegistryUtil.registerAspectItem(::AspectOfZeal, "aspect_of_zeal")
    val ASPECT_OF_GHOSTS = RegistryUtil.registerAspectItem(::AspectOfGhosts, "aspect_of_ghosts")
    val ASPECT_OF_FORTITUDE = RegistryUtil.registerAspectItem(::AspectOfFortitude, "aspect_of_fortitude")
    val ASPECT_OF_SAVAGERY = RegistryUtil.registerAspectItem(::AspectOfSavagery, "aspect_of_savagery")
    val ASPECT_OF_EMINENCE = RegistryUtil.registerAspectItem(::AspectOfEminence, "aspect_of_eminence")
    val ASPECT_OF_ANCESTORS = RegistryUtil.registerAspectItem(::AspectOfAncestors, "aspect_of_ancestors")
    val ASPECT_OF_DUALITY = RegistryUtil.registerAspectItem(::AspectOfDuality, "aspect_of_duality")
    val ASPECT_OF_THE_GROVE = RegistryUtil.registerAspectItem(::AspectOfTheGrove, "aspect_of_the_grove")
}