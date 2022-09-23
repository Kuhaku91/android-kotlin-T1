package hariyanto.alif.com.tugas2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import android.widget.Toast
import com.google.firebase.database.*


import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var db: DatabaseReference
    lateinit var db1 : DatabaseReference
    lateinit var adapter: ListAdapter
    var alMhs = ArrayList<HashMap<String, Any>>()
    var nim = ""
    var mhs = Mahasiswa()
    var alProdi = ArrayList<HashMap<String,Any>>()
    var kodeProdi = ""
    var prodi = Prodi()
    var hm = HashMap<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        btnInsertMhs.setOnClickListener(this)
        btnDeleteMhs.setOnClickListener(this)
        btnUpdateMhs.setOnClickListener(this)
        lsDataMhs.setOnItemClickListener(itemClickListener)

        btnInsertProdi.setOnClickListener(this)
        btnDeleteProdi.setOnClickListener(this)
        btnUpdateProdi.setOnClickListener(this)
        lsDataProdi.setOnItemClickListener(itemClickListener1)
    }

    override fun onStart() {
        super.onStart()
        db = FirebaseDatabase.getInstance().getReference("TabelMahasiswa")
        showDataMhs()
        db1 = FirebaseDatabase.getInstance().getReference("TabelProdi")
        showDataProdi()
    }
    fun showDataMhs() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var dataSnapshotIterable = snapshot.children
                var iterator = dataSnapshotIterable.iterator()
                alMhs.clear()
                while (iterator.hasNext()){
                    mhs = iterator.next().getValue(Mahasiswa::class.java)!!
                    hm = HashMap()
                    hm.put("nim",mhs.nim!!)
                    hm.put("namaMhs",mhs.namaMhs!!)
                    hm.put("alamatMhs",mhs.alamatMhs!!)
                    alMhs.add(hm)
                }
                adapter = SimpleAdapter(
                    this@MainActivity,
                    alMhs,
                    R.layout.row_mahasiswa,
                    arrayOf("nim","namaMhs","alamatMhs"),
                    intArrayOf(R.id.rowNim, R.id.rowNama, R.id.rowAlamat)
                )
                lsDataMhs.setAdapter(adapter)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Connection to database error : ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDataProdi() {
        db1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataSnapshotIterable = snapshot.children
                val iterator = dataSnapshotIterable.iterator()
                alProdi.clear()
                while (iterator.hasNext()){
                    prodi = iterator.next().getValue(Prodi::class.java)!!
                    hm = HashMap()
                    hm.put("kodeProdi",prodi.kodeProdi!!)
                    hm.put("namaProdi",prodi.namaProdi!!)
                    hm.put("lokasi",prodi.lokasi!!)
                    alProdi.add(hm)
                }
                adapter = SimpleAdapter(
                    this@MainActivity,
                    alProdi,
                    R.layout.row_prodi,
                    arrayOf("kodeProdi","namaProdi","lokasi"),
                    intArrayOf(R.id.mkkd, R.id.mknm, R.id.mklokasi)
                )
                lsDataProdi.setAdapter(adapter)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Connection to database error : ${error.message}",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    val itemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
        hm = HashMap()
        hm = alMhs.get(i)
        edNim.setText(hm.get("nim").toString()!!)
        edNama.setText(hm.get("namaMhs").toString()!!)
        edAlamat.setText(hm.get("alamatMhs").toString()!!)
    }
    val itemClickListener1 = AdapterView.OnItemClickListener { adapterView, view, i, l ->
        hm = HashMap()
        hm = alProdi.get(i)
        edkdProdi.setText(hm.get("kodeProdi").toString())
        ednmProdi.setText(hm.get("namaProdi").toString())
        lokasi.setText(hm.get("lokasi").toString())
    }
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnInsertMhs -> {
                mhs.nim = edNim.text.toString()
                mhs.namaMhs = edNama.text.toString()
                mhs.alamatMhs = edAlamat.text.toString()
                db.child(mhs.nim!!).setValue(mhs)
            }
            R.id.btnDeleteMhs -> {
                db.child(mhs.nim!!).removeValue()
            }
            R.id.btnUpdateMhs -> {
                db.child(mhs.nim!!).child("namaMhs").setValue(edNama.text.toString())
                db.child(mhs.nim!!).child("alamatMhs").setValue(edAlamat.text.toString())
            }
            R.id.btnInsertProdi -> {
                prodi.kodeProdi = edkdProdi.text.toString()
                prodi.namaProdi = ednmProdi.text.toString()
                prodi.lokasi = lokasi.text.toString()
                db1.child(prodi.kodeProdi!!).setValue(prodi)
            }
            R.id.btnDeleteProdi -> {
                db1.child(prodi.kodeProdi!!).removeValue()
            }
            R.id.btnUpdateProdi -> {
                db1.child(prodi.kodeProdi!!).child("namaProdi").setValue(ednmProdi.text.toString())
                db1.child(prodi.kodeProdi!!).child("lokasi").setValue(lokasi.text.toString())
            }
        }
        edNim.setText("");edNama.setText("");edAlamat.setText("")
    }
}