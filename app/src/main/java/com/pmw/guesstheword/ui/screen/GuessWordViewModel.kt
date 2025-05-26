package com.pmw.guesstheword.ui.screen

import androidx.lifecycle.ViewModel
import com.pmw.guesstheword.data.wordData
import com.pmw.guesstheword.model.GuessWordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GuessWordViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(GuessWordUiState())
    val uiState: StateFlow<GuessWordUiState> = _uiState.asStateFlow()
    private lateinit var pickedWord: String
    private lateinit var pickedWordHint: String
    private var score = 0
    private var hint = 5
    var remainingWord = 10
    private var usedWord = mutableSetOf<String>()
    init {
        startNewRound()
    }
    fun showHint(){
        if (hint != 0 && remainingWord != 0) {
            hint--
            _uiState.update {
                it.copy(
                    isShowHint = true,
                    isHintButtonEnable = false,
                    hint = hint
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isShowHint = false,
                    isHintButtonEnable = false
                )
            }
        }
    }

    /*private fun generateLettersRow(wordString: String): List<List<Char>> {
        val wordChar = wordString.uppercase().toList()
        val randomLetters = ('A'..'Z').filter { it !in wordChar }.shuffled()
        var row1 = mutableListOf<Char>()
        var row2 = mutableListOf<Char>()

        wordChar.forEachIndexed { index, char ->
            if (index % 2 == 0 && row1.size < 4){
                row1.add(char)
            } else {
                row2.add(char)
            }
        }
        randomLetters.forEach { char ->
            if (row1.size < 4) {
                row1.add(char)
            } else {
                if (row2.size <4)
                    row2.add(char)
            }
        }
        row1 = row1.shuffled() as MutableList<Char>
        row2 = row2.shuffled() as MutableList<Char>
        return listOf(row1, row2)
    }*/
    //gemini's suggestion
    private fun generateLettersRow(wordString: String): List<List<Char>> {
        val wordChar = wordString.uppercase().toList()
        // လိုအပ်တဲ့ random letters အရေအတွက်ကို တွက်ပါ။ (ဥပမာ 8 လုံးဆိုရင် wordChar.size ကိုနှုတ်ပါ)
        val neededRandomLettersCount = 8 - wordChar.size // ဥပမာ- 4x2 grid အတွက် စာလုံး ၈ လုံး
        val randomLetters = ('A'..'Z')
            .filter { it !in wordChar }
            .shuffled()
            .take(neededRandomLettersCount)

        val allLetters = (wordChar + randomLetters).shuffled()

        // ဥပမာ: ၄x၂ grid အတွက်
        val row1 = allLetters.take(4)
        val row2 = allLetters.drop(4).take(4) // ကျန်တဲ့ ၄ လုံး

        // ဒါမှမဟုတ် List<List<Char>> အတွက် ဘုတ်အရွယ်အစားကို ပိုပြီး Dynamic ဖြစ်အောင် တွက်ချက်နိုင်ပါတယ်။
        // val numCols = 4 // သတ်မှတ်ထားသော column အရေအတွက်
        // return allLetters.chunked(numCols) // List ကို chunk တွေခွဲထုတ်

        return listOf(row1, row2)
    }
    fun letterSelection(row: Int, col: Int){
        val currentState = _uiState.value
        val position = row to col

        val currentOrder = currentState.selectedPosition.toList()
        val newSelectedPosition = if (currentState.selectedPosition.contains(position)){
            currentOrder.filter { it != position }.toSet()
        } else {
            if (currentState.selectedPosition.size < currentState.pickedWord.length){
                (currentOrder + position).toSet()
            } else {
                currentState.selectedPosition
            }
        }
        _uiState.update {
            it.copy(
                selectedPosition = newSelectedPosition,
                currentGuess = generateCurrentGuess(
                    newSelectedPosition,
                    selectedLetters = it.lettersRow,
                )
            )
        }
    }

    private fun generateCurrentGuess(
        selectedPosition: Set<Pair<Int, Int>>,
        selectedLetters: List<List<Char>>,
        ): List<Char> {
        val orderedPosition = selectedPosition.toList()
        return orderedPosition.map { (row, col) -> selectedLetters[row][col] }
    }

    fun clearSelection(){
        _uiState.update {
            it.copy(
                selectedPosition = emptySet(),
                currentGuess = emptyList(),
            )
        }
    }

    fun submitGuess(){
        val currentState = _uiState.value
        val guessedWord = currentState.currentGuess.joinToString("")
        val isCorrect = guessedWord.equals(currentState.pickedWord, ignoreCase = true)
        if (isCorrect) {
            score += 10
            remainingWord--
            usedWord.add(pickedWord)
            _uiState.update { it.copy(score = score) }
            if (hint != 0) {
                _uiState.update { it.copy(isHintButtonEnable = true) }
            } else {
                _uiState.update { it.copy(isHintButtonEnable = false) }
            }
            if (remainingWord <= 0) {
                _uiState.update { it.copy(isGameOver = true) }
            } else {
                startNewRound()
            }
        } else {
            _uiState.update { it.copy(isGuessWrong = true) }
        }
    }

    fun resetGame(){
        hint = 3
        remainingWord = 5
        score = 0
        usedWord.clear()
        _uiState.update { GuessWordUiState() }
        startNewRound()
    }

    private fun startNewRound(){
        do {
            val randomData = wordData.random()
            pickedWord = randomData.word
            pickedWordHint = randomData.hint
        } while (usedWord.contains(pickedWord))

        val lettersRow = generateLettersRow(pickedWord)
        _uiState.update {
            it.copy(
                pickedWord =pickedWord,
                pickedWordHint = pickedWordHint,
                lettersRow = lettersRow,
                selectedPosition = emptySet(),
                currentGuess = emptyList(),
                isGameOver = false,
                isGuessWrong = false,
                isShowHint = false
            )
        }
    }
}