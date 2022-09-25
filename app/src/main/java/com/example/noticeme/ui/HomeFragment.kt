package com.example.noticeme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noticeme.R
import com.example.noticeme.adapter.NoteItemAdapter
import com.example.noticeme.databinding.FragmentHomeBinding
import com.example.noticeme.dummy.QueryMenu
import com.example.noticeme.model.Note
import com.example.noticeme.model.NoteViewModel
import com.example.noticeme.model.UserViewModel
import com.example.noticeme.sharedpref.SharedPref

class HomeFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var noteItemAdapter: NoteItemAdapter
    private lateinit var noteVM: NoteViewModel
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)
        noteVM = ViewModelProvider(this)[NoteViewModel::class.java]
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        setView()

        binding.fabAddNote.setOnClickListener {
            showBottomSheet(menu = QueryMenu.ADD, null)
        }

        // setting toolbar menu item to clickable
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setHasOptionsMenu(true)
    }

    fun showBottomSheet(menu: QueryMenu, note: Note?){
        val addNoteBottomSheetFragment = BottomSheetNoteFragment(menu, note)
        addNoteBottomSheetFragment.show(requireActivity().supportFragmentManager.beginTransaction(), addNoteBottomSheetFragment.tag)
    }

    private fun setView(){
        val username = arguments?.getString(SharedPref.username)
        val userId = sharedPref.getInt(SharedPref.userId, -1)
        if(userId != -1){ // check the userId that shared pref store is it null or not
            binding.tvUsername.text = "$username,"
            noteItemAdapter = NoteItemAdapter()
            noteVM.getAllNotes(userId).observe(viewLifecycleOwner) { notes ->
                noteItemAdapter.setNoteList(notes as List<Note>)
            }
        }else Toast.makeText(context, "Terjadi kesalahan dalam sistem", Toast.LENGTH_SHORT).show()
        noteItemAdapter.setOnItemClickListener(object : NoteItemAdapter.OnItemClickListener{
            override fun onItemClick(menu: QueryMenu, note: Note) {
                when(menu){
                    QueryMenu.EDIT-> showBottomSheet(QueryMenu.EDIT, note)
                    QueryMenu.DELETE-> deleteItem(note)
                    else -> {}
                }
            }
        })
        val layoutManager = object: LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvNoteList.layoutManager = layoutManager
        binding.rvNoteList.adapter = noteItemAdapter
    }

    // Delete Success
    private fun deleteItem(note: Note) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            noteVM.deleteNote(note)
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${note.title}",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${note.title}?")
        builder.setMessage("Are you sure you want to delete ${note.title}?")
        builder.create().show()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d("Home Fragment", "onCreateOptionsMenu: search")
        inflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.app_bar_search) as MenuItem
        val searchView = search.actionView as SearchView
        Log.d("Home Fragment", "onCreateOptionsMenu: $searchView")
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Submit", Toast.LENGTH_SHORT).show()
                Log.d("Home Fragment", "onQueryTextSubmit: Started")
                if(query != null){
                    searchInDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Toast.makeText(context, "Text Change", Toast.LENGTH_SHORT).show()
                Log.d("Home Fragment", "onQueryTextChange: Started")
                if(query != null){
                    searchInDatabase(query)
                }
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        Log.d("Menu Item", "onMenuItemSelected: $menuItem selected")
        return when(menuItem.itemId) {
            R.id.logout -> {
                Log.d("Menu Item", "onMenuItemSelected: Logout Clicked")
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->
                    run {
                        // when "YES" option was clicked shared pref will clear the all data that was store
                        val exit = sharedPref.edit()
                        exit.clear()
                        exit.apply()
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_homeFragment_to_loginFragment)
                    }
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.setTitle("Logout your account?")
                builder.setMessage("Are you sure to logout your account?")
                builder.create().show()
                true
            }
            R.id.deleteAll -> {
                Log.d("Home Fragment", "onMenuItemSelected: DeleteAll")
                val userId = sharedPref.getInt(SharedPref.userId, -1)
                if(userId != -1){
                    if((noteVM.getAllNotes(userId).value?.size ?: 0) != 0){
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setPositiveButton("Yes") { _, _ ->
                            noteVM.deleteAllNotes(userId)
                            Toast.makeText(context, "Delete all successfully!", Toast.LENGTH_SHORT).show()
                        }
                        builder.setNegativeButton("No") { _, _ -> }
                        builder.setTitle("Delete all notes?")
                        builder.setMessage("Are you sure to delete all your notes?")
                        builder.create().show()
                    }else{
                        Toast.makeText(context, "You don't have any note yet", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Terjadi kesalahan dalam sistem", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> false
        }
    }

    private fun searchInDatabase(query: String) {
        val userId = sharedPref.getInt(SharedPref.userId,-1)
        Log.d("Home Fragment", "searchInDatabase: $query")
        val searchQuery ="%$query%"
        noteVM.searchDatabase(searchQuery, userId).observe(viewLifecycleOwner) {
            noteItemAdapter.setNoteList(it as List<Note>)
        }
    }

}