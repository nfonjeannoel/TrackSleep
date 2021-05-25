package com.example.tracksleep.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.tracksleep.R
import com.example.tracksleep.SleepAdapter
import com.example.tracksleep.database.SleepDatabase
import com.example.tracksleep.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar


class SleepTrackerFragment : Fragment() {

    private lateinit var binding: FragmentSleepTrackerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_tracker, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        val sleepTrackerViewModel = ViewModelProvider(this,  viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.lifecycleOwner = this
        binding.sleepTackerViewModel = sleepTrackerViewModel

        val adapter = SleepAdapter()
        binding.sleepList.adapter = adapter

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(this, Observer{
            if (it == true){
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT
                ).show()
                sleepTrackerViewModel.doneShowingSnackBar()
            }
        })

        sleepTrackerViewModel.navigateToSleepQuality.observe(this, Observer { night ->
            night?.let {
                this.findNavController()
                        .navigate(SleepTrackerFragmentDirections
                                .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                sleepTrackerViewModel.doneNavigating()
            }
        })
        return  binding.root
    }

}