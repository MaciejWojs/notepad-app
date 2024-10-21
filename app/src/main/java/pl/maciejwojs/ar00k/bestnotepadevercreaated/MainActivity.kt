package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.maciejwojs.ar00k.bestnotepadevercreaated.ui.theme.BestNotepadEverCreatedTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

                Column {
                    Row(
                        Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp), // Add some padding to the row
                            horizontalArrangement = Arrangement.SpaceBetween // Arrange items in row
                        ) {
                            generateIconButton {}
                            var searchCounter: Int = 0;
                            generateIconButton(Icons.Default.Search, "Search menu") {
                                Toast.makeText(
                                    applicationContext,
                                    "Clicked ${++searchCounter} ${if (searchCounter < 2) "time" else "times"}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    val currentDate = LocalDate.now()
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val formattedDate = currentDate.format(formatter)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(
                                rememberScrollState(1)

                            )

                    ) {
//                    var i = 0


                        for (i in 0..7) {
                            val rnd: Random = Random()
                            val color: Color =
                                Color(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                            var weight: FontWeight? = null
                            if (i % 2 == 0) {
                                weight = FontWeight(900)
                            }
                            val roundness = 10; // zaokraglenie musi być zsynchronizowane
                            Box(

                                Modifier

                                    .clip(RoundedCornerShape(roundness.dp))
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = Color.Black,
                                        shape = RoundedCornerShape(roundness.dp)
                                    )

                                    .padding(7.dp)
                                    .scale(0.9f)
                                    .height(250.dp)
                                    .fillMaxWidth(0.8f)

                            )

                            {
                                Column {
                                    Greeting(
                                        name = "Witaj android",
                                        weight = weight,
                                        modifier = Modifier.padding(bottom = 20.dp)
                                    )
                                    Text(
                                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id nisl eget.",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                ) {

                                    Text(
                                        text = "20-10-2024", //przyszłe pociagniecie z bazy
                                        modifier = Modifier
//                                        .align(Alignment.BottomEnd)
                                    )
                                    Text(
                                        text = "$formattedDate", //przyszłe pociagniecie z bazy
                                        modifier = Modifier
//                                        .align(Alignment.BottomEnd)

                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(50.dp))
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
fun Greeting(name: String, modifier: Modifier = Modifier, weight: FontWeight? = FontWeight(400)) {
    Text(
        text = "Hello $name!",


        modifier = modifier,
        fontWeight = weight

    )
}

@Composable
fun generateIconButton(
    icon: ImageVector = Icons.Default.Menu,
    message: String = "Menu",
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick, modifier) {
        Icon(icon, contentDescription = message)
    }
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