package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheBearEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheEagleEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheMammothEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheRhinoEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheSerpentEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheStagEffect
import de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings.BlessingOfTheWolfEffect
import de.fuballer.mcendgame.main.util.minecraft.RegistryUtil
import de.maucon.mauconframework.di.annotation.Injectable

@Injectable
object CustomStatusEffects {
    val FURY = RegistryUtil.registerStatusEffect("fury", FuryEffect())
    val RESILIENCE = RegistryUtil.registerStatusEffect("resilience", ResilienceEffect())
    val SCORCH = RegistryUtil.registerStatusEffect("scorch", ScorchEffect())
    val VERDANT_ECHO = RegistryUtil.registerStatusEffect("verdant_echo", VerdantEchoEffect())
    val MOLTEN_ROAR = RegistryUtil.registerStatusEffect("molten_roar", MoltenRoarEffect())
    val ANCIENT_BLIGHT = RegistryUtil.registerStatusEffect("ancient_blight", AncientBlightEffect())

    // beastweaver blessings
    val BLESSING_OF_THE_BEAR = RegistryUtil.registerStatusEffect("blessing_of_the_bear", BlessingOfTheBearEffect())
    val BLESSING_OF_THE_EAGLE = RegistryUtil.registerStatusEffect("blessing_of_the_eagle", BlessingOfTheEagleEffect())
    val BLESSING_OF_THE_MAMMOTH = RegistryUtil.registerStatusEffect("blessing_of_the_mammoth", BlessingOfTheMammothEffect())
    val BLESSING_OF_THE_RHINO = RegistryUtil.registerStatusEffect("blessing_of_the_rhino", BlessingOfTheRhinoEffect())
    val BLESSING_OF_THE_SERPENT = RegistryUtil.registerStatusEffect("blessing_of_the_serpent", BlessingOfTheSerpentEffect())
    val BLESSING_OF_THE_STAG = RegistryUtil.registerStatusEffect("blessing_of_the_stag", BlessingOfTheStagEffect())
    val BLESSING_OF_THE_WOLF = RegistryUtil.registerStatusEffect("blessing_of_the_wolf", BlessingOfTheWolfEffect())
}