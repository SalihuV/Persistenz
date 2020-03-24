package overview

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.persistenz.R

class SharedPreferencesViewModel : ViewModel() {

    private val PREF_TEA_PREFFERED = "teaPreferred"
    private val PREF_TEA_WITH_SUGAR = "teaWithSugar"
    private val PREF_TEA_SWEETENER = "teaSweetener"

    private var preferencesSummary: MutableLiveData<String> = MutableLiveData()
    private lateinit var prefs: SharedPreferences
    private lateinit var res: Resources

    fun getPreferencesSummary(): MutableLiveData<String> {
        return preferencesSummary
    }

    fun setResources(resources: Resources) {
        res = resources
    }

    fun setPreferences(sharedPrefs: SharedPreferences) {
        prefs = sharedPrefs
        buildPreferencesSummaryString()
    }

    fun setDefaultPreferences() {
        val editor = prefs.edit()
        editor.putString(PREF_TEA_PREFFERED, "Lipton")
        editor.putBoolean(PREF_TEA_WITH_SUGAR, true)
        editor.putString(PREF_TEA_SWEETENER, "natural")
        editor.apply()
        buildPreferencesSummaryString()
    }

    fun buildPreferencesSummaryString() {
        val sweetener = prefs.getBoolean(PREF_TEA_WITH_SUGAR, true)
        val teaSweetener = prefs.getString(PREF_TEA_SWEETENER, "natural")
        var resID = res.getStringArray(R.array.teaSweetenerValues).indexOf(teaSweetener)
        if (sweetener) {
            preferencesSummary.value =
                "Ich trinke am liebsten " + prefs.getString(
                    PREF_TEA_PREFFERED,
                    "Lipton"
                ) + ", mit " + res.getStringArray(
                    R.array.teaSweetener
                )[resID]
        } else {
            preferencesSummary.value = "Ich trinke am liebsten ungesüssten" + prefs.getString(
                PREF_TEA_PREFFERED,
                "Lipton/Pfefferminze"
            )
        }
    }

}