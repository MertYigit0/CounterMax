package com.example.countermax

import java.io.Serializable

class Counter(val name: String, count: Int = 0) {

    var count: Int = count
        private set

    fun increment() {
        count += 1
    }

    fun decrement() {
        count -= 1
    }
}