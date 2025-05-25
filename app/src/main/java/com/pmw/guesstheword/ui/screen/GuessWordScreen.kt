package com.pmw.guesstheword.ui.screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmw.guesstheword.ui.theme.GuessTheWordTheme

@Composable
fun GuessWordScreen(
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Guess The Word",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Score: 0",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "PLANET",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Guess the correct word from given letters.",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "The word has 5 letters.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.size(20.dp))
        HintLetters()
        Spacer(modifier = Modifier.size(20.dp))
        ButtonLayout(
            onClearClick = { },
            onSubmitClick = {  },
            modifier = Modifier
        )

    }
}

@Composable
fun HintLetters(
    modifier: Modifier = Modifier
) {
    val row1 = listOf('A', 'B', 'C', 'D')
    val row2 = listOf('E', 'F', 'G', 'H')
    val rows = listOf(row1, row2)
    val isSelected = remember {mutableStateOf(false)}

    Card(
        elevation = CardDefaults.cardElevation(5.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            rows.forEach { row ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { letter ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected.value -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                                .clickable { isSelected.value = true }
                        ) {
                            Text(
                                text = letter.toString(),
                                fontSize = 30.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp)) // Add space between rows
            }
        }
    }
}


@Composable
fun ButtonLayout(
    onClearClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
){
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
            modifier = Modifier.weight(1f)
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuessWordPreview(){
    GuessTheWordTheme {
        GuessWordScreen()
    }
}