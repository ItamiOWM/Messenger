package com.example.itami_chat.contacts_feature.presentation


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import coil.ImageLoader
import com.example.itami_chat.contacts_feature.presentation.contacts.ContactsScreen
import com.example.itami_chat.contacts_feature.presentation.contacts.ContactsViewModel
import com.example.itami_chat.core.presentation.navigation.Graph
import com.example.itami_chat.core.presentation.navigation.Screen


fun NavGraphBuilder.contactsGraph(
    navController: NavController,
    imageLoader: ImageLoader,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        route = Graph.CONTACTS_GRAPH,
        startDestination = Screen.Contacts.fullRoute
    ) {
        composable(
            Screen.Contacts.fullRoute,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(350)
                ).plus(fadeIn(tween(350)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(350)
                ).plus(fadeOut(tween(350)))
            }
        ) {
            val viewModel: ContactsViewModel = hiltViewModel()
            ContactsScreen(
                onShowSnackbar = onShowSnackbar,
                onNavigateBack = {
                    if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                        navController.popBackStack()
                    }
                },
                onNavigateToSearchUsers = {
                    navController.navigate(Screen.SearchUsers.fullRoute) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                onNavigateToUserProfile = { userId ->
                    navController.navigate(Screen.UserProfile.getRouteWithArgs(userId)) {
                        navController.currentDestination?.id?.let { id ->
                            this.popUpTo(id) {
                                saveState = true
                            }
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                imageLoader = imageLoader,
                state = viewModel.state,
                theme = viewModel.theme,
                isDarkMode = viewModel.isDarkMode,
                searchContactsQuery = viewModel.searchContactsQueryState,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent
            )
        }
    }
}
