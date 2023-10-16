package de.fuballer.mcendgame.framework

import de.fuballer.mcendgame.framework.stereotype.Bean
import de.fuballer.mcendgame.framework.stereotype.Configuration

object DependencyInjector {
    fun instantiateClasses(classes: MutableSet<Class<*>>): Collection<Any> {
        val dependentClasses = mapToDependentClasses(classes)
        return instantiateDependentClasses(dependentClasses)
    }

    private fun mapToDependentClasses(classes: MutableSet<Class<*>>) = classes.flatMap { clazz ->
        if (Configuration::class.java.isAssignableFrom(clazz)) {
            mapMethodsToDependentClasses(clazz)
        } else {
            mapClassToDependentClass(clazz)
        }
    }

    private fun mapClassToDependentClass(clazz: Class<*>): List<DependentClass> {
        val constructor = clazz.constructors.first()

        return listOf(
            DependentClass(
                clazz,
                constructor.parameterTypes.asList()
            )
            { args -> constructor.newInstance(*args) }
        )
    }

    private fun mapMethodsToDependentClasses(clazz: Class<*>): List<DependentClass> {
        val classObject = clazz.constructors.first().newInstance()

        return clazz.methods
            .filter { it.isAnnotationPresent(Bean::class.java) }
            .map {
                DependentClass(
                    it.returnType,
                    it.parameterTypes.asList()
                )
                { args -> it.invoke(classObject, *args) }
            }
    }

    private fun instantiateDependentClasses(classes: List<DependentClass>): Collection<Any> {
        val instantiatedClasses = mutableMapOf<Class<*>, Any>()
        val toInstantiate = classes.toMutableList()
        toInstantiate.sortBy { it.dependencies.size }

        while (toInstantiate.isNotEmpty()) {
            val instantiated = mutableListOf<DependentClass>()

            for (dependentClass in toInstantiate) {
                val dependencies = gatherDependencies(dependentClass.dependencies, instantiatedClasses) ?: continue

                val classObject = dependentClass.constructor.invoke(dependencies)
                instantiatedClasses[dependentClass.type] = classObject

                instantiated.add(dependentClass)
            }

            if (!toInstantiate.removeAll(instantiated)) {
                throw IllegalStateException("Couldn't resolve dependencies!")
            }
        }

        return instantiatedClasses.values
    }

    private fun gatherDependencies(
        dependencies: List<Class<*>>,
        instantiatedClasses: MutableMap<Class<*>, Any>
    ): Array<Any>? {
        val resolvedDependencies = dependencies.mapNotNull { instantiatedClasses[it] }
        if (dependencies.size != resolvedDependencies.size) return null

        return resolvedDependencies.toTypedArray()
    }

    data class DependentClass(
        val type: Class<*>,
        val dependencies: List<Class<*>>,
        val constructor: (Array<Any>) -> Any
    )
}
