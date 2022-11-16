package com.farhanryanda.challangechapter5.viewmodel

import com.farhanryanda.challangechapter5.model.ResponseUserItem
import com.farhanryanda.challangechapter5.model.DataUser
import com.farhanryanda.challangechapter5.network.RestfulUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class ViewModelUserTest {
    lateinit var service: RestfulUser

    @Before
    fun setUp() {
        service = mockk()
    }

    @Test
    fun getAllUser() : Unit = runBlocking {

        val responGetAllUser = mockk<Call<List<ResponseUserItem>>>()

        every {
            runBlocking {
                service.getAllUser()
            }
        } returns responGetAllUser

        val result = service.getAllUser()

        verify {
            runBlocking {
                service.getAllUser()
            }
        }
        assertEquals(result,responGetAllUser)
    }

    @Test
    fun postUser() : Unit = runBlocking {

        val responPostUser = mockk<Call<ResponseUserItem>>()

        every {
            runBlocking {
                service.postUser(DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))
            }
        } returns responPostUser

        val result = service.postUser(DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))

        verify {
            runBlocking {
                service.postUser(DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))
            }
        }
        assertEquals(result,responPostUser)
    }

    @Test
    fun updateUser() : Unit = runBlocking {

        val responUpdateUser = mockk<Call<ResponseUserItem>>()

        every {
            runBlocking {
                service.updateUser("12",DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))
            }
        } returns responUpdateUser

        val result = service.updateUser("12",DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))

        verify {
            runBlocking {
                service.updateUser("12",DataUser("Farhan Ryanda","frhz","2022","22","Bekasi"))
            }
        }
        assertEquals(result,responUpdateUser)
    }

    @Test
    fun getUserById() : Unit = runBlocking {

        val responGetUserById = mockk<Call<ResponseUserItem>>()

        every {
            runBlocking {
                service.getUserById("12")
            }
        } returns responGetUserById

        val result = service.getUserById("12")

        verify {
            runBlocking {
                service.getUserById("12")
            }
        }
        assertEquals(result,responGetUserById)
    }
}