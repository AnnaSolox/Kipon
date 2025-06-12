package com.annasolox.kipon.domain.account

import com.annasolox.kipon.core.utils.mappers.AccountMapper
import com.annasolox.kipon.data.repository.AccountRepository
import com.annasolox.kipon.ui.models.AccountDetails
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class GetAccountUseCase (
    private val accountRepository: AccountRepository,
    private val mutex: Mutex
) {
    suspend operator fun invoke (id: Long): AccountDetails {
        return mutex.withLock {
            val accountResponse = accountRepository.getAccountById(id)
            AccountMapper.toDetailAccount(accountResponse)
        }
    }
}