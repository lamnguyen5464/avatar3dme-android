package com.lamnguyen5464.avatar3dme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.card.MaterialCardView
import com.lamnguyen5464.avatar3dme.R
import com.lamnguyen5464.avatar3dme.core.http.SimpleHttpSuccessResponse
import com.lamnguyen5464.avatar3dme.core.providers.Providers
import com.lamnguyen5464.avatar3dme.core.viewer.ObjModel
import com.lamnguyen5464.avatar3dme.feature.RequestFactory
import com.lamnguyen5464.avatar3dme.model.CreditCardModel
import com.lamnguyen5464.avatar3dme.model.CreditCardsModel
import com.lamnguyen5464.avatar3dme.view.CustomizeActivity
import kotlinx.coroutines.launch

class CustomizeViewModel(private val activity: CustomizeActivity) {

    private val stream = MutableLiveData<CreditCardsModel>()

    val modelState = MutableLiveData<ObjModel>()

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

    private fun getMockModel() =
        activity.applicationContext.resources.openRawResource(R.raw.default_face).let {
            val model = ObjModel(it)
            Providers.currentModel = model
            it.close()
            model
        }

    fun init() {
        updateCards()
        modelState.value = Providers.currentModel ?: getMockModel()
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
        }
    }

    private fun requestDecor() = Providers.commonIOScope.launch {
        val request = RequestFactory.createDecorRequest(MOCK_ITEMS.random())
        when (val response = Providers.httpClient.send(request)) {
            is SimpleHttpSuccessResponse -> {
                val model = ObjModel(response.inputStream)
                activity.runOnUiThread {
                    modelState.value = model
                }
            }

            else -> {
                // TODO: handle exception here
            }
        }
    }


    private fun updateCards() {
        println("Update card")
        this.requestDecor()
        stream.value = CreditCardsModel(
            cardOneLeft = cardOneLeft,
            cardCenter = cardCenter,
            cardOneRight = cardOneRight
        )
    }

    companion object {
        private val MOCK_ITEMS = listOf(
            "hair_female_1",
            "hair_female_2",
            "hair_male_0",
            "hair_male_1",
            "hair_male_2",
            "hair_male_3",
        )
    }
}