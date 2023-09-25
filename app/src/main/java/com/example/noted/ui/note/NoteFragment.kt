package com.example.noted.ui.note

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noted.MainActivity.Companion.showFragment
import com.example.noted.R
import com.example.noted.core.domain.model.Note
import com.example.noted.core.ui.NoteAdapter
import com.example.noted.databinding.FragmentNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment : Fragment() {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var adapter: NoteAdapter
    private val noteViewModel: NoteViewModel by viewModels()
    private var categoryName = ""
    private var isFragmentFirstCreated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryName = arguments?.getString("categoryName").toString()

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvNote.layoutManager = layoutManager
        binding.rvNote.setHasFixedSize(true)

        categoryName?.let {
            if (categoryName == "All") {
                noteViewModel.notes.observe(viewLifecycleOwner) {
                    it?.let { notes ->
                        setData(notes)
                    }
                }
            } else {
                noteViewModel.notesByCategory(categoryName)
                noteViewModel.notesByCategory.observe(viewLifecycleOwner) {
                    it?.let { notes ->
                        setData(notes)
                    }
                }
            }
        }

        binding.noteBtnAddNote.setOnClickListener {
            val action = NoteFragmentDirections.actionNoteFragmentToDetailNoteActivity(
                categoryName = categoryName
            )
            view.findNavController().navigate(action)
        }
    }

    private fun setData(notes: List<Note>) {
        adapter = NoteAdapter(notes)
        adapter.setOnItemLongClickCallback(object : NoteAdapter.OnItemLongClickCallback {
            override fun onItemLongClicked(data: Note) {
                showDialogRemove(data)
            }
        })
        binding.rvNote.adapter = adapter

        isFragmentFirstCreated = true
    }

    private fun showDialogRemove(data: Note) {
        val builder = AlertDialog.Builder(requireContext())
        val customView = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_remove, null)
        builder.setView(customView)
        builder.setCancelable(true)

        val myDialog = builder.create()
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val title = myDialog.findViewById<TextView>(R.id.tv_title_delete)
        val content = myDialog.findViewById<TextView>(R.id.tv_content_delete)
        val buttonCancel = myDialog.findViewById<Button>(R.id.btn_cancel)
        val buttonSubmit = myDialog.findViewById<Button>(R.id.btn_ok)

        title.text = "Delete note"
        val boldText = "<b>${data.noteTitle}</b>"
        content.text = HtmlCompat.fromHtml(
            "Are you sure you want to delete $boldText?",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        buttonCancel.setOnClickListener {
            myDialog.dismiss()
        }

        buttonSubmit.setOnClickListener {
            lifecycleScope.launch {
                noteViewModel.deleteNote(data)
            }
            if (categoryName.isEmpty()) {
                showFragment(categoryName, parentFragmentManager)
            } else {
                showFragment("All", parentFragmentManager)
            }

            myDialog.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFragmentFirstCreated) {
            showFragment(categoryName, parentFragmentManager)
        }
    }

    companion object {
        fun newInstance(categoryName: String): NoteFragment {
            val fragment = NoteFragment()
            val args = Bundle()
            args.putString("categoryName", categoryName)
            fragment.arguments = args
            return fragment
        }
    }
}