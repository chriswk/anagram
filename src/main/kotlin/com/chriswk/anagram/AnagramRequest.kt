package com.chriswk.anagram

data class AnagramRequest(val word: String, val minChars: Int = 3)

data class PangramForm(val letters: String = "", val mustcontain: String = "", val language: String = "us")
