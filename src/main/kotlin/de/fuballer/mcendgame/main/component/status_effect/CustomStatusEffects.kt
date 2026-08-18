package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.status_effect.molten_roar.MoltenRoarEffect
import de.fuballer.mcendgame.main.component.status_effect.resilience.ResilienceEffect
import de.fuballer.mcendgame.main.component.status_effect.verdant_echo.VerdantEchoEffect
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
}