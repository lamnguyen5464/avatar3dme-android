package com.lamnguyen5464.avatar3dme.viewmodel

import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.card.MaterialCardView
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.model.CreditCardModel
import com.lamnguyen5464.avatar3dme.model.CreditCardsModel
import com.lamnguyen5464.avatar3dme.view.CustomizeActivity

class CreditCardsViewModel(private val activity: CustomizeActivity) {

    private val stream = MutableLiveData<CreditCardsModel>()

    val modelStream: LiveData<CreditCardsModel>
        get() = stream

    private val data = listOf(
        CreditCardModel(thumbnailURI = R.drawable.hair_1, accessoryID = "1"),
        CreditCardModel(thumbnailURI = R.drawable.hair_2, accessoryID = "2"),
        CreditCardModel(thumbnailURI = R.drawable.hair_3, accessoryID = "3"),
        CreditCardModel(thumbnailURI = R.drawable.hair_4, accessoryID = "4"),
    )
    private var currentIndex = 0

    private val cardOneLeft
        get() = data[currentIndex % data.size]
    private val cardCenter
        get() = data[(currentIndex + 1) % data.size]
    private val cardOneRight
        get() = data[(currentIndex + 2) % data.size]

    init {
        updateCards()
    }

    fun swipeRight() {
        currentIndex += 1
        updateCards()
    }

    fun swipeLeft() {
        if (currentIndex == 0) {
            currentIndex = data.size - 1
        } else {
            currentIndex -= 1
        }
        updateCards()
    }

    fun initClickListener() {
        activity.findViewById<MaterialCardView>(R.id.cardCenter).setOnClickListener {
            println("Applying accesory #" + cardCenter.accessoryID)
            //TODO: apply accessory to face
        }
    }

    private fun updateCards() {
        stream.value = CreditCardsModel(
            cardOneLeft = cardOneLeft,
            cardCenter = cardCenter,
            cardOneRight = cardOneRight
        )
    }
}