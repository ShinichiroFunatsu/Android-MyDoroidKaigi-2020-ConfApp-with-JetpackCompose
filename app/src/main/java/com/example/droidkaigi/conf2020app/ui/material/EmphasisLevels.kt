package com.example.droidkaigi.conf2020app.ui.material

import androidx.compose.staticAmbientOf
import androidx.ui.graphics.Color
import androidx.ui.material.Emphasis
import androidx.ui.material.EmphasisLevels

data class EmphasisLevels(
    /**
     * Emphasis used to express high emphasis, such as for selected text fields.
     */
    val high: Emphasis = DefaultHighEmphasis,
    /**
     * Emphasis used to express medium emphasis, such as for placeholder text in a text field.
     */
    val medium: Emphasis = DefaultMediumEmphasis,
    /**
     * Emphasis used to express disabled state, such as for a disabled button.
     */
    val disabled: Emphasis = DefaultDisabledEmphasis
)


/**
 * Ambient containing the current [EmphasisLevels] in this hierarchy.
 */
val EmphasisAmbient = staticAmbientOf { EmphasisLevels() }

/**
 * Default implementation for expressing high emphasis.
 */
private val DefaultHighEmphasis: Emphasis = object : Emphasis {
    override fun emphasize(color: Color) = color.copy(alpha = HighEmphasisAlpha)
}

/**
 * Default implementation for expressing medium emphasis.
 */
private val DefaultMediumEmphasis: Emphasis = object : Emphasis {
    override fun emphasize(color: Color) = color.copy(alpha = MediumEmphasisAlpha)
}

/**
 * Default implementation for expressing disabled emphasis.
 */
private val DefaultDisabledEmphasis: Emphasis = object : Emphasis {
    override fun emphasize(color: Color) = color.copy(alpha = DisabledEmphasisAlpha)
}

private const val HighEmphasisAlpha = 0.87f
private const val MediumEmphasisAlpha = 0.60f
private const val DisabledEmphasisAlpha = 0.38f