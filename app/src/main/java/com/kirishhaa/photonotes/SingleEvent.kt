package com.kirishhaa.photonotes

class SingleEvent<T>(
    value: T
) {

    private var currentValue: T? = value

    fun getValue(): T? {
        val v = currentValue
        if (v != null) {
            currentValue = null
            return v
        }
        return null
    }

}