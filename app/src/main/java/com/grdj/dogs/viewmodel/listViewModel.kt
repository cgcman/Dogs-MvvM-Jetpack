package com.grdj.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grdj.dogs.model.DogBreed
import com.grdj.dogs.model.DogDatabase
import com.grdj.dogs.model.DogsApiService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class listViewModel(application: Application): BaseViewModel(application) {
    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        fetchFromRemote()
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogList : List<DogBreed>) {
                        /*dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false*/
                        storeDogsLocaly(dogList)
                        //dogsRetrieve(dogList)
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun dogsRetrieve(dogList : List<DogBreed>){
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    private fun storeDogsLocaly(dogList : List<DogBreed>){
        launch {
            DogDatabase(getApplication()).dog
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}