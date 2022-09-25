package com.example.tp1appweb_dokhoa.mettreAJour.liste

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1appweb_dokhoa.R
import com.example.tp1appweb_dokhoa.mettreAJour.detail.PersonneDetailActivity
import com.google.firebase.database.*

class ListePersonnesActivity : AppCompatActivity() {

    private lateinit var actionBar: ActionBar

    private lateinit var personneRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var personnneList: ArrayList<PersonneModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listepersonne)

        actionBar = supportActionBar!!
        actionBar.title = "Liste des personnes"

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        personneRecyclerView = findViewById(R.id.rvPersonne)
        personneRecyclerView.layoutManager = LinearLayoutManager(this)
        personneRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        personnneList = arrayListOf<PersonneModel>()

        getPersonnesData()

    }

    private fun getPersonnesData() {

        personneRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Personnes")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                personnneList.clear()
                if (snapshot.exists()){
                    for (personneSnap in snapshot.children){
                        val personneData = personneSnap.getValue(PersonneModel::class.java)
                        personnneList.add(personneData!!)
                    }
                    val mAdapter = PersonneAdapter(personnneList)
                    personneRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : PersonneAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@ListePersonnesActivity, PersonneDetailActivity::class.java)

                            //put extras
                            intent.putExtra("personneId", personnneList[position].id)
                            intent.putExtra("personneNom", personnneList[position].nom)
                            intent.putExtra("personneEmail", personnneList[position].email)
                            intent.putExtra("personneTelephone", personnneList[position].telephone)
                            startActivity(intent)
                        }

                    })

                    personneRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
