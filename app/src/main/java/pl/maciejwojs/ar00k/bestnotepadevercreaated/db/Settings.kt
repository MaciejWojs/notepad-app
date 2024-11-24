package pl.maciejwojs.ar00k.bestnotepadevercreaated.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
* @brief Reprezentuje encję Ustawienia z nazwą, opisem, statusem i identyfikatorem.
 *
* Klasa Ustawienia jest oznaczona adnotacją @Entity, aby wskazać, że jest encją bazy danych.
* Zawiera nazwę, opis, status i unikalny identyfikator.
 *
* @property name Nazwa ustawienia.
* @property description Opis ustawienia.
* @property isSet Status ustawienia.
* @property settingsID Unikalny identyfikator dla każdego ustawienia, automatycznie generowany przez bazę danych.
 */
@Parcelize
@Entity(tableName = "settings")
data class Settings(
    val name: String,
    val description: String = "",
    val isSet: Boolean = false,
    @PrimaryKey(autoGenerate = true) val settingsID: Long = 0,
) : Parcelable
