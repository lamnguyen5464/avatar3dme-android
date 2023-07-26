package com.lamnguyen5464.avatar3dme.viewmodel

import android.widget.Button
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

enum class DecorMode(val rawValue: String) {
    HAIR("HAIR"), GLASSES("GLASSES")
}

class CustomizeViewModel(private val activity: CustomizeActivity) {

    private val stream = MutableLiveData<CreditCardsModel>()

    val modelState = MutableLiveData<ObjModel>()
    var currentHair = ""
    val currentMode = MutableStateFlow(DecorMode.HAIR)

    val modelStream: LiveData<CreditCardsModel>
        get() = stream

    private val hairs = listOf(
        CreditCardModel(thumbnailURI = R.drawable.hair_male_0, accessoryID = "hair_male_0"),
        CreditCardModel(thumbnailURI = R.drawable.hair_male_1, accessoryID = "hair_male_1"),
        CreditCardModel(thumbnailURI = R.drawable.hair_male_2, accessoryID = "hair_male_2"),
        CreditCardModel(thumbnailURI = R.drawable.hair_male_3, accessoryID = "hair_male_3"),
        CreditCardModel(thumbnailURI = R.drawable.hair_female_1, accessoryID = "hair_female_1"),
    )

    private val glasses = listOf(
        CreditCardModel(thumbnailURI = R.drawable.glasses0, accessoryID = "glasses0"),
        CreditCardModel(thumbnailURI = R.drawable.glasses2, accessoryID = "glasses2"),
        CreditCardModel(thumbnailURI = R.drawable.glasses4, accessoryID = "glasses4"),
    )


    private var data = hairs

    private var currentIndex = 0

    private fun cardOneLeft() = data[currentIndex % data.size]
    private fun cardCenter() = data[(currentIndex + 1) % data.size]
    private fun cardOneRight() = data[(currentIndex + 2) % data.size]

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

        currentMode.onEach {
            val nextData = if (it.rawValue == DecorMode.GLASSES.rawValue) {
                glasses
            } else {
                hairs
            }
            if (data != nextData) {
                data = nextData
                updateCards()
            }
        }.launchIn(Providers.commonIOScope)
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
            println("Applying accesory #" + cardCenter().accessoryID)
        }

        activity.findViewById<Button>(R.id.bt_mode).setOnClickListener {
            val mode =
                if (currentMode.value.rawValue != DecorMode.HAIR.rawValue) DecorMode.HAIR else DecorMode.GLASSES
            Providers.commonIOScope.launch {
                currentMode.emit(mode)
            }
        }
    }

    private fun requestDecor(accessoryID: String) = Providers.commonIOScope.launch {
        val attachment = if (accessoryID.contains("hair")) {
            currentHair = accessoryID
            ""
        } else {
            "_$currentHair"
        }
        activity.showLoading()
        val request = RequestFactory.createDecorRequest(accessoryID, attachment)
        when (val response = Providers.httpClient.send(request)) {
            is SimpleHttpSuccessResponse -> {
                val model = ObjModel(response.inputStream)
                activity.runOnUiThread {
                    modelState.value = model
                }
            }

            else -> {
            }
        }
        activity.hideLoading()
    }


    private fun updateCards() {
        this.requestDecor(cardCenter().accessoryID)
        activity.runOnUiThread {
            stream.value = CreditCardsModel(
                cardOneLeft = cardOneLeft(),
                cardCenter = cardCenter(),
                cardOneRight = cardOneRight()
            )
        }
    }

}