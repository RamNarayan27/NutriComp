package com.example.prototype

import android.Manifest
import android.app.Instrumentation
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStream
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var adapter: ProductAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val productsList: Array<String> = arrayOf(
        "Aashirvaad Superior MP atta",
        "Aashirvaad Atta with Multigrains",
        "Aashirvaad Select Sharbati atta",
        "Pillsbury Chakki Fresh atta",
        "Annapurna Farm Fresh atta",
        "Amul Cheese",
        "Brittania Cheese Block",
        "Milky Mist Cheese Slices"
    )
    private var products = mutableListOf<ProductModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = ProductAdapter(products)
        recyclerView.adapter = adapter
        Log.i("test", products.toString())
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),REQUEST_IMAGE_CAPTURE);
        }
        val fab: View = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val image = InputImage.fromBitmap(imageBitmap, 0)
            val recognizer = TextRecognition.getClient()
            val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val resultText = visionText.text
                        val product = strMatch(productsList, resultText)
                        addCard(product)
                        Toast.makeText(this, product, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                        Toast.makeText(this, "Image not Processed", Toast.LENGTH_SHORT).show()
                    }
        }
    }

    private fun addCard(product: String) {
        Log.i("test", "entered addCard")
        val obj = JSONObject(readJSONFromAsset())
        val productsArray = obj.getJSONArray("Products")
        for(i in 0 until productsArray.length()){
            val prod: JSONObject = productsArray.getJSONObject(i)
            val productName: String = prod.getString("Product Name")
            val energy: String = prod.getString("Energy (kcal)")
            val protein: String = prod.getString("Protein")
            val carbohydrate: String = prod.getString("Carbohydrate")
            val owSugar: String = prod.getString("of which sugar")
            Log.i("val", owSugar)
            val addedSugar: String = prod.getString("Added Sugar")
            val dietaryFibre: String = prod.getString("Dietary Fibre")
            val fats: String = prod.getString("Fats")
            val sfa: String = prod.getString("Saturated Fatty Acids")
            val mfa: String = prod.getString("Monounsaturated Fatty Acids")
            val pfa: String = prod.getString("Polyunsaturated Fatty Acids")
            val tfa: String = prod.getString("Trans Fatty Acids")
            val cholesterol: String = prod.getString("Cholesterol")
            val iron: String = prod.getString("Iron (mg)")
            val vitb1: String = prod.getString("Vitamin  B1 (mg)")
            val sodium: String = prod.getString("Sodium (mg)")
            val calcium: String = prod.getString("Calcium (mg)")
            val phosphorus: String = prod.getString("Phosphorus (mg)")
            val vita: String = prod.getString("Vitamin - A (mcg)")
            if(product == productName){
                Log.i("test", "Adding product to arraylist")
                products.add(
                    ProductModel(
                        productName,
                        energy,
                        protein,
                        carbohydrate,
                        owSugar,
                        addedSugar,
                        dietaryFibre,
                        fats,
                        sfa,
                        mfa,
                        pfa,
                        tfa,
                        cholesterol,
                        iron,
                        vitb1,
                        sodium,
                        calcium,
                        phosphorus,
                        vita
                    )
                )
                recyclerView.adapter?.notifyItemChanged(products.size - 1)
                Log.i("helloooo", products.size.toString())
                Log.i("test", "Added product to arraylist")
                break
            }
        }
    }

    private fun readJSONFromAsset() : String {
        var json: String? = null
        try{
            val inputStream: InputStream = assets.open("products.json")
            json = inputStream.bufferedReader().use{it.readText()}
        } catch (ex: Exception){
            Log.e("JSON", "Read Error")
            return "NULL"
        }
        return json
    }

    private fun strMatch(products_list: Array<String>, ocrtext: String): String {
        var score = 0
        var pname = "Product not available"
        for (product in products_list) {
            val a = product.toLowerCase().split(" ")
            val b = ocrtext.toLowerCase().split(" ")
            val result = a.intersect(b).map { x -> min(a.count { it == x }, b.count { it == x }) }.sum()
            if (score < result) {
                score = result
                pname = product
            }
        }
        return pname
    }
}