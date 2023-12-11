package ru.ulstu.`is`.pmu.ui.student.edit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.ulstu.`is`.pmu.R
import ru.ulstu.`is`.pmu.database.student.model.Group
import ru.ulstu.`is`.pmu.database.student.model.Student
import ru.ulstu.`is`.pmu.ui.AppViewModelProvider
import ru.ulstu.`is`.pmu.ui.theme.PmudemoTheme

@Composable
fun StudentEdit(
    navController: NavController,
    viewModel: StudentEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    groupViewModel: GroupDropDownViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    groupViewModel.setCurrentGroup(viewModel.studentUiState.studentDetails.groupId)
    StudentEdit(
        studentUiState = viewModel.studentUiState,
        groupUiState = groupViewModel.groupUiState,
        groupsListUiState = groupViewModel.groupsListUiState,
        onClick = {
            coroutineScope.launch {
                viewModel.saveStudent()
                navController.popBackStack()
            }
        },
        onUpdate = viewModel::updateUiState,
        onGroupUpdate = groupViewModel::updateUiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupDropDown(
    groupUiState: GroupUiState,
    groupsListUiState: GroupsListUiState,
    onGroupUpdate: (Group) -> Unit
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(top = 7.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            value = groupUiState.group?.name
                ?: stringResource(id = R.string.student_group_not_select),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize()
        ) {
            groupsListUiState.groupList.forEach { group ->
                DropdownMenuItem(
                    text = {
                        Text(text = group.name)
                    },
                    onClick = {
                        onGroupUpdate(group)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentEdit(
    studentUiState: StudentUiState,
    groupUiState: GroupUiState,
    groupsListUiState: GroupsListUiState,
    onClick: () -> Unit,
    onUpdate: (StudentDetails) -> Unit,
    onGroupUpdate: (Group) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = studentUiState.studentDetails.firstName,
            onValueChange = { onUpdate(studentUiState.studentDetails.copy(firstName = it)) },
            label = { Text(stringResource(id = R.string.student_firstname)) },
            singleLine = true
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = studentUiState.studentDetails.lastName,
            onValueChange = { onUpdate(studentUiState.studentDetails.copy(lastName = it)) },
            label = { Text(stringResource(id = R.string.student_lastname)) },
            singleLine = true
        )
        GroupDropDown(
            groupUiState = groupUiState,
            groupsListUiState = groupsListUiState,
            onGroupUpdate = {
                onUpdate(studentUiState.studentDetails.copy(groupId = it.uid))
                onGroupUpdate(it)
            }
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = studentUiState.studentDetails.phone,
            onValueChange = { onUpdate(studentUiState.studentDetails.copy(phone = it)) },
            label = { Text(stringResource(id = R.string.student_phone)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = studentUiState.studentDetails.email,
            onValueChange = { onUpdate(studentUiState.studentDetails.copy(email = it)) },
            label = { Text(stringResource(id = R.string.student_email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Button(
            onClick = onClick,
            enabled = studentUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.student_save_button))
        }
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StudentEditPreview() {
    PmudemoTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            StudentEdit(
                studentUiState = Student.getStudent().toUiState(true),
                groupUiState = Group.DEMO_GROUP.toUiState(),
                groupsListUiState = GroupsListUiState(listOf()),
                onClick = {},
                onUpdate = {},
                onGroupUpdate = {}
            )
        }
    }
}