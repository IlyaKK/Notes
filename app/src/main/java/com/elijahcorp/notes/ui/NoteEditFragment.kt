package com.elijahcorp.notes.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.elijahcorp.notes.R
import com.elijahcorp.notes.domain.Note
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicReference
import java.util.regex.Pattern

class NoteEditFragment : Fragment() {
    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var note: Note
    private var isEmptyNote = false
    private var controller: Controller? = null
    private var topAppBarListener: TopAppBarListener? = null

    override fun onSaveInstanceState(outState: Bundle) {
        if (titleEditText.text != null && descriptionEditText.text != null) {
            note.title = titleEditText.text.toString()
            note.description = descriptionEditText.text.toString()
            if (isEmptyNote) {
                note.timeCreate = ""
            }
            outState.putParcelable(CHANGE_NOTE_KEY, note)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Controller) {
            controller = context
            topAppBarListener = context as TopAppBarListener
        } else {
            throw IllegalStateException("Activity doesn't have impl NoteEditFragment.Controller or NoteEditFragment.TopAppBarListener interface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_note_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        initialiseTopAppBar()
        noteFromNotesList()
        fillEditTexts()
        initialiseBackSpaceCallBack()
        changerOrientationScreen(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (!isEmptyNote) {
            inflater.inflate(R.menu.notes_edit_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_create_time) {
            initialiseChangeCreateTimeDataPicker()
            return true
        }
        return false
    }

    private fun initialiseChangeCreateTimeDataPicker() {
        val data = AtomicReference<String>()
        @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        try {
            val date = formatter.parse(note.timeCreate)
            if (date != null) {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.select_data_create)
                    .setSelection(date.time)
                    .build()
                val timePicker = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setTitleText(R.string.select_time_created)
                    .setHour(
                        Pattern.compile(" ").split(note.timeCreate, 2)[1].split(":")[0].toInt()
                    )
                    .setMinute(
                        Pattern.compile(" ").split(note.timeCreate, 2)[1].split(":")[1].toInt()
                    )
                    .build()
                datePicker.show(parentFragmentManager, "data picker")
                datePicker.addOnPositiveButtonClickListener { selection: Long? ->
                    @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("dd.MM.yyyy")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selection!!
                    timePicker.show(parentFragmentManager, "time picker")
                    data.set(format.format(calendar.time))
                }
                timePicker.addOnPositiveButtonClickListener {
                    @SuppressLint("DefaultLocale") val time =
                        String.format("%02d:%02d", timePicker.hour, timePicker.minute)
                    data.set(data.get().toString() + " " + time)
                    note.timeCreate = data.get()
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun initViews(view: View) {
        titleEditText = view.findViewById(R.id.title_edit_text)
        descriptionEditText = view.findViewById(R.id.description_edit_text)
    }

    private fun initialiseTopAppBar() {
        topAppBarListener!!.changeTopAppBar(NOTE_EDIT_FRAGMENT)
    }

    private fun noteFromNotesList() {
        arguments?.let {
            note = if (it.containsKey(CHANGE_NOTE_KEY)) {
                it.getParcelable(CHANGE_NOTE_KEY)!!
            } else {
                it.getParcelable(INFO_NOTE_KEY)!!
            }
        }
    }


    private fun fillEditTexts() {
        if (note.title.isEmpty() && note.description.isEmpty()) {
            isEmptyNote = true
        }
        titleEditText.setText(note.title)
        descriptionEditText.setText(note.description)
    }

    private fun initialiseBackSpaceCallBack() {
        controller!!.initialiseNavigationIconBack()
    }

    fun saveChangeNote() {
        if (titleEditText.text != null && descriptionEditText.text != null) {
            note.title = titleEditText.text.toString()
            note.description = descriptionEditText.text.toString()
            if (isEmptyNote) {
                note.timeCreate = ""
            }
            val bundle = Bundle()
            bundle.putParcelable(CHANGE_NOTE_KEY, note)
            arguments = bundle
        }
    }

    private fun changerOrientationScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val note: Note? = savedInstanceState.getParcelable(CHANGE_NOTE_KEY)
            controller!!.displayNoteEdit(note)
            controller!!.deleteOldFragment(this)
        }
    }

    internal interface Controller {
        fun initialiseNavigationIconBack()
        fun displayNoteEdit(note: Note?)
        fun deleteOldFragment(noteEditFragment: Fragment?)
    }

    internal interface TopAppBarListener {
        fun changeTopAppBar(nameFragment: String?)
    }

    override fun onDestroy() {
        controller = null
        topAppBarListener = null
        super.onDestroy()
    }

    companion object {
        const val INFO_NOTE_KEY = "info_note_key"
        const val CHANGE_NOTE_KEY = "change_note_key"
        const val NOTE_EDIT_FRAGMENT = "NOTE_EDIT_FRAGMENT"
        fun newInstance(note: Note?): NoteEditFragment {
            val noteEditFragment = NoteEditFragment()
            val bundle = Bundle()
            bundle.putParcelable(INFO_NOTE_KEY, note)
            noteEditFragment.arguments = bundle
            return noteEditFragment
        }
    }
}

