package com.example.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.fittrack.ui.theme.FitTrackTheme
import kotlinx.serialization.Serializable
import androidx.compose.ui.text.style.TextAlign

data class ExerciseType(
    val name: String,
    val description: String
)
val exerciseTypes = listOf(
    ExerciseType("Strength", "Build muscle and increase physical strength."),
    ExerciseType("Cardio", "Enhance cardiovascular health and endurance."),
    ExerciseType("Flexibility", "Stretches to improve flexibility and mobility."),
    ExerciseType("Yoga", "Static postures for relaxation and peace of mind.")
)

@Serializable
object DashBoardRoute

@Serializable
object StrengthRoute

@Serializable
object CardioRoute

@Serializable
data class WorkoutSelectionRoute(
    val categoryName: String
)

// Could add toggle for Material You (looks weird on Pixel sometimes)
// Needs actual variables for workouts and time spent, logging, user input/details screen





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

        // Seems ok for now?
        val categoryName = navBackStackEntry?.arguments?.getString("categoryName")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when {
                                currentDestination?.contains("DashBoardRoute") == true -> "FitTrack Dashboard"
                                currentDestination?.contains("WorkoutSelectionRoute") == true ->  "$categoryName Exercises"
                                else -> ""
                            }
                        )
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = { navController.navigateUp() }) {
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
                        onUserClick = { categoryName ->
                            navController.navigate(WorkoutSelectionRoute(categoryName = categoryName))
                        }
                    )
                }
                composable<WorkoutSelectionRoute> { backStackEntry ->
                    val selectionRoute: WorkoutSelectionRoute = backStackEntry.toRoute()
                    WorkoutSelectionScreen(
                        categoryName = selectionRoute.categoryName
                    )
                }
            }
        }
    }
}

// Needs today's stats at the top instead of placeholder
@Composable
fun DashboardScreen(
    exerciseTypes: List<ExerciseType>,
    windowSizeClass: WindowSizeClass,
    onUserClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        ExerciseList(
            exerciseTypes = exerciseTypes,
            onUserClick = onUserClick,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Composable
fun ExerciseList(
    exerciseTypes: List<ExerciseType>,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today's Summary",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Workouts Done",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Total mins",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        item {
            Text(
                text = "Workout Categories",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Left
            )
        }
        items(exerciseTypes) { exerciseType ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(exerciseType.name) },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = exerciseType.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = exerciseType.description,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Left
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Workout Log",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "There are no recent workouts.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Left
                )
            }

        }
    }
}

@Composable
fun WorkoutSelectionScreen(
    categoryName: String
) {
    val category = exerciseTypes.find { it.name == categoryName }
    val exercises = when (categoryName.lowercase()) {
        "strength" -> listOf("Push-ups","Squats", "Bench Press", "Deadlift")
        "cardio" -> listOf("Running", "Outdoor Cycling", "Swimming", "Jumping Jacks")
        "flexibility" -> listOf("Cobra Stretch", "Seated Hamstring Stretch", "Leg Raises", "Shoulder Stretch")
        "yoga" -> listOf("Warrior I", "Warrior II", "Warrior III", "Tree Pose")
        else -> emptyList()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(exercises) { exercise ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = exercise,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Left
                            )
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                }
            }
        }
    }
}



