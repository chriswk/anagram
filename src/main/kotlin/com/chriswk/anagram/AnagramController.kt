package com.chriswk.anagram

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.lang.IllegalArgumentException

@Controller
class AnagramController {
    val anagram: Anagram = Anagram()

    @PostMapping("/anagram", produces = ["application/json"])
    @ResponseBody
    fun postAnagramsFor(@RequestBody anagramRequest: AnagramRequest): Map<Int, List<String>> {
        return anagram.anagramsFor(anagramRequest.word, anagramRequest.minChars)
    }

    @GetMapping("/anagram", produces = ["application/json"])
    @CrossOrigin()
    @ResponseBody
    fun getAnagramsFor(@RequestParam("word") word: String,
                       @RequestParam("minCount", required = false, defaultValue = "3") minCount: Int,
                       @RequestParam("language", required = false, defaultValue = "en") language: String): Map<String, List<String>> {
        if (word.length > 20) {
            throw IllegalArgumentException("Word is too long")
        }
        return mapOf(Pair("anagrams", anagram.anagramsFor(word, minCount, language).values.flatten()))
    }

    @GetMapping("/pangram", produces = ["application/json"])
    @CrossOrigin
    @ResponseBody
    fun getPangramsFor(@RequestParam("letters") letters: String,
                       @RequestParam("language", defaultValue = "en", required = false) language: String,
                       @RequestParam("mustcontain") mustContain: Char): Map<String, List<String>> {
        return mapOf(Pair("pangrams", anagram.pangram(letters, mustContain = mustContain).sortedByDescending { it.length }))
    }

    @GetMapping("/")
    fun redirectToFrontend(): String {
        return "redirect:https://anagramfrontend.herokuapp.com"
    }
}
