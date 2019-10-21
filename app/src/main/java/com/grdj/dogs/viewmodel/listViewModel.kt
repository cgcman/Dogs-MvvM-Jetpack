package com.grdj.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grdj.dogs.model.DogBreed
import com.grdj.dogs.model.DogDao
import com.grdj.dogs.model.DogDatabase
import com.grdj.dogs.model.DogsApiService
import com.grdj.dogs.util.SharePreferencesHelper
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
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // NANOSECONDS
    private var prefHelper = SharePreferencesHelper(application)

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh(){
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime()- updateTime < refreshTime){
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase(){
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieve(dogs)
            Toast.makeText(getApplication(), "Dogs Retrieve fom local database", Toast.LENGTH_SHORT).show()
        }
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
                        Toast.makeText(getApplication(), "Dogs Retrieve fom remote", Toast.LENGTH_SHORT).show()
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
            val dao: DogDao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()

            val result= dao.insertAll(*dogList.toTypedArray())

            var i = 0

            while(i < dogList.size){
                dogList[i].uuid = result[i].toInt()
                i++
            }

            dogsRetrieve(dogList)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}