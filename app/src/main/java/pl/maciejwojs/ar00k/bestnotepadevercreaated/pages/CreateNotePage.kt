/**
 * Plik zawiera implementację strony tworzenia notatki.
 *
 * @file CreateNotePage.kt
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.playback.AndroidAudioPlayer
import pl.maciejwojs.ar00k.bestnotepadevercreaated.record.AndroidAudioRecorder
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.io.File
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Strona tworzenia notatki.
 *
 * @param navigator Nawigator do nawigacji między stronami.
 * @param viewModel [NotesViewModel] Obiekt ViewModel do obsługi notatek.
 * @param tags Lista tagów.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNotePage(
    navigator: NavController,
    viewModel: NotesViewModel,
//    onCreate: (String, String, Map<Tag, Boolean>) -> Unit,
    tags: List<Tag>,
    requestCameraPermission: () -> Unit,
    cameraPreview: @Composable (onPhotoTaken: (URI) -> Unit, exitCamera: () -> Unit) -> Unit,
    requestMicrophonePermission: () -> Unit,
) {
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val isPrivate = remember { mutableStateOf(false) }
    var showCameraPreview by remember { mutableStateOf(false) }

    var showMicrophoneRecordComposable by remember { mutableStateOf(false) }
    val audioRecorder = AndroidAudioRecorder(context)
    val audioPlayer = AndroidAudioPlayer(context)
    var currentAudioFile = remember { mutableStateOf<String?>(null) }
//    var recordedAudio by remember { mutableStateOf<>(null) }

    // Initialize checkedMap to keep track of each tag’s selection state
    val checkedMap =
        remember {
            mutableStateMapOf<Tag, Boolean>().apply {
                tags.forEach { tag -> this[tag] = false }
            }
        }

    Log.d("CreateNotePage", "Tags: ${tags.size}")
    var capturedImage by remember { mutableStateOf<URI?>(null) }

    fun saveNote() {
        if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
            val note =
                Note(
                    noteTitle,
                    noteContent,
                    imageFile = capturedImage?.path,
                    isPrivate = isPrivate.value,
                )
            viewModel.onEvent(NotesEvent.InsertNote(note, checkedMap.filter { it.value }))
//                                onCreate(noteTitle, noteContent, checkedMap.filter { it.value })
        } else {
            Toast.makeText(
                context,
                "Title and content cannot be empty",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            saveNote()
        }
    }

    if (showMicrophoneRecordComposable) {
        BestNotepadEverCreatedTheme {
            Scaffold { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    currentAudioFile.value = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("HH_mm_ss-dd_MM_yyyy")) + ".mp3"
                    val file =
                        File(
                            context.getExternalFilesDir("recordings"),
                            currentAudioFile.value!!,
                        )
                    file.parentFile?.mkdirs()
                    Button(onClick = { audioRecorder.start(file) }) {
                        Text("Start recording")
                    }

                    Button(onClick = {
                        audioRecorder.stop()
                        showMicrophoneRecordComposable = false
                    }) {
                        Text("Start recording")
                    }
//                    val audioFileDeferred = CompletableDeferred<File>()
//                    audioRecorder(
//                        { file ->
//                            audioFileDeferred.complete(file)
//                            showMicrophoneRecordComposable = false
//                        },
//                        {
//                            showMicrophoneRecordComposable = false
//                        },
//                    )
//
//                    LaunchedEffect(Unit) {
//                        val temp = audioFileDeferred.await()
//                        Log.d("CreateNotePage", "Audio file: ${temp.path}")
//                    }
                }
            }
        }
    } else if (showCameraPreview) {
        BestNotepadEverCreatedTheme {
            Scaffold { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    val photoDeferred = CompletableDeferred<URI>()
                    cameraPreview(
                        { uri ->
                            photoDeferred.complete(uri)
                            showCameraPreview = false
//                            Log.d("CreateNotePage", "Photo size: ${bitmap.byteCount}")
                        },
                        {
                            showCameraPreview = false
                        },
                    )

                    LaunchedEffect(Unit) {
                        val temp = photoDeferred.await()
                        capturedImage = temp
                    }
                }
            }
        }
    } else {
        BestNotepadEverCreatedTheme {
            Scaffold(bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier =
                        Modifier
                            .fillMaxWidth()
//                        .height(50.dp)
                            .background(MaterialTheme.colorScheme.onSecondary)
//                        .padding(WindowInsets.systemBars.asPaddingValues())
                            .padding(
                                bottom =
                                    WindowInsets.systemBars
                                        .asPaddingValues()
                                        .calculateBottomPadding(),
                            ),
                ) {
                    Button(
                        onClick = { showBottomSheet = true },
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "tags")
                        Text(text = "Add tags")
                    }

                    Button(
                        onClick = {
                            requestCameraPermission()
                            showCameraPreview = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "add photo",
                        )
                        Text(text = "Take photo")
                    }

                    Button(
                        onClick = {
                            requestMicrophonePermission()
                            showMicrophoneRecordComposable = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "add photo",
                        )
                        Text(text = "Record note")
                    }

                    Button(
                        onClick = {
                            isPrivate.value = !isPrivate.value
                            Toast.makeText(
                                context,
                                if (isPrivate.value) "Note is now private. Don't forget to save!" else "Note is no longer private. Don't forget to save!",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                    ) {
                        Text(text = if (isPrivate.value) "Make public" else "Make private")
                    }
                }
            }, modifier = Modifier.fillMaxSize()) { innerPadding ->

                Column(
                    modifier =
                        Modifier
                            .padding(innerPadding)
                            .scrollable(
                                state = rememberScrollState(),
                                orientation = Orientation.Vertical,
                            ),
                ) {
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        GenerateIconButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            "Back to main screen",
                        ) {
                            if (!navigator.popBackStack()) {
                                navigator.navigate("MainPage")
                            }
                        }

                        BasicTextField(
                            value = noteTitle,
                            onValueChange = { noteTitle = it },
                            singleLine = true,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp)
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = MaterialTheme.shapes.small,
                                            )
                                            .padding(horizontal = 4.dp, vertical = 8.dp),
                                ) {
                                    if (noteTitle.isEmpty()) {
                                        Text(
                                            text = "Note title",
                                            Modifier.alpha(0.5f),
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                        )
                    }

                    OutlinedTextField(
                        value = noteContent,
                        onValueChange = { noteContent = it },
                        label = { Text("Content") },
                        modifier =
                            Modifier
//                                    .fillMaxSize()
                                .weight(1f)
//                                .height()
                                .fillMaxWidth()
                                .padding(8.dp)
                                .defaultMinSize(minHeight = 300.dp),
                    )

                    if (currentAudioFile.value != null) {
                        Button(
                            onClick = {
                                audioPlayer.play(
                                    File(
                                        context.getExternalFilesDir("recordings"),
                                        currentAudioFile.value!!,
                                    ),
                                )
                            },
                        ) {
                            Text("Play audio")
                        }

                        Button(
                            onClick = {
                                audioPlayer.stop()
                            },
                        ) {
                            Text("Stop audio")
                        }
                    }

                    if (capturedImage != null) {
                        val file = File(capturedImage!!.path)
                        val bitmap = BitmapFactory.decodeFile(file.path).asImageBitmap()
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Captured image",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState,
                        ) {
                            if (tags.isEmpty()) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    text = "No tags available",
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally, // Center all rows horizontally
                                ) {
                                    items(tags, key = { it.tagID }) { tag ->
                                        Row(
                                            modifier =
                                                Modifier
                                                    .fillMaxWidth(0.8f) // Limit row width to 80% of available width for centering
                                                    .clickable {
                                                        if (checkedMap[tag] != null) {
                                                            checkedMap[tag] = !checkedMap[tag]!!
                                                        } else {
                                                            checkedMap[tag] = true
                                                        }
                                                    },
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween, // Space between Text and Switch
                                        ) {
                                            Text(
                                                text = tag.name,
                                                modifier = Modifier.weight(1f), // Text takes remaining space
                                            )
                                            Switch(
                                                checked = checkedMap[tag] ?: false,
                                                onCheckedChange = { isChecked ->
                                                    checkedMap[tag] = isChecked
                                                },
                                            )
                                        }
                                    }
                                }
                            }

                            Button(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) showBottomSheet = false
                                    }
                                },
                            ) {
                                Text("Hide")
                            }
                        }
                    }
                }
            }
        }
    }
}
