package de.fuballer.mcendgame.client.component.block.demonic_crucible

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState

class DemonicCrucibleBlockEntityRenderState : BlockEntityRenderState() {
    var item: ItemStackRenderState = ItemStackRenderState()
    var animationProgressTicks = 0
}