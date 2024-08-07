package com.example.rpn_calculator.domain

import com.example.rpn_calculator.domain.model.Operation
import com.example.rpn_calculator.domain.repository.CalculatorRepository
import javax.inject.Inject

/**
 * Class handles the business logic for processing
 * arithmetic operations and interacting with the repository.
 */
class CalculateUseCase @Inject constructor(
    private val repository: CalculatorRepository
) {
    val stateFlow = repository.stateFlow
    val errorFlow = repository.errorFlow  // Error handling (done here rather than States)

    fun executeCommand(command: Operation) {
        repository.executeCommand(command)
    }
}
