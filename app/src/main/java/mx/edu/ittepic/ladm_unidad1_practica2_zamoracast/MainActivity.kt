package mx.edu.ittepic.ladm_unidad1_practica2_zamoracast

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAbrir.setOnClickListener {
            if (txtArchivo.text.toString().isEmpty()) { //Inicio Verificar el nombre archivo IF
                Toast.makeText(this, "Ingrese el nombre del archivo", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {//Verificar el nombre archivo ELSE
                if (rInterno.isChecked == true || rExterno.isChecked == true) { //Inicio Verificar opcion de Almacenamiento
                    if (rInterno.isChecked == true) {
                        leerArchivoInterno()
                    }
                    if (rExterno.isChecked == true) {
                        if (ContextCompat.checkSelfPermission(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                        { ActivityCompat.requestPermissions(
                                this,
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ),
                                0
                            )
                        } else {

                            leerArchivoSD()
                        }
                    }
                } else {//Fin Verificar opcion de Almacenamiento
                    Toast.makeText(
                        this,
                        "Seleccione una opcion de almacenamiento",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } ////FIN Verificar el nombre archivo ELSE

        }

        btnGuardar.setOnClickListener {
            if (rInterno.isChecked == true || rExterno.isChecked == true) {
                if (rInterno.isChecked == true) {
                    guardarArchivoInterno()
                }
                if (rExterno.isChecked == true) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) ==
                        PackageManager.PERMISSION_DENIED

                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            0
                        )
                    } else {
                        if (txtArchivo.text.toString().isEmpty()) {
                            Toast.makeText(
                                this,
                                "Ingrese el nombre del archivo en el formato correcto",
                                Toast.LENGTH_LONG
                            ).show()
                            return@setOnClickListener
                        } else {
                            guardarArchivoSD()
                        }
                    }

                }
            } else {
                Toast.makeText(this, "Seleccione una opcion de almacenamiento", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
    fun leerArchivoSD() {
        if (noSD()) {
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtArchivo.text.toString())
            var flujoEnntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEnntrada.readLine()
            var vector = data.split("&")
            asignarText(vector[0])
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }
    fun guardarArchivoSD() {
        if (noSD()) {
            mensaje("No hay memoria")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtArchivo.text.toString())
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = txtFrase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }// todo :------> aguardarArchivo en la SD

    fun noSD(): Boolean {
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED) {
            return true
        } else {
            return false
        }
    }

    private fun guardarArchivoInterno() {
        try {
            var flujoSalida =
                OutputStreamWriter(openFileOutput(txtArchivo.text.toString(), Context.MODE_PRIVATE))
            var data = txtFrase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoInterno() {
        try {
            var flujoEnntrada =
                BufferedReader(InputStreamReader(openFileInput(txtArchivo.text.toString())))
            var data = flujoEnntrada.readLine()
            var vector = data.split("&")
            asignarText(vector[0])
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }
    fun mensaje(m: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(m)
            .setPositiveButton("ACEPTAR") { d, i ->
            }.show()
    }
    fun asignarText(t1: String) {
        txtFrase.setText(t1)
    }
}
