package com.pmw.guesstheword.data

data class Words(
    val word: String,
    val hint: String
)
val wordData = listOf(
    Words("dog", hint = "Ar woof"),
    Words(word = "cat", hint = "Meow Meow"),
    Words(word = "air", hint = "the one you breath"),
    Words(word = "cow", hint = "give milk to us"),
    Words(word = "pen", hint = "to write on paper"),
    Words(word = "hat", hint = "it's for your head"),
    Words(word = "sun", hint = "shining in the sky"),
    Words(word = "water", hint = "the one you cannot cut and kill"),
    Words(word = "star", hint = "twinkling in the sky"),
    Words(word = "animal", hint = "creatures you can find in zoo"),
    Words(word = "zoo", hint = "a place where you can find animals"),
    Words(word = "moon", hint = "it's luna in Latin"),
    Words(word = "chair", hint = "thing to sit on"),
    Words(word = "music", hint = "good to listen"),
    Words(word = "present", hint = "for your birthday"),
    Words(word = "flower", hint = "used to give as present"),
    Words(word = "yellow", hint = "it's a color"),
    Words(word = "red", hint = "it's a color"),
    Words(word = "green", hint = "it's a color"),
    Words(word = "clock", hint = "it tells the time"),
    Words(word = "light", hint = "to escape from dark"),
)
