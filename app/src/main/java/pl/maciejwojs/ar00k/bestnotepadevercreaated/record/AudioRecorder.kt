package pl.maciejwojs.ar00k.bestnotepadevercreaated.record

import java.io.File
/**
 * Interfejs odpowiedzialny za nagrywanie dźwięku.
 *
 *
 * @see AndroidAudioRecorder
 */
interface AudioRecorder {
    /**
     * Rozpoczyna nagrywanie dźwięku.
     *
     * @param outputFile Plik, do którego zostanie zapisany dźwięk.
     */
    fun start(outputFile: File)

    /**
     * Zatrzymuje nagrywanie dźwięku.
     */
    fun stop()
}
