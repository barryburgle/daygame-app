package com.barryburgle.gameapp.ui.utilities.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

/**
 * Keeps track of how many dialogs are currently on screen, no matter which screen opened them.
 *
 * Screens read [DialogOverlayState.isDialogOpen] to blur their content (and let the dialog window
 * provide the usual dim behind it) exactly like the event insertion dialogs do.
 */
object DialogOverlayState {
    private var openDialogs by mutableIntStateOf(0)

    val isDialogOpen: Boolean
        get() = openDialogs > 0

    internal fun registerDialog() {
        openDialogs++
    }

    internal fun unregisterDialog() {
        openDialogs = (openDialogs - 1).coerceAtLeast(0)
    }
}

/**
 * Registers the current dialog in [DialogOverlayState] for as long as it stays in composition.
 * Call it at the beginning of every dialog composable so the underlying screen gets blurred.
 */
@Composable
fun TrackDialogOverlay() {
    DisposableEffect(Unit) {
        DialogOverlayState.registerDialog()
        onDispose { DialogOverlayState.unregisterDialog() }
    }
}
