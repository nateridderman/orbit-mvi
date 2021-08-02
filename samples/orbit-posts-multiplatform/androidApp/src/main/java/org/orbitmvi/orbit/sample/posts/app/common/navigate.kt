package org.orbitmvi.orbit.sample.posts.app.common

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

fun NavController.navigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    args: List<Pair<String, Parcelable>>? = null,
) {
    if (args == null || args.isEmpty()) {
        navigate(route, navOptions, navigatorExtras)
        return
    }
    navigate(route, navOptions, navigatorExtras)
    val addedEntry: NavBackStackEntry = backQueue.last()
    val argumentBundle: Bundle = addedEntry.arguments ?: Bundle().also {
        addedEntry.arguments = it
    }
    args.forEach { (key, arg) ->
        argumentBundle.putParcelable(key, arg)
    }
}

inline fun <reified T : Parcelable> NavController.navigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
    arg: T? = null,

) {
    if (arg == null) {
        navigate(route, navOptions, navigatorExtras)
        return
    }
    navigate(
        route = route,
        navOptions = navOptions,
        navigatorExtras = navigatorExtras,
        args = listOf(T::class.qualifiedName!! to arg),
    )
}

fun NavBackStackEntry.requiredArguments(): Bundle = arguments ?: throw IllegalStateException("Arguments were expected, but none were provided!")

@Composable
inline fun <reified T : Parcelable> NavBackStackEntry.rememberRequiredArgument(
    key: String = T::class.qualifiedName!!,
): T = remember {
    requiredArguments().getParcelable<T>(key) ?: throw IllegalStateException("Expected argument with key: $key of type: ${T::class.qualifiedName!!}")
}

@Composable
inline fun <reified T : Parcelable> NavBackStackEntry.rememberArgument(
    key: String = T::class.qualifiedName!!,
): T? = remember {
    arguments?.getParcelable(key)
}
