package com.example.simplegame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlayViewModel(private val repository: Repository) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
    private val _leftField = MutableLiveData<Array<IntArray>?>()
    val leftField: MutableLiveData<Array<IntArray>?>
        get() = _leftField
    private val _rightField = MutableLiveData<Array<IntArray>?>()
    val rightField: MutableLiveData<Array<IntArray>?>
        get() = _rightField
    private val _upField = MutableLiveData<Array<IntArray>?>()
    val upField: MutableLiveData<Array<IntArray>?>
        get() = _upField
    private val _downField = MutableLiveData<Array<IntArray>?>()
    val downField: MutableLiveData<Array<IntArray>?>
        get() = _downField
    val gameOver: Boolean
        get() = invalidMove(_downField.value!!) && invalidMove(_upField.value!!)
                && invalidMove(_leftField.value!!) && invalidMove(_rightField.value!!)

    fun predict() {
        viewModelScope.launch(Dispatchers.Default + handler) {
            val left = async { repository.swipeLeft() }
            _leftField.postValue(left.await())
            val right = async { repository.swipeRight() }
            _rightField.postValue(right.await())
            val up = async { repository.swipeUp() }
            _upField.postValue(up.await())
            val down = async { repository.swipeDown() }
            _downField.postValue(down.await())
        }
    }

    fun reset() {
        _leftField.postValue(null)
        _rightField.postValue(null)
        _upField.postValue(null)
        _downField.postValue(null)
    }

    fun getField() = repository.getField()
    fun setField(curField: Array<IntArray>) {
        repository.setField(curField)
    }

    fun setNewGameField() {
        repository.setNewGameField()
    }

    fun addNewNum(targetField: Array<IntArray>) {
        repository.getNewNum(targetField)
    }

    fun invalidMove(newField: Array<IntArray>): Boolean {
        val curField = repository.getField()
        for (i in newField.indices) {
            for (j in newField.indices) {
                if (newField[i][j] != curField[i][j]) return false
            }
        }
        return true
    }
}