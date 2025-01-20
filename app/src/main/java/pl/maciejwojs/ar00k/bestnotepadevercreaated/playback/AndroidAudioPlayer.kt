package pl.maciejwojs.ar00k.bestnotepadevercreaated.playback

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

/**
 * Klasa odpowiedzialna za odtwarzanie dźwięku na platformie Android.
 *
 * @property context Kontekst aplikacji.
 */

class AndroidAudioPlayer(
    private val context: Context,
) : AudioPlayer {
    private var player: MediaPlayer? = null

    /**
     * Odtwarza dźwięk z pliku.
     *
     * @param file Plik dźwiękowy.
     */

    override fun play(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }


    /**
     * Zatrzymuje odtwarzanie dźwięku.
     */
    override fun stop() {
        player?.apply {
            stop()
            release()
        }
    }
}
