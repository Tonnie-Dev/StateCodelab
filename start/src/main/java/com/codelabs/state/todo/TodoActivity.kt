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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.codelabs.state.ui.StateCodelabTheme

class TodoActivity : ComponentActivity() {

    //initialize viewModel
    private val viewModel by viewModels<TodoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateCodelabTheme {
                Surface {
                    //integrating TodoScreen composable into TodoActivity
                    TodoActivityScreen(viewModel)

                }
            }
        }
    }


}


@Composable
fun TodoActivityScreen(todoViewModel: TodoViewModel) {

//observing LiveData inside todoViewModel
    //val items: List<TodoItem> by todoViewModel.todoItems.observeAsState(listOf())

    //Call the stateless composable passing in the items and the 2 events
    TodoScreen(items = todoViewModel.todoItems,
               currentEditItem = todoViewModel.currentEditItem,
               onAddItem = todoViewModel::addItem,
               onRemoveItem = { item -> todoViewModel.removeItem(item) },
               onEditItemSelected = todoViewModel::onEditItemSelected,
               onEditItemChange = todoViewModel::onEditItemChange,
               onEditDone = todoViewModel::onEditDone

  )

}
