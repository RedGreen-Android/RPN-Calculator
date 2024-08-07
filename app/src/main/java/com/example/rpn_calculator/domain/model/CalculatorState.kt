package com.example.rpn_calculator.domain.model

/**
 * Represents the state of the calculator's stack.
 *
 * **Properties**:
 * - `stack`: A list of `Double` values representing the operands and results in the stack.
 *
 * **Default Value**:
 * - An empty list indicating an initial empty stack.
 */
data class CalculatorState(
    val stack: List<Double> = emptyList(),
    val shouldQuit: Boolean = false // Flag to mimic Quit functionality
)

sealed class Operation {
    data class PushNumber(val number: Double) : Operation()
    object Add : Operation()
    object Subtract : Operation()
    object Multiply : Operation()
    object Divide : Operation()
    object Quit : Operation()
    object Error : Operation()
}