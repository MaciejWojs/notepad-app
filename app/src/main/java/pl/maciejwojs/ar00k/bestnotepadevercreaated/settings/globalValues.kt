package pl.maciejwojs.ar00k.bestnotepadevercreaated.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

const val roundness = 10
const val iconWeightRatio = 0.8f
val iconModifier =
    Modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(roundness.dp))
        .border(1.dp, androidx.compose.ui.graphics.Color.Gray, RoundedCornerShape(roundness.dp))
        .width(100.dp)
