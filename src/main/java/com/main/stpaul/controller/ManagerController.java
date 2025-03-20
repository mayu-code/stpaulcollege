package com.main.stpaul.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.main.stpaul.dto.ResponseDTO.DataResponse;
import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.request.PaymentDetailRequest;
import com.main.stpaul.dto.request.StudentAddRequest;
import com.main.stpaul.dto.response.StudentDetailResponse;
import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.BiofocalSubject;
import com.main.stpaul.entities.Documents;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.PaymentDetail;
import com.main.stpaul.entities.Stream;
import com.main.stpaul.entities.Student;
import com.main.stpaul.entities.StudentAcademics;
import com.main.stpaul.entities.Subject;
import com.main.stpaul.mapper.AdmissionFromMapper;
import com.main.stpaul.mapper.BankDetailMapper;
import com.main.stpaul.mapper.GuardianInfoMapper;
import com.main.stpaul.mapper.LastSchoolMapper;
import com.main.stpaul.mapper.PaymentDetailMapper;
import com.main.stpaul.mapper.StudentAcademicsMapper;
import com.main.stpaul.mapper.StudentMapper;
import com.main.stpaul.services.impl.AdmissionFormServiceImpl;
import com.main.stpaul.services.impl.BankDetailServiceImpl;
import com.main.stpaul.services.impl.BioFocalSubjectServiceImpl;
import com.main.stpaul.services.impl.DocumentServiceImpl;
import com.main.stpaul.services.impl.GuardianInfoServiceImpl;
import com.main.stpaul.services.impl.LastSchoolServiceImpl;
import com.main.stpaul.services.impl.PaymentDetailServiceImpl;
import com.main.stpaul.services.impl.StreamServiceImpl;
import com.main.stpaul.services.impl.StudentAcademicsServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;
import com.main.stpaul.services.impl.SubjectServiceImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private StudentServiceImpl studentServiceImpl;

    @Autowired
    private LastSchoolServiceImpl lastSchoolServiceImpl;

    @Autowired
    private BankDetailServiceImpl bankDetailServiceImpl;

    @Autowired
    private GuardianInfoServiceImpl guardianInfoServiceImpl;

    @Autowired
    private AdmissionFormServiceImpl admissionFormServiceImpl;

    @Autowired
    private StudentAcademicsServiceImpl studentAcademicsServiceImpl;

    @Autowired
    private SubjectServiceImpl subjectServiceImpl;

    @Autowired
    private StreamServiceImpl streamServiceImpl;

    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    @Autowired
    private PaymentDetailServiceImpl paymentDetailServiceImpl;

    @Autowired
    private BioFocalSubjectServiceImpl bioFocalSubjectServiceImpl;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private BankDetailMapper bankDetailMapper;

    @Autowired
    private LastSchoolMapper lastSchoolMapper;

    @Autowired
    private GuardianInfoMapper guardianInfoMapper;

    @Autowired
    private AdmissionFromMapper admissionFromMapper;

    @Autowired
    private PaymentDetailMapper paymentDetailMapper;

    @Autowired
    private StudentAcademicsMapper studentAcademicsMapper;

    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@RequestPart("studentAdd")StudentAddRequest request)throws Exception{

        try {
            Student student=this.studentMapper.toStudent(request.getStudent());
            student.setSession(request.getAdmissionForm().getSession());
            student.setAdmissionDate(request.getAdmissionForm().getAdmissionDate());
            student.setStdClass(request.getAdmissionForm().getStdClass());
            student=this.studentServiceImpl.addStudent(student);
            log.info("Student Added Successfully ");

            AdmissionForm admissionForm = this.admissionFromMapper.toAdmissionForm(request.getAdmissionForm());
            admissionForm.setStudent(student);
            this.admissionFormServiceImpl.addAdmissionForm(admissionForm);
            log.info("admission form Added Successfully ");

            BankDetail bankDetail = this.bankDetailMapper.toBankDetail(request.getBankDetail());
            bankDetail.setStudent(student);
            this.bankDetailServiceImpl.addBankDetail(bankDetail);
            log.info("Bank Detail Added Successfully ");

            LastSchool lastSchool = this.lastSchoolMapper.toLastSchool(request.getLastSchool());
            lastSchool.setStudent(student);
            this.lastSchoolServiceImpl.addLastSchool(lastSchool);
            log.info("Last School Detail Added Successfully ");

            GuardianInfo guardianInfo = this.guardianInfoMapper.toGuardianInfo(request.getGuardianInfo());
            guardianInfo.setStudent(student);
            this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);
            log.info("Guardian Into Added Successfully ");

            StudentAcademics studentAcademics = new StudentAcademics();
            studentAcademics.setStdClass(admissionForm.getStdClass());
            studentAcademics.setStudent(student);
            this.studentAcademicsServiceImpl.addStudentAcademics(studentAcademics);
            log.info("Academic Detail Added Successfully ");

            Stream stream = new Stream();
            stream.setAcademics(studentAcademics);
            stream.setMedium(request.getSubject().getMedium());
            stream.setStream(request.getSubject().getStream());
            stream.setSubStream(request.getSubject().getSubStream());
            stream = this.streamServiceImpl.addStream(stream);
            for(String subject:request.getSubject().getSubjects()){
                Subject sb = new Subject();
                sb.setName(subject);
                sb.setStream(stream);
                this.subjectServiceImpl.addSubject(sb);
            }
            log.info("Subjects Added Successfully ");

            BiofocalSubject biofocalSubject = new BiofocalSubject();
            biofocalSubject.setMedium(request.getBioFocalSubject().getMedium());
            biofocalSubject.setAcademics(studentAcademics);
            biofocalSubject.setSubStream(request.getBioFocalSubject().getSubStream());
            biofocalSubject.setSubject(request.getBioFocalSubject().getSubject());
            this.bioFocalSubjectServiceImpl.addBiofocalSubject(biofocalSubject);
            log.info("Bio Focal Subject Added Successfully ");

            DataResponse response = DataResponse.builder()
                                                .data(student.getStudentId())
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Student Register Successfully !")
                                                .build();
            log.info("Student Admission Successfully ");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            log.error("Error Occured While addmision of Student");
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/students/{id}/documents")
    public ResponseEntity<?> uploadDoucuments(@PathVariable("id")String id,
                                            @RequestParam Map<String, MultipartFile> files)throws Exception{

        log.info("Uploading Student Documents");
        StudentDetailResponse student = this.studentServiceImpl.getStudentById(id);
        if(student ==null){
            log.warn("student not found for id : {}",id);
            throw new EntityNotFoundException("Student not present !");
        }                                         

        files.forEach((docName,file)->{
            try{
                Documents document = new Documents();
                document.setDocumentType(docName);
                document.setDocument(file.getBytes());
                document.setStudent(this.studentMapper.toStudent(student));
                this.documentServiceImpl.addDocuments(document);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Documents Added Successfully !");
        return ResponseEntity.status(HttpStatus.OK).body(response);  
    }
    
    @GetMapping("/students")
    public ResponseEntity<?> allStudents()throws Exception{
        try {
            DataResponse response = DataResponse.builder()
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Get All Users Successfully !")
                                                .data(this.studentServiceImpl.getAllStudents())
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> studentById(@PathVariable("id")String id)throws Exception{
        try {
                StudentDetailResponse student = this.studentServiceImpl.getStudentById(id);
                student.setStudentAcademics(this.studentAcademicsServiceImpl.getAcademicsByStudent(id));
                student.setGuardianInfo(this.guardianInfoServiceImpl.getGuardianInfoByStudent(id));
                student.setBankDetail(this.bankDetailServiceImpl.getBankDetailByStudent(id));
                student.setLastSchool(this.lastSchoolServiceImpl.getLastSchoolByStudent(id));

            DataResponse response = DataResponse.builder()
                                                .status(HttpStatus.OK)
                                                .statusCode(200)
                                                .message("Get All Users Successfully !")
                                                .data(student)
                                                .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(){
        return null;
    }

    @PutMapping("/students/{studentId}/bank-detail/{bkId}")
    public ResponseEntity<?> updateBankDetail(@PathVariable("studentId")String studentId,@PathVariable("bkId")String bkId,
                                                @RequestBody BankDetailRequest bankDetail)throws Exception{
        try {
            StudentDetailResponse student = this.studentServiceImpl.getStudentById(studentId);
            if(student ==null){
                throw new EntityNotFoundException("Student not present !");
            } 
            this.bankDetailServiceImpl.updateBankDetail(bankDetail, bkId);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"bank detail updated Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/last-school/{lsId}")
    public ResponseEntity<?> updateLastSchool()throws Exception{
        try {
            
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Last School Detail updated Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PutMapping("/students/{studentId}/guardian-info/{lsId}")
    public ResponseEntity<?> updateGuardianInfo()throws Exception{
        try {
            
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Guardian information updated Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id")String id)throws Exception{
        try {
            this.studentServiceImpl.deleteStudent(id);
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/bank-detail/{bkId}")
    public ResponseEntity<?> deleteBankDetail(@PathVariable("bkId")String bkId)throws Exception{
        try {
        
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/guardian-info/{giId}")
    public ResponseEntity<?> deleteGuardianInfo(@PathVariable("giId")String giId)throws Exception{
        try {
            
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/students/last-school/{lsId}")
    public ResponseEntity<?> deleteLastSchool(@PathVariable("lsId")String lsId)throws Exception{
        try {
            
            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student deleted Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/students/academics/{academicId}/payment-detail")
    public ResponseEntity<?> addPaymentDetail(@PathVariable("academicId")String academicId,@RequestBody PaymentDetailRequest paymentDetail)throws Exception{
       try {
        StudentAcademics academics = this.studentAcademicsMapper.toStudentAcademics(this.studentAcademicsServiceImpl.getAcademicsById(academicId));
        PaymentDetail paymentDetail2 = this.paymentDetailMapper.toPaymentDetail(paymentDetail);
        paymentDetail2.setStudentAcademics(academics);
        this.paymentDetailServiceImpl.addPaymentDetail(paymentDetail2);
        SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Payment Detail Added Successfully !");
        return ResponseEntity.status(HttpStatus.OK).body(response);
       } catch (Exception e) {
            throw new Exception(e.getMessage());
       }
    }
}
