package org.jetbrains.haskell.cabal

import com.intellij.psi.tree.IElementType
import com.intellij.lang.PsiBuilder
import com.intellij.lang.ASTNode
import org.jetbrains.haskell.parser.BaseParser
import org.jetbrains.cabal.parser.CabalTokelTypes
import com.intellij.psi.TokenType


class CabalParser(p0: IElementType, builder: PsiBuilder) : BaseParser(p0, builder) {

    public fun parse(): ASTNode {
        return parseInternal(root)
    }

    fun parsePropertyKey() = start(CabalTokelTypes.PROPERTY_KEY) {
        token(CabalTokelTypes.ID)
    }

    fun indentSize(str : String) : Int  {
        val indexOf = str.lastIndexOf('\n')
        return str.size - indexOf - 1
    }

    fun parsePropertyValue(level : Int) = start(CabalTokelTypes.PROPERTY_VALUE) {
        while (!builder.eof()) {
            if (builder.getTokenType() == TokenType.NEW_LINE_INDENT) {
                if (indentSize(builder.getTokenText()!!) == level) {
                    break;
                }
            }
            builder.advanceLexer()
        }
        true;
    }

    fun parseProperty(level : Int) = start(CabalTokelTypes.PROPERTY) {
        var r = parsePropertyKey()
        r = r && token(CabalTokelTypes.COLON)
        r = r && parsePropertyValue(level)
        r
    }

    fun parseSectionType() = start(CabalTokelTypes.SECTION_TYPE) {
        token(CabalTokelTypes.ID);
    }

    fun parsePropertyies(indent : Int) {
        while (!builder.eof()) {
            if (!parseProperty(indent)) {
                if (builder.getTokenType() == TokenType.NEW_LINE_INDENT) {
                    if (indentSize(builder.getTokenText()!!) < indent) {
                        return
                    }
                }
                builder.advanceLexer()
            }
        }
    }

    fun parseSection(level : Int) = start(CabalTokelTypes.SECTION) {
        val sections = listOf("source-repository", "flag", "executable")

        val result : Boolean = if (sections.contains(builder.getTokenText())) {
            parseSectionType() && token(CabalTokelTypes.ID)
        } else if (builder.getTokenText() == "library") {
            parseSectionType()
        } else {
            false
        }
        if (result) {
            if (builder.getTokenType() == TokenType.NEW_LINE_INDENT) {
                val newIndent = indentSize(builder.getTokenText()!!)
                parsePropertyies(newIndent);
            }
        }
        result
    }

    fun parseInternal(root: IElementType): ASTNode {
        val rootMarker = mark()

        while (!builder.eof()) {
            if (!(parseProperty(0) || parseSection(0))) {
                builder.advanceLexer()
            }
        }

        rootMarker.done(root)
        return builder.getTreeBuilt()!!
    }

}