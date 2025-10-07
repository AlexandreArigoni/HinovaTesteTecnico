package com.example.hinovateste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hinovateste.data.local.UserPreferences
import com.example.hinovateste.data.remote.ApiService
import com.example.hinovateste.data.remote.AutoShopDto
import com.example.hinovateste.data.repository.AutoShopRepository
import com.example.hinovateste.data.repository.UserRepository
import com.example.hinovateste.domain.usecase.ValidateLoginUseCase
import com.example.hinovateste.ui.autoShopDetails.AutoShopDetailsScreen
import com.example.hinovateste.ui.login.LoginScreen
import com.example.hinovateste.ui.login.LoginViewModel
import com.example.hinovateste.ui.login.LoginViewModelFactory
import com.example.hinovateste.ui.autoShops.AutoShopViewModel
import com.example.hinovateste.ui.principal.PrincipalScreen
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    private val api by lazy { ApiService.create() }
    private val repo by lazy { AutoShopRepository(api) }
    private lateinit var prefs: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = UserPreferences(applicationContext)

        setContent {
            val navController = rememberNavController()

            MaterialTheme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        val context = LocalContext.current
                        val userRepo = remember { UserRepository() }
                        val validate = remember { ValidateLoginUseCase() }

                        val loginVm: LoginViewModel = viewModel(
                            factory = LoginViewModelFactory(
                                repository = userRepo,
                                validate = validate,
                                prefs = prefs,
                                context = context.applicationContext
                            )
                        )

                        LoginScreen(viewModel = loginVm) {
                            navController.navigate("main") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                    
                    composable("main") {
                        val autoShopsVm = remember { AutoShopViewModel(repo) }

                        PrincipalScreen(
                            autoShopViewModel = autoShopsVm,
                            onOpenDetails = { autoShop ->
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("autoShops", autoShop)
                                navController.navigate("details")
                            }
                        )
                    }

                    composable("details") {
                        val autoShopDto: AutoShopDto? = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<AutoShopDto>("autoShops")
                        AutoShopDetailsScreen(
                            autoShop = autoShopDto,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
