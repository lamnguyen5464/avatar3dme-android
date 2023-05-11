package com.lamnguyen5464.avatar3dme.view

//import com.lamnguyen5464.avatar3dme.core.utils.toBase64
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpSuccessResponse
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.utils.toStringData
import com.lamnguyen5464.avatar3dme.core.viewer.ModelSurfaceView
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.feature.RequestFactory
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Only display the onboarding the first time download and open the app
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isFirstRun = prefs.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            findViewById<Button>(R.id.toCaptureViewBt).setOnClickListener {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }

//            val editor = prefs.edit()
//            editor.putBoolean("isFirstRun", false)
//            editor.apply()
        } else {
            findViewById<Button>(R.id.toCaptureViewBt).setOnClickListener {
                startActivity(Intent(this, FaceShootActivity::class.java))
            }
        }

        findViewById<Button>(R.id.toModelViewBt).setOnClickListener {
//            startActivity(Intent(this, CustomizeActivity::class.java))

            val bottomSheetShare = SharePlaygroundFragment()
            bottomSheetShare.show(supportFragmentManager, SharePlaygroundFragment.TAG)

        }
//        startActivity(Intent(this, FaceShootActivity::class.java))

//        startActivity(
//            FlutterActivity.createDefaultIntent(this)
//        )
//        supportFragmentManager.beginTransaction()
//            .add(R.id.flutterfragment, FlutterFragment.createDefault(), "tag")
//            .commit()

    }
}
