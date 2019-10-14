package com.grdj.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grdj.dogs.model.DogBreed

class DetailViewModel: ViewModel() {
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){
        val dog= DogBreed("1", "Corgi", "15 years", "breedGroup", "breedFor", "temperamented", "")
        dogLiveData.value = dog
    }

}