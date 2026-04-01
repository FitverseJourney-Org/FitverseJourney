package org.fitverse.project.navigation

import HistoricDestination
import LeaderboardsDestination
import MealsPlanBuilderScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.presentation.screens.ui.planMeals.MealPlanScreenState
import com.example.presentation.screens.ui.planMeals.PlanMealsListScreen
import com.example.presentation.theme.DarkGamifiedColors
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.modal_destinations.achievement.AchievementDestination
import org.fitverse.project.destinations.homepage.community.AddPostDestination
import org.fitverse.project.destinations.homepage.community.CommunityDestination
import org.fitverse.project.destinations.homepage.dashboad.DashboardDestination
import org.fitverse.project.destinations.modal_destinations.device.DevicesDestination
import org.fitverse.project.destinations.modal_destinations.helpSupport.HelpSupportDestination
import org.fitverse.project.destinations.homepage.meals.MealsDestination
import org.fitverse.project.destinations.homepage.dashboad.NotificationDestination
import org.fitverse.project.destinations.homepage.profile.ProfileDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutDestination
import org.fitverse.project.destinations.modal_destinations.friends.FriendsDestination
import org.fitverse.project.destinations.modal_destinations.progress.ProgressDestination
import org.fitverse.project.destinations.payments.PlanDestination
import org.fitverse.project.routes.NavRoutes

