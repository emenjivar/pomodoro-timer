package com.emenjivar.core.usecase

import com.emenjivar.core.repository.SettingsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SetSoundsEnableUseCaseTest {
    private lateinit var repository: SettingsRepository
    private lateinit var useCase: SetSoundsEnableUseCase

    @Before
    fun setup() {
        repository = Mockito.mock(SettingsRepository::class.java)
        useCase = SetSoundsEnableUseCase(repository)
    }

    @Test
    fun `setSoundsEnableUseCase when param is true`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setSounds(true)).then {
                value = true
                it
            }
            useCase.invoke(true)
            Assert.assertTrue(value ?: false)
        }

    @Test
    fun `setSoundsEnableUseCase when param is false`(): Unit =
        runBlocking {
            var value: Boolean? = null
            Mockito.`when`(repository.setSounds(false)).then {
                value = false
                it
            }
            useCase.invoke(false)
            Assert.assertFalse(value ?: true)
        }
}
