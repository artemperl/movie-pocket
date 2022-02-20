package com.ap.moviepocket.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

/**
 * Executes business logic in its execute method and keep posting updates to the result as
 * [UseCaseResult<R>].
 * Handling an exception (emit [UseCaseResult.Error] to the result) is the subclasses's responsibility.
 */
abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {
    operator fun invoke(parameters: P): Flow<UseCaseResult<R>> = execute(parameters)
        .onStart { emit(UseCaseResult.Loading) }
        .catch { e -> emit(UseCaseResult.Error(Exception(e))) }
        .flowOn(coroutineDispatcher)

    protected abstract fun execute(parameters: P): Flow<UseCaseResult<R>>
}
