package com.example.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.ui.theme.FitTrackTheme
import kotlinx.serialization.Serializable

val exerciseTypes = listOf("Strength", "Cardio", "Flexibility", "Yoga")

@Serializable
object DashBoardRoute

@Serializable
object StrengthRoute

@Serializable
object CardioRoute



class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitTrackTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                FitTrackApp(windowSizeClass)
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FitTrackApp(windowSizeClass: WindowSizeClass) {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        val canNavigateBack = navController.previousBackStackEntry != null

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when {
                                currentDestination?.contains("DashBoardRoute") == true -> "Dashboard"
                                currentDestination?.contains("StrengthRoute") == true -> "Strength Exercises"
                                currentDestination?.contains("CardioRoute") == true -> "Cardio Exercises"
                                currentDestination?.contains("FlexRoute") == true -> "Flexibility Exercises"
                                currentDestination?.contains("YogaRoute") == true -> "Yoga Exercises"
                                else -> ""
                            }
                        )
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = { navController.navigateUp()}) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Navigate back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = DashBoardRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<DashBoardRoute> {
                    DashboardScreen(
                        exerciseTypes = exerciseTypes,
                        windowSizeClass = windowSizeClass,
                        onUserClick = {}

                    )
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    exerciseTypes: List<String>,
    windowSizeClass: WindowSizeClass,
    onUserClick: (String) -> Unit
) {

}

@Composable
fun ExerciseList(
    exerciseTypes: List<String>,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(exerciseTypes) { exerciseType ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable{ onUserClick(exerciseType) },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = exerciseType,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}


