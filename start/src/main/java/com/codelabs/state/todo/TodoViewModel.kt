/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codelabs.state.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
    // remove the LiveData and replace it with a mutableStateListOf
    //private var _todoItems = MutableLiveData(listOf<TodoItem>())
    //val todoItems: LiveData<List<TodoItem>> = _todoItems


//STATES
    // state: todoItems
    var todoItems = mutableStateListOf<TodoItem>()
        //restricting writes to this state object - only visible inside the ViewModel.
        private set

    //state
    private var editPosition by mutableStateOf(-1)

    // state
    val currentEditItem: TodoItem?
        get() = todoItems.getOrNull(editPosition)


    //EVENTS
    // event: add Item to the list
    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }


    // event: item selected
    fun onEditItemSelected(item: TodoItem) {

        editPosition = todoItems.indexOf(item)
    }


    //event: editItemChanged
    fun onEditItemChange(item: TodoItem) {

        //ensure the currentItem is non-null
        val currentItem = requireNotNull(currentEditItem)

        //ensure the ids match otherwise throw an exception
        require(currentItem.id == item.id) {
            //exception message
            "You can only change an item with the same id as currentEditItem"
        }

        //set currentItem to item
        todoItems[editPosition] = item
    }

    //event: editing done
    fun onEditDone() {
        editPosition = -1
    }

    // event: removeItem
    fun removeItem(item: TodoItem) {
        todoItems.remove(item)
        onEditDone() // don't keep the editor open when removing items
    }

}
