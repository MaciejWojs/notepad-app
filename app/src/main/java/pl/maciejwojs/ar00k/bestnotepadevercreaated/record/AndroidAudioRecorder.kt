package pl.maciejwojs.ar00k.bestnotepadevercreaated.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

/**
 * Klasa odpowiedzialna za nagrywanie dźwięku na platformie Android.
 *
 * @property context Kontekst aplikacji.
 */
class AndroidAudioRecorder(
    private val context: Context,
) : AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var isRecording: Boolean = false
        get() = recorder != null

    /**
     * Rozpoczyna nagrywanie dźwięku.
     *
     * @param outputFile Plik, do którego zostanie zapisany dźwięk.
     */

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            return MediaRecorder()
        }
    }

    /**
     * Rozpoczyna nagrywanie dźwięku.
     *
     * @param outputFile Plik, do którego zostanie zapisany dźwięk.
     */

    override fun start(outputFile: File) {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
            recorder = this
            isRecording = true
        }
    }

    /**
     * Zatrzymuje nagrywanie dźwięku.
     */

    override fun stop() {
        if (isRecording) {
            recorder?.apply {
                stop()
                release()
            }
            recorder = null
            isRecording = false
        }
    }
}
