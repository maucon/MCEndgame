package de.fuballer.mcendgame.main.component.block.blocks.crystalforge.network

import de.fuballer.mcendgame.main.component.block.blocks.crystalforge.CrystalForgeBlock
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class CrystalForgePayload : CustomPacketPayload {
    companion object {
        val IDENTIFIER = IdentifierUtil.default("${CrystalForgeBlock.ID}.forge")
        val ID = CustomPacketPayload.Type<CrystalForgePayload>(IDENTIFIER)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, CrystalForgePayload> = StreamCodec.ofMember({ _, _ -> }, { _ -> CrystalForgePayload() })
    }

    override fun type() = ID
}