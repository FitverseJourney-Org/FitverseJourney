package org.fitverse.project.navigation

import HistoricDestination
import LeaderboardsDestination
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
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
import com.example.expect.DateTimeManager
import com.example.expect.PlatformDateFormatter
import com.example.presentation.screens.ui.friends.viewmodel.FriendsViewModel
import com.example.presentation.screens.ui.progress.viewmodel.ProgressViewModel
import com.example.presentation.screens.ui.wiki.viewmodel.WikiViewModel
import com.example.presentation.screens.ui.workout.PastWorkoutLog
import com.example.presentation.screens.ui.workout.SetRecord
import com.example.presentation.screens.ui.workout.WorkoutAnalyzer
import com.example.presentation.screens.ui.workout.WorkoutCompletionScreen
import com.example.presentation.screens.ui.workout.WorkoutDataSource
import com.example.presentation.theme.DarkGamifiedColors
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.homepage.community.AddPostDestination
import org.fitverse.project.destinations.homepage.community.CommunityDestination
import org.fitverse.project.destinations.homepage.dashboad.DashboardDestination
import org.fitverse.project.destinations.homepage.dashboad.NotificationDestination
import org.fitverse.project.destinations.homepage.meals.MealsDestination
import org.fitverse.project.destinations.homepage.profile.ProfileDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutDestination
import org.fitverse.project.destinations.homepage.workout.WorkoutSessionDestination
import org.fitverse.project.destinations.modal_destinations.achievement.AchievementDestination
import org.fitverse.project.destinations.modal_destinations.device.DevicesDestination
import org.fitverse.project.destinations.modal_destinations.friends.FriendsDestination
import org.fitverse.project.destinations.modal_destinations.helpSupport.HelpSupportDestination
import org.fitverse.project.destinations.modal_destinations.progress.ProgressDestination
import org.fitverse.project.destinations.payments.PlanDestination
import org.fitverse.project.destinations.wiki.WikiFitnessDestination
import org.fitverse.project.routes.NavRoutes
import org.koin.compose.koinInject

