/**
 * @file BiometricPromptManager.kt
 * @brief Plik zawiera implementację menedżera biometrycznego.
 *
 * Plik ten definiuje klasę BiometricPromptManager, która zarządza wyświetlaniem
 * biometrycznego promptu autoryzacyjnego w aplikacji.
 */
package pl.maciejwojs.ar00k.bestnotepadevercreaated

import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Klasa zarządzająca wyświetlaniem biometrycznego promptu autoryzacyjnego.
 *
 * @param activity Aktywność, w której wyświetlany jest prompt.
 */
class BiometricPromptManager(
    private val activity: FragmentActivity,
) {
    private val resultChannel = Channel<BiometricResult>()
    val promptResults = resultChannel.receiveAsFlow()

    /**
     * Wyświetla biometryczny prompt autoryzacyjny.
     *
     * @param title Tytuł promptu.
     * @param description Opis promptu.
     */
    fun showBiometricPrompt(
        title: String,
        description: String,
    ) {
        val manager = BiometricManager.from(activity)
        val authenticators =
            if (Build.VERSION.SDK_INT >= 30) {
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL or BIOMETRIC_WEAK
            } else {
                BIOMETRIC_STRONG
            }

        val promptInfo =
            PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setAllowedAuthenticators(authenticators)

        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.HardwareUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.FeatureUnavailable)
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationNotSet)
                return
            }
            else -> Unit
        }

        val prompt =
            BiometricPrompt(
                activity,
                object : BiometricPrompt.AuthenticationCallback() {
                    /**
                     * Wywoływana, gdy wystąpi błąd autoryzacji.
                     *
                     * @param errorCode Kod błędu.
                     * @param errString Opis błędu.
                     */
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence,
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        resultChannel.trySend(BiometricResult.AuthenticationError(errString.toString()))
                    }

                    /**
                     * Wywoływana, gdy autoryzacja zakończy się sukcesem.
                     *
                     * @param result Wynik autoryzacji.
                     */
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                    }

                    /**
                     * Wywoływana, gdy autoryzacja nie powiedzie się.
                     */
                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        resultChannel.trySend(BiometricResult.AuthenticationFailed)
                    }
                },
            )
        prompt.authenticate(promptInfo.build())
    }

    /**
     * Interfejs wyników biometrycznej autoryzacji.
     */
    sealed interface BiometricResult {
        /**
         * Brak dostępnego sprzętu biometrycznego.
         */
        data object HardwareUnavailable : BiometricResult

        /**
         * Funkcja biometryczna niedostępna.
         */
        data object FeatureUnavailable : BiometricResult

        /**
         * Błąd autoryzacji biometrycznej.
         *
         * @param error Opis błędu.
         */
        data class AuthenticationError(val error: String) : BiometricResult

        /**
         * Autoryzacja biometryczna nie powiodła się.
         */
        data object AuthenticationFailed : BiometricResult

        /**
         * Autoryzacja biometryczna zakończona sukcesem.
         */
        data object AuthenticationSuccess : BiometricResult

        /**
         * Brak ustawionej autoryzacji biometrycznej.
         */
        data object AuthenticationNotSet : BiometricResult
    }
}
