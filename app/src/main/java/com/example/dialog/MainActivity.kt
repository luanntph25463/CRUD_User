@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.dialog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dialog.ui.theme.DialogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DialogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserListScreen()
                }
            }
        }
    }
}

@Composable
fun DialogExample() {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Show Dialog")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Dialog Title") },
                text = { Text(text = "Dialog Content") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(text = "Dismiss")
                    }
                }
            )
        }
    }
}

@Composable
fun UserListScreen() {
    val userViewModel: UserViewModel = viewModel()
    val users = userViewModel.users
    var showDialog by remember { mutableStateOf(false) }
    var editUser by remember { mutableStateOf<User?>(null) }
    var userName by remember { mutableStateOf("") }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        // Display the list of users
        UserList(
            users = users,
            onEditUser = { user ->
                editUser = user
                userName = user.name
                showDialog = true
            },
            onDeleteUser = { userViewModel.deleteUser(it) }
        )
        // Thêm danh sách người dùng
        LaunchedEffect(true) {
            userViewModel.addUser("John Doe")
            userViewModel.addUser("Jane Smith")
            userViewModel.addUser("Michael Johnson")
        }

        // Dialog for adding or editing a user
        if (showDialog) {
            val isEdit = (editUser != null)
            val dialogTitle = if (isEdit) "Edit User" else "Add User"
            val buttonText = if (isEdit) "Save" else "Add"

            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    editUser = null
                    userName = ""
                },
                title = { Text(text = dialogTitle) },
                text = {
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text(text = "Name") }
                    )
                },
                confirmButton = {
                    IconButton(
                        onClick = {
                            val inputUserName = userName
                            if (isEdit && editUser != null) {
                                val updatedUser = editUser!!.copy(name = inputUserName)
                                userViewModel.updateUser(updatedUser)
                            } else {
                                userViewModel.addUser(inputUserName)
                            }
                            showDialog = false
                            editUser = null
                            userName = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = buttonText
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            editUser = null
                            userName = ""
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        } else {
            // Nút "Add" khi không có hộp thoại hiển thị
            IconButton(
                onClick = {
                    showDialog = true
                    editUser = null // Đặt editUser thành null để xác định là thêm người dùng mới
                    userName = "" // Đặt userName thành rỗng để xác định là thêm người dùng mới
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Composable
fun UserList(
    users: List<User>,
    onEditUser: (User) -> Unit,
    onDeleteUser: (User) -> Unit,modifier: Modifier = Modifier.padding(30.dp)
) {
    LazyColumn {
        items(users) { user ->
            UserListItem(
                user = user,
                onEditUser = onEditUser,
                onDeleteUser = onDeleteUser
            )
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onEditUser: (User) -> Unit,
    onDeleteUser: (User) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = user.name, Modifier.weight(1f))
        Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconButton(
                onClick = { onEditUser(user) },
                modifier = Modifier.padding(start = 8.dp).
                background(Color.Green) .clip(shape = RoundedCornerShape(16.dp)), // Đặt góc bo tròn ở đây,

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit"
                )
            }
            Spacer(Modifier.padding(top = 10.dp))
            IconButton(
                onClick = { onDeleteUser(user) },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(Color.Red)
                     .clip(shape = RoundedCornerShape(16.dp)),
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete user",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DialogTheme {
        UserListScreen()
    }
}