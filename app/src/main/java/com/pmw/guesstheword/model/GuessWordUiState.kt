package com.pmw.guesstheword.model

data class GuessWordUiState (
    val score: Int = 0,
    val isGameOver: Boolean = false,
    val isGuessWrong: Boolean = false,
    val pickedWord: String = "",
    val pickedWordHint: String = "",
    val isShowHint: Boolean = false,
    val isHintButtonEnable: Boolean = true,
    val lettersRow: List<List<Char>> = emptyList(),
    val selectedPosition: Set<Pair<Int, Int>> = emptySet(),
    val currentGuess: List<Char> = emptyList(),
    val hint: Int = 5

    )