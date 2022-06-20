package com.example.bozorbek_vol2.repository

import android.util.Log
import com.example.bozorbek_vol2.util.Constants
import kotlinx.coroutines.Job

open class JobManager constructor(
    private val className:String
) {

    val TAG = Constants.LOG
    private val jobs:HashMap<String, Job> = HashMap()

    fun addJob(methodName:String, job: Job)
    {
        cancelJob(methodName)

        jobs[methodName] = job
    }

    private fun cancelJob(methodName: String) {
        getJob(methodName)?.cancel()
        Log.d(TAG, "cancel job: ${methodName}")
    }

    private fun getJob(methodName: String): Job? {
        if (jobs.containsKey(methodName))
        {
            jobs[methodName]?.let {
                return it
            }
        }
        return null
    }

    fun cancelActiveJob()
    {
        for ((methodName, job) in jobs)
        {
            if (job.isActive)
            {
                Log.d(TAG, "${className}: cancelling job in method:${methodName}")
                job.cancel()
            }
        }
    }

}