package pl.maciejwojs.ar00k.bestnotepadevercreaated.record

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File

class AndroidAudioRecorder(
    private val context: Context,
) : AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var isRecording: Boolean = false
        get() = recorder != null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            return MediaRecorder()
        }
    }

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
