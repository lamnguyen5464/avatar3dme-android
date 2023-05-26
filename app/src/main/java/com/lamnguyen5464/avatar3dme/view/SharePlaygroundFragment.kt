package com.lamnguyen5464.avatar3dme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.feature.PreloadFlutterEngine.Companion.ENGINE_ID
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.android.TransparencyMode
import kotlinx.android.synthetic.main.fragment_share_playground.flutterfragment

class SharePlaygroundFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "SharePlaygroundFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share_playground, container, false)

        val shareFlutterFragment = FlutterFragment.withCachedEngine(ENGINE_ID)
            .transparencyMode(TransparencyMode.transparent)
            .build<FlutterFragment>()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.flutterfragment, shareFlutterFragment, "shareFlutterFragment")
            .commit()
        return view

    }
}