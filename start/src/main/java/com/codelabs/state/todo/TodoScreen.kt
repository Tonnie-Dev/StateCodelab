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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codelabs.state.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */


@Composable
fun TodoScreen(
    items: List<TodoItem>,
    currentEditItem: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onEditItemSelected: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit

) {
    Column {

        // add TodoItemInputBackground and TodoItem at the top of TodoScreen
        TodoItemInputBackground(elevate = true, modifier = Modifier.fillMaxWidth()) {
            TodoItemEntryInput(onItemComplete = onAddItem)
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todoItem ->

                if (currentEditItem?.id == todoItem.id) {
                    TodoItemInlineEditor(
                        item = todoItem,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        
                    )
                } else {


                    TodoRow(
                        todo = todoItem,
                        onItemClicked = { onEditItemSelected(it) },
                        modifier = Modifier.fillParentMaxWidth(),
                        iconAlpha = 0.8f
                    )
                }

            }
        }

        // For quick testing, a random item generator button
        Button(
            onClick = { onAddItem(generateRandomTodoItem()) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text("Add random item")
        }
    }
}

/**
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 */


@Composable
fun TodoRow(
    todo: TodoItem, onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha: Float = remember(todo.id) { randomTint() }
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)

        Icon(
            imageVector = todo.icon.imageVector,
            tint = LocalContentColor.current.copy(alpha = iconAlpha),
            contentDescription = stringResource(id = todo.icon.contentDesc)
        )
    }
}

private fun randomTint(): Float {

    return Random.nextFloat().coerceIn(0.3f, 0.9f)

}

//STATEFUL - No UI Code


@Composable
fun TodoItemEntryInput(onItemComplete: (TodoItem) -> Unit) {

    //hold state for TextField
    val (text, setText) = remember { mutableStateOf("") }

    //hold state for icon row state - currently selected icon
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.default) }
    val isIconRowVisible = text.isNotBlank()

    //declare lambda function submit that handles  a submit event
    val submitAction = {

        onItemComplete(TodoItem(text, icon)) // send onItemComplete event up
        setIcon(TodoIcon.default) //reset it to default when the user is done
        // entering a TodoItem.
        setText("") // clear the internal text
    }


    TodoItemInput(
        text = text,
        onTextChange = setText,
        icon = icon,
        onIconChange = setIcon,
        submitAction = submitAction,
        isIconRowVisible = isIconRowVisible
    )

}

//STATELESS - has all of our UI-related code
@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    icon: TodoIcon,
    onIconChange: (TodoIcon) -> Unit,
    submitAction: () -> Unit,
    isIconRowVisible: Boolean
) {
    Column {


        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {

            //TEXTFIELD
            TodoInputText(
                text = text,
                onTextChanged = onTextChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                onImeAction = submitAction
            )

            //BUTTON
            TodoEditButton(
                onClick = {
                    submitAction()
                },
                text = "Add",
                enabled = text.isNotBlank(), // enable Button if text is not blank
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )

        }

        //swapping AnimatedIconRow for a Spacer.
        if (isIconRowVisible) {
            AnimatedIconRow(
                icon = icon,
                onIconChange = onIconChange,
                modifier = Modifier.padding(
                    top = 8.dp
                )
            )

        } else {
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}


//INLINE_EDITOR - stateless - has Views and Hoists State

@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,



) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submitAction = onEditDone,
    isIconRowVisible = true
)

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square),
        TodoItem("Valentine", TodoIcon.Event)
    )
    TodoScreen(items, null, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}


@Preview("TodoItemInput")
@Composable
fun PreviewTodoItemInput() = TodoItemEntryInput {}
@Preview(name = "TodoItemInlineEditor Preview")
@Composable
fun PreviewTodoItemInlineEditor()  = TodoItemInlineEditor(
    item = TodoItem(task = "", icon =TodoIcon.Event),
    onEditItemChange = { },
    onEditDone ={},

)




