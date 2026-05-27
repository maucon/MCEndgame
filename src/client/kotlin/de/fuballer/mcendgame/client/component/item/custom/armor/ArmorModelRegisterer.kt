package de.fuballer.mcendgame.client.component.item.custom.armor

import de.fuballer.mcendgame.client.component.item.custom.armor.model.abyssal_mask.AbyssalMaskModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.bound_abyss.BoundAbyssModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.broodmother.BroodmotherModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.druids.DruidsLeggingsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.emberchant.EmberchantModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.emberreign.EmberreignModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.geistergaloschen.GeistergaloschenModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.gilded_tempest.GildedTempestModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.iceborne.IceborneModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.lamias_gift.LamiasGiftModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.moonshadow.MoonshadowModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.stoneward.StonewardModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.suede.SuedeLeggingsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.voidweaver.VoidweaverModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.windstrider.WindstriderModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseBootsModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseChestplateModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseHelmetModel
import de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose.WitherRoseLeggingsModel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry

@Injectable
object ArmorModelRegisterer {
    @Initializer
    fun register() {
        EntityModelLayerRegistry.registerModelLayer(
            IceborneModel.MODEL_LAYER,
            IceborneModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            BoundAbyssModel.MODEL_LAYER,
            BoundAbyssModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            DruidsHelmetModel.MODEL_LAYER,
            DruidsHelmetModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            DruidsChestplateModel.MODEL_LAYER,
            DruidsChestplateModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            DruidsLeggingsModel.MODEL_LAYER,
            DruidsLeggingsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            DruidsBootsModel.MODEL_LAYER,
            DruidsBootsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            EmberchantModel.MODEL_LAYER,
            EmberchantModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            LamiasGiftModel.MODEL_LAYER,
            LamiasGiftModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            WitherRoseHelmetModel.MODEL_LAYER,
            WitherRoseHelmetModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            WitherRoseChestplateModel.MODEL_LAYER,
            WitherRoseChestplateModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            WitherRoseLeggingsModel.MODEL_LAYER,
            WitherRoseLeggingsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            WitherRoseBootsModel.MODEL_LAYER,
            WitherRoseBootsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            SuedeHelmetModel.MODEL_LAYER,
            SuedeHelmetModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            SuedeChestplateModel.MODEL_LAYER,
            SuedeChestplateModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            SuedeLeggingsModel.MODEL_LAYER,
            SuedeLeggingsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            SuedeBootsModel.MODEL_LAYER,
            SuedeBootsModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            StonewardModel.MODEL_LAYER,
            StonewardModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            MoonshadowModel.MODEL_LAYER,
            MoonshadowModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            GeistergaloschenModel.MODEL_LAYER,
            GeistergaloschenModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            VoidweaverModel.MODEL_LAYER,
            VoidweaverModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            AbyssalMaskModel.MODEL_LAYER,
            AbyssalMaskModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            GildedTempestModel.MODEL_LAYER,
            GildedTempestModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            WindstriderModel.MODEL_LAYER,
            WindstriderModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            BroodmotherModel.MODEL_LAYER,
            BroodmotherModel.Companion::getTexturedModelData
        )
        EntityModelLayerRegistry.registerModelLayer(
            EmberreignModel.MODEL_LAYER,
            EmberreignModel.Companion::getTexturedModelData
        )
    }
}