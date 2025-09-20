package org.avium.lockscreenedit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.avium.lockscreenedit.edit.LockscreenEditScreen
import org.avium.lockscreenedit.list.LockscreenListScreen
import org.avium.lockscreenedit.ui.theme.LockScreenEditTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LockScreenEditTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "list") {
                        composable("list") {
                            LockscreenListScreen(navController, activity = this@MainActivity)
                        }
                        composable(
                            "edit/{styleId}",
                            arguments = listOf(navArgument("styleId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val styleId = backStackEntry.arguments?.getInt("styleId") ?: 0
                            LockscreenEditScreen(navController, styleId)
                        }
                    }
                }
            }
        }
    }
}