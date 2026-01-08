package com.example.game2048_unknownhero88

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFF5F5F5)
            ) {
                GameScreen()
            }
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
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 🔹 HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2048",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            )
            ScoreCard(vm.score)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 BOARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
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
                }
                .background(Color(0xFFBBADA0), RoundedCornerShape(20.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Board(vm)
        }
    }

    // 🔹 GAME OVER DIALOG
    if (vm.isGameOver) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text("No more moves possible 😢") },
            confirmButton = {
                TextButton(onClick = { vm.resetGame() }) {
                    Text("Restart")
                }
            }
        )
    }
}

@Composable
fun Board(vm: GameViewModel) {

    val tileSize =
        (LocalConfiguration.current.screenWidthDp.dp - 96.dp) / 4

    Box(
        modifier = Modifier
            .size(tileSize * 4 + 24.dp)
    ) {
        vm.board.forEachIndexed { r, row ->
            row.forEachIndexed { c, value ->
                if (value != 0) {
                    Tile(value, r, c)
                }
            }
        }
    }
}


@Composable
fun Tile(
    value: Int,
    row: Int,
    col: Int
) {
    val tileSize =
        (LocalConfiguration.current.screenWidthDp.dp - 96.dp) / 4

    // 🎯 Target position
    val targetX = col * (tileSize + 8.dp)
    val targetY = row * (tileSize + 8.dp)

    // 🎞 Animate position
    val offsetX by animateDpAsState(
        targetValue = targetX,
        label = "offsetX"
    )

    val offsetY by animateDpAsState(
        targetValue = targetY,
        label = "offsetY"
    )

    val bgColor by animateColorAsState(
        targetValue = when (value) {
            0 -> Color(0xFFCDC1B4)
            2 -> Color(0xFFEEE4DA)
            4 -> Color(0xFFEDE0C8)
            8 -> Color(0xFFF2B179)
            16 -> Color(0xFFF59563)
            32 -> Color(0xFFF67C5F)
            64 -> Color(0xFFF65E3B)
            else -> Color(0xFFEDC22E)
        },
        label = "tileColor"
    )

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(tileSize)
            .background(bgColor, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                value.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (value <= 4) Color.DarkGray else Color.White
            )
        }
    }
}

@Composable
fun ScoreCard(score: Int) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEEE4DA),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Score", fontSize = 14.sp)
            Text(score.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
