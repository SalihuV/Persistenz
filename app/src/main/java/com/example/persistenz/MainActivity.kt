package com.example.persistenz


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import overview.OverviewFragment
import overview.PermissionsViewModel
import overview.TeaPreferenceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val permissionsViewModel : PermissionsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main, OverviewFragment.newInstance()).commit()
        }

    }

    fun setPreferences(view: View) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main, TeaPreferenceFragment.newInstance()).addToBackStack(null)
            .commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsViewModel.handleResult(requestCode, grantResults)
    }
}
