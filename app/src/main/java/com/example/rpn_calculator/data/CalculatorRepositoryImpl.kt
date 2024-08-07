package com.example.rpn_calculator.data

import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.model.CalculatorState
import com.example.rpn_calculator.domain.RpnCalculator
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorRepositoryImpl @Inject constructor(
    private val calculator: RpnCalculator
) : CalculatorRepository {
    private val _stateFlow = MutableStateFlow(CalculatorState())
    override val stateFlow = _stateFlow.asStateFlow()

    private val _errorFlow = MutableStateFlow("")
    override val errorFlow = _errorFlow.asStateFlow()

    override fun executeCommand(command: Operation) {
        // Execute Operation and Update state flow with the new state
        try {
            calculator.execute(command)
            _stateFlow.value = calculator.state
            if (command is Operation.Error) {
                _errorFlow.value = "Error: Invalid operation"
            }
        } catch (e: Exception) {
            _errorFlow.value = "Error: Invalid operation, ${e.message}"
        }
    }
}

