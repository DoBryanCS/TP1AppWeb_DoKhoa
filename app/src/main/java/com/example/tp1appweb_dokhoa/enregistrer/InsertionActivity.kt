package com.example.tp1appweb_dokhoa.enregistrer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.tp1appweb_dokhoa.R
import com.example.tp1appweb_dokhoa.mettreAJour.liste.PersonneModel
import com.google.firebase.database.*
import java.lang.StringBuilder

class InsertionActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        actionBar = supportActionBar!!
        actionBar.title = "Enregistrement de données"

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        dbRef = FirebaseDatabase.getInstance().getReference("Personnes")

        this.findViewById<Button>(R.id.button).setOnClickListener {

            var nomR = this.findViewById<EditText>(R.id.editText)
            var emailR = this.findViewById<EditText>(R.id.editText2)
            var telephoneR = this.findViewById<EditText>(R.id.editText3)

            var nom = nomR.text.toString()
            var email = emailR.text.toString()
            var telephone = telephoneR.text.toString()

            if (nom.isEmpty()) {
                nomR.error = "Entrez un nom svp"
            }
            if (email.isEmpty()) {
                emailR.error = "Entrez un email svp"
            }
            if (telephone.isEmpty()) {
                telephoneR.error = "Entrez un telephone svp"
            } else {

                val id = dbRef.push().key!!

                val personne = PersonneModel(id, nom, email, telephone)
                dbRef.child(id).setValue(personne)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                        nomR.text.clear()
                        emailR.text.clear()
                        telephoneR.text.clear()

                    }.addOnFailureListener { err ->
                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }

        var getdata = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                var sb = StringBuilder()
                for(i in p0.children) {
                    var nom1 = i.child("nom").getValue()
                    var email1 = i.child("email").getValue()
                    var telephone1 = i.child("telephone").getValue()
                    sb.append("$nom1\n $email1\n $telephone1\n\n")
                }
                findViewById<TextView>(R.id.textView).setText(sb)
            }
        }
        dbRef.addValueEventListener(getdata)
        dbRef.addListenerForSingleValueEvent(getdata)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}