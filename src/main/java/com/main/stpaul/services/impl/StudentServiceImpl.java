package com.main.stpaul.services.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.main.stpaul.constants.Result;
import com.main.stpaul.constants.Status;
import com.main.stpaul.dto.request.ExcelStudent;
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.BankDetail;
import com.main.stpaul.entities.GuardianInfo;
import com.main.stpaul.entities.LastSchool;
import com.main.stpaul.entities.Student;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.services.serviceInterface.StudentService;


@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private GuardianInfoServiceImpl guardianInfoServiceImpl;

    @Autowired
    private BankDetailServiceImpl bankDetailServiceImpl;

    @Autowired
    private LastSchoolServiceImpl lastSchoolServiceImpl;

    @Override
    public Student addStudent(Student student) {
        String id = UUID.randomUUID().toString();
        student.setStudentId(id);
        return this.studentRepo.save(student);
    }

    @Override
    public Student getStudentById(String id) {
        Student response = this.studentRepo.findByStudentId(id).orElse(null);
        return response;
    }

    @Override
    public List<Student> getAllStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findAllStudents(query,stdClass,section,session);
    }

    @Override
    public Student updateStudent(Student student) {
        return this.studentRepo.save(student);
    }

    @Override
    public void deleteStudent(String id) {
        this.studentRepo.deleteStudent(id);
        return;
    }

    @Override
    public Student findById(String id) {
        return this.studentRepo.findById(id).get();
    }

    @Override
    public List<PendingStudents> getPendingStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findByStatus(query,stdClass,section,session,Status.Pending);
    }

    @Override
    public List<Student> getFailStudents(String query,String stdClass,String section,String session) {
        return this.studentRepo.findFailStudents(query,stdClass,section,session,Result.FAIL);
    }

 /**
     * Loads student data from a CSV file and saves it to the database.
     * @param file The CSV file containing student data.
     * @return A Result object indicating the success or failure of the operation.
 * @throws Exception 
     */

    @Override
    public void saveStudentFromCSV(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty. Please upload a valid CSV file.");
        }
        try {
            List<Student> students = parseCsv(file);
            for (Student student : students) {
                String id = UUID.randomUUID().toString();
                student.setStudentId(id);
                this.studentRepo.save(student);
            }
        } catch (Exception e) {
            throw new Exception("Error while saving student data from CSV: " + e.getMessage());
        }
        return;
    }

    @Override
    public void saveStudentFromExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("File is empty. Please upload a valid Excel file.");
        }
        try {
            List<List<Object>> studentDataList = parseXlsx(file);
            for (List<Object> data : studentDataList) {
                System.out.println(data.get(0));
                Student student = new Student();
                GuardianInfo guardianInfo = new GuardianInfo();
                // BankDetail bankDetail = new BankDetail();
                LastSchool lastSchool = new LastSchool();
    
                for (Object obj : data) {
                    if (obj instanceof Student) {
                        student = (Student) obj;
                    } else if (obj instanceof GuardianInfo) {
                        guardianInfo = (GuardianInfo) obj;
                    // } else if (obj instanceof BankDetail) {
                    //     bankDetail = (BankDetail) obj;
                    // }
                    } else if (obj instanceof LastSchool) {
                        lastSchool = (LastSchool) obj;
                    }
                }
    
                if (student != null) {
                    student = addStudent(student);
                    guardianInfo.setStudent(student);
                    this.guardianInfoServiceImpl.addGuardianInfo(guardianInfo);
                    // bankDetail.setStudent(student);
                    // this.bankDetailServiceImpl.addBankDetail(bankDetail);
                    lastSchool.setStudent(student);
                    this.lastSchoolServiceImpl.addLastSchool(lastSchool);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error while saving student data from Excel: " + e.getMessage(), e);
        }
        return;
    }



    @Override
    public ByteArrayInputStream loadStudentDataToCSV(String query, String stdClass, String section, String session) {
        List<Student> students = this.studentRepo.findAllStudents(query,stdClass,section,session);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try(CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), CSVFormat.DEFAULT.withHeader("studentId", "name", "fatherName", "motherName", "surname", "section",
                 "session","gender","stdClass","adharNo","phoneNo","email","dateOfBirth"))) {
            for (Student student : students) {
                csvPrinter.printRecord(student.getStudentId(), student.getFirstName(), 
                student.getFatherName(), student.getMotherName(), student.getSurname(), student.getSection(), student.getSession(),student.getGender(), student.getStdClass(), student.getAdharNo(), student.getPhoneNo(), student.getEmail(), student.getDateOfBirth());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());

    }



    /**
     * Parses a CSV file and converts it into a list of Student objects.
     * @param file The CSV file to parse.
     * @return A list of Student objects.
     * @throws Exception If an error occurs while parsing the file.
     */

    private List<Student> parseCsv(MultipartFile file) throws Exception{
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(),StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            for (CSVRecord csvRecord : csvParser) {
                Student student = new Student();
                student.setFirstName(csvRecord.get("name"));
                student.setFatherName(csvRecord.get("fatherName"));
                student.setMotherName(csvRecord.get("motherName"));
                student.setSurname(csvRecord.get("surname"));
                student.setSection(csvRecord.get("section"));
                student.setSession(csvRecord.get("session"));
                student.setStdClass(csvRecord.get("stdClass"));
                student.setEmail(csvRecord.get("email"));
                student.setPhoneNo(csvRecord.get("phoneNo"));
                student.setAdharNo(csvRecord.get("adharNo"));
                student.setGender(csvRecord.get("gender"));
                student.setDateOfBirth(LocalDate.parse(csvRecord.get("dateOfBirth")));

                // Add other fields as necessary
                students.add(student);
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<List<Object>> parseXlsx(MultipartFile file) throws Exception {
        List<List<Object>> students = new ArrayList<>();
    
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
    
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
    
            if (!rowIterator.hasNext()) {
                return students;
            }
    
            Row headerRow = rowIterator.next();
    
            int nameIdx = getColumnIndex(headerRow, "firstName");
            int fatherNameIdx = getColumnIndex(headerRow, "fatherName");
            int motherNameIdx = getColumnIndex(headerRow, "motherName");
            int surnameIdx = getColumnIndex(headerRow, "surname");
            int emailIdx = getColumnIndex(headerRow, "email");
            int phoneNoIdx = getColumnIndex(headerRow, "phoneNo");
            int dobIdx = getColumnIndex(headerRow, "dateOfBirth");
            int genderIdx = getColumnIndex(headerRow, "gender");
            int adharNoIdx = getColumnIndex(headerRow, "aadharNo");
            int bloodGroupIdx = getColumnIndex(headerRow, "bloodGroup");
            int casteIdx = getColumnIndex(headerRow, "caste");
            int categoryIdx = getColumnIndex(headerRow, "category");
            int scholarshipIdx = getColumnIndex(headerRow, "scholarship");
            int admissionDateIdx = getColumnIndex(headerRow, "admissionDate");
            int sectionIdx = getColumnIndex(headerRow, "section");
            int sessionIdx = getColumnIndex(headerRow, "session");
            int stdClassIdx = getColumnIndex(headerRow, "stdClass");
            int rollNoIdx = getColumnIndex(headerRow, "rollNo");
    
            // int bankNameIdx = getColumnIndex(headerRow, "bankName");
            // int branchNameIdx = getColumnIndex(headerRow, "branchName");
            // int accountNoIdx = getColumnIndex(headerRow, "accountNo");
            // int ifscCodeIdx = getColumnIndex(headerRow, "ifscCode");
    
            int guardianNameIdx = getColumnIndex(headerRow, "guardianName");
            int guardianPhoneNoIdx = getColumnIndex(headerRow, "guardianPhoneNo");
            int guardianRelationIdx = getColumnIndex(headerRow, "guardianRelation");
            int guardianOccupationIdx = getColumnIndex(headerRow, "guardianOccupation");
            int guardianIncomeIdx = getColumnIndex(headerRow, "guardianIncome");

            int lastSchoolNameIdx = getColumnIndex(headerRow, "lastSchoolName");
            int lastStudentIdIdx = getColumnIndex(headerRow, "lastStudentId");
            int lastRollNoIdx = getColumnIndex(headerRow, "lastRollNo");
            int lastSchoolUidIdx = getColumnIndex(headerRow, "uId");   
            int lastExaminationIdx = getColumnIndex(headerRow, "lastExamination");  
            int examMonthIdx = getColumnIndex(headerRow, "examMonth");
            int marksObtainedIdx = getColumnIndex(headerRow, "marksObtained");
            int resultIdx = getColumnIndex(headerRow, "result");
    
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<Object> objects = new ArrayList<>();
    
                Student student = new Student();
                student.setFirstName(getCellValue(row.getCell(nameIdx)));
                student.setFatherName(getCellValue(row.getCell(fatherNameIdx)));
                student.setMotherName(getCellValue(row.getCell(motherNameIdx)));
                student.setSurname(getCellValue(row.getCell(surnameIdx)));
                student.setEmail(getCellValue(row.getCell(emailIdx)));
                student.setPhoneNo(getCellValue(row.getCell(phoneNoIdx)));
    
                // Date of Birth
                student.setDateOfBirth(parseLocalDate(row.getCell(dobIdx)));
    
                student.setGender(getCellValue(row.getCell(genderIdx)));
                student.setAdharNo(String.valueOf(getCellValue(row.getCell(adharNoIdx))));
                student.setBloodGroup(getCellValue(row.getCell(bloodGroupIdx)));
                student.setCaste(getCellValue(row.getCell(casteIdx)));
                student.setCategory(getCellValue(row.getCell(categoryIdx)));
                student.setScholarshipCategory(getCellValue(row.getCell(scholarshipIdx)));
                student.setAdmissionDate(parseLocalDate(row.getCell(admissionDateIdx)));
                student.setSection(getCellValue(row.getCell(sectionIdx)));
                student.setSession(getCellValue(row.getCell(sessionIdx)));
                student.setStdClass(getCellValue(row.getCell(stdClassIdx)));
                student.setRollNo(getCellValue(row.getCell(rollNoIdx)));
    
                GuardianInfo guardianInfo = new GuardianInfo();
                guardianInfo.setName(getCellValue(row.getCell(guardianNameIdx)));
                guardianInfo.setPhone(getCellValue(row.getCell(guardianPhoneNoIdx)));
                guardianInfo.setRelation(getCellValue(row.getCell(guardianRelationIdx)));
                guardianInfo.setOccupation(getCellValue(row.getCell(guardianOccupationIdx)));
                guardianInfo.setIncome(safeParseDouble(getCellValue(row.getCell(guardianIncomeIdx))));
    
                // BankDetail bankDetail = new BankDetail();
                // bankDetail.setBankName(getCellValue(row.getCell(bankNameIdx)));
                // bankDetail.setBranchName(getCellValue(row.getCell(branchNameIdx)));
                // bankDetail.setAccountNo(getCellValue(row.getCell(accountNoIdx)));
                // bankDetail.setIfscCode(getCellValue(row.getCell(ifscCodeIdx)));

                LastSchool lastSchool = new LastSchool();
                lastSchool.setCollegeName(getCellValue(row.getCell(lastSchoolNameIdx)));
                lastSchool.setLastStudentId(getCellValue(row.getCell(lastStudentIdIdx)));
                lastSchool.setRollNo(getCellValue(row.getCell(lastRollNoIdx)));
                lastSchool.setUid(getCellValue(row.getCell(lastSchoolUidIdx)));
                lastSchool.setExamination(getCellValue(row.getCell(lastExaminationIdx)));
                lastSchool.setExamMonth(getCellValue(row.getCell(examMonthIdx)));
                lastSchool.setMarksObtained(safeParseDouble(getCellValue(row.getCell(marksObtainedIdx))));
                lastSchool.setResult(getCellValue(row.getCell(resultIdx)));
    
                objects.add(student);
                objects.add(guardianInfo);
                // objects.add(bankDetail);
                objects.add(lastSchool);
                students.add(objects);
            }
        }
    
        return students;
    }
    

// Get the column index by header name
private int getColumnIndex(Row headerRow, String columnName) {
    for (Cell cell : headerRow) {
        if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
            return cell.getColumnIndex();
        }
    }
    return -1; // Return -1 if not found
}

// Get cell value as string
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    private LocalDate parseLocalDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            return LocalDate.parse(getCellValue(cell));
        } catch (Exception e) {
            return null;
        }
    }
    
    private Double safeParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

}
