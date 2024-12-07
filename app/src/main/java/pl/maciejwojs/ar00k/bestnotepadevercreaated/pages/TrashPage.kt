/**
 * @file TrashPage.kt
 * @brief Plik zawiera implementację strony kosza.
 *
 * Plik ten definiuje kompozycje i funkcje związane z wyświetlaniem notatek
 * znajdujących się w koszu w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNoteTrash
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

@Composable
fun TrashPage(
    navigator: NavController,
    viewModel: NotesViewModel,
) {
    val notes by viewModel.trashNotes.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    BestNotepadEverCreatedTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate("pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.CreateNotePage")
                },
                containerColor = MaterialTheme.colorScheme.onSecondary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(20.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSecondary)
                        .padding(horizontal = 8.dp), // 1 Add some padding to the row
                    horizontalArrangement = Arrangement.SpaceBetween, // Arrange items in row
                ) {
                    GenerateIconButton { navigator.navigate("HamburgerPage") }
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
                                // TODO search implementation
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
                    GenerateIconButton(icon = Icons.Default.Settings, "Settings") {
                        navigator.navigate("SettingsPage")
                    }
                }

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    item {
                        Text(
                            text = "Deleted Notes:",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(8.dp),
                        )
                    }

                    items(notes, key = { it.noteID }) { singleNote ->
                        GenerateNoteTrash(
                            note = singleNote,
                            onRestore = {
                                Log.d("TestPage", "Deleting note: ${singleNote.title}")
//                                viewModel.onEvent(NotesEvent.DeleteNote(singleNote))
                                viewModel.onEvent(NotesEvent.UpdateNoteTrash(singleNote))
                                // Log the current state after deletion
//                                Log.d(
//                                    "TestPage",
//                                    "Current notes after deletion: ${notes.notes.map { it.title }}",
//                                )
                            },
                            onDelete = {
                                Log.d("TestPage", "Deleting note: ${singleNote.title}")
                                viewModel.onEvent(NotesEvent.DeleteNote(singleNote))
                                // Log the current state after deletion
                            },
                        )
                    }
                }
            }
        }
    }
}
