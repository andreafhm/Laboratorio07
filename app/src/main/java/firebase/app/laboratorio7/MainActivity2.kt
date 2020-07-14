package firebase.app.laboratorio7

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayOutputStream


class MainActivity2 : AppCompatActivity() {

    val READ_REQUEST_CODE = 3454
    val CAMARA_PERMISSION = 654
    val CAMERA_REQUEST = 663
    val FILENAME = "prueba"
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        guardar.setOnClickListener {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(i, READ_REQUEST_CODE)
        }
        tomar.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
            ) {
                Toast.makeText(this, "Se requieren permisos para continuar", Toast.LENGTH_LONG)
                        .show()
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMARA_PERMISSION)
            } else {
                // Abrir la camara
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (data?.extras?.get("data") as Bitmap)
            Glide.with(this).load(bitmap).into(imageView)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        }
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val dc = DocumentFile.fromTreeUri(this,Uri.parse(data?.dataString))
            val nuevoArchivo = dc?.createFile("image/png",FILENAME)
            val outputStream  = contentResolver.openOutputStream(nuevoArchivo?.uri!!)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
    }
}