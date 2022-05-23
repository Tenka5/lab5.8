package study.android.room2


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var rbStudent: RadioButton
    private lateinit var rbSubject: RadioButton
    private lateinit var spinner: Spinner
    private lateinit var listCaption: TextView
    private lateinit var recyclerView: RecyclerView
    var a1:String = ""
    val db by lazy {
        Room.databaseBuilder(
            this,
            SchoolDatabase::class.java, "school.db"
        ).allowMainThreadQueries().build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rbStudent = findViewById(R.id.rbStudent)
        rbSubject = findViewById(R.id.rbSubject)
        spinner = findViewById(R.id.spinner)
        listCaption = findViewById(R.id.listCaption)
        recyclerView = findViewById(R.id.recyclerView)
        val context = this
        recyclerView.layoutManager = LinearLayoutManager(this)
        rbStudent.setOnClickListener{
            listCaption.text = "Student's subjects"
            GlobalScope.launch {
                val s1=db.schoolDao.getStudents()
                val s2 = MutableList<String>(0){""}
                for(i in s1)
                    s2.add(i.studentName)
                withContext(Dispatchers.Main){
                    spinner.adapter=ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, s2)
                }
            }
        }
        rbSubject.setOnClickListener{
            listCaption.text = "Students study"
            GlobalScope.launch {
                val s1=db.schoolDao.getSubjects()
                val s2 = MutableList<String>(0){""}
                for(i in s1)
                    s2.add(i.subjectName)
                withContext(Dispatchers.Main){
                    spinner.adapter=ArrayAdapter<String>(applicationContext, android.R.layout.simple_spinner_item, s2)
                }
            }
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {
                if(view==spinner.selectedView) {
                        a1 = spinner.getSelectedItem().toString()
                        val s1 = MutableList<String>(0) { "" }
                        val s2 = MutableList<String>(0) { "" }
                        val b = MutableList<String>(0) { "" }
                        val s3 = db.schoolDao.getSubjects()
                        for (i in s3)
                            s1.add(i.subjectName)
                        val d = db.schoolDao.getStudents()
                        for (i in d)
                            s2.add(i.studentName)
                        if (s1.contains(a1)) {
                            GlobalScope.launch {
                                for (i in db.schoolDao.getStudentsOfSubject(a1))
                                    b.add(i)
                            }
                        } else {
                            GlobalScope.launch {
                                for (i in db.schoolDao.getSubjectsOfStudent(a1))
                                    b.add(i)
                            }
                        }
                        recyclerView.adapter = ASSAdapter(b)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        val dao = db.schoolDao;
        lifecycleScope.launch {
            DataExample.directors.forEach { dao.insertDirector(it) }
            DataExample.schools.forEach { dao.insertSchool(it) }
            DataExample.subjects.forEach { dao.insertSubject(it) }
            DataExample.students.forEach { dao.insertStudent(it) }
            DataExample.studentSubjectRelations.forEach { dao.insertStudentSubjectCrossRef(it) }
        }
    }
}