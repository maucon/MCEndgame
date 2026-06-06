package de.fuballer.mcendgame.client.component.datagen

import com.google.gson.JsonObject
import de.fuballer.mcendgame.main.MCEndgame
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import java.util.concurrent.CompletableFuture

class CustomDamageTypeProvider(
    val packOutput: FabricPackOutput,
) : DataProvider {
    override fun getName() = "${MCEndgame.MOD_ID} Damage Type Provider"

    override fun run(writer: CachedOutput): CompletableFuture<*> {
        val damageTypes = listOf(
            generateDamageTypeJSON()
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.SWEEPING.identifier().path)) },

            generateDamageTypeJSON(CustomDamageTypes.SPELL.identifier().path)
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.SPELL.identifier().path)) },

            generateDamageTypeJSON()
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.GENERIC_ATTACK.identifier().path)) },

            generateDamageTypeJSON()
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE.identifier().path)) },

            generateDamageTypeJSON()
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.PIERCE_ATTACK.identifier().path)) },

            generateDamageTypeJSON()
                .let { DataProvider.saveStable(writer, it, getPath(CustomDamageTypes.KINETIC_ATTACK.identifier().path)) },
        ).toTypedArray()

        return CompletableFuture.allOf(*damageTypes)
    }

    private fun generateDamageTypeJSON(
        messageId: String = "mob",
        exhaustion: Double = 0.1,
        scaleWithDifficulty: String = "when_caused_by_living_non_player",
        deathMessageType: String = "default",
        soundEffects: String = "hurt",
    ) = JsonObject().apply {
        addProperty("message_id", messageId)
        addProperty("exhaustion", exhaustion)
        addProperty("scaling", scaleWithDifficulty)
        addProperty("death_message_type", deathMessageType)
        addProperty("effects", soundEffects)
    }

    private fun getPath(name: String) = packOutput.outputFolder.resolve("data/${MCEndgame.MOD_ID}/damage_type/$name.json")
}