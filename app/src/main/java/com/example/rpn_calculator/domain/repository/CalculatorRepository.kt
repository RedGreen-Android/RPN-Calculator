package com.example.rpn_calculator.domain.repository

import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.model.CalculatorState
import kotlinx.coroutines.flow.Flow

interface CalculatorRepository {
    val stateFlow: Flow<CalculatorState>
    val errorFlow: Flow<String>  // Error handling for invalid input

    fun executeCommand(command: Operation)
}