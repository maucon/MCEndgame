package de.fuballer.mcendgame.framework.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Bean(
    val name: String = ""
)
