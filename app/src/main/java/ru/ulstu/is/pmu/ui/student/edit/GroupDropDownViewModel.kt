package ru.ulstu.`is`.pmu.ui.student.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ulstu.`is`.pmu.database.student.model.Group
import ru.ulstu.`is`.pmu.database.student.repository.GroupRepository

class GroupDropDownViewModel(
    private val groupRepository: GroupRepository
) : ViewModel() {
    var groupsListUiState by mutableStateOf(GroupsListUiState())
        private set

    var groupUiState by mutableStateOf(GroupUiState())
        private set

    init {
        viewModelScope.launch {
            groupsListUiState = GroupsListUiState(groupRepository.getAllGroups())
        }
    }

    fun setCurrentGroup(groupId: Int) {
        val group: Group? =
            groupsListUiState.groupList.firstOrNull { group -> group.uid == groupId }
        group?.let { updateUiState(it) }
    }

    fun updateUiState(group: Group) {
        groupUiState = GroupUiState(
            group = group
        )
    }
}

data class GroupsListUiState(val groupList: List<Group> = listOf())

data class GroupUiState(
    val group: Group? = null
)

fun Group.toUiState() = GroupUiState(group = Group(uid = uid, name = name))