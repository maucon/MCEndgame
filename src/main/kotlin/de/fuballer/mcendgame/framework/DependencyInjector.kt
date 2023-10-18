package de.fuballer.mcendgame.framework

import de.fuballer.mcendgame.framework.annotation.*
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import java.lang.reflect.Parameter

object DependencyInjector {
    private val componentAnnotations = arrayOf(Repository::class.java, Service::class.java, Configuration::class.java)

    fun instantiateClasses(startingClass: Class<*>): Collection<Any> {
        val injectables = scanForInjectables(startingClass)
        val dependentBeans = mapToDependentBeans(injectables)
        return instantiateDependentBeans(dependentBeans)
    }

    private fun scanForInjectables(startingClass: Class<*>): Set<Class<*>> {
        val reflection = Reflections(startingClass)
        val query = Scanners.TypesAnnotated.with(*componentAnnotations)

        return reflection.get(query)
            .map { Class.forName(it) }
            .filter { !it.isInterface && !it.isAnnotation }
            .toSet()
    }

    private fun mapToDependentBeans(classes: Set<Class<*>>) = classes.flatMap { clazz ->
        if (clazz.isAnnotationPresent(Configuration::class.java)) {
            mapMethodsToDependentBeans(clazz)
        } else {
            mapClassToDependentBean(clazz)
        }
    }

    private fun mapClassToDependentBean(clazz: Class<*>): List<DependentBean> {
        val constructor = clazz.constructors.first()

        return listOf(
            DependentBean(
                BeanQualifier(clazz, clazz.simpleName.lowercase()),
                mapParameterToQualifiedBeans(constructor.parameters.asList())
            )
            { args -> constructor.newInstance(*args) }
        )
    }

    private fun mapMethodsToDependentBeans(clazz: Class<*>): List<DependentBean> {
        val classObject = clazz.constructors.first().newInstance()

        return clazz.methods
            .filter { it.isAnnotationPresent(Bean::class.java) }
            .map {
                val bean = it.getAnnotation(Bean::class.java)
                val qualifierName = bean.name.ifEmpty { it.name }

                DependentBean(
                    BeanQualifier(it.returnType, qualifierName.lowercase()),
                    mapParameterToQualifiedBeans(it.parameters.asList())
                )
                { args -> it.invoke(classObject, *args) }
            }
    }

    private fun mapParameterToQualifiedBeans(parameter: List<Parameter>) =
        parameter.map {
            if (it.isAnnotationPresent(Qualifier::class.java)) {
                val qualifier = it.getAnnotation(Qualifier::class.java)
                BeanQualifier(it.type, qualifier.name.lowercase())
            } else {
                val classType = it.type
                BeanQualifier(classType, classType.simpleName.lowercase())
            }
        }

    private fun instantiateDependentBeans(beans: List<DependentBean>): Collection<Any> {
        val instantiatedBeans = mutableMapOf<BeanQualifier, Any>()
        val toInstantiate = beans.toMutableList()
        toInstantiate.sortBy { it.dependencies.size }

        while (toInstantiate.isNotEmpty()) {
            val instantiated = mutableListOf<DependentBean>()

            for (dependentBean in toInstantiate) {
                val dependencies = gatherDependencies(dependentBean.dependencies, instantiatedBeans) ?: continue

                val classObject = dependentBean.constructor.invoke(dependencies)
                instantiatedBeans[dependentBean.beanQualifier] = classObject

                instantiated.add(dependentBean)
            }

            if (!toInstantiate.removeAll(instantiated)) {
                throw IllegalStateException("Couldn't resolve ${toInstantiate.size} dependencies!")
            }
        }

        return instantiatedBeans.values
    }

    private fun gatherDependencies(
        dependencies: List<BeanQualifier>,
        instantiatedClasses: MutableMap<BeanQualifier, Any>
    ): Array<Any>? {
        val resolvedDependencies = dependencies.mapNotNull { instantiatedClasses[it] }
        if (dependencies.size != resolvedDependencies.size) return null

        return resolvedDependencies.toTypedArray()
    }

    private data class DependentBean(
        val beanQualifier: BeanQualifier,
        val dependencies: List<BeanQualifier>,
        val constructor: (Array<Any>) -> Any
    )

    private data class BeanQualifier(
        val type: Class<*>,
        val qualifier: String
    )
}
