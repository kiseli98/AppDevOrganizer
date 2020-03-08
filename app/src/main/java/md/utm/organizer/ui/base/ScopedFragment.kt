package md.utm.organizer.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// to used local scope for fragments instead of global one
// reason:
// if Global finishes after fragment is destroyed -> crash
abstract class ScopedFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job

    // need because ScopedFragment is CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main // job will be running on the main thread (Everything from UI is in MAIN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        // kill jobs if fragment is destroyed
        job.cancel()
    }

}