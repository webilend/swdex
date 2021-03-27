package it.webilend.swdex

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            openAppModeAlert()
        }, 1500)

    }

    fun openAppModeAlert() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle(R.string.choice_appmode_title)
            .setMessage(R.string.choice_appmode_msg)
            .setPositiveButton(R.string.choice_appmode_grid) { dialog, which ->
                Log.i("Choice mode","grid")
            }
            .setNegativeButton(R.string.choice_appmode_list){ dialog, which ->
                Log.i("Choice mode","list")
            }

        dialogBuilder.show()
    }
}