package com.example.tracksleep.sleepquality

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
import com.example.tracksleep.database.SleepDatabase
import com.example.tracksleep.databinding.FragmentSleepQualityBinding


class SleepQualityFragment : Fragment() {
    private lateinit var binding: FragmentSleepQualityBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_quality, container, false)

        val application  = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val args = SleepQualityFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = SleepQualityViewModelFactory(args.sleepNightKey, dataSource)
        val sleepQualityViewModel = ViewModelProvider(this, viewModelFactory).get(SleepQualityViewModel::class.java)

        binding.sleepQulaityViewModel = sleepQualityViewModel

        sleepQualityViewModel.navigateToSleepTracker.observe(this, Observer{
            if (it == true){
                this.findNavController()
                        .navigate(SleepQualityFragmentDirections
                                .actionSleepQualityFragmentToSleepTrackerFragment())

                sleepQualityViewModel.doneNavigating()
            }
        })

        return binding.root
    }

}