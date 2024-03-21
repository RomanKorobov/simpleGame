package com.example.simplegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlayViewModel(private val repository: Repository): ViewModel() {
    private val handler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private val _leftField = MutableLiveData<Array<IntArray>>()
    val leftField: MutableLiveData<Array<IntArray>>
        get() = _leftField
    private val _rightField = MutableLiveData<Array<IntArray>>()
    val rightField: MutableLiveData<Array<IntArray>>
        get() = _rightField
    private val _upField = MutableLiveData<Array<IntArray>>()
    val upField: MutableLiveData<Array<IntArray>>
        get() = _upField
    private val _downField = MutableLiveData<Array<IntArray>>()
    val downField: MutableLiveData<Array<IntArray>>
        get() = _downField
    private val _leftReady = MutableLiveData<Boolean>()
    private val _rightReady = MutableLiveData<Boolean>()
    private val _upReady = MutableLiveData<Boolean>()
    private val _downReady = MutableLiveData<Boolean>()
    val leftReady: MutableLiveData<Boolean>
        get() = _leftReady
    val rightReady: MutableLiveData<Boolean>
        get() = _rightReady
    val upReady: MutableLiveData<Boolean>
        get() = _upReady
    val downReady: MutableLiveData<Boolean>
        get() = _downReady

    fun predict() {
        viewModelScope.launch (Dispatchers.Default + handler){
            val left = async { repository.swipeLeft() }
            _leftField.postValue(left.await())
            _leftReady.postValue(true)
            val right = async { repository.swipeRight() }
            _rightField.postValue(right.await())
            _rightReady.postValue(true)
            val up = async { repository.swipeUp() }
            _upField.postValue(up.await())
            _upReady.postValue(true)
            val down = async { repository.swipeDown() }
            _downField.postValue(down.await())
            _downReady.postValue(true)
        }
    }
    fun reset() {
        _downReady.postValue(false)
        _leftReady.postValue(false)
        _upReady.postValue(false)
        _rightReady.postValue(false)
    }
}