package com.chriswk.anagram

data class AnagramRequest(val word: String, val minChars: Int = 3)
