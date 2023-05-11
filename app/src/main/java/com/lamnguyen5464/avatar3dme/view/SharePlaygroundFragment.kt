package com.lamnguyen5464.avatar3dme.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lamnguyen5464.avatar3dme.R
import io.flutter.embedding.android.FlutterFragment
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

        childFragmentManager.let {
            it.beginTransaction()
            .replace(R.id.flutterfragment, FlutterFragment.createDefault(), "tag")
            .commit()
        }
        return view

    }
}