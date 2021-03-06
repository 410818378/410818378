package com.example.myapplication
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
class MainActivity : ComponentActivity() {
    val carurl="https://i.imgur.com/XKVG25R.gif"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }

        GlobalScope.launch {
            withContext(Dispatchers.IO){

                val url: URL = URL(carurl)
                var mycon: HttpURLConnection =url.openConnection() as HttpURLConnection
                if ( mycon != null){
                    mycon.allowUserInteraction=false
                    mycon.instanceFollowRedirects =true
                    mycon.requestMethod="GET"
                    mycon.connect()
                    if (mycon.responseCode == HTTP_OK){
                        var bmp: Bitmap = BitmapFactory.decodeStream(mycon.inputStream)
                        img= bmp.asImageBitmap()
                        imgready.value=true
                    }
                }
                // delay(5000)
            }

            withContext(Dispatchers.Main){
                Toast.makeText(applicationContext,"test",Toast.LENGTH_LONG).show()

            }

        }

    }
}
lateinit var img: ImageBitmap
var imgready = mutableStateOf<Boolean>(false)
@Composable
fun Greeting(name: String) {
    Column() {
        if ( imgready.value) {
            Image(painter = BitmapPainter(img), contentDescription = "image")
        }
    }

// Text(text = "Hello $name!")
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
