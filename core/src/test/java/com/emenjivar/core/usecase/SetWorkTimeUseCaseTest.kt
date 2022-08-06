package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetWorkTimeUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SetWorkTimeUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = SetWorkTimeUseCase(repository)
    }

    @Test
    fun `setWorkTimeUseCase should save param value`(): Unit =
        runBlocking {
            var value: Long? = null
            Mockito.`when`(repository.setWorkTime(1000L)).then {
                value = 1000L
                it
            }
            useCase.invoke(1000L)
            Assert.assertEquals(1000L, value)
        }
}
