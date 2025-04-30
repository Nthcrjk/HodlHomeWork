package ru.flx.hodlhomework.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import ru.flx.hodlhomework.R


var CenturyGothic = androidx.compose.ui.text.font.FontFamily(
    Font((R.font.centurygothic))
)

var CenturyGothicBold = androidx.compose.ui.text.font.FontFamily(
    Font((R.font.centurygothic_bold))
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = CenturyGothicBold,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = CenturyGothicBold,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
    ),

    headlineLarge = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),

    titleLarge = TextStyle(
        fontFamily = CenturyGothicBold,
        fontSize = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = CenturyGothicBold,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),

    bodyLarge = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = CenturyGothic,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    labelLarge = TextStyle(
        fontFamily = CenturyGothicBold,
        fontSize = 18.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = CenturyGothicBold,
        fontSize = 18.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = CenturyGothic,
        fontSize = 10.sp,
    )
)