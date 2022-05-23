package study.android.room2

import androidx.room.*
import study.android.room2.entities.Director
import study.android.room2.entities.School
import study.android.room2.entities.Student
import study.android.room2.entities.Subject
import study.android.room2.entities.relations.*

@Dao
interface SchoolDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchool(school: School)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirector(director: Director)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef)

    @Query("SELECT DISTINCT * FROM student")
    fun getStudents(): List<Student>

    @Query("SELECT DISTINCT * FROM subject")
    fun getSubjects(): List<Subject>

    @Query("SELECT studentName FROM studentsubjectcrossref WHERE subjectName = :subjectName")
    suspend fun getStudentsOfSubject(subjectName: String): List<String>

    @Query("SELECT subjectName FROM studentsubjectcrossref WHERE studentName = :studentName")
    suspend fun getSubjectsOfStudent(studentName: String): List<String>
}