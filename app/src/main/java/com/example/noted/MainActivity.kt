package com.example.noted

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noted.core.domain.model.Category
import com.example.noted.core.domain.model.Note
import com.example.noted.core.ui.CategoryAdapter
import com.example.noted.core.ui.SearchNoteAdapter
import com.example.noted.databinding.ActivityMainBinding
import com.example.noted.ui.note.NoteFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchNoteAdapter
    private lateinit var binding: ActivityMainBinding
    private var isActivityFirstCreated = false
    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeStatusBarColor()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.layoutManager = layoutManager
        binding.rvCategory.setHasFixedSize(true)

        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.amRvNote.layoutManager = gridLayoutManager
        binding.amRvNote.setHasFixedSize(true)

        val fragmentManager = supportFragmentManager

        mainViewModel.category.observe(this) { categories ->
            categories?.let {
                setData(it, fragmentManager)
            }
        }

        mainViewModel.checkNotesWithNoCategory().observe(this) {
            val newCategory = Category(categoryName = "Uncategorized")
            if (it) {
                lifecycleScope.launch {
                    mainViewModel.insertCategory(newCategory)
                    mainViewModel.changeNotesCategory("All", newCategory.categoryName)
                }
            } else {
                lifecycleScope.launch {
                    mainViewModel.deleteCategoryAndNotes(newCategory.categoryName)
                }
            }
        }

        hideActionBar()
        addChipPressed(fragmentManager)
        showFragment("All", fragmentManager)

        binding.amSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    binding.amRvNote.visibility = View.VISIBLE
                    binding.rvCategory.visibility = View.INVISIBLE
                    binding.amFrameLayout.visibility = View.INVISIBLE
                    binding.amAddChip.visibility = View.INVISIBLE

                    mainViewModel.searchNotes(newText)
                    mainViewModel.notes.observe(this@MainActivity) {
                        it?.let {
                            setDataNote(it)
                        }
                    }
                } else {
                    binding.amRvNote.visibility = View.INVISIBLE
                    binding.rvCategory.visibility = View.VISIBLE
                    binding.amFrameLayout.visibility = View.VISIBLE
                    binding.amAddChip.visibility = View.VISIBLE
                }
                return true
            }
        })
    }

    private fun changeStatusBarColor() {
        window.statusBarColor = Color.WHITE
    }

    private fun setData(
        categories: List<Category>,
        fragmentManager: FragmentManager
    ) {
        val adapter = CategoryAdapter(categories)
        adapter.setOnItemClickCallback(object : CategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Category) {
                showFragment(data.categoryName, fragmentManager)
                categoryName = data.categoryName
            }
        })
        adapter.setOnItemLongClickCallback(object : CategoryAdapter.OnItemLongClickCallback {
            override fun onItemLongClicked(data: Category) {
                if (data.categoryName != "Uncategorized") {
                    showDialogRemove(data.categoryName, fragmentManager)
                }
            }
        })
        binding.rvCategory.adapter = adapter

        isActivityFirstCreated = true
    }

    private fun showDialogRemove(categoryName: String, fragmentManager: FragmentManager) {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this)
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

        title.text = "Delete category"
        val boldText = "<b>$categoryName</b>"
        content.text = HtmlCompat.fromHtml(
            "Are you sure you want to delete $boldText?",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        buttonCancel.setOnClickListener {
            myDialog.dismiss()
        }

        buttonSubmit.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.deleteCategoryAndNotes(categoryName)
            }
            showFragment("All", fragmentManager)

            myDialog.dismiss()
        }
    }

    private fun setDataNote(notes: List<Note>) {
        searchAdapter = SearchNoteAdapter(notes)
        searchAdapter.setOnItemLongClickCallback(object :
            SearchNoteAdapter.OnItemLongClickCallback {
            override fun onItemLongClicked(data: Note) {
                showDialogRemoveNote(data)
            }

        })
        binding.amRvNote.adapter = searchAdapter
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun addChipPressed(fragmentManager: FragmentManager) {
        binding.amAddChip.setOnClickListener {
            showDialogAddChip(fragmentManager)
        }
    }

    private fun showDialogRemoveNote(data: Note) {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this)
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
                mainViewModel.deleteNote(data)
            }
            myDialog.dismiss()
        }
    }

    private fun showDialogAddChip(fragmentManager: FragmentManager) {
        val builder = AlertDialog.Builder(this)
        val customView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_sheet_add_note, null)
        builder.setView(customView)
        builder.setCancelable(true)

        val myDialog = builder.create()
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        val buttonCancel = myDialog.findViewById<Button>(R.id.btn_cancel)
        val buttonSubmit = myDialog.findViewById<Button>(R.id.btn_ok)
        val editTextNewFolder = myDialog.findViewById<EditText>(R.id.et_new_folder)

        editTextNewFolder.requestFocus()

        buttonCancel.setOnClickListener {
            myDialog.dismiss()
        }

        buttonSubmit.setOnClickListener {
            val categoryName = editTextNewFolder.text.toString()

            if (categoryName.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please fill the empty blank first",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                var bool = true
                lifecycleScope.launch {
                    mainViewModel.category.observe(this@MainActivity) {
                        for (category in it) {
                            if (category.categoryName.lowercase() == categoryName.lowercase()) {
                                bool = false
                            }
                        }
                    }
                }
                if (!bool) {
                    Toast.makeText(
                        this@MainActivity,
                        "A folder with the same name already exists",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    lifecycleScope.launch {
                        mainViewModel.insertCategory(
                            Category(
                                categoryName
                            )
                        )
                        showFragment("All", fragmentManager)
                    }
                }
            }
            myDialog.dismiss()
        }
    }

    companion object {
        fun showFragment(categoryName: String, fragmentManager: FragmentManager) {
            val fragmentTransaction = fragmentManager.beginTransaction()

            val noteFragment = NoteFragment.newInstance(categoryName)

            fragmentTransaction.replace(R.id.all_fragment, noteFragment)
            fragmentTransaction.commit()
        }
    }
}