@Composable
fun HomeNavigation(
    onLogout: () -> Unit
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlow.Dashboard::class, NavRoutes.HomeFlow.Dashboard.serializer())
                    subclass(NavRoutes.HomeFlow.Workout::class, NavRoutes.HomeFlow.Workout.serializer())
                    subclass(NavRoutes.HomeFlow.Meals::class, NavRoutes.HomeFlow.Meals.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow::class, NavRoutes.PlanWorkoutFlow.serializer())
                    subclass(NavRoutes.TasksFlow::class, NavRoutes.TasksFlow.serializer())
                    subclass(NavRoutes.PlanPaymentScreen::class, NavRoutes.PlanPaymentScreen.serializer())
                    subclass(NavRoutes.HomeFlow.Profile::class, NavRoutes.HomeFlow.Profile.serializer())
                }
            }
        },
        NavRoutes.HomeFlow.Dashboard
    )

    val bottomBarItems = listOf(
        NavRoutes.HomeFlow.Dashboard,
        NavRoutes.HomeFlow.Workout,
        NavRoutes.HomeFlow.Community,
        NavRoutes.HomeFlow.Meals,
        NavRoutes.HomeFlow.Profile,
    )


    val currentKey = rootBackStack.lastOrNull()
    val isMainScreen = when (currentKey) {
        is NavRoutes.HomeFlow.Dashboard,
        is NavRoutes.HomeFlow.Workout,
        is NavRoutes.HomeFlow.Community,
        is NavRoutes.HomeFlow.Meals,
        is NavRoutes.HomeFlow.Profile -> true
        else -> false
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalDrawerSheetMainScreen(
        drawerState = if(isMainScreen) drawerState else rememberDrawerState(initialValue = DrawerValue.Closed),
        onNavigate = {
            rootBackStack.add(it)
        },
        onLogout = onLogout,
        gesturesEnabled = isMainScreen,
        content = {
            Scaffold(
                modifier = Modifier,
                bottomBar = {
                    val currentKey = rootBackStack.lastOrNull()

                    val showBottomBar = when (currentKey) {
                        is NavRoutes.HomeFlow.Dashboard,
                        is NavRoutes.HomeFlow.Workout,
                        is NavRoutes.HomeFlow.Community,
                        is NavRoutes.HomeFlow.Meals,
                        is NavRoutes.HomeFlow.Profile-> true
                        else -> false
                    }

                    AnimatedVisibility(
                        visible = showBottomBar,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = FastOutSlowInEasing
                            )
                        ),
                        exit = fadeOut(
                            animationSpec = tween(
                                durationMillis = 200
                            )
                        )
                    ) {
                        FitVerseBottomBar(
                            items = bottomBarItems,
                            backStack = rootBackStack
                        )
                    }
                }
            ){
                NavDisplay(
                    modifier = Modifier.padding(it),
                    backStack = rootBackStack,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    onBack = {
                        val consumed = handleHomeBackPress(rootBackStack)
                        if (!consumed) {
                            rootBackStack.removeLastOrNull()
                        }

                    },
                    entryProvider = entryProvider {
                        entry<NavRoutes.HomeFlow.Dashboard> {
                            DashboardDestination(
                                toNotification = {
                                    rootBackStack.add(NavRoutes.HomeFlow.NotificationScreen)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Workout> {
                            WorkoutDestination(
                                toWorkoutSession = {}
                            )
                        }
                        entry<NavRoutes.HomeFlow.Community> {
                            CommunityDestination(
                                toAddPost = {
                                    rootBackStack.add(NavRoutes.HomeFlow.AddPost)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Meals> {
                            MealsDestination(
                                toAddMeal = {
                                    rootBackStack.add(NavRoutes.PlanMealsFlow.ListMeals)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlow.Profile>{
                            ProfileDestination()
                        }
                        entry<NavRoutes.HomeFlow.AddPost> {
                            AddPostDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.HomeFlow.NotificationScreen> {
                            NotificationDestination(
                                toDashboard = {
                                    rootBackStack.clear()
                                    rootBackStack.add(NavRoutes.HomeFlow.Dashboard)
                                }
                            )
                        }
                        entry<NavRoutes.PlanWorkoutFlow> {
                            PlanWorkoutNavigation(
                                toBack = {
                                    rootBackStack.removeLastOrNull()
                                }
                            )
                        }
                        entry<NavRoutes.TasksFlow> {
                            TasksNavigation(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Friends>{
                            FriendsDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Leaderboards>{
                            LeaderboardsDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Historic>{
                            HistoricDestination(
                                navigateBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Progress>{
                            ProgressDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Achievements>{
                            AchievementDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.Devices>{
                            DevicesDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.HelpSupport>{
                            HelpSupportDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.PlanPaymentScreen>{
                            PlanDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.PlanMealsFlow.PlanList>{
                            PlanMealsListScreen(
                                state = MealPlanScreenState(),
                                onBack = {
                                    rootBackStack.removeLastOrNull()
                                },
                                onNewPlanClick = {
                                    rootBackStack.add(NavRoutes.PlanMealsFlow.PlanBuilder)
                                },
                                onPlanClick = {
                                    rootBackStack.add(NavRoutes.PlanMealsFlow.PlanBuilder)
                                }
                            )
                        }
                        entry<NavRoutes.PlanMealsFlow.PlanBuilder>{
                            MealsPlanBuilderScreen(
                                onBackClick = { rootBackStack.removeLastOrNull() },
                                onSavePlan = { rootBackStack.removeLastOrNull() },
                                onAddFoodClick = {

                                },
                            )
                        }

                    }
                )
            }
        }
    )
}



fun handleHomeBackPress(homeBackStack: MutableList<NavKey>): Boolean {
    val current = homeBackStack.lastOrNull() ?: return false

    return when (current) {
        is NavRoutes.HomeFlow.Dashboard -> {
            false
        }
        is NavRoutes.HomeFlow.Workout,
        is NavRoutes.HomeFlow.Meals,
        is NavRoutes.HomeFlow.Community -> {
            homeBackStack.clear()
            homeBackStack.add(NavRoutes.HomeFlow.Dashboard)
            true
        }
        else -> {
            homeBackStack.removeLastOrNull()
            true
        }
    }

}
@Composable
fun FitVerseBottomBar(
    items: List<NavKey>,
    backStack: NavBackStack<NavKey>
) {
    val currentDestination = backStack.lastOrNull()
    val haptic = LocalHapticFeedback.current

    // Container com efeito de "Vidro Fumê"
    Surface(
        // Cor de fundo levemente translúcida para deixar o gradiente do fundo passar
        color = DarkGamifiedColors.Surface.copy(alpha = 0.92f),
        // Borda superior sutil usando PrimarySoft para o efeito de "linha de energia"
        border = BorderStroke(
            width = 0.5.dp,
            brush = Brush.horizontalGradient(
                listOf(
                    Color.Transparent,
                    DarkGamifiedColors.PrimarySoft.copy(alpha = 0.3f),
                    Color.Transparent
                )
            )
        ),
        shadowElevation = 20.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // O fundo do Scaffold ou do Background controla
            tonalElevation = 0.dp,
            modifier = Modifier
                .navigationBarsPadding()
                .height(80.dp)
        ) {
            val colors = MaterialTheme.colorScheme

            items.forEach { item ->
                val isSelected = currentDestination == item

                // Mapeamento de Cores e Identidade por Seção
                val (label, icon, activeColor) = when (item) {
                    is NavRoutes.HomeFlow.Dashboard -> Triple("Home", Icons.Rounded.Home, colors.secondary) // Azul: Dados/Visão Geral
                    is NavRoutes.HomeFlow.Workout -> Triple("Treino", Icons.Rounded.FitnessCenter, colors.primary) // Roxo: Força/Ação
                    is NavRoutes.HomeFlow.Community -> Triple("Social", Icons.Rounded.Groups, colors.primary) // Roxo: Clã/Equipe
                    is NavRoutes.HomeFlow.Meals -> Triple("Dieta", Icons.Rounded.Restaurant, colors.tertiary) // Verde: Nutrição/Saúde
                    is NavRoutes.HomeFlow.Profile -> Triple("Perfil", Icons.Rounded.Person, colors.secondary) // Azul: Atributos
                    else -> Triple("", Icons.Rounded.Block, colors.outline)
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            backStack.add(item)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(
                            text = label.uppercase(), // Uppercase para estilo Gamer
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        // O ícone selecionado brilha com a cor do domínio
                        selectedIconColor = activeColor,
                        selectedTextColor = activeColor,
                        // Indicador (Pill): Sutil para manter o efeito de "vidro" do fundo
                        indicatorColor = activeColor.copy(alpha = 0.1f),
                        // Não selecionado: OnSurfaceVariant (Cinza Muted)
                        unselectedIconColor = colors.onSurfaceVariant.copy(alpha = 0.5f),
                        unselectedTextColor = colors.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}