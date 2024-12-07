/**
 * @file MainActivity.kt
 * @brief Plik odpowiadający za stworzenie instancji view modeli i nawigację do odpowiednich stron
 *
 *
 *
 */

package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.CreateNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.EditNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.HamburgerPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.MainPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesWithTagPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.SettingsPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.TrashPage

/**
* Główna aktywność aplikacji BestNotepadEverCreated.
*
* Ta aktywność rozszerza [FragmentActivity] i służy jako główny punkt wejścia do aplikacji.
* Używa Jetpack Compose do interfejsu użytkownika i wyświetla komunikaty Toast podczas zmian cyklu życia.
*/
// Possibly a big change, Zmieniłem z ComponentActivity na Framgent Activity bo wymagało tego biometricPromptManager
class MainActivity : FragmentActivity() {
    private lateinit var biometricPromptManager: BiometricPromptManager

    /**
     * Wywoływana, gdy aktywność jest uruchamiana.
     *
     * Tutaj powinno się odbywać większość inicjalizacji: wywołanie [setContentView] w celu nadmuchania UI aktywności,
     * użycie [findViewById] do programatycznej interakcji z widżetami w UI, wywołanie [managedQuery] w celu pobrania
     * dostawców treści, itp.
     *
     * @param savedInstanceState Jeśli aktywność jest ponownie inicjalizowana po wcześniejszym zamknięciu, to ten Bundle
     * zawiera dane, które dostarczyła ostatnio w [onSaveInstanceState].
     * @note Uwaga: W przeciwnym razie jest null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BiometricPromptManager to handle biometric authentication
        biometricPromptManager = BiometricPromptManager(this)
        // Initialize DAO to interact with the database
        val dao: NotesDao = NotesDatabase.getInstance(this).dao

        val context = this
        enableEdgeToEdge()

        // Launch coroutine to set up default relations and tags in the database if not already present
        lifecycleScope.launch {
//            if (dao.isAddingRelations() == 0) {
//                dao.insertRelation()
//            }
//            if (dao.getTagsCount() == 0L) {
//                dao.insertTag(tag = Tag("Zakupy"))
//                dao.insertTag(tag = Tag("Szkoła"))
//            }
        }

        // Initialize ViewModels with a custom factory
        val notesViewModel by viewModels<NotesViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(dao) as T
                }
            }
        })
        val tagsViewModel by viewModels<TagsViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TagsViewModel(dao) as T
                }
            }
        })
        val notesWithTagPageViewModel by viewModels<NotesTagsCrossRefViewModel>(factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NotesTagsCrossRefViewModel(dao) as T
                }
            }
        })

        // Set up the Jetpack Compose UI
        setContent {
            val navController = rememberNavController()
            NavHost(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                navController = navController,
                startDestination = "MainPage",
                enterTransition = {
                    slideInHorizontally { it } +
                        fadeIn(
                            initialAlpha = 0.75f,
                            animationSpec = tween(1000),
                        )
                },
                exitTransition = {
                    slideOutHorizontally { -it } +
                        fadeOut(
                            targetAlpha = 0.75f,
                            animationSpec = tween(1000),
                        )
                },
                popEnterTransition = {
                    slideInHorizontally { -it } +
                        fadeIn(
                            initialAlpha = 0.75f,
                            animationSpec = tween(1000),
                        )
                },
                popExitTransition = {
                    slideOutHorizontally { it } +
                        fadeOut(
                            targetAlpha = 0.75f,
                            animationSpec = tween(1000),
                        )
                },
            ) {
                composable("MainPage") {
                    MainPage(
                        navController,
                        viewModel = notesViewModel,
                        navigateToEditNotePage = { note ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("note", note)
                            navController.navigate("EditNotePage")
                        },
                        biometricPromptManager = biometricPromptManager,
                        context = context,
                    )
                }

                composable("TrashPage") {
                    TrashPage(navController, notesViewModel)
                }

                composable("SettingsPage") {
                    SettingsPage(navController)
                }
                composable("HamburgerPage") {
                    HamburgerPage(
                        navController,
                        tagsViewModel,
//                        onCreate = { tagName ->
//                        tagsViewModel.viewModelScope.launch {
//                            dao.insertTag(
//                                Tag(tagName),
//                            )
//                        }
//                    }, onDelete = { tag ->
//                        tagsViewModel.viewModelScope.launch {
//                            dao.deleteTag(tag)
//                        }
//                    }, onEdit = { tag, name ->
//                        tagsViewModel.viewModelScope.launch {
//                            dao.updateTag(id = tag.tagID, name = name)
//                        }
//                        onEvent = { tagsViewModel.onEvent(it) },
// //                        viewModel = TODO(),
// //                        onTag = TODO()
                    )
                }
                composable("pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.CreateNotePage") {
                    val tags = tagsViewModel.state.collectAsState().value.tags
                    CreateNotePage(
                        navigator = navController,
                        viewModel = notesViewModel,
//                        onCreate = { title, content, map ->
//                            val note = Note(title, content, isPrivate = )
//                            notesViewModel.viewModelScope.launch {
//                                val insertedNoteID = dao.insertNote(note)
//                                if (map.isNotEmpty()) {
//                                    map.forEach { entry ->
//                                        if (entry.value) {
//                                            dao.insertRelationBetweenNoteAndTag(
//                                                insertedNoteID,
//                                                entry.key.tagID,
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        },
                        tags = tags,
                    )
                }
                composable(
                    "NotesWithTagPage/{tagID}",
                    arguments =
                        listOf(
                            navArgument("tagID") {
                                type = NavType.LongType
                                nullable = false
                            },
                        ),
                ) { backStackEntry ->
                    val tagID =
                        backStackEntry.arguments?.getLong("tagID")
                            ?: error("Required argument 'tagID' is missing")
                    NotesWithTagPage(
                        navigator = navController,
                        viewModel = notesWithTagPageViewModel,
                        notesViewModel = notesViewModel,
                        biometricPromptManager = biometricPromptManager,
                        tagID = tagID,
                        navigateToEditNotePage = { note ->
                            navController.currentBackStackEntry?.savedStateHandle?.set("note", note)
                            navController.navigate("EditNotePage")
                        },
                    )
                }
                composable("EditNotePage") {
                    val note =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Note>("note")
                    if (note != null) {
                        notesWithTagPageViewModel.loadTagsByNote(note.noteID)

                        val currentTags =
                            notesWithTagPageViewModel.state.collectAsState().value.tagsWithNote.flatMap { it.tags }

                        // Ensure that the tags are loaded before showing the EditNotePage
                        EditNotePage(
                            navigator = navController,
//                            onEdit = { title, content ->
// //                                notesViewModel.viewModelScope.launch {
// //                                    dao.updateNote(note.noteID, title, content)
// //                                }
// //                                notesViewModel.onEvent(NotesEvent.UpdateNote(note.copy(title = title, content = content)))
//                                 },
//                            viewModel = notesViewModel,
                            onEvent = { notesViewModel.onEvent(it) },
                            note = note,
                            tags = tagsViewModel.state.collectAsState().value.tags,
                            currentNoteTags = currentTags, // This will now be populated correctly
                            onTagEdit = { n, tag, addNote ->
                                tagsViewModel.onEvent(
                                    if (addNote) {
                                        TagsEvent.AddTagToNote(
                                            noteID = n.noteID,
                                            tagID = tag.tagID,
                                        )
                                    } else {
                                        TagsEvent.RemoveTagFromNote(
                                            noteID = n.noteID,
                                            tagID = tag.tagID,
                                        )
                                    },
                                )
                                notesWithTagPageViewModel.loadTagsByNote(noteID = note.noteID)
                            },
                        )
                    }
                }
            }
        }
    }
}

/**
* Kompozycyjna funkcja wyświetlająca tytuł notatki.
*
* Ta funkcja wyświetla tekst tytułu notatki z określoną grubością czcionki.
*
* @param noteTitle Tytuł notatki do wyświetlenia.
* @param modifier [Modifier] zastosowany do kompozycyjnego układu. Domyślnie jest to pusty modyfikator.
* @param weight [FontWeight] zastosowany do stylu czcionki tekstu tytułu notatki. Domyślnie jest to FontWeight(900).
*/
@Composable
fun CreateNoteTitle(
    noteTitle: String,
    modifier: Modifier = Modifier,
    weight: FontWeight? = FontWeight(weight = 900),
) {
    Text(
        text = noteTitle,
        modifier = modifier,
        fontWeight = weight,
    )
}
