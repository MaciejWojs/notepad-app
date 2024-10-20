package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.util.Random

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
        enableEdgeToEdge()
        setContent {
            BestNotepadEverCreatedTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                Column(Modifier.verticalScroll(rememberScrollState(1))) {
//                    var i = 0
                    for (i in 0..7) {
                        val rnd: Random = Random()
                        val color: Color =
                            Color(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                        Box(
                            Modifier
                                .background(color)
                                .padding(5.dp)
                                .scale(0.9f)
                                .fillMaxWidth()
                                .height(250.dp)
                        )
                        {
                            Greeting(name = "Witaj swiecie")
                        }
                    }
                }
            }
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

    /**
     * Wywoływana przed zniszczeniem aktywności.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onDestroy().
     *
     * @see ComponentActivity.onDestroy
     */
    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "onDestroy()", Toast.LENGTH_LONG).show()
    }

    /**
     * Wywoływana, gdy system zamierza wznowić wcześniejszą aktywność.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onPause().
     *
     * @see ComponentActivity.onPause
     */
    override fun onPause() {
        super.onPause()
        Toast.makeText(applicationContext, "onPause()", Toast.LENGTH_LONG).show()
    }

    /**
     * Wywoływana po zatrzymaniu aktywności i ponownym uruchomieniu.
     *
     * Wyświetla wiadomość Toast, aby wskazać, że aktywność jest w stanie onRestart().
     *
     * @see ComponentActivity.onRestart
     */
    override fun onRestart() {
        super.onRestart()
        Toast.makeText(applicationContext, "onRestart()", Toast.LENGTH_LONG).show()
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
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

//test workflow