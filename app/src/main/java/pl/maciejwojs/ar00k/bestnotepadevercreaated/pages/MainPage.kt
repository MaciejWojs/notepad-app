/**
 * @file MainPage.kt
 * @brief Plik zawiera implementację głównej strony aplikacji.
 *
 * Plik ten definiuje kompozycje i funkcje związane z wyświetlaniem głównej strony
 * oraz obsługą nawigacji w aplikacji.
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
import androidx.compose.foundation.layout.Spacer
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
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.BiometricPromptManager
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesEvent
import pl.maciejwojs.ar00k.bestnotepadevercreaated.NotesViewModel
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateIconButton
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNote
import pl.maciejwojs.ar00k.bestnotepadevercreaated.content.GenerateNotePrivate
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

@Composable
fun MainPage(
    navigator: NavController,
    viewModel: NotesViewModel,
    navigateToEditNotePage: (Note) -> Unit,
    biometricPromptManager: BiometricPromptManager,
    context: android.content.Context,
) {
    val coroutine = rememberCoroutineScope()
    val isLoggedIn by viewModel.authenticated.collectAsState()
    val state = viewModel.state.collectAsState().value
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
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    GenerateIconButton { navigator.navigate("HamburgerPage") }
                    Row(
                        Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .align(Alignment.CenterVertically)
                            .fillMaxHeight(0.55f)
                            .fillMaxWidth(0.75f)
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
                        Text(text = "Search", Modifier.padding(start = 5.dp))
                        GenerateIconButton(Icons.Default.Search, "Search menu") {}
                    }
                    GenerateIconButton(icon = Icons.Default.Settings, "Settings") {
                        navigator.navigate("SettingsPage")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(state.notes.filter { !it.isDeleted }, key = { it.noteID }) { singleNote ->
                        if (singleNote.isPrivate && !isLoggedIn) {
                            Log.d("TestPage", "MAINPAGE: Access authenticated: ${state.authenticated}")
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
                                                    viewModel.setAuthenticated(true)
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
                                    viewModel.onEvent(NotesEvent.UpdateNoteTrash(singleNote))
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
