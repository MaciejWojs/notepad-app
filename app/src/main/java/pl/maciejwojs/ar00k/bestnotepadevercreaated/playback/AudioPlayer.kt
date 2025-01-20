package pl.maciejwojs.ar00k.bestnotepadevercreaated.playback

import java.io.File

/**
 * Interfejs odpowiedzialny za odtwarzanie dźwięku.
 *
 * @see AndroidAudioPlayer
 */

interface AudioPlayer {

    /**
     * Odtwarza dźwięk z pliku.
     *
     * @param file Plik dźwiękowy.
     */
    fun play(file: File)

    /**
     * Zatrzymuje odtwarzanie dźwięku.
     */
    fun stop()
}
