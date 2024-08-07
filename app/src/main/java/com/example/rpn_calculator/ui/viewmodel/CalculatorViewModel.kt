package com.example.rpn_calculator.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpn_calculator.R
import com.example.rpn_calculator.domain.CalculateUseCase
import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculateUseCase: CalculateUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _output = MutableStateFlow<Resource<String>>(Resource.loading(null))
    val output: StateFlow<Resource<String>> = _output.asStateFlow()

    private val _error = MutableStateFlow<Resource<String>>(Resource.loading(null))
    val error: StateFlow<Resource<String>> = _error.asStateFlow()

    private val _quit = MutableLiveData<Boolean>()
    val quit: LiveData<Boolean> = _quit  //(used livedata instead flow to show both)

    init {
        calculateUseCase.stateFlow.onEach { state ->
            val result = state.stack.lastOrNull()?.toString() ?: ""
            _output.value = Resource.success(result)

            if (state.shouldQuit) {
                _quit.value = true
            }
        }.launchIn(viewModelScope)

        calculateUseCase.errorFlow.onEach { errorMessage ->
            _error.value =
                errorMessage
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
        _error.value = Resource.loading(null)
    }

    //Parse string input, used to convert input into a format to be processed
    private fun parseInput(input: String): Operation {
        return when (input.trim()) {
            context.getString(R.string.operation_add) -> Operation.Add
            context.getString(R.string.operation_subtract) -> Operation.Subtract
            context.getString(R.string.operation_multiply) -> Operation.Multiply
            context.getString(R.string.operation_divide) -> Operation.Divide
            context.getString(R.string.operation_quit) -> Operation.Quit
            else -> input.toDoubleOrNull()?.let { Operation.PushNumber(it) }
                ?: Operation.Error
        }
    }
}
