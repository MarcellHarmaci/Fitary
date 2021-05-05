package hu.bme.aut.fitary.interactor

interface Observable<T> {
    val observers: MutableList<Observer<T>>

    open fun addObserver(observer: Observer<T>) {
        observers += observer
    }

    fun removeObserver(observer: Observer<T>) {
        observers.remove(observer)
    }

    fun notifyObservers(newValue: T) {
        observers.forEach { it.notify(newValue) }
    }
}