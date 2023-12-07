package com.example.apkcocina.features.recetasBase.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.features.recetasBase.usecase.GetRecetasBaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecetasBaseViewModel @Inject constructor(
val getRecetasBaseUseCase: GetRecetasBaseUseCase
) : ViewModel(){

    val mutableRecetas = MutableLiveData<List<Receta>>()
    val loading = MutableLiveData<Boolean>()
    fun loadRecetas(){
        viewModelScope.launch(Dispatchers.IO){
            loading.postValue(true)
            val result = getRecetasBaseUseCase()
            if(result != null)mutableRecetas.postValue(result)
            loading.postValue(false)
        }
    }

}