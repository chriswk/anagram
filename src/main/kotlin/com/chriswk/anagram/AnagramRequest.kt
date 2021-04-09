package com.chriswk.anagram

data class AnagramRequest(val word: String, val minChars: Int = 3)

data class PangramRequest(val letters: String, val mustcontain: String, val minChars: Int = 4, val language: String = "us")
