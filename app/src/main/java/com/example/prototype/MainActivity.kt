package com.example.prototype

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var adapter: ProductAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val productsList: Array<String> = arrayOf("Aashirvaad Superior MP atta", "Aashirvaad Atta with Multigrains", "Aashirvaad Select Sharbati atta", "Pillsbury Chakki Fresh atta", "Annapurna Farm Fresh atta", "Amul Cheese", "Brittania Cheese Block", "Milky Mist Cheese Slices")
    private val test = ProductModel("Aashirvaad Superior MP atta","367","10.9","77","5.4","-","10.5","1.7","-","-","-","-","-","-","-","-","-","-","-")
    private val products: ArrayList<ProductModel> = arrayListOf(test)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = ProductAdapter(products)
        recyclerView.adapter = adapter
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
        /**
         * Open JSON file
         * Check for the product match
         * Add the details in the card
         * Add the card to the Recycler View
         * */

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