@Composable
fun HomeNavigation(
    onLogout: () -> Unit
) {
    val rootBackStack = rememberNavBackStack(
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(NavRoutes.HomeFlowMenu.Dashboard::class, NavRoutes.HomeFlowMenu.Dashboard.serializer())
                    subclass(NavRoutes.HomeFlowMenu.Workout::class, NavRoutes.HomeFlowMenu.Workout.serializer())
                    subclass(NavRoutes.HomeFlowMenu.Community::class, NavRoutes.HomeFlowMenu.Community.serializer())
                    subclass(NavRoutes.HomeFlowMenu.Meals::class, NavRoutes.HomeFlowMenu.Meals.serializer())
                    subclass(NavRoutes.HomeFlowMenu.Profile::class, NavRoutes.HomeFlowMenu.Profile.serializer())
                    subclass(NavRoutes.PlanWorkoutFlow::class, NavRoutes.PlanWorkoutFlow.serializer())
                    subclass(NavRoutes.TasksFlow::class, NavRoutes.TasksFlow.serializer())
                    subclass(NavRoutes.PlanPaymentScreen::class, NavRoutes.PlanPaymentScreen.serializer())
                    subclass(NavRoutes.Shopping::class, NavRoutes.Shopping.serializer())
                    subclass(NavRoutes.Friends::class, NavRoutes.Friends.serializer())
                    subclass(NavRoutes.Leaderboards::class, NavRoutes.Leaderboards.serializer())
                    subclass(NavRoutes.Historic::class, NavRoutes.Historic.serializer())
                    subclass(NavRoutes.Progress::class, NavRoutes.Progress.serializer())
                    subclass(NavRoutes.Achievements::class, NavRoutes.Achievements.serializer())
                    subclass(NavRoutes.Devices::class, NavRoutes.Devices.serializer())
                    subclass(NavRoutes.HelpSupport::class, NavRoutes.HelpSupport.serializer())
                    subclass(NavRoutes.HomeFlow.NotificationScreen::class, NavRoutes.HomeFlow.NotificationScreen.serializer())
                    subclass(NavRoutes.HomeFlow.AddPost::class, NavRoutes.HomeFlow.AddPost.serializer())
                    subclass(NavRoutes.WorkoutFlow.WorkoutSession::class, NavRoutes.WorkoutFlow.WorkoutSession.serializer())
                    subclass(NavRoutes.WorkoutFlow.Workout::class, NavRoutes.WorkoutFlow.Workout.serializer())
                    subclass(NavRoutes.WorkoutFlow.WorkoutCompleted::class, NavRoutes.WorkoutFlow.WorkoutCompleted.serializer())
                    subclass(NavRoutes.WikiFitness::class, NavRoutes.WikiFitness.serializer())
                }
            }
        },
        NavRoutes.HomeFlowMenu.Dashboard
    )

    val bottomBarItems = listOf(
        NavRoutes.HomeFlowMenu.Dashboard,
        NavRoutes.HomeFlowMenu.Workout,
        NavRoutes.HomeFlowMenu.Community,
        NavRoutes.HomeFlowMenu.Meals,
        NavRoutes.HomeFlowMenu.Profile,
    )


    val currentKey = rootBackStack.lastOrNull()
    val isMainScreen = when (currentKey) {
        is NavRoutes.HomeFlowMenu.Dashboard,
        is NavRoutes.HomeFlowMenu.Workout,
        is NavRoutes.HomeFlowMenu.Community,
        is NavRoutes.HomeFlowMenu.Meals,
        is NavRoutes.HomeFlowMenu.Profile -> true
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
                        is NavRoutes.HomeFlowMenu.Dashboard,
                        is NavRoutes.HomeFlowMenu.Workout,
                        is NavRoutes.HomeFlowMenu.Community,
                        is NavRoutes.HomeFlowMenu.Meals,
                        is NavRoutes.HomeFlowMenu.Profile-> true
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
                        entry<NavRoutes.HomeFlowMenu.Dashboard> {
                            DashboardDestination(
                                toNotification = {
                                    rootBackStack.add(NavRoutes.HomeFlow.NotificationScreen)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlowMenu.Workout> {
                            WorkoutDestination(
                                toWorkoutSession = {
                                    rootBackStack.add(NavRoutes.WorkoutFlow.WorkoutSession)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlowMenu.Community> {
                            CommunityDestination(
                                toAddPost = {
                                    rootBackStack.add(NavRoutes.HomeFlow.AddPost)
                                }
                            )
                        }
                        entry<NavRoutes.HomeFlowMenu.Meals> {
                            MealsDestination(
                                toAddMeal = {

                                }
                            )
                        }
                        entry<NavRoutes.HomeFlowMenu.Profile>{
                            ProfileDestination()
                        }
                        entry<NavRoutes.HomeFlow.AddPost> {
                            AddPostDestination(
                                toBack = { rootBackStack.removeLastOrNull() }
                            )
                        }
                        entry<NavRoutes.WorkoutFlow.WorkoutSession> {
                            WorkoutSessionDestination(
                                toCompletedWorkout = {
                                    rootBackStack.add(NavRoutes.WorkoutFlow.WorkoutCompleted)
                                },
                            )
                        }
                        entry<NavRoutes.WorkoutFlow.WorkoutCompleted> {
                            WorkoutCompletedDestination(
                                toBack = {
                                    rootBackStack.clear()
                                    rootBackStack.add(NavRoutes.HomeFlowMenu.Workout)

                                },

                            )
                        }
                        entry<NavRoutes.HomeFlow.NotificationScreen> {
                            NotificationDestination(
                                toDashboard = {
                                    rootBackStack.removeLastOrNull()
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
                            val viewmodel = koinInject<FriendsViewModel>()

                            FriendsDestination(
                                onBack = { rootBackStack.removeLastOrNull() },
                                viewModel = viewmodel,
                                onNavigateToQrScanner = {
//                                    rootBackStack.add(NavRoutes.Friends.QrScanner)
                                }
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
                            val viewmodel = koinInject<ProgressViewModel>()

                            ProgressDestination(
                                viewmodel = viewmodel,
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
                        entry<NavRoutes.Shopping>{
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                Text("SHOPPING")
                            }
                        }
                        entry<NavRoutes.WikiFitness>{
                            val viewmodel = koinInject<WikiViewModel>()


                            WikiFitnessDestination(
                                onNavigateToArticle = {

                                },
                                viewModel = viewmodel,
                                onBack = {
                                    rootBackStack.removeLastOrNull()
                                }
                            )
                        }
                    }
                )
            }
        }
    )
}
@Composable
fun WorkoutCompletedDestination(
    toBack: () -> Unit,
    shareWorkout: () -> Unit = {},
    viewStatsGraph: () -> Unit = {}
) {
    // Calculamos um timestamp de 7 dias atrás para o histórico
    val oneWeekAgo = DateTimeManager.nowMillis() - (7 * 24 * 60 * 60 * 1000)

    val exerciseHistory = mapOf(
        1 to listOf( // Supino Reto
            PastWorkoutLog(
                id = "hist_1",
                exerciseId = 1,
                exerciseName = "Supino Reto",
                totalVolume = 1000f,
                timestamp = oneWeekAgo,
                date = PlatformDateFormatter.formatFullDate(oneWeekAgo),
                // Corrigido: setNumber (Int), weight (String), reps (String), isCompleted (Boolean)
                sets = listOf(
                    SetRecord(setNumber = 1, weight = "50", reps = "10", isCompleted = true),
                    SetRecord(setNumber = 2, weight = "50", reps = "10", isCompleted = true)
                ),
                personalRecord = false
            )
        ),
        2 to listOf( // Agachamento
            PastWorkoutLog(
                id = "hist_2",
                exerciseId = 2,
                exerciseName = "Agachamento",
                totalVolume = 1200f,
                timestamp = oneWeekAgo,
                date = PlatformDateFormatter.formatFullDate(oneWeekAgo),
                sets = listOf(
                    SetRecord(setNumber = 1, weight = "60", reps = "10", isCompleted = true),
                    SetRecord(setNumber = 2, weight = "60", reps = "10", isCompleted = true)
                ),
                personalRecord = false
            )
        )
    )

    // O restante do código (allExerciseSets e analyzeWorkout) permanece igual
    val allExerciseSets = mapOf(
        1 to listOf(
            SetRecord(weight = "60", reps = "10", isCompleted = true, setNumber = 2),
            SetRecord(weight = "60", reps = "10", isCompleted = true, setNumber = 2),
            SetRecord(weight = "65", reps = "8", isCompleted = true, setNumber = 2)
        ),
        2 to listOf(
            SetRecord(weight = "60", reps = "12", isCompleted = true, setNumber = 2),
            SetRecord(weight = "60", reps = "12", isCompleted = true, setNumber = 2),
            SetRecord(weight = "60", reps = "12", isCompleted = true, setNumber = 2)
        )
    )

    val result = WorkoutAnalyzer.analyzeWorkout(
        workoutPlan = WorkoutDataSource.getMockWorkoutPlan(),
        allExerciseSets = allExerciseSets,
        exerciseHistory = exerciseHistory,
        durationSeconds = 2100,
        totalXp = 150
    )

    WorkoutCompletionScreen(
        result = result,
        onContinue = toBack,
        onShareWorkout = shareWorkout,
        onViewStats = viewStatsGraph
    )
}


fun handleHomeBackPress(homeBackStack: MutableList<NavKey>): Boolean {
    val current = homeBackStack.lastOrNull() ?: return false

    return when (current) {
        is NavRoutes.HomeFlow.Dashboard -> {
            false
        }
        is NavRoutes.HomeFlowMenu.Workout,
        is NavRoutes.HomeFlowMenu.Meals,
        is NavRoutes.HomeFlowMenu.Community -> {
            homeBackStack.clear()
            homeBackStack.add(NavRoutes.HomeFlowMenu.Dashboard)
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
        color = DarkGamifiedColors.PrimarySoft.copy(alpha = 0.25f),
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
                    is NavRoutes.HomeFlowMenu.Dashboard -> Triple("Home", Icons.Rounded.Home, colors.secondary) // Azul: Dados/Visão Geral
                    is NavRoutes.HomeFlowMenu.Workout -> Triple("Treino", Icons.Rounded.FitnessCenter, colors.secondary) // Roxo: Força/Ação
                    is NavRoutes.HomeFlowMenu.Community -> Triple("Social", Icons.Rounded.Groups, colors.secondary) // Roxo: Clã/Equipe
                    is NavRoutes.HomeFlowMenu.Meals -> Triple("Dieta", Icons.Rounded.Restaurant, colors.secondary) // Verde: Nutrição/Saúde
                    is NavRoutes.HomeFlowMenu.Profile -> Triple("Perfil", Icons.Rounded.Person, colors.secondary) // Azul: Atributos
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