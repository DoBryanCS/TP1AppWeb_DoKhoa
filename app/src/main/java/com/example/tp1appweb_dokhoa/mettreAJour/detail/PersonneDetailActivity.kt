package com.example.tp1appweb_dokhoa.mettreAJour.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tp1appweb_dokhoa.R
import com.example.tp1appweb_dokhoa.mettreAJour.liste.ListePersonnesActivity
import com.example.tp1appweb_dokhoa.mettreAJour.liste.PersonneModel
import com.google.firebase.database.FirebaseDatabase

class PersonneDetailActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar

    private lateinit var tvPersonneId: TextView
    private lateinit var tvPersonneNom: TextView
    private lateinit var tvPersonneEmail: TextView
    private lateinit var tvPersonneTelephone: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personnedetail)

        actionBar = supportActionBar!!
        actionBar.title = "Mise a jour des données"

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("personneId").toString(),
                intent.getStringExtra("personneNom").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("personneId").toString()
            )
        }

    }

    private fun initView() {
        tvPersonneId = findViewById(R.id.tvPersonneId)
        tvPersonneNom = findViewById(R.id.tvPersonneNom)
        tvPersonneEmail = findViewById(R.id.tvPersonneEmail)
        tvPersonneTelephone = findViewById(R.id.tvPersonneTelephone)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvPersonneId.text = intent.getStringExtra("personneId")
        tvPersonneNom.text = intent.getStringExtra("personneNom")
        tvPersonneEmail.text = intent.getStringExtra("personneEmail")
        tvPersonneTelephone.text = intent.getStringExtra("personneTelephone")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Personnes").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Personne data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ListePersonnesActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        personneId: String,
        personneNom: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etPersonneNom = mDialogView.findViewById<EditText>(R.id.etPersonneNom)
        val etPersonneEmail = mDialogView.findViewById<EditText>(R.id.etPersonneEmail)
        val etPersonneTelephone = mDialogView.findViewById<EditText>(R.id.etPersonneTelephone)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etPersonneNom.setText(intent.getStringExtra("personneNom").toString())
        etPersonneEmail.setText(intent.getStringExtra("personneEmail").toString())
        etPersonneTelephone.setText(intent.getStringExtra("personneTelephone").toString())

        mDialog.setTitle("Updating $personneNom Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updatePersonneData(
                personneId,
                etPersonneNom.text.toString(),
                etPersonneEmail.text.toString(),
                etPersonneTelephone.text.toString()
            )

            Toast.makeText(applicationContext, "Personne Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvPersonneNom.text = etPersonneNom.text.toString()
            tvPersonneEmail.text = etPersonneEmail.text.toString()
            tvPersonneTelephone.text = etPersonneTelephone.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updatePersonneData(
        id: String,
        nom: String,
        email: String,
        telephone: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Personnes").child(id)
        val personneInfo = PersonneModel(id, nom, email, telephone)
        dbRef.setValue(personneInfo)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}