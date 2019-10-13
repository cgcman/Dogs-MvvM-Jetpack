package com.grdj.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grdj.dogs.model.DogBreed

class listViewModel: ViewModel() {
    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        val dog1 = DogBreed("1", "Corgi", "15 years", "breedGroup", "breedFor", "temperamented", "")
        val dog2 = DogBreed("2", "Labrador", "10 years", "breedGroup", "breedFor", "temperamented", "")
        val dog3 = DogBreed("3", "Boxer", "5 years", "breedGroup", "breedFor", "temperamented", "")
        val dogList = arrayListOf<DogBreed>(dog1,dog2,dog3)

        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}