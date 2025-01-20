/**
 * @file CameraPreview.kt
 *
 * Ten plik zawiera implementację funkcji composable CameraPreview.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Composable, który wyświetla podgląd kamery.
 *
 * @param controller Kontroler kamery.
 * @param modifier Modyfikator.
 */

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier,
    )
}
