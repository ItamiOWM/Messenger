package com.example.itami_chat.core.presentation.navigation
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}

//Implementation by https://stackoverflow.com/a/75472964/20298826
typealias NavResultCallback<T> = (T) -> Unit

private const val NavResultCallbackKey = "NavResultCallbackKey"


fun <T> NavController.setNavResultCallback(callback: NavResultCallback<T>) {
    currentBackStackEntry?.savedStateHandle?.set(NavResultCallbackKey, callback)
}


fun <T> NavController.getNavResultCallback(): NavResultCallback<T>? {
    return previousBackStackEntry?.savedStateHandle?.remove(NavResultCallbackKey)
}


fun <T> NavController.popBackStackWithResult(result: T) {
    getNavResultCallback<T>()?.invoke(result)
    popBackStack()
}


fun <T> NavController.navigateForResult(
    route: String,
    navResultCallback: NavResultCallback<T>,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    setNavResultCallback(navResultCallback)
    navigate(route, navOptions, navigatorExtras)
}


fun <T> NavController.navigateForResult(
    route: String,
    navResultCallback: NavResultCallback<T>,
    builder: NavOptionsBuilder.() -> Unit
) {
    setNavResultCallback(navResultCallback)
    navigate(route, builder)
}