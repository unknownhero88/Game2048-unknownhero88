package com.example.game2048_unknownhero88

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen(vm: GameViewModel = viewModel()) {

    var refresh by remember { mutableStateOf(0) }

    fun trigger() { refresh++ }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(refresh) {
                detectDragGestures { change, drag ->
                    change.consume()

                    if (abs(drag.x) > abs(drag.y)) {
                        if (drag.x > 0) vm.moveRight() else vm.moveLeft()
                    } else {
                        if (drag.y > 0) vm.moveDown() else vm.moveUp()
                    }

                    trigger()
                }
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "2048",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Text("Score: ${vm.score}", fontSize = 20.sp)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            vm.board.forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { value ->
                        Tile(value)
                    }
                }
            }
        }
    }
    if (vm.isGameOver) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text("No more moves possible 😢") },
            confirmButton = {
                TextButton(onClick = {
                    vm.resetGame()
                }) {
                    Text("Restart")
                }
            }
        )
    }

}

@Composable
fun Tile(value: Int) {
    val bg = when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        else -> Color(0xFFEDCF72)
    }

    Box(
        modifier = Modifier
            .size(70.dp)
            .background(bg, RoundedCornerShape(10.dp)),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        if (value != 0) {
            Text(
                "$value",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
