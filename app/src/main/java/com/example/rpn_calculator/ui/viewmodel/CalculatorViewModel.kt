package com.example.rpn_calculator.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpn_calculator.domain.CalculateUseCase
import com.example.rpn_calculator.domain.model.Operation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculateUseCase: CalculateUseCase
) : ViewModel() {
    private val _output = MutableStateFlow<String>("")
    val output = _output.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _quit = MutableLiveData<Boolean>()
    val quit: LiveData<Boolean> = _quit  //(used livedata instead flow to show both)

    init {
        calculateUseCase.stateFlow.onEach { state ->
            _output.value = state.stack.lastOrNull()?.toString() ?: ""
        }.launchIn(viewModelScope)

        calculateUseCase.errorFlow.onEach { errorMessage ->
            _error.value = errorMessage
        }.launchIn(viewModelScope)
    }

    fun onInput(input: String) {
        val commands = input.trim().split("\\s+".toRegex())
        for (command in commands) {
            viewModelScope.launch {
                val operation = parseInput(command)
                calculateUseCase.executeCommand(operation)
                if (operation is Operation.Quit) {
                    _quit.value = true
                    return@launch // Exit if quit command is processed
                }
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    //Parse string input, used to convert input into a format to be processed
    private fun parseInput(input: String): Operation {
        return when (input.trim()) {
            "+" -> Operation.Add
            "-" -> Operation.Subtract
            "*" -> Operation.Multiply
            "/" -> Operation.Divide
            "q" -> Operation.Quit
            else -> input.toDoubleOrNull()?.let { Operation.PushNumber(it) }
                ?: Operation.Error
        }
    }
}
