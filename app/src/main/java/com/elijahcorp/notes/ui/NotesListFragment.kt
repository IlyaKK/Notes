package com.elijahcorp.notes.ui

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import com.elijahcorp.notes.R
import com.elijahcorp.notes.domain.Note
import com.elijahcorp.notes.domain.NotesRepo
import com.elijahcorp.notes.impl.NotesRepoImpl

class NotesListFragment : Fragment() {
    private lateinit var notesRecycleView: RecyclerView
    private val notesAdapter = NotesAdapter()
    private val notesRepo: NotesRepo = NotesRepoImpl()
    private lateinit var swipeDeleteCallback: ItemTouchHelper.SimpleCallback
    private var controller: Controller? = null
    private var topAppBarListener: TopAppBarListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Controller) {
            controller = context
            topAppBarListener = context as TopAppBarListener
        } else {
            throw IllegalStateException("Activity doesn't have impl NotesListFragment.Controller or NotesListFragment.TopAppBarListener interface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialiseViews(view)
        initialiseTopAppBar()
        fillDataFromFile()
        initialiseDeleteNoteSwipe()
        initialiseNotesRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.notes_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_note_menu) {
            initialiseAddNoteToNotesList()
            return true
        }
        return false
    }

    private fun initialiseAddNoteToNotesList() {
        val note = Note("", "")
        controller!!.displayNoteEdit(note)
    }

    private fun initialiseViews(view: View) {
        notesRecycleView = view.findViewById(R.id.notes_recycle_view)
    }

    fun initialiseTopAppBar() {
        topAppBarListener!!.changeTopAppBar(NOTES_LIST_FRAGMENT)
    }

    private fun fillDataFromFile() {
        notesRepo.readNotes(requireContext())
    }

    private fun initialiseDeleteNoteSwipe() {
        swipeDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected((viewHolder as NoteVh).foregroundNoteCardView)
                }
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                         viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                         actionState: Int, isCurrentlyActive: Boolean) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, (viewHolder as NoteVh).foregroundNoteCardView, dX, dY,
                        actionState, isCurrentlyActive)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                getDefaultUIUtil().clearView((viewHolder as NoteVh).foregroundNoteCardView)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                     actionState: Int, isCurrentlyActive: Boolean) {
                getDefaultUIUtil().onDraw(c, recyclerView, (viewHolder as NoteVh).foregroundNoteCardView, dX, dY,
                        actionState, isCurrentlyActive)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                notesRepo.deleteNote(requireContext(), viewHolder.adapterPosition)
                notesAdapter.setData(notesRepo.notes)
            }
        }
    }

    private fun initialiseNotesRecycleView() {
        notesRecycleView.layoutManager = LinearLayoutManager(requireContext())
        notesRecycleView.itemAnimator = DefaultItemAnimator()
        notesRecycleView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        notesRecycleView.adapter = notesAdapter
        ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(notesRecycleView)
        notesAdapter.setData(notesRepo.notes)
        notesAdapter.setOnCardClickListener { note: Note -> onCardClickListener(note) }
    }

    private fun onCardClickListener(note: Note) {
        controller!!.displayNoteEdit(note)
    }

    fun setNoteChange(note: Note) {
        if (note.timeCreate.isEmpty()) {
            notesRepo.createNote(requireContext(), note)
        } else {
            notesRepo.updateNote(requireContext(), note)
        }
        notesAdapter.setData(notesRepo.notes)
    }

    internal interface Controller {
        fun displayNoteEdit(note: Note?)
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
        const val NOTES_LIST_FRAGMENT = "NOTES_LIST_FRAGMENT"
    }
}