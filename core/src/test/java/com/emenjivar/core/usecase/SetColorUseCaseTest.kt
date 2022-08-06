package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetColorUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SetColorUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = SetColorUseCase(repository)
    }

    @Test
    fun `setColorUseCase when color param is set should save this value`(): Unit =
        runBlocking {
            var value: Int? = null
            Mockito.`when`(repository.setColor(10)).then {
                value = 10
                it
            }
            useCase.invoke(10)
            Assert.assertEquals(10, value)
        }
}
