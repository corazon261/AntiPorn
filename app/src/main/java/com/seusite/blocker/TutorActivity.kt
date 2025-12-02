package com.seusite.blocker
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TutorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor) // Crie layout com um ListView

        val listView = findViewById<ListView>(R.id.logsListView)
        val logs = ArrayList<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, logs)
        listView.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        // Escuta em tempo real mudanças na coleção "logs_seguranca"
        db.collection("logs_seguranca")
            .orderBy("data", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener

                logs.clear()
                for (doc in snapshots!!) {
                    val evento = doc.getString("evento") ?: "Evento desconhecido"
                    logs.add(evento)
                }
                adapter.notifyDataSetChanged()
            }
    }
}