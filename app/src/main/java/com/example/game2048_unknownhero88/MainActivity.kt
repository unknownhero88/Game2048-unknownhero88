package com.example.game2048_unknownhero88

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
        var lastMoveTime by remember { mutableStateOf(0L) }
        // 🔹 BOARD
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(refresh) {
                    detectDragGestures { change, drag ->
                        val now = System.currentTimeMillis()
                        if (now - lastMoveTime < 120) return@detectDragGestures
                        lastMoveTime = now

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

    if (vm.isGameOver) {
        GameOverDialog(
            score = vm.score,
            onRestart = {
                vm.resetGame()
            }
        )
    }

}

@Composable
fun GameOverDialog(
    score: Int,
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(
                    color = Color(0xFFF8F5F0),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 💀 Emoji
            Text(
                text = "😵",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // GAME OVER
            Text(
                text = "Game Over",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "No more moves available",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // SCORE CARD
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEEE4DA)
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your Score", fontSize = 14.sp)
                    Text(
                        text = score.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // RESTART BUTTON
            Button(
                onClick = onRestart,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF2B179)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Restart Game",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun Board(vm: GameViewModel) {

    val tileSize =
        (LocalConfiguration.current.screenWidthDp.dp - 96.dp) / 4

    val boardSize = tileSize * 4f + 24.dp

    Box(
        modifier = Modifier.size(boardSize)
    ) {
        vm.board.forEachIndexed { r, row ->
            row.forEachIndexed { c, value ->
                if (value != 0) {
                    Tile(
                        value = value,
                        row = r,
                        col = c,
                        isNew = vm.newTilePosition == (r to c)
                    )
                }
            }
        }
    }
}


@Composable
fun Tile(
    value: Int,
    row: Int,
    col: Int,
    isNew: Boolean
) {
    val tileSize =
        (LocalConfiguration.current.screenWidthDp.dp - 96.dp) / 4

    // 🎯 Position (sliding animation already added)
    val targetX = (tileSize + 8.dp) * col.toFloat()
    val targetY = (tileSize + 8.dp) * row.toFloat()

    val offsetX by animateDpAsState(targetValue = targetX, label = "offsetX")
    val offsetY by animateDpAsState(targetValue = targetY, label = "offsetY")

    // 🎨 Color animation
    val bgColor by animateColorAsState(
        targetValue = when (value) {
            0 -> Color(0xFFCDC1B4)
            2 -> Color(0xFFEEE4DA)
            4 -> Color(0xFFEDE0C8)
            8 -> Color(0xFFF2B179)
            16 -> Color(0xFFF59563)
            32 -> Color(0xFFF67C5F)
            64 -> Color(0xFFF65E3B)
            128 -> Color(0xFFEDCF72)
            256 -> Color(0xFFEDCC61)
            512 -> Color(0xFFEDC850)
            1024 -> Color(0xFFEDC53F)
            2048 -> Color(0xFFEDC22E)
            else -> Color.Black
        },
        label = "tileColor"
    )

    val scale = remember { Animatable(1f) }
    var hasPopped by remember { mutableStateOf(false) }

// 🟢 NEW TILE POP
    LaunchedEffect(isNew) {
        if (isNew && !hasPopped) {
            hasPopped = true
            scale.snapTo(0.7f)   // 👈 kabhi 0 mat rakho
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 180,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

// 🔵 MERGE BOUNCE
    LaunchedEffect(value) {
        if (!isNew && value != 0) {
            if (scale.value < 1f) {
                scale.snapTo(1f)
            }
            scale.animateTo(1.2f, tween(120))
            scale.animateTo(1f, tween(120))
        }
    }

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(tileSize)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .background(bgColor, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
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
