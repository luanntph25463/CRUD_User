package com.example.dialog

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    val users = mutableStateListOf<User>()

    fun addUser(name: String) {

        // size.toString() laasy
        val user = User(id = users.size.toString(), name = name)
        users.add(user)
    }

    fun updateUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
    }

    fun deleteUser(user: User) {
        users.remove(user)
    }
}
//

//class UserViewModel : ViewModel() {
//    private val _users = MutableLiveData<List<User>>()
//    val users: LiveData<List<User>> get() = _users
//
//    init {
//        _users.value = emptyList()
//    }
//
//    fun addUser(name: String) {
//        val userList = _users.value.orEmpty().toMutableList()
//        val user = User(id = userList.size.toString(), name = name)
//        userList.add(user)
//        _users.value = userList
//    }
//
//    fun updateUser(user: User) {
//        val userList = _users.value.orEmpty().toMutableList()
//        val index = userList.indexOfFirst { it.id == user.id }
//        if (index != -1) {
//            userList[index] = user
//            _users.value = userList
//        }
//    }
//
//    fun deleteUser(user: User) {
//        val userList = _users.value.orEmpty().toMutableList()
//        userList.remove(user)
//        _users.value = userList
//    }
//}