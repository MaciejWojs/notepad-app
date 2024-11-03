package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Note
import pl.maciejwojs.ar00k.bestnotepadevercreaated.db.Tag
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.CreateNotePage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.HamburgerPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.MainPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.NotesListPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.SettingsPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.pages.TestPage
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme

/**
 * Główna aktywność aplikacji BestNotepadEverCreated.
 *
 * Ta aktywność rozszerza [ComponentActivity] i służy jako główny punkt wejścia do aplikacji.
 * Używa Jetpack Compose do wyświetlania interfejsu użytkownika i wyświetla wiadomości Toast w trakcie zmian cyklu życia.
 */
class MainActivity : ComponentActivity() {

    /**
     * Wywoływana, gdy aktywność jest po raz pierwszy tworzona.
     *
     * Ta metoda ustawia interfejs użytkownika dla aktywności przy użyciu Jetpack Compose.
     *
     * @param savedInstanceState Pakiet zawierający poprzednio zapisany stan aktywności.
     * @see ComponentActivity.onCreate
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao: NotesDao = NotesDatabase.getInstance(this).dao
        enableEdgeToEdge()
        lifecycleScope.launch {
            dao.insertNote(note = Note("Pierwsza notatka", "lorem ipsum abc"))
            dao.insertNote(note = Note("Druga notatka", "Las jest jednym z najcenniejszych ekosystemów na Ziemi, pełnym różnorodnych gatunków roślin i zwierząt, które wzajemnie się wspierają i tworzą złożony system biologiczny. Drzewa, jako najważniejszy składnik lasu, pełnią kluczową rolę w produkcji tlenu, który jest niezbędny dla życia na naszej planecie. Liście drzew pochłaniają dwutlenek węgla i dzięki procesowi fotosyntezy przekształcają go w tlen. Las jest również miejscem zamieszkania dla wielu gatunków zwierząt, od drobnych owadów po duże ssaki. Różne gatunki zamieszkują poszczególne piętra lasu, tworząc wielowarstwowy system, gdzie każde stworzenie ma swoją rolę. Wśród drzew znajdują schronienie, pożywienie i możliwość rozmnażania się. Ludzie także korzystają z bogactw lasu, pozyskując drewno, owoce leśne czy grzyby. Wiele społeczności zależy od zasobów lasów do codziennego przetrwania. Niestety, działalność człowieka, taka jak wycinka drzew i zanieczyszczenie środowiska, prowadzi do degradacji lasów. W związku z tym ochrona lasów stała się kluczowym celem działań ekologicznych. Warto zrozumieć, jak ważne są lasy, by móc dążyć do ich zrównoważonej ochrony i przyszłości dla wszystkich mieszkańców naszej planety."))
            dao.insertNote(note = Note("trzecia notatka", "lorem ipsum abc"))
            dao.insertTag(tag = Tag("Zakupy"))
        }
        val notesViewModel by viewModels<NotesViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return NotesViewModel(dao) as T
                    }
                }
            }
        )
        val tagsViewModel by viewModels<TagsViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return TagsViewModel(dao) as T
                    }
                }
            }
        )


        setContent {
            val state by notesViewModel.state.collectAsState()
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "MainPage") {
                composable("MainPage") {
                    MainPage(navController, viewModel = notesViewModel)
                }
                composable("SettingsPage") {
                    SettingsPage(navController)
                }
                composable("NotesListPage") {
                    NotesListPage(navController, viewModel=notesViewModel)
                }
                composable("TestPage") {
                    TestPage(navController, viewModel = notesViewModel, dao = dao )
                }
                composable("HamburgerPage") {
                    HamburgerPage(navController, tagsViewModel)
                }
                composable("CreateNotePage") {
                    CreateNotePage(navController)
                }
            }
//            DestinationsNavHost(navGraph = RootNavGraph)
        }
    }


    /**
     * Wywoływana, gdy aktywność staje się widoczna dla użytkownika.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onStart().
     *
     * @see ComponentActivity.onStart
     */
    override fun onStart() {
        super.onStart()
        Toast.makeText(applicationContext, "onStart()", Toast.LENGTH_LONG).show()
    }

}

/**
 * Funkcja kompozytowa, która wyświetla wiadomość powitalną.
 *
 * Ta funkcja wyświetla tekst powitalny z użyciem podanej nazwy.
 *
 * @param name Nazwa, która ma być wyświetlana w wiadomości powitalnej.
 * @param modifier [Modifier] stosowany do układu kompozytowego. Domyślnie jest to pusty modifier.
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(400)) {
    Text(
        text = "$name!", modifier = modifier, fontWeight = weight

    )
}

@Composable
fun CreateNoteTitle(
    noteTitle: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(900)
) {
    Text(
        text = noteTitle, modifier = modifier, fontWeight = weight

    )
}

/**
 * Funkcja podglądu dla kompozytu [Greeting].
 *
 * Wyświetla podgląd wiadomości powitalnej z "Android" jako nazwą.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BestNotepadEverCreatedTheme {
        Greeting("Android")
    }
}