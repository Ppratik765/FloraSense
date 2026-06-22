package com.priyanshu.floralens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.priyanshu.floralens.classifier.TFLiteImageClassifier
import com.priyanshu.floralens.ui.screens.HistoryScreen
import com.priyanshu.floralens.ui.screens.ScanScreen
import com.priyanshu.floralens.ui.screens.WelcomeScreen
import com.priyanshu.floralens.ui.theme.BotanicalGreen
import com.priyanshu.floralens.ui.theme.FloraLensTheme
import com.priyanshu.floralens.ui.theme.LeafGreen
import com.priyanshu.floralens.ui.theme.PastelGreenCard
import com.priyanshu.floralens.ui.theme.PremiumWhite
import com.priyanshu.floralens.ui.theme.TextDark
import com.priyanshu.floralens.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val classifier = TFLiteImageClassifier(this)
        viewModel.setClassifier(classifier)

        setContent {
            FloraLensTheme {
                FloraLensApp(viewModel)
            }
        }
    }
}

@Composable
fun FloraLensApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar on scan screen
    val bottomBarVisible = currentRoute != "scan"

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                FloraLensBottomBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            // We don't apply innerPadding to the NavHost itself so ScanScreen can be edge-to-edge
            // The individual screens that need padding will handle it, or we apply it dynamically.
            modifier = Modifier.fillMaxSize()
        ) {
            composable("welcome") {
                // Apply padding only to non-fullscreen screens
                Modifier.padding(innerPadding).let {
                    WelcomeScreen()
                }
            }
            composable("scan") {
                ScanScreen(viewModel = viewModel)
            }
            composable("history") {
                Modifier.padding(innerPadding).let {
                    HistoryScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun FloraLensBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = PremiumWhite,
        contentColor = LeafGreen
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "welcome",
            onClick = {
                navController.navigate("welcome") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LeafGreen,
                unselectedIconColor = TextDark,
                indicatorColor = PastelGreenCard
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.CameraAlt, contentDescription = "Scan") },
            label = { Text("Scan", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "scan",
            onClick = {
                navController.navigate("scan") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LeafGreen,
                unselectedIconColor = TextDark,
                indicatorColor = PastelGreenCard
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "history",
            onClick = {
                navController.navigate("history") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = LeafGreen,
                unselectedIconColor = TextDark,
                indicatorColor = PastelGreenCard
            )
        )
    }
}