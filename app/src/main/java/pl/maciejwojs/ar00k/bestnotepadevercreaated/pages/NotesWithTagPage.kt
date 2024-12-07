/**
 * @file NotesWithTagPage.kt
 * @brief Plik zawiera implementację strony notatek z wybranym tagiem.
 *
 * Plik ten definiuje kompozycje i funkcje związane z wyświetlaniem notatek
 * przypisanych do konkretnego tagu w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated.pages

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.BiometricPromptManager
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesTagsCrossRefViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNote
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNotePrivate
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * Strona notatek z wybranym tagiem.
 *
 * @param navigator Nawigator do nawigacji między ekranami
 * @param viewModel ViewModel dla relacji między notatkami a tagami
 * @param notesViewModel ViewModel dla notatek
 * @param biometricPromptManager Manager biometrycznego promptu
 * @param tagID Identyfikator tagu
 * @param navigateToEditNotePage Funkcja nawigująca do edycji notatki
 */
@Composable
fun NotesWithTagPage(
    navigator: NavController,
    viewModel: NotesTagsCrossRefViewModel,
    notesViewModel: NotesViewModel,
    biometricPromptManager: BiometricPromptManager,
    tagID: Long,
    navigateToEditNotePage: (Note) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val coroutine = rememberCoroutineScope()
    val isLoggedIn by notesViewModel.authenticated.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(tagID) {
        Log.d("NotesWithTagPage", "Loading notes for tagID: $tagID")
        viewModel.loadNotesByTag(tagID)
    }
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
                            // Przejdź do głównego ekranu lub innego widoku początkowego
                            navigator.navigate("MainPage")
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
                        icon = Icons.Filled.Settings,
                        "Settings",
                        transparent = true,
                        isEnabled = false,
                    ) {}
                }

//                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    item {
                        Text(
                            text = "Notes with tag: #${state.notesWithTag.firstOrNull()?.tag?.name}",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(8.dp),
//                                .align(Alignment.CenterHorizontally),
                        )
                    }
                    Log.d("NotesWithTagPage", "Loading notes for tagID: $tagID")
                    val notes = state.notesWithTag.flatMap { it.notes } // Flatten notes
                    items(notes.filter { !it.isDeleted }, key = { it.noteID }) { singleNote -> // Use flattened notes list
                        if (singleNote.isPrivate && !isLoggedIn) {
                            GenerateNotePrivate(
                                note = singleNote,
                                onDelete = {
                                    Log.d("TestPage", "Deleting note: ${singleNote.title}")
                                },
                                onEdit = {
                                    // Wykonaj akcje związane z biometrycznym promptem w kontekście kompozycji
                                    coroutine.launch {
                                        biometricPromptManager.showBiometricPrompt(
                                            title = "Biometric Authentication",
                                            description = "Authenticate to access the private note",
                                        )

                                        biometricPromptManager.promptResults.collect { result ->
                                            when (result) {
                                                is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
//                                                    navigateToEditNotePage(singleNote)
                                                    notesViewModel.setAuthenticated(true)
//                                                    Log.d("TestPage", "Authentication success")
                                                }

                                                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                                                    // Wyświetl Toast w kontekście kompozycji
                                                    Toast.makeText(
                                                        context,
                                                        "Authentication error: ${result.error}",
                                                        Toast.LENGTH_SHORT,
                                                    ).show()
                                                }

                                                else -> {
                                                    Toast.makeText(
                                                        context,
                                                        "Authentication failed",
                                                        Toast.LENGTH_SHORT,
                                                    ).show()
                                                }
                                            }
                                        }
                                    }
                                },
                            )
                        } else {
                            GenerateNote(
                                note = singleNote,
                                onDelete = {
                                    Log.d("TestPage", "Deleting note: ${singleNote.title}")
                                    notesViewModel.onEvent(NotesEvent.UpdateNoteTrash(singleNote))
                                    coroutine.launch {
                                        viewModel.loadNotesByTag(tagID)
                                    }
                                },
                                onEdit = {
                                    navigateToEditNotePage(singleNote)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
