package com.example.apkcocina.features.recetasOnline.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apkcocina.features.recetasOnline.useCase.GetRandomRecetaUseCase
import com.example.apkcocina.features.recetasOnline.useCase.GetRecetaUseCase
import com.example.apkcocina.features.recetasOnline.useCase.GetRecetasFavUseCase
import com.example.apkcocina.features.recetasOnline.useCase.GetRecetasOnlineUseCase
import com.example.apkcocina.features.recetasOnline.useCase.RemoveRecetaFavUseCase
import com.example.apkcocina.features.recetasOnline.useCase.SetRecetaFavUseCase
import com.example.apkcocina.utils.core.Event
import com.example.apkcocina.utils.model.Receta
import com.example.apkcocina.utils.states.CrearRecetaStateUI
import com.example.apkcocina.utils.states.RandomRecetaState
import com.example.apkcocina.utils.states.RecetaState
import com.example.apkcocina.utils.states.RecetasOnlineState
import com.example.apkcocina.utils.states.SetRecetaFavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRecetasViewModel @Inject constructor(
   private val getRecetasOnlineUseCase: GetRecetasOnlineUseCase,
   private val getRecetaUseCase: GetRecetaUseCase,
   private val getRandomRecetaUseCase: GetRandomRecetaUseCase,
   private val setRecetaFavUseCase: SetRecetaFavUseCase,
   private val removeRecetaFavUseCase: RemoveRecetaFavUseCase,
   private val getRecetasFavUseCase: GetRecetasFavUseCase
) : ViewModel() {

    private val _recetasFavState = MutableStateFlow(CrearRecetaStateUI())
    val recetasFavState : StateFlow<CrearRecetaStateUI> = _recetasFavState

    //LIST RECETAS
    private val _recetasResult = MutableLiveData<Event<List<Receta>>>()
    val recetasResult: LiveData<Event<List<Receta>>> = _recetasResult

    private val _recetasError = MutableLiveData<Event<String>>()
    val recetasError: LiveData<Event<String>> = _recetasError

    //RECETA
    private val _recetaResult = MutableLiveData<Event<Pair<Receta,Boolean>>>()
    val recetaResult: LiveData<Event<Pair<Receta,Boolean>>> = _recetaResult

    private val _recetaError = MutableLiveData<Event<String>>()
    val recetaError: LiveData<Event<String>> = _recetasError

    //RECETA RANDOM
    private val _idRecetaRandom = MutableLiveData<Event<Pair<String,String>>>()
    val idRecetaRandom: LiveData<Event<Pair<String,String>>> = _idRecetaRandom

    private val _idRecetaRandomeError = MutableLiveData<Event<String>>()
    val idRecetaRandomeError: LiveData<Event<String>> = _idRecetaRandomeError

    //RECETAS FAV
    private val _setRecetaFav = MutableLiveData<Event<Boolean>>()
    val setRecetaFav: LiveData<Event<Boolean>> = _setRecetaFav

    private val _setRecetaFavError = MutableLiveData<Event<String>>()
    val setRecetaFavError: LiveData<Event<String>> = _setRecetaFavError

    private val _removeRecetaFav = MutableLiveData<Event<Boolean>>()
    val removeRecetaFav: LiveData<Event<Boolean>> = _removeRecetaFav

    private val _removeRecetaFavError = MutableLiveData<Event<String>>()
    val removeRecetaFavError: LiveData<Event<String>> = _removeRecetaFavError

    private val _getRecetasFav = MutableLiveData<Event<List<Receta>>>()
    val getRecetasFav: LiveData<Event<List<Receta>>> = _getRecetasFav

    private val _getRecetasFavError = MutableLiveData<Event<String>>()
    val getRecetasFavError: LiveData<Event<String>> = _getRecetasFavError

    private val _sinRecetasFav = MutableLiveData<Event<Boolean>>()
    val sinRecetasFav: LiveData<Event<Boolean>> = _sinRecetasFav

    fun getRecetas() = viewModelScope.launch {
        when(val result = getRecetasOnlineUseCase() ){
            is RecetasOnlineState.Error -> _recetasError.value = Event(result.error)
            is RecetasOnlineState.Successfull -> _recetasResult.value = Event(result.listRecetas)
            else->{}
        }
    }

    fun getReceta(recetaId : String,collection : String) = viewModelScope.launch {
        when(val result = getRecetaUseCase(recetaId,collection) ){
            is RecetaState.Error -> _recetaError.value = Event(result.error)
            is RecetaState.Successfull -> _recetaResult.value = Event(Pair(result.receta,result.isFav))
        }
    }

    fun getRandomReceta() = viewModelScope.launch {
        when(val result = getRandomRecetaUseCase()){
            is RandomRecetaState.Error -> _idRecetaRandomeError.value = Event(result.error)
            is RandomRecetaState.Successfull -> _idRecetaRandom.value = Event(Pair(result.idRecetaRandom,result.nombreReceta))
        }
    }

    fun changeFav(recetaId: String,fav : Boolean){
        if(fav)
            setRecetaFav(recetaId)
        else
            deleteRecetaFav(recetaId)
    }

    private fun setRecetaFav(recetaId : String){
        viewModelScope.launch {
            when(val result = setRecetaFavUseCase(recetaId)){
                is SetRecetaFavState.Error -> {_setRecetaFavError.value = Event(result.error)
                }
                SetRecetaFavState.Successfull ->{_setRecetaFav.value = Event(true)
                }
            }
        }
    }

    private fun deleteRecetaFav(recetaId : String){
        viewModelScope.launch {
            when(val result = removeRecetaFavUseCase(recetaId)){
                is SetRecetaFavState.Error -> {_removeRecetaFavError.value = Event(result.error) }
                SetRecetaFavState.Successfull ->{_removeRecetaFav.value = Event(true)}
            }
        }
    }

    fun getRecetasFav(){
        viewModelScope.launch {
            _recetasFavState.value = CrearRecetaStateUI(isLoading = true)
            when(val result = getRecetasFavUseCase()){
                is RecetasOnlineState.Error -> {_getRecetasFavError.value = Event(result.error) }
                is RecetasOnlineState.Successfull -> {_getRecetasFav.value = Event(result.listRecetas) }
                RecetasOnlineState.SinRecetasFav -> {_sinRecetasFav.value = Event(true)}
            }
            _recetasFavState.value = CrearRecetaStateUI(isLoading = false)
        }
    }
}