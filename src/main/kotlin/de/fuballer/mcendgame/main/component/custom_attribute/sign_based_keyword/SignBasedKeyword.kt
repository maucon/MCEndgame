package de.fuballer.mcendgame.main.component.custom_attribute.sign_based_keyword

import net.minecraft.network.chat.Component

private const val LANGUAGE_KEY_PREFIX = "attribute.mcendgame."

enum class SignBasedKeyword(
    val positive: Component,
    val negative: Component,
) {
    INCREASED(Component.translatable("${LANGUAGE_KEY_PREFIX}increased"), Component.translatable("${LANGUAGE_KEY_PREFIX}reduced")),
    MORE(Component.translatable("${LANGUAGE_KEY_PREFIX}more"), Component.translatable("${LANGUAGE_KEY_PREFIX}less"));
}