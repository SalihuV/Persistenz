package overview

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class PermissionsViewModel : ViewModel() {
    private lateinit var file: File
    private lateinit var activity: Activity
    private lateinit var textToSave: String
    private val PERMISSION_WRITE_EXT = 1
    private val PERMISSION_READ_EXT = 2
    private val finalString: MutableLiveData<String> = MutableLiveData()

    fun setActivity(act: Activity) {
        activity = act
    }

    fun getFinalString(): MutableLiveData<String> {
        return finalString
    }

    fun loadText(extChecked: Boolean) {
        if (extChecked) {
            file = File("/storage/emulatd/0/file.txt")
            if (hasPermission(PERMISSION_READ_EXT)) {
                if (file.exists()) {
                    finalString.value = file.readText()
                } else {
                    finalString.value = "Keine Datei gefunden!"
                }
            } else {
                reqPermission(PERMISSION_READ_EXT)
            }
        } else {
            file = File(activity.filesDir, "file.txt")
            if (file.exists()) {
                finalString.value = file.readText()
            } else {
                finalString.value = "Keine Datei gefunden!"
            }
        }
    }

    fun saveText(text: String, extChecked: Boolean) {
        textToSave = text
        if (extChecked) {
            file = File("/storage/emulated/0/file.txt")
            if (hasPermission(PERMISSION_WRITE_EXT)) {
                file.writeText(textToSave)
                finalString.value =
                    "Datei " + file.name + "gespeichert in " + file.absolutePath

            } else {
                reqPermission(PERMISSION_WRITE_EXT)
            }
        } else {
            file = File(activity.filesDir, "file.txt")
            file.writeText(textToSave)
            finalString.value = "Datei " + file.name + "gespeichert in " + file.absolutePath
        }

    }

    private fun reqPermission(permission: Int) {
        activity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), permission)
    }

    private fun hasPermission(permission: Int): Boolean {
        when (permission) {
            PERMISSION_READ_EXT -> return (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            PERMISSION_WRITE_EXT -> return (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        }
        return false
    }

    fun handleResult(reqCode: Int, grantResults: IntArray) {
        when (reqCode) {
            PERMISSION_WRITE_EXT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    file.writeText(textToSave)
                    finalString.value =
                        "Datei " + file.name + " gespeichert in " + file.absolutePath
                } else {
                    finalString.value = "Sie haben keine Berechtigung"
                }
            }
            PERMISSION_READ_EXT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    finalString.value = file.readText()
                } else {
                    finalString.value = "Sie haben keine Berechtigung"
                }
            }
        }
    }
}