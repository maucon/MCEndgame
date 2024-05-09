package de.fuballer.mcendgame.util.extension

object ListExtension {
    /**
     * Returns an iterable that cycles through the elements of this list indefinitely.
     * If the list is empty, the returned iterable will also be empty.
     * If the list has only one element, the returned iterable will continuously yield that element.
     *
     * @return An iterable that cycles through the elements of this list.
     */
    fun <T> List<T>.cycle(): Iterable<T> {
        return object : Iterable<T> {

            override fun iterator(): Iterator<T> {

                return object : Iterator<T> {
                    var index = 0

                    override fun hasNext(): Boolean {
                        return this@cycle.isNotEmpty()
                    }

                    override fun next(): T {
                        val element = this@cycle[index % size]
                        index++
                        return element
                    }
                }
            }
        }
    }
}