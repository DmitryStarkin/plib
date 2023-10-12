**This repository is deprecated**

Use [android async lib](https://github.com/DmitryStarkin/android_async_lib)

**A small Kotlin library for asynchronous task execution on Android**

This library is written for educational purposes in the Kotlin language,  
but it is fully functional although quite minimalistic


Commands are represented as implementations of the interface "Processor"  which  
accepts input data and outputs result, processors are started for execution  
using the implementation of the "Executor" interface

Currently, the library has a single implementations  of  interface "Executor",  
it performs all the tasks  sequentially, it is also possible to pass previous  
task results as input  data for the next task

The library also has extension functions for easy execution of tasks in thread,
and execute lambda as processor, and send lambda for update UI (like runOnUiThread)

Usage:

1 in project level build.gradle add:
```
repositories {
........
        maven { url "https://jitpack.io" }
   }
```

2 in module level build.gradle add:
```
dependencies {
...........
         implementation 'com.github.DmitryStarkin:plib:1.4.2b'
   }
```
3 Create an implementation of the  interface "Processor" for data processing,  
pass this object to the executor along with input data and callbacks to pass the  
result or error

Currently, you can only pass a task from the main thread. The processor will run  
asynchronously, and callbacks will be called in the main thread

Code example
```kotlin

class StringToFileWriter(private val file: File): Processor<String, File>{

    override fun processing(dataForProcessing: String): File{
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

Extension functions example

```kotlin
class SomeClass: Activity(){

    val textView: TextView = //init view
    var thread: Thread? = null
    
    fun someFun (){
        val inputStream: InputStream = File("example.txt").inputStream()
        thread = inputStream.runOnThread({text -> textView.text = text},{e -> textView.text = e.toString()})
        { bufferedReader().use { it.readText() }}
    
    }
    
    fun someFun1 (){
     
        val file = File("example.txt")
        thread = file.processingOnThread({text -> textView.text = text},{e -> textView.text = e.toString()})
        { it.inputStream().bufferedReader().use { it.readText() }}
    }
    
    fun someFun2 (){
     
        val file = File("example.txt")
        thread = handleOnThread(file, {text -> textView.text = text},{e -> textView.text = e.toString()})
        { it.inputStream().bufferedReader().use { reader -> reader.readText() }}
    }
    
    private val processorsExecutor = SequentiallyProcessorExecutor()
    
    fun someFun3 (){
    
        val file = File("example.txt")
        processorsExecutor.executeAsProcessor({text -> textView.text = text},{e -> textView.text = e.toString()})
        {file.inputStream().bufferedReader().use { it.readText() }}
    }
    
    fun someFun4 (){

        val file = File("example.txt")
        processorsExecutor.executeAsProcessor(DELIVER_.TO_NEXT)
        {file.inputStream().bufferedReader().use { it.readText() }}
            .executeAsProcessorWithData(null, {text -> textView.text = text},{ e -> textView.text = e.toString()})
            { it?.replace("\r\n", "") }
    }
    
    fun someFun5 (){

        val file = File("example.txt")
        file.runOnExecutor(DELIVER_.TO_NEXT)
        {inputStream().bufferedReader().use { it.readText() }}
            .handleData(null, {text -> textView.text = text},{ e -> textView.text = e.toString()})
            { it?.replace("\r\n", "") };
            
            {textView.text  = "4"}.runInUI()
    }
    
    override fun onDestroy() {

        thread?.interrupt()
        }
 } 

```


See [Docs](https://dmitrystarkin.github.io/plib/)
