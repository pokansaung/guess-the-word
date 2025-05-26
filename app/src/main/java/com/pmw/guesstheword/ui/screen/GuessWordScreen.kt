package com.pmw.guesstheword.ui.screen

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmw.guesstheword.ui.theme.GuessTheWordTheme

@Composable
fun GuessWordScreen(
    modifier: Modifier = Modifier,
    viewModel: GuessWordViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Guess The Word",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Score: ${uiState.score}",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Word remaining: ${viewModel.remainingWord} ",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = uiState.currentGuess.joinToString(" ") { it.toString() },
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = buildAnnotatedString {
                append("Guess the correct word from given letters.")
                append("The word has ")
                withStyle(style = SpanStyle(
                    color = Color(0xFFC70039),
                    fontWeight = FontWeight.Bold
                )) {
                    append("${uiState.pickedWord.length}")
                }
                append(" letters.")
            },
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(20.dp))
        HintLetters(
            selectedPosition = uiState.selectedPosition,
            onLetterSelected = { row, col -> viewModel.letterSelection(row, col) },
            letterRow = uiState.lettersRow,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.size(20.dp))
        ButtonLayout(
            onClearClick = { viewModel.clearSelection() },
            onSubmitClick = { viewModel.submitGuess() },
            isSubmitButtonEnabled = uiState.currentGuess.isNotEmpty(),
            modifier = Modifier
        )
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedButton(
            onClick = { viewModel.showHint() },
            enabled = uiState.isHintButtonEnable,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Hint (${uiState.hint})") }
        Spacer(modifier = Modifier.size(10.dp))
        if (uiState.isGuessWrong) {
            Text(
                text = "Wrong",
                color = MaterialTheme.colorScheme.error
            )
        }
        if (uiState.isShowHint) {
            Text(
                text = uiState.pickedWordHint,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    AnimatedVisibility(
        visible = uiState.isGameOver,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        GameOverScreen(
            score = uiState.score,
            onPlayAgainClick = { viewModel.resetGame() },
            modifier = modifier
        )
    }
}

@Composable
fun HintLetters(
    selectedPosition: Set<Pair<Int, Int>>,
    onLetterSelected: (Int, Int) -> Unit,
    letterRow: List<List<Char>>,
    modifier: Modifier = Modifier
) {


    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.elevatedCardColors(),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            letterRow.forEachIndexed { rowIndex, row ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEachIndexed { colIndex, letter ->
                        val isSelected = selectedPosition.contains(rowIndex to colIndex)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .clickable {
                                    onLetterSelected(rowIndex, colIndex)
                                }
                        ) {
                            Text(
                                text = letter.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp)) // Add space between rows
            }
        }
    }
}


@Composable
fun ButtonLayout(
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    isSubmitButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = onClearClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Clear")
        }
        Button(
            onClick = onSubmitClick,
            enabled = isSubmitButtonEnabled,
            modifier = Modifier.weight(1f)
        ) {
            Text("Submit")
        }
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun GameOverScreen(
    score: Int,
    onPlayAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val activity = LocalContext.current as? Activity
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Congratulation") },
        text = { Text("Your score: $score") },
        confirmButton = {
            OutlinedButton(
                onClick = { activity?.finish() },
            ) { Text("Exit") }
            Spacer(modifier = Modifier.size(5.dp))
            Button(
                onClick = onPlayAgainClick,
            ) { Text("Play Again") }

        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GuessWordPreview() {
    GuessTheWordTheme {
        GuessWordScreen()
    }
}