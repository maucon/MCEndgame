package de.fuballer.mcendgame.component.technical.registry

class KeyedRegistry<T : Keyed> : HashMap<String, T>() {
    fun register(t: T) {
        this[t.key.toString()] = t
    }
}