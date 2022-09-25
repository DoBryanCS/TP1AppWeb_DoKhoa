package com.example.tp1appweb_dokhoa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.tp1appweb_dokhoa.authentification.LoginActivity
import com.example.tp1appweb_dokhoa.databinding.ActivityMainBinding
import com.example.tp1appweb_dokhoa.enregistrer.InsertionActivity
import com.example.tp1appweb_dokhoa.mettreAJour.liste.ListePersonnesActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var actionBar: ActionBar

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Accueil"

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        var btnEnregistrerDonnees = binding.btnEnregistrerDonnees
        var btnMettreAJourDonnees = binding.btnMettreAJourDonnees

        btnEnregistrerDonnees.setOnClickListener {
            val intent = Intent(this, InsertionActivity::class.java)
            startActivity(intent)
        }

        btnMettreAJourDonnees.setOnClickListener {
            val intent = Intent(this, ListePersonnesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email
            binding.emailTv.text = email
        }
        else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}