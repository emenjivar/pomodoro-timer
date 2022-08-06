package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetRestTimeUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SetRestTimeUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = SetRestTimeUseCase(repository)
    }

    @Test
    fun `setAutoPlayUseCase should save the param value`(): Unit =
        runBlocking {
            var value: Long? = null
            Mockito.`when`(repository.setRestTime(1000L)).then {
                value = 1000L
                it
            }
            useCase.invoke(1000L)
            Assert.assertEquals(1000L, value)
        }
}
