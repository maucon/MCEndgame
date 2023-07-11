package de.fuballer.mcendgame.framework

import de.fuballer.mcendgame.framework.stereotype.Injectable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Modifier

object DependencyInjector {
    fun getInjectedObjects(): MutableCollection<Any> {
        val classObjects = getInjectableClassObjects().toMutableSet()
        return instantiateClasses(classObjects)
    }

    private fun getInjectableClassObjects() = getInjectableClassObjects("de.fuballer.mcendgame", mutableSetOf())

    private fun getInjectableClassObjects(
        packageName: String,
        classes: MutableSet<Class<*>>
    ): Set<Class<*>> {
        val systemClassLoader = ClassLoader.getSystemClassLoader()
        val stream = systemClassLoader.getResourceAsStream(packageName.replace("[.]".toRegex(), "/"))!!
        val reader = BufferedReader(InputStreamReader(stream))

        for (line in reader.lines()) {
            if (!line.endsWith(".class")) {
                val subPackageClasses = getInjectableClassObjects("$packageName.$line", classes)
                classes.addAll(subPackageClasses)
                continue
            }

            val clazz = getClass(line, packageName) ?: continue
            if (!Injectable::class.java.isAssignableFrom(clazz)) continue
            if (clazz.isInterface) continue
            if (Modifier.isAbstract(clazz.modifiers)) continue

            classes.add(clazz)
        }

        return classes
    }

    private fun getClass(
        className: String,
        packageName: String
    ): Class<*>? {
        try {
            return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')))
        } catch (_: ExceptionInInitializerError) {
        } catch (_: NoClassDefFoundError) {
        }
        return null
    }

    fun instantiateClasses(classes: MutableSet<Class<*>>): MutableCollection<Any> {
        // zero dependencies
        val map = classes
            .filter { it.constructors[0].parameterCount == 0 }
            .onEach { classes.remove(it) }
            .associateWith { it.constructors[0].newInstance() }
            .toMutableMap()

        while (classes.isNotEmpty()) {
            val done = mutableSetOf<Any>()

            classLoop@ for (clazz in classes) {
                val constructor = clazz.constructors.first()
                val constructorParams = mutableListOf<Any>()

                for (parameterType in constructor.parameterTypes) {
                    val paramType = map[parameterType] ?: continue@classLoop
                    constructorParams.add(paramType)
                }

                map[clazz] = constructor.newInstance(*constructorParams.toTypedArray())
                done.add(clazz)
            }

            if (!classes.removeAll(done)) {
                throw IllegalStateException("Couldn't resolve dependencies!")
            }
        }

        return map.values
    }
}
