package org.orbitmvi.orbit.sample.posts

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}