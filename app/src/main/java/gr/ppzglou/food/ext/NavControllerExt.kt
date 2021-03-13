package gr.ppzglou.food.ext

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import gr.ppzglou.food.R

fun defaultNavOptions(popToId: Int? = null, inclusive: Boolean = false): NavOptions {
    val navOptionsBuilder = NavOptions.Builder()
        .setEnterAnim(R.anim.enter_from_right)
        .setExitAnim(R.anim.exit_to_left)
        .setPopEnterAnim(R.anim.enter_from_left)
        .setPopExitAnim(R.anim.exit_to_right)

    // check popTo
    popToId?.let {
        navOptionsBuilder.setPopUpTo(it, inclusive)
    }

    return navOptionsBuilder.build()
}

fun defaultNavOptionsOnBack(popToId: Int? = null, inclusive: Boolean = false): NavOptions {
    val navOptionsBuilder = NavOptions.Builder()
        .setEnterAnim(R.anim.enter_from_left)
        .setExitAnim(R.anim.exit_to_right)
    // check popTo
    popToId?.let {
        navOptionsBuilder.setPopUpTo(it, inclusive)
    }

    return navOptionsBuilder.build()
}


/**
 ** prevent quick-double-tap crash
 **/
fun NavController.safeNavigate(
    action: NavDirections,
    currentId: Int,
) {
    if (currentDestination?.id == currentId) {
        navigate(action, defaultNavOptions())
    }
}

fun NavController.safeNavigateOnBack(
    action: NavDirections,
    currentId: Int,
) {
    if (currentDestination?.id == currentId) {
        navigate(action, defaultNavOptionsOnBack())
    }
}
