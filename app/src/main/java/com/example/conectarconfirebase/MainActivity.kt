package com.example.conectarconfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.conectarconfirebase.databinding.ActivityMainBinding
import com.example.conectarconfirebase.model.Gender
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var database: FirebaseDatabase
    lateinit var generosRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database

        generosRef = database.getReference("generos")
 delete()
        read()
    }

    fun create() {
        val nuevoId = generosRef.push().key
        val nuevoGenero = Gender("1", "Masculino")
        generosRef.child(nuevoId!!).setValue(nuevoGenero)
    }

    fun read() {
        generosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val genero = snapshot.getValue(Gender::class.java)
                    Toast.makeText(this@MainActivity, genero!!.name, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Maneja errores aquí.
            }
        })
    }

    fun update() {
        val generoId =
            "-NheKgWEaahaeJWPNC4V" // Reemplaza con el ID del género que deseas actualizar

        val nuevosDatos = HashMap<String, Any>()
        nuevosDatos["name"] = "Femenino"

        generosRef.child(generoId).updateChildren(nuevosDatos)
    }

    fun delete (){

        val generoId = "-NheKsLDV9YlY530ZYg2" // Reemplaza con el ID del género que deseas eliminar

        generosRef.child(generoId).removeValue()
    }
    fun getID(){
        val generoId = "-NheKgWEaahaeJWPNC4V" // Reemplaza con el ID del género que deseas buscar

        generosRef.orderByChild("id").equalTo(generoId).limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Obtiene el primer género que coincida con el ID
                        val genero = dataSnapshot.children.first().getValue(Gender::class.java)
                        // Hacer algo con el objeto "genero"
                    } else {
                        // El género con el ID no se encontró
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar errores aquí
                }
            })
    }
}