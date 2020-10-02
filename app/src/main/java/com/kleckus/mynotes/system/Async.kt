package com.kleckus.mynotes.system

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class Async(private val slowStandAloneProcessing : () -> Unit)
{
    // Used on async calls that are executed conditionally and require some default execution after them
    // The onTrue argument function must call "after()" when it finishes the execution
    // Example: Async.condition(someCondition) { a -> promise().onComplete{ a() }  }.andThen{ alwaysExecuted }
    class condition(private val condition : Boolean, private val onTrue : (after : () -> Unit) -> Unit)
    {
        private var onFalse : (after : () -> Unit) -> Unit = { after -> after() }
        private var after : () -> Unit = {}

        // Sets the function, Potentially async, that should be executed in case the condition is false
        // The onFalse function must call "after()" when finishes all its processing
        fun elseDo(onFalse : (after : () -> Unit) -> Unit) : condition
        {
            this.onFalse = onFalse
            return this
        }

        // Sets the function that will be after all no matter the result of the condition
        fun andThen(afterEverything : () -> Unit = {})
        {
            after = afterEverything

            if(condition) { onTrue(after) }
            else { onFalse(after) }
        }
    }

    // Receives the function that must be executed after the async execution
    // The execution of the function passed here is guaranteed to run on main thread
    fun andThen(mainThreadedPostProcessing : () -> Unit)
    {
        GlobalScope.launch {
            slowStandAloneProcessing()
            MainScope().launch {
                mainThreadedPostProcessing()
            }
        }
    }
}

// Forces the argument function to be executed on the main thread
fun ensureMainThread(doIt : () -> Unit) { MainScope().launch { doIt() } }

// A promise is a class makes a promise that a value will be set as result of it in an unknown future
// One may set a listener to execute a function when the promise is complete
class Promise<T>
{
    var result : T? = null
        private set

    private var listener : (result : T) -> Unit = {}

    // Completes a promise notifying the listener
    fun complete(result : T) : Promise<T>
    {
        // This exception must not be catch
        if(this.result != null)
            throw Exception("Bad usage. Complete must be called only once per promise.")

        this.result = result
        listener(result)
        return this
    }

    // Sets a listener that awaits the promise completion
    fun onComplete(listener :  (result : T) -> Unit)
    {
        this.listener = listener
        if(result != null) this.listener(result!!)
    }
}