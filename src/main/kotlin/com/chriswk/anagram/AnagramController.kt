package com.chriswk.anagram

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class AnagramController {
    val anagram: Anagram = Anagram()

    @RequestMapping("/anagram", produces = ["application/json"])
    @ResponseBody
    fun getAnagramsFor(@RequestBody anagramRequest: AnagramRequest): Map<String, List<String>> {
        return anagram.anagramsFor(anagramRequest.word, anagramRequest.minChars)
    }

    @RequestMapping("/test", produces = ["application/json"])
    @ResponseBody
    fun testBody(@RequestBody anagramRequest: AnagramRequest): AnagramRequest {
        return anagramRequest
    }

    @GetMapping("/anagram", produces = ["text/html"])
    fun getAnagramsFor(@RequestParam("word") word: String, model: ModelMap): String {
        model.addAttribute("anagrams", anagram.anagramsFor(word, 3))
        return "anagrams"
    }
}
