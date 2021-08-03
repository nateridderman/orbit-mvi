package org.orbitmvi.orbit.sample.posts.app.common

import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview

private const val FLAGS = Base64.NO_WRAP or Base64.NO_PADDING or Base64.URL_SAFE

fun <T : Parcelable> String.toRouteParcelable(): T {
    @Suppress("UNCHECKED_CAST")
    return parcel {
        val serializedBytes = Base64.decode(this@toRouteParcelable, FLAGS)
        unmarshall(serializedBytes, 0, serializedBytes.size)
        setDataPosition(0)
        readParcelable<Parcelable>(PostOverview::class.java.classLoader) as T
    }
}

fun Parcelable.toRouteString(): String {
    return parcel {
        writeParcelable(this@toRouteString, 0)
        Base64.encode(marshall(), FLAGS).toString(Charsets.UTF_8)
    }
}

private fun <T> parcel(block: Parcel.() -> T): T {
    return Parcel.obtain().run {
        block().also {
            recycle()
        }
    }
}
