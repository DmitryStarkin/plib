**A small library that implements a pattern similar to the command pattern**

This library is written for educational purposes in the Kotlin language,  
but it is fully functional although quite minimalistic


Commands are represented as implementations of the processor interface which  
accepts input data and outputs output dataprocessors are started for execution  
by an object that implements the executor interface

Currently, the library has a single executor that performs all the tasks passed  
to it one by one, it is also possible to pass the results of the task as input  
data to the next one

Usage:

1 add a module to the project

2 in build.gradle write
```
implementation project(':module_name')
```
Create an implementation of the processor interface to encapsulate data processing  
pass this object to the executor along with input data and callbacks to pass the  
result or error

Currently, you can only pass a task from the main thread. The processor will run  
asynchronously, and callbacks will be called in the main thread

```kotlin

 class StringToFileWriter(private val file: File): Processor<String, File>{

    override fun processing(dataForProcessing: File): String{
        // Code that writes a string to a file
        }
}

class NumberToStringConverter: Processor<Number, String>{

    override fun processing(dataForProcessing: Number): String{
        // Code that convert Number to String
        }
}


class SomeClass: Activity(){
private val processorsExecutor = SequentiallyProcessorExecutor()

//Some code

    fun writeStringToFile(file: File, data: String) {

        processorsExecutor.processing(StringToFileWriter(file),
            data,
            { f -> Log.d(TAG, "file wrote " + f?.path) },
            { e -> Log.e(TAG, "file write error " + e.toString())})
        }
    
    fun writeNumberToFileAsString(file: File, data: Number) {

        processorsExecutor.processing(NumberToStringConverter(), data, DELIVER_.TO_NEXT)
        processorsExecutor.processing(StringToFileWriter(file),
            null,
            { f -> Log.d(TAG, "file wrote " + f?.path) },
            { e -> Log.e(TAG, "file write error " + e.toString())})
        }
    
    override fun onDestroy() {

        processorsExecutor.processing(ExecutorCommands.ShutdownNow())
        }
   } 
```
See [Docs](https://dmitrystarkin.github.io/processor_lib/)