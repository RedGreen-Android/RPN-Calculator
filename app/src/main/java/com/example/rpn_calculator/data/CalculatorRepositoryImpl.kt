package com.example.rpn_calculator.data

import android.content.Context
import com.example.rpn_calculator.R
import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.model.CalculatorState
import com.example.rpn_calculator.domain.RpnCalculator
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import com.example.rpn_calculator.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorRepositoryImpl @Inject constructor(
    private val calculator: RpnCalculator,
    @ApplicationContext private val context: Context
) : CalculatorRepository {
    private val _stateFlow = MutableStateFlow(CalculatorState())
    override val stateFlow = _stateFlow.asStateFlow()

    private val _errorFlow = MutableStateFlow<Resource<String>>(Resource.loading(null))
    override val errorFlow = _errorFlow.asStateFlow()

    override fun executeCommand(command: Operation) {
        try {
           calculator.execute(command)
            _stateFlow.value = if (command is Operation.Quit) {
                _stateFlow.value.copy(shouldQuit = true)
            } else {
                _stateFlow.value.copy(stack = calculator.state.stack)
            }

            if (command is Operation.Error) {
                _errorFlow.value = Resource.error(null, context.getString(R.string.error_invalid_operation))
            } else {
                _errorFlow.value = Resource.success(null)
            }
        } catch (e: Exception) {
            _errorFlow.value = Resource.error(null,  context.getString(R.string.error_invalid_operation, e.message))
        }
    }
}

