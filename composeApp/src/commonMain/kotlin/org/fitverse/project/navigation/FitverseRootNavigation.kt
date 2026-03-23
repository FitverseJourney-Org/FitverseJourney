package org.fitverse.project.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.DatabaseDriverFactory
import com.example.DbHelper
import com.example.data.User
import com.example.data.UserDaoImpl
import kotlinx.coroutines.launch
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.fitverse.project.destinations.onboading.OnboardingDestination
import org.fitverse.project.destinations.splash.SplashDestination
import org.fitverse.project.destinations.trial.TrialDestination
import org.fitverse.project.routes.NavRoutes
import kotlin.collections.listOf



@Composable
fun FitverseRootNavigation(
    dbDriverFactory: DatabaseDriverFactory
) {
    val dbHelper = remember { DbHelper(dbDriverFactory) }
    val userDao = remember { UserDaoImpl(dbHelper) }

    val scope = rememberCoroutineScope()

    var userName by remember { mutableStateOf("") }
    var userPrice by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    // 🔄 Função para recarregar lista
    fun loadUsers() {
        scope.launch {
            users = (userDao.selectAllUsers() ?: emptyList())
        }
    }

    // 🔥 Carrega ao abrir tela
    LaunchedEffect(Unit) {
        loadUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Usuários", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // 🧑 Nome
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 💰 Preço
        OutlinedTextField(
            value = userPrice,
            onValueChange = { userPrice = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ➕ Adicionar
        Button(
            onClick = {
                scope.launch {
                    val price = userPrice.toDoubleOrNull()
                    if (userName.isNotBlank() && price != null) {
                        userDao.insertUser(userName, price)
                        userName = ""
                        userPrice = ""
                        loadUsers()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar usuário")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔎 ID
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🔍 Buscar por ID
        Button(
            onClick = {
                scope.launch {
                    val id = userId.toLongOrNull()
                    if (id != null) {
                        selectedUser = userDao.selectUserById(id)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar usuário")
        }

        selectedUser?.let {
            Text("Selecionado: ${it.name} - R$ ${it.price} (ID: ${it.id})")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ❌ Remover
        Button(
            onClick = {
                scope.launch {
                    val id = userId.toLongOrNull()
                    if (id != null) {
                        userDao.deleteUser(id)
                        selectedUser = null
                        loadUsers()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Remover usuário")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        // 🔄 Atualizar lista manual
        Button(
            onClick = { loadUsers() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Atualizar lista")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Lista de usuários:")

        LazyColumn {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedUser = user
                        }
                        .padding(8.dp)
                ) {
                    Text("${user.name} - R$ ${user.price} (ID: ${user.id})")
                }
            }
        }
    }
}

//@Composable
//fun FitverseRootNavigation(
//    dbHelper: DatabaseDriverFactory
//) {
//
//    val dbHelper = DbHelper(dbHelper)
//    val userDao = UserDaoImpl(dbHelper)
//
//    val scope = rememberCoroutineScope()
//
//
//    val rootBackStack = rememberNavBackStack(
//        SavedStateConfiguration {
//            serializersModule = SerializersModule {
//                polymorphic(NavKey::class) {
//                    subclass(NavRoutes.SplashScreen::class, NavRoutes.SplashScreen.serializer())
//                    subclass(NavRoutes.OnboardingScreen::class, NavRoutes.OnboardingScreen.serializer())
//                    subclass(NavRoutes.TrialScreen::class, NavRoutes.TrialScreen.serializer())
//                    subclass(NavRoutes.AuthFlow::class, NavRoutes.AuthFlow.serializer())
//                    subclass(NavRoutes.HomeFlow::class, NavRoutes.HomeFlow.serializer())
//                    subclass(NavRoutes.WorkoutFlow::class, NavRoutes.WorkoutFlow.serializer())
//                }
//            }
//        },
//        NavRoutes.SplashScreen
//    )
//
//    NavDisplay(
//        modifier = Modifier,
//        backStack = rootBackStack,
//        entryDecorators = listOf(
//            rememberSaveableStateHolderNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator()
//        ),
//        transitionSpec = {
//            EnterTransition.None togetherWith ExitTransition.None
//        },
//        popTransitionSpec = {
//            // Slide in from left when navigating back
//            slideInHorizontally(initialOffsetX = { -it }) togetherWith
//                    slideOutHorizontally(targetOffsetX = { it })
//        },
//        predictivePopTransitionSpec = {
//            // Slide in from left when navigating back
//            slideInHorizontally(initialOffsetX = { -it }) togetherWith
//                    slideOutHorizontally(targetOffsetX = { it })
//        },
//        entryProvider = entryProvider {
//            entry<NavRoutes.SplashScreen>{
//                SplashDestination(
//                    toLogin = {
//                        rootBackStack.add(NavRoutes.AuthFlow)
//                    },
//                    toTrial = {
//                        rootBackStack.add(NavRoutes.TrialScreen)
//                    },
//                    toHome = {
//                        rootBackStack.clear()
//                        rootBackStack.add(NavRoutes.HomeFlow)
//                    },
//                    toOnboarding = {
//                        rootBackStack.add(NavRoutes.OnboardingScreen)
//                    }
//                )
//            }
//            entry<NavRoutes.OnboardingScreen>{
//                OnboardingDestination(
//                    toLogin = {
//
//                    },
//                    toTrial = {
//
//                    }
//                )
//            }
//            entry<NavRoutes.TrialScreen>{
//                TrialDestination(
//                    toLogin = {
//
//                    },
//                )
//            }
//            entry<NavRoutes.AuthFlow>{
//                AuthNavigation(
//                    toHomeFlow = {
//                        rootBackStack.clear()
//                        rootBackStack.add(NavRoutes.HomeFlow)
//                    }
//                )
//            }
//            entry<NavRoutes.HomeFlow>{
//                HomeNavigation(
//                    onLogout = {
//                        rootBackStack.clear()
//                        rootBackStack.add(NavRoutes.AuthFlow)
//                    }
//                )
//            }
//        }
//    )
//}