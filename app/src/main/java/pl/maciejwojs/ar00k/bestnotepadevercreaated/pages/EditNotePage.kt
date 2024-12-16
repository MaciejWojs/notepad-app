/**
 * Plik zawiera implementację strony edycji notatki.
 *
 * @file EditNotePage.kt
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.SideEffect
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
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.iconModifier
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.iconWeightRatio
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.io.File
import java.net.URI

/**
 * Strona edycji notatki.
 *
 * @param navigator Nawigator do nawigacji między ekranami.
 * @param onEvent Lambda do obsługi zdarzeń.
 * @param onTagEdit Lambda do obsługi edycji tagów.
 * @param note Obiekt notatki.
 * @param tags Lista tagów.
 * @param currentNoteTags Lista tagów przypisanych do notatki.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNotePage(
    navigator: NavController,
//    onEdit: (String, String) -> Unit,
//    viewModel: NotesViewModel,
    onEvent: (NotesEvent) -> Unit,
    onTagEdit: (Note, Tag, Boolean) -> Unit,
    note: Note,
    tags: List<Tag>,
    currentNoteTags: List<Tag>,
    requestCameraPermission: () -> Unit,
    cameraPreview: @Composable (onPhotoTaken: (URI) -> Unit, exitCamera: () -> Unit) -> Unit,
) {
    var noteTitle by remember { mutableStateOf(note.title) }
    var noteContent by remember { mutableStateOf(note.content) }

    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Initialize checkedMap to keep track of each tag’s selection state
    // Initialize checkedMap with an empty map
    val checkedMap = remember { mutableStateMapOf<Tag, Boolean>() }
    val context = LocalContext.current
    val isPrivate = remember { mutableStateOf(note.isPrivate) }
    var showCameraPreview by remember { mutableStateOf(false) }
    var currentImage by remember {
        mutableStateOf(
            note.imageFile?.let { URI(it) } ?: URI(""),
        )
    }

    fun saveNote() {
        if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
//            Log.d("EditNotePage", "Saving note, image size: ${currentImage?.byteCount}")
            val temp = currentImage
//            Log.d("EditNotePageTemp", "Saving note, image size: ${temp?.size}")
            onEvent(
                NotesEvent.UpdateNote(
                    note.copy(
                        title = noteTitle,
                        content = noteContent,
                        isPrivate = isPrivate.value,
                        imageFile = temp.path,
                    ),
                ),
            )
            checkedMap.forEach { entry ->
                Log.i("TAG", "id: ${entry.key} ${entry.value}")
                onTagEdit(note, entry.key, entry.value)
            }
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
    // Update checkedMap based on the current note's tags when tags or currentNoteTags change
    LaunchedEffect(tags, currentNoteTags) {
        checkedMap.clear() // Clear any previous values
        tags.forEach { tag ->
            // Initialize the checked state for each tag based on its presence in currentNoteTags
            checkedMap[tag] = currentNoteTags.contains(tag)
        }
    }

    Log.i("Liczba", "l tagow: ${currentNoteTags.size}")

    if (showCameraPreview) {
        BestNotepadEverCreatedTheme {
            Scaffold { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
//                    Log.d("EditNotePage", "Old image size: ${currentImage?.byteCount}")
                    val photoDeferred = CompletableDeferred<URI>()
                    cameraPreview(
                        { URI ->
                            photoDeferred.complete(URI)
                            showCameraPreview = false
//                            Log.d("EditNotePage", "Photo size: ${bitmap.byteCount}")
                        },
                        {
                            showCameraPreview = false
                        },
                    )

//                    val latestBitmap by rememberUpdatedState()
                    SideEffect {
                        scope.launch {
                            val temp = photoDeferred.await()
                            if (currentImage.path.isNotEmpty()) {
                                val file = File(currentImage.path)
                                file.delete()
                            }
                            currentImage = temp
//                            currentImage = photoDeferred.await()
//                            Log.d("EditNotePage", "New image size: ${currentImage?.byteCount}")
                        }
                    }
                }
            }
        }
    } else {
        // Use LaunchedEffect to load the note when the noteID changes
        BestNotepadEverCreatedTheme {
            Scaffold(bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
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
                    IconButton(
                        modifier =
                            Modifier
                                .weight(iconWeightRatio)
                                .then(iconModifier),
                        onClick = { showBottomSheet = true },
                    ) {
                        Icon(imageVector = Icons.Default.Bookmarks, contentDescription = "Add tag")
//                        Text(text = "Add/modify tags")
                    }

                    IconButton(
                        modifier =
                            Modifier
                                .weight(iconWeightRatio)
                                .then(iconModifier),
                        onClick = {
                            requestCameraPermission()
                            showCameraPreview = true
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "add photo",
                        )
//                        Text(text = "Take photo")
                    }

                    IconButton(
                        modifier =
                            Modifier
                                .weight(iconWeightRatio)
                                .then(iconModifier),
                        onClick = {
                            isPrivate.value = !isPrivate.value
                            Toast.makeText(
                                context,
                                if (isPrivate.value) "Note is now private. Don't forget to save!" else "Note is no longer private. Don't forget to save!",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                    ) {
//                        Text(text = if (isPrivate.value) "Make public" else "Make private")
                        Icon(
                            imageVector = if (isPrivate.value) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Notes Privacy",
                        )
                    }
                }
            }, modifier = Modifier.fillMaxSize()) { innerPadding ->

                Column(modifier = Modifier.padding(innerPadding)) {
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .padding(horizontal = 8.dp), // 1 Add some padding to the row
                        horizontalArrangement = Arrangement.SpaceBetween, // Arrange items in row
                        verticalAlignment = Alignment.CenterVertically, // Center items vertically
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
                            modifier = Modifier.padding(0.dp),
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
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()),
                    ) {
                        // Note Content TextField
                        OutlinedTextField(
                            value = noteContent,
                            onValueChange = { noteContent = it },
                            label = { Text("Content") },
                            modifier =
                                Modifier
                                    .weight(1f)
//                            .fillMaxSize()
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .defaultMinSize(minHeight = 300.dp),
                        )

                        if (currentImage.path.isNotEmpty()) {
                            val file = File(currentImage.path)
                            val bitmap = BitmapFactory.decodeFile(file.path).asImageBitmap()
                            Image(
                                bitmap = bitmap,
                                contentDescription = "Captured image",
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
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
//        }
            }
        }
    }
}
