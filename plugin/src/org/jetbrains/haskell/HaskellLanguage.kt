package org.jetbrains.haskell

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import org.jetbrains.haskell.highlight.HaskellHighlighter

class HaskellLanguage : Language("Haskell", "text/haskell") {

    companion object {
        val INSTANCE: HaskellLanguage = HaskellLanguage()
    }
}