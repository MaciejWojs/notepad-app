/**
 * Plik zawiera funkcje i komponenty do edycji notatek.
 *
 * @file EditNotePage.kt
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.settings.roundness
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * Strona edycji notatki.
 *
 * @param navigator Kontroler nawigacji.
 * @param onEdit Funkcja wywoływana przy edycji notatki.
 * @param onTagEdit Funkcja wywoływana przy edycji tagów notatki.
 * @param note Notatka do edycji.
 * @param tags Lista dostępnych tagów.
 * @param currentNoteTags Lista tagów przypisanych do bieżącej notatki.
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
    // Update checkedMap based on the current note's tags when tags or currentNoteTags change
    LaunchedEffect(tags, currentNoteTags) {
        checkedMap.clear() // Clear any previous values
        tags.forEach { tag ->
            // Initialize the checked state for each tag based on its presence in currentNoteTags
            checkedMap[tag] = currentNoteTags.contains(tag)
        }
    }

    Log.i("Liczba", "l tagow: ${currentNoteTags.size}")

    // Use LaunchedEffect to load the note when the noteID changes
    BestNotepadEverCreatedTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(horizontal = 8.dp), // 1 Add some padding to the row
                    horizontalArrangement = Arrangement.SpaceBetween, // Arrange items in row
                ) {
                    GenerateIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        "Back to main screen",
                    ) {
                        if (!navigator.popBackStack()) {
                            navigator.navigate("MainPage")
                        } else {
                            if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                                onEvent(NotesEvent.UpdateNote(note.copy(title = noteTitle, content = noteContent)))
//                                checkedMap.forEach { entry ->
//                                    Log.i("TAG", "id: ${entry.key} ${entry.value}")
//                                    onTagEdit(note, entry.key, entry.value)
//                                }
                            } else {
                                Toast.makeText(context, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    Row(
                        Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .align(Alignment.CenterVertically)
                            .fillMaxHeight(0.55f)
                            .fillMaxWidth(0.75f)
//                                    .background(Color.Red)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(25.dp),
                            )
                            .clickable {
                                // TODO search Implementation
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Search",
                            Modifier.padding(start = 5.dp),
//                                            .align(alignment = Alignment.CenterVertically)
                        )

                        GenerateIconButton(Icons.Default.Search, "Search menu") {}
                    }
                    GenerateIconButton(
                        icon = Icons.Default.Check,
                        "Save Note",
                    ) {
                        if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                            onEvent(NotesEvent.UpdateNote(note.copy(title = noteTitle, content = noteContent)))
                            checkedMap.forEach { entry ->
                                Log.i("TAG", "id: ${entry.key} ${entry.value}")
                                onTagEdit(note, entry.key, entry.value)
                            }
                        } else {
                            Toast.makeText(context, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                }`

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .verticalScroll(
                                rememberScrollState(1),
                            ),
                ) {
                    Card(
                        Modifier.shadow(
                            elevation = 20.dp,
                            spotColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(roundness.dp))
//                                        .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(roundness.dp),
                                    )
                                    .padding(7.dp)
                                    .scale(0.9f)
                                    .defaultMinSize(minHeight = 500.dp)
                                    .fillMaxWidth(0.8f),
                        ) {
                            // Note title and Content
                            Column {
                                // Note Title TextField
                                OutlinedTextField(
                                    value = noteTitle,
                                    onValueChange = { noteTitle = it },
                                    label = { Text("Title") },
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                )

                                // Note Content TextField
                                OutlinedTextField(
                                    value = noteContent,
                                    onValueChange = { noteContent = it },
                                    label = { Text("Content") },
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .defaultMinSize(minHeight = 300.dp),
                                )
                                Button(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = { showBottomSheet = true },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit tags",
                                    )
                                    Text(text = "Add/modify tags")
                                }
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState,
                    ) {
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

                        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                        }) {
                            Text("Hide")
                        }
                    }
                }
            }
        }
    }
}
