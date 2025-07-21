package com.rafih.justfocus.domain.usecase.focushistory

import com.rafih.justfocus.data.model.FocusHistory
import com.rafih.justfocus.data.repository.FocusHistoryRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchFocusHistoryUseCase @Inject constructor(
    private val focusHistoryRepository: FocusHistoryRepositoryImpl
) {
    operator fun invoke(): Flow<Map<Long, List<FocusHistory>>> {
        return focusHistoryRepository.getFocusHistory()
    }
}