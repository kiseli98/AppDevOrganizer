package md.utm.organizer.internal

import kotlinx.coroutines.*

// to provide coroutine scope to lazy init
fun <T> lazyDeffered(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>>{
    return lazy {
        // will be launched only upon call
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}