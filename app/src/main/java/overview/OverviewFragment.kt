package overview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.example.persistenz.R
import kotlinx.android.synthetic.main.fragment_overview.*

class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()
    private val permissionsViewModel: PermissionsViewModel by activityViewModels()

    private val SHAREDPREFERENCES: String = "CounterPreference"
    private val COUNTER_KEY = "counterKey"

    companion object {
        fun newInstance(): OverviewFragment {
            return OverviewFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        setupCounter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPreferencesViewModel.setResources(resources)
        sharedPreferencesViewModel.setPreferences(
            PreferenceManager.getDefaultSharedPreferences(
                context
            )
        )
        sharedPreferencesViewModel.getPreferencesSummary().observe(
            viewLifecycleOwner,
            Observer { newVal: String -> preferencesText.text = newVal })
        btn_set_default_preferences.setOnClickListener {
            sharedPreferencesViewModel.setDefaultPreferences()
        }
        permissionsViewModel.setActivity(requireActivity())
        permissionsViewModel.getFinalString()
            .observe(viewLifecycleOwner, Observer { newVal: String -> editText.setText(newVal) })
        btn_save.setOnClickListener {
            permissionsViewModel.saveText(editText.text.toString(), chk_external_store.isChecked)
        }
        btn_load.setOnClickListener {
            permissionsViewModel.loadText(chk_external_store.isChecked)
        }
    }

    private fun setupCounter() {
        val preferences =
            requireActivity().getSharedPreferences(SHAREDPREFERENCES, Context.MODE_PRIVATE)
        var preferenceCounter = preferences.getInt(COUNTER_KEY, 0)
        val editor = preferences.edit()
        editor.putInt(COUNTER_KEY, ++preferenceCounter)
        editor.apply()

        counterViewElement.text =
            "MainActivity.onResume wurde seit der Installation dieser App $preferenceCounter aufgeruft"
    }

}