package de.fuballer.mcendgame.client.component.entity.custom.entities.bandit

import com.geckolib.constant.dataticket.DataTicket
import de.fuballer.mcendgame.main.component.entity.custom.entities.bandit.BanditType
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.util.Mth

class BanditRenderState : HumanoidRenderState() {
    var banditType: BanditType = BanditType.DEFAULT
    var arrowCount: Int = 0
    var stingerCount: Int = 0
    var showHat: Boolean = true
    var showJacket: Boolean = true
    var showLeftPants: Boolean = true
    var showRightPants: Boolean = true
    var showLeftSleeve: Boolean = true
    var showRightSleeve: Boolean = true
    var fallFlyingTimeInTicks: Float = 0f
    var shouldApplyFlyingYRot: Boolean = false
    var flyingYRot: Float = 0f
    var id: Int = 0
    var showExtraEars: Boolean = false
    val heldOnHead: ItemStackRenderState = ItemStackRenderState()

    fun fallFlyingScale(): Float {
        return Mth.clamp(this.fallFlyingTimeInTicks * this.fallFlyingTimeInTicks / 100.0f, 0.0f, 1.0f)
    }

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}