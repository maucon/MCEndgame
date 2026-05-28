package de.fuballer.mcendgame.main.component.custom_attribute.affinity

object AttributeAffinities {
    val EMPTY = listOf<AttributeAffinity>()
    val BENEFICIAL = listOf(SimpleAttributeAffinity(Affinity.BENEFICIAL))
    val DETRIMENTAL = listOf(SimpleAttributeAffinity(Affinity.DETRIMENTAL))
    val NEUTRAL = listOf(SimpleAttributeAffinity(Affinity.NEUTRAL))
    val BENEFICIAL_CONDITIONAL_BENEFICIAL = listOf(SimpleAttributeAffinity(Affinity.BENEFICIAL), ConditionalAttributeAffinity(Affinity.BENEFICIAL, 0))
    val BENEFICIAL_CONDITIONAL_DETRIMENTAL = listOf(SimpleAttributeAffinity(Affinity.BENEFICIAL), ConditionalAttributeAffinity(Affinity.DETRIMENTAL, 0))
    val TRIPLE_BENEFICIAL = listOf(SimpleAttributeAffinity(Affinity.BENEFICIAL), SimpleAttributeAffinity(Affinity.BENEFICIAL), SimpleAttributeAffinity(Affinity.BENEFICIAL))
}