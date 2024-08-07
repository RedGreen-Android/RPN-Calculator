package com.example.rpn_calculator.domain.repository

import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.model.CalculatorState
import com.example.rpn_calculator.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CalculatorRepository {
    val stateFlow: Flow<CalculatorState>
    val errorFlow: Flow<Resource<String>> // Error handling for invalid input

    fun executeCommand(command: Operation)
}