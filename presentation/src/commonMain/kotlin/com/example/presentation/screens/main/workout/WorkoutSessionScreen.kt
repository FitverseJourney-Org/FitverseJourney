package com.example.presentation.screens.main.workout

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ExerciseType { TIMED, REPS }

data class Exercise(
    val id: Int,
    val title: String,
    val description: String = "",
    val durationSeconds: Int = 30,   // para TIMED
    val reps: Int = 10,              // para REPS
    val sets: Int = 1,
    val restAfterSeconds: Int = 30,
    val animationAsset: String? = null, // lottie json id ou url
    val xp: Int = 10,
    val type: ExerciseType = ExerciseType.TIMED
)

data class WorkoutPlan(
    val id: Int,
    val title: String,
    val exercises: List<Exercise>
)

@Composable
fun WorkoutSessionScreen(
    modifier: Modifier,
    workout: WorkoutPlan,
    onFinish: (resultXp: Int) -> Unit,
) {
    // estado
    var currentIndex by remember { mutableStateOf(0) }
    val current = workout.exercises.getOrNull(currentIndex) ?: return
    var currentSet by remember { mutableStateOf(1) }
    var isRunning by remember { mutableStateOf(false) }
    var isResting by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(current.durationSeconds) } // countdown for TIMED
    var completedSets by remember { mutableStateOf(0) }
    var totalXp by remember { mutableStateOf(0) }
    val scoped = rememberCoroutineScope()


    // iniciar timer auto quando isRunning true
    LaunchedEffect(isRunning, isResting, currentIndex, currentSet) {
        if (!isRunning) return@LaunchedEffect
        if (isResting) return@LaunchedEffect

        scoped.launch {
            if (current.type == _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.TIMED) {
                // countdown
                while (timerSeconds > 0 && isRunning && !isResting) {

                    delay(1000L)
                    timerSeconds -= 1
                }
                if (timerSeconds <= 0 && isRunning && !isResting) {
                    // marcou fim de set/exercise
                    // avança para rest ou next set/exercise
                    completedSets += 1
                    totalXp += current.xp
                    if (currentSet < current.sets) {
                        currentSet += 1
                        timerSeconds = current.durationSeconds
                        // pausa breve? podemos auto-continuar
                    } else {
                        // iniciar rest entre exercícios
                        isResting = true
                        isRunning = false
                    }
                }

            }
        }
    }

    // Rest timer
    LaunchedEffect(key1 = isResting) {
        if (!isResting) return@LaunchedEffect
        var rest = current.restAfterSeconds
        while (rest > 0) {
            delay(1000L)
            rest -= 1
        }
        // rest finished -> go next exercise
        isResting = false
        currentIndex = (currentIndex + 1).coerceAtMost(workout.exercises.lastIndex)
        currentSet = 1
        val next = workout.exercises.getOrNull(currentIndex)
        if (next != null && next.type == _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.TIMED) {
            timerSeconds = next.durationSeconds
        }
        isRunning = true
    }

    // UI
    Scaffold(
        modifier = modifier,
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(it).padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(workout.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
                    Text("${currentIndex + 1}/${workout.exercises.size} • ${workout.exercises.sumOf { it.durationSeconds } / 60} min",
                        color = Color.White.copy(alpha = 0.6f) )
                }
                // Exit
                Text(
                    "00:30",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            // Current exercise card
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.workout.SurfaceGreen)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Title + set info
                    Text(current.title, style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(Modifier.height(6.dp))
                    Text("Set $currentSet / ${current.sets}", color = Color.White.copy(alpha = 0.8f))

                    Spacer(Modifier.height(12.dp))

                    // Placeholder para animação -> substitua por Lottie com FitnessLottieAnimation
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), contentAlignment = Alignment.Center) {
                        // exemplo: colocar Lottie ou imagem
                        Text(text = current.animationAsset ?: "▶️ Demo", color = Color.White.copy(alpha = 0.6f))
                    }

                    Spacer(Modifier.height(12.dp))

                    // Timer / Reps
                    if (current.type == _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.TIMED) {
                        Text(
                            _root_ide_package_.com.example.presentation.screens.main.workout.formatTime(
                                timerSeconds
                            ), style = MaterialTheme.typography.displaySmall, color = Color.White)
                    } else {
                        Text("${current.reps} reps", style = MaterialTheme.typography.headlineSmall, color = Color.White)
                    }

                    Spacer(Modifier.height(12.dp))

                    // Controls
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = {
                            // prev
                            if (currentIndex > 0) {
                                currentIndex -= 1
                                currentSet = 1
                                val prev = workout.exercises[currentIndex]
                                timerSeconds = prev.durationSeconds
                                isRunning = false
                                isResting = false
                            }
                        }) { Text("Prev") }

                        Button(onClick = {
                            isRunning = !isRunning
                            // if starting a new exercise ensure timerSeconds initialized
                            if (isRunning && current.type == _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.TIMED && timerSeconds <= 0) {
                                timerSeconds = current.durationSeconds
                            }
                        }) {
                            Text(if (isRunning) "Pause" else "Start")
                        }

                        Button(onClick = {
                            // skip to next immediately
                            totalXp += current.xp
                            currentIndex = (currentIndex + 1).coerceAtMost(workout.exercises.lastIndex)
                            currentSet = 1
                            isResting = false
                            isRunning = false
                            val next = workout.exercises.getOrNull(currentIndex)
                            if (next != null) timerSeconds = next.durationSeconds
                        }) { Text("Next") }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Upcoming exercises row (preview)
            Text("Next", color = Color.White.copy(alpha = 0.8f))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                val upcoming = workout.exercises.drop(currentIndex + 1)
                itemsIndexed(upcoming) { idx, ex ->
                    _root_ide_package_.com.example.presentation.screens.main.workout.SmallExercisePreview(
                        ex
                    ) {
                        // abrir modal preview ou pular
                        // aqui só salto para o exercício
                        currentIndex = currentIndex + 1 + idx
                        currentSet = 1
                        isRunning = false
                        isResting = false
                        timerSeconds = ex.durationSeconds
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Footer quick info
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("XP gained: $totalXp", color = Color.White)
                Text("${workout.exercises.sumOf { it.xp }} XP total", color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
fun SmallExercisePreview(ex: com.example.presentation.screens.main.workout.Exercise, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(width = 120.dp, height = 80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = _root_ide_package_.com.example.presentation.screens.main.workout.SurfaceGreen),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.Start) {
            Text(ex.title, maxLines = 2, color = Color.White)
            Spacer(Modifier.weight(1f))
            Text(
                when (ex.type) {
                    _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.TIMED -> "${ex.durationSeconds}s"
                    _root_ide_package_.com.example.presentation.screens.main.workout.ExerciseType.REPS -> "${ex.reps} reps"
                }, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}

private fun String.format(min: Int, sec: Int): String {
    return "${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}"
}
