package com.chriswk.anagram

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class AnagramController {
    val anagram: Anagram = Anagram()

    @PostMapping("/anagram", produces = ["application/json"])
    @ResponseBody
    fun postAnagramsFor(@RequestBody anagramRequest: AnagramRequest): Map<String, List<String>> {
        return anagram.anagramsFor(anagramRequest.word, anagramRequest.minChars)
    }

    @GetMapping("/anagram", produces = ["application/json"])
    @ResponseBody
    fun getAnagramsFor(@RequestParam("word") word: String): Map<String, List<String>> {
        return mapOf(Pair("anagrams", anagram.anagramsFor(word).values.flatten()))
    }
}
