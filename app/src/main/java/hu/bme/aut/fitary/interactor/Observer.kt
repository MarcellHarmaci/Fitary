package hu.bme.aut.fitary.interactor

interface Observer<T> {
    fun notify(newValue: T)
}