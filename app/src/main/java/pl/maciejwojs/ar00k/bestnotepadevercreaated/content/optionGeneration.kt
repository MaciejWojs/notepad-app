package pl.maciejwojs.ar00k.bestnotepadevercreaated.content


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun GenerateOption(optionText: String = "sample text", optionTitle: String = "sample title") {
    var checked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
//            .background(Color.Red)
            .clickable { }
            .fillMaxWidth(0.8f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        )
    {
        Column {
            Text(
                text = optionTitle,
                modifier = Modifier,
//                .padding(10.dp),

                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,


                )
            Text(
                text = optionText,
                modifier = Modifier
//                .padding(10.dp)
            )
        }

        Switch(

            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
    }
}