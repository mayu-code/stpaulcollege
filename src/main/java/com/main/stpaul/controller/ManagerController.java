package com.main.stpaul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.main.stpaul.dto.ResponseDTO.SuccessResponse;
import com.main.stpaul.dto.request.AdmissionFormRequest;
import com.main.stpaul.dto.request.BankDetailRequest;
import com.main.stpaul.dto.request.GuardianInfoRequest;
import com.main.stpaul.dto.request.LastSchoolRequest;
import com.main.stpaul.dto.request.StudentRequest;
import com.main.stpaul.entities.AdmissionForm;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.Student;
import com.main.stpaul.mapper.AdmissionFromMapper;
import com.main.stpaul.mapper.BankDetailMapper;
import com.main.stpaul.mapper.GuardianInfoMapper;
import com.main.stpaul.mapper.LastSchoolMapper;
import com.main.stpaul.mapper.StudentMapper;
import com.main.stpaul.services.impl.AdmissionFormServiceImpl;
import com.main.stpaul.services.impl.BankDetailServiceImpl;
import com.main.stpaul.services.impl.GuardianInfoServiceImpl;
import com.main.stpaul.services.impl.LastSchoolServiceImpl;
import com.main.stpaul.services.impl.StudentServiceImpl;

@RequestMapping("/api/manager")
@RestController
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
    private StudentMapper studentMapper;

    @Autowired
    private BankDetailMapper bankDetailMapper;

    @Autowired
    private LastSchoolMapper lastSchoolMapper;

    @Autowired
    private GuardianInfoMapper guardianInfoMapper;

    @Autowired
    private AdmissionFromMapper admissionFromMapper;


    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@RequestPart("admissionForm")AdmissionFormRequest admissionFormRequest,
                                            @RequestPart("student")StudentRequest studentRequest,
                                            @RequestPart("lastSchool")LastSchoolRequest lastSchoolRequest,
                                            @RequestPart("bankDetail")BankDetailRequest bankDetailRequest,
                                            @RequestPart("guardianInfo")GuardianInfoRequest guardianInfoRequest)throws Exception{

        try {

            Student student=this.studentServiceImpl.addStudent(this.studentMapper.toStudent(studentRequest));

            AdmissionForm admissionForm = this.admissionFromMapper.toAdmissionForm(admissionFormRequest);
            // admissionForm.
            this.admissionFormServiceImpl.addAdmissionForm(admissionForm);

            BankDetail bankDetail = this.bankDetailMapper.toBankDetail(bankDetailRequest);
            bankDetail.setStudent(student);
            this.bankDetailServiceImpl.addBankDetail(bankDetail);

            LastSchool lastSchool = this.lastSchoolMapper.toLastSchool(lastSchoolRequest);
            lastSchool.setStudent(student);
            this.lastSchoolServiceImpl.addLastSchool(lastSchool);

            GuardianInfo guardianInfo = this.guardianInfoMapper.toGuardianInfo(guardianInfoRequest);
            guardianInfo.setStudent(student);
            this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);

            SuccessResponse response = new SuccessResponse(HttpStatus.OK,200,"Student Register Successfully !");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    @GetMapping("/students")
    public ResponseEntity<?> allStudents(){
        return null;
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> studentById(){
        return null;
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(){
        return null;
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id")String id){
        return null;
    }
}
