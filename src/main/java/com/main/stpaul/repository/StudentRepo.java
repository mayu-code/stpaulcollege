package com.main.stpaul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.dto.response.StudentResponse;
import com.main.stpaul.entities.Student;

public interface StudentRepo extends JpaRepository<Student,String>{
    
    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentResponse(
        s.studentId, s.firstName, s.fatherName, s.motherName, s.surname, s.email, s.phoneNo, s.dateOfBirth, s.gender,
        s.adharNo, s.bloodGroup, s.caste, s.category, s.scholarshipCategory,
        s.admissionDate, s.session, s.stdClass, s.status) 
        FROM Student s WHERE s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    List<StudentResponse> findAllStudents();


    @Query("""
        SELECT new com.main.stpaul.dto.response.StudentResponse(
        s.studentId, s.firstName, s.fatherName, s.motherName, s.surname, s.email, s.phoneNo, s.dateOfBirth, s.gender,
        s.adharNo, s.bloodGroup, s.caste, s.category, s.scholarshipCategory,
        s.admissionDate, s.session, s.stdClass, s.status) 
        FROM Student s WHERE s.studentId=:id
        AND s.isDelete = false
        ORDER BY s.addDate DESC
       """)
    Optional<StudentResponse> findByStudentId(@Param("id")String id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email AND s.isDelete = false")
    boolean existsByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Student s SET s.deleteDate=now , s.isDelete=true WHERE s.studentId=:id")
    void deleteStudent(String id);

    @Query(value = """
    SELECT 
        -- Student Details
        s.student_id AS studentId, s.first_name AS firstName, s.father_name AS fatherName, 
        s.mother_name AS motherName, s.surname, s.email, s.phone_no AS phoneNo, 
        s.date_of_birth AS dateOfBirth, s.gender, s.adhar_no AS adharNo, 
        s.blood_group AS bloodGroup, s.caste, s.category, s.scholarship_category AS scholarshipCategory, 
        s.admission_date AS admissionDate, s.session, s.std_class AS stdClass, s.status,

        -- Last School Details
        ls.ls_id AS lastSchoolId, ls.college_name AS lastCollegeName, ls.last_student_id AS lastStudentId, 
        ls.roll_no AS lastRollNo, ls.uid, ls.examination AS lastExamination, 
        ls.exam_month AS lastExamMonth, ls.marks_obtained AS lastMarksObtained, ls.result AS lastResult,

        -- Bank Details
        b.bank_detail_id AS bankDetailId, b.bank_name AS bankName, b.branch_name AS branchName, 
        b.account_no AS accountNo, b.ifsc_code AS ifscCode,

        -- Guardian Details
        g.gi_id AS guardianId, g.name AS guardianName, g.phone AS guardianPhone, 
        g.occupation AS guardianOccupation, g.relation AS guardianRelation, g.income AS guardianIncome,

        -- Academic Details
        sa.student_academics_id AS studentAcademicsId, sa.college_name AS collegeName, 
        sa.roll_no AS rollNo, sa.examination AS examination, sa.exam_month AS examMonth, 
        sa.marks_obtained AS marksObtained, sa.std_class AS academicStdClass, sa.result AS academicResult, 
        sa.is_alumni AS isAlumni, sa.promotion_date AS promotionDate, sa.status AS academicStatus,

        -- Stream Details
        st.id AS streamId, st.stream AS streamName, st.sub_stream AS subStream, st.medium AS streamMedium,

        -- Biofocal Subject Details
        bf.id AS biofocalId, bf.sub_stream AS biofocalSubStream, bf.subject AS biofocalSubject, bf.medium AS biofocalMedium,

        -- Address Details
        a.address_id AS addressId, a.address_type AS addressType, a.street, a.city, a.state, a.pincode, a.country,

        -- Document Details
        d.document_id AS documentId, d.document_type AS documentType, d.document AS documentData

    FROM student s
    LEFT JOIN last_school ls ON s.student_id = ls.student_id
    LEFT JOIN bank_detail b ON s.student_id = b.student_id
    LEFT JOIN guardian_info g ON s.student_id = g.student_id
    LEFT JOIN student_academics sa ON s.student_id = sa.student_id
    LEFT JOIN stream st ON sa.student_academics_id = st.student_academics_id
    LEFT JOIN biofocal_subject bf ON sa.student_academics_id = bf.student_academics_id
    LEFT JOIN address a ON s.student_id = a.student_id
    LEFT JOIN document d ON s.student_id = d.student_id
    WHERE s.student_id = :id 
      AND s.is_delete = false
    ORDER BY s.add_date DESC
    """, nativeQuery = true)
    Optional<StudentDetailResponse> findByStudentID(@Param("id") String id);


}
