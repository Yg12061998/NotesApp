package com.yogigupta1206.notesapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.repository.NotesRepository
import com.yogigupta1206.notesapp.utils.FOR_ADDING_DATA
import com.yogigupta1206.notesapp.utils.FOR_UPDATING_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.function.Predicate
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NotesRepository
) : ViewModel() {

    sealed class NavigateState {
        object NavigateBack : NavigateState()
        data class NavigateToProfile(
            val name: String
        ) : NavigateState()
    }

    sealed class UiState {
        object Loading : UiState()
        object LoadingFinish : UiState()
        object ErrorState : UiState()
    }

    sealed class NotesDataQuery {

        data class AddAllData(
            val list: MutableList<Note>
        ) : NotesDataQuery()

        data class AddData(
            val note: Note
        ) : NotesDataQuery()

        data class UpdateDataAtPosition(
            val note: Note,
            val index: Int
        ) : NotesDataQuery()

        data class DeleteDataAtPosition(
            val note: Note,
            val index: Int
        ) : NotesDataQuery()

        object DeleteAllData : NotesDataQuery()

    }

    sealed class AddOrRemoveFragmentEvent{
        data class Init(
            val note: Note
        ): AddOrRemoveFragmentEvent()

        object CloseFragment: AddOrRemoveFragmentEvent()
    }

    val navigateState: LiveData<NavigateState> get() = _navigateState
    val uiState: LiveData<UiState> get() = _uiState
    val notesDataQuery: LiveData<NotesDataQuery> get() = _notesDataQuery
    val addOrRemoveFragmentEvent: LiveData<AddOrRemoveFragmentEvent?> get() = _addOrRemoveFragmentEvent

    private var _navigateState = MutableLiveData<NavigateState>()
    private var _uiState = MutableLiveData<UiState>()
    private var _notesDataQuery = MutableLiveData<NotesDataQuery>()
    private var _addOrRemoveFragmentEvent = MutableLiveData<AddOrRemoveFragmentEvent?>()

    private var notesData = arrayListOf<Note>()

    fun init() {
        _uiState.value = UiState.Loading
        if (notesData.isEmpty()) {
            viewModelScope.launch {
                val data = withContext(Dispatchers.IO) { repository.getNotesData() }
                _uiState.value = UiState.LoadingFinish
                if (data.isNotEmpty()) {
                    data.let {
                        notesData.clear()
                        notesData.addAll(it)
                        _notesDataQuery.value = NotesDataQuery.AddAllData(it)
                    }
                } else {
                    _uiState.postValue(UiState.ErrorState)
                }
            }

        } else {
            _uiState.postValue(UiState.LoadingFinish)
            _notesDataQuery.value = NotesDataQuery.AddAllData(notesData)
        }
    }

    fun backPressed() {
        _navigateState.value = NavigateState.NavigateBack
    }

    fun itemClicked(index: Int) {
        if (index < notesData.size) {
            _navigateState.value =
                NavigateState.NavigateToProfile(notesData[index].title.toString())
        }
    }

    fun filter(text: String): MutableList<Note> {
        val filteredList = mutableListOf<Note>()

        if (text.isBlank())
            return notesData

        for (item in notesData) {
            if (item.title?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true ||
                item.description?.lowercase(Locale.getDefault())
                    ?.contains(text.lowercase(Locale.getDefault())) == true
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    fun deleteAllData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteAllNotes()
                notesData.clear()
            }
            _notesDataQuery.value = NotesDataQuery.DeleteAllData
            _uiState.value = UiState.ErrorState
        }
    }

    fun deleteData(index: Int, note: Note) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteNote(note)
                notesData.remove(note)
            }

            if (notesData.isEmpty()) {
                _uiState.value = UiState.ErrorState
                _notesDataQuery.value = NotesDataQuery.DeleteAllData
            } else {
                _notesDataQuery.value = NotesDataQuery.DeleteDataAtPosition(note, index)
            }
        }
    }

    private var note: Note = Note()
    private var addUpdateCalledFor = 0
    private var updateNoteAtIndex = 0

    fun initAddUpdateFragment(purpose: Int, index: Int = 0, noteData: Note? = null) {
        noteData?.let {
            note = it
        }
        Log.d("Testing", "initAddUpdateFragment() called: index $index")
        updateNoteAtIndex = index
        addUpdateCalledFor = purpose

        if(purpose == FOR_UPDATING_DATA)
            _addOrRemoveFragmentEvent.value = AddOrRemoveFragmentEvent.Init(note)
        else
            _addOrRemoveFragmentEvent.value = null

    }

    fun saveData(title: String, description: String) {
        Log.d("Testing", "saveData() called")
        val noteData = Note(title = title, description = description)
        viewModelScope.launch {

            if (addUpdateCalledFor == FOR_ADDING_DATA) {
                val data = withContext(Dispatchers.IO) { repository.insertData(noteData) }
                Log.d("Testing", "returned from insertData() called : $data")
                notesData.add(0,data)
                _notesDataQuery.value = NotesDataQuery.AddData(data)
                closeFragment()

            } else {
                noteData.noteId = note.noteId
                val data = withContext(Dispatchers.IO){ repository.updateNote(noteData) }
                notesData = ArrayList(notesData.filter {
                    it.noteId != noteData.noteId
                })
                notesData.add(0,data)
                _notesDataQuery.value = NotesDataQuery.UpdateDataAtPosition(data, updateNoteAtIndex)
                closeFragment()
            }
        }
    }

    fun closeFragment(){
        _addOrRemoveFragmentEvent.value = AddOrRemoveFragmentEvent.CloseFragment
    }

    fun setNullEvent(){
        _addOrRemoveFragmentEvent.value = null
    }

}