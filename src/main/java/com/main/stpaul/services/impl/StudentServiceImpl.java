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
import com.main.stpaul.dto.response.PendingStudents;
import com.main.stpaul.entities.Student;
import com.main.stpaul.repository.StudentRepo;
import com.main.stpaul.services.serviceInterface.StudentService;


@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepo studentRepo;

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
            List<Student> students = parseXlsx(file);
            for (Student student : students) {
                String id = UUID.randomUUID().toString();
                student.setStudentId(id);
                this.studentRepo.save(student);
            }
        } catch (Exception e) {
            throw new Exception("Error while saving student data from Excel: " + e.getMessage());
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

    private List<Student> parseXlsx(MultipartFile file) throws Exception {
    List<Student> students = new ArrayList<>();

    try (InputStream inputStream = file.getInputStream(); Workbook workbook = WorkbookFactory.create(inputStream)) {
        Sheet sheet = workbook.getSheetAt(0); 
        Iterator<Row> rowIterator = sheet.iterator();

        Row headerRow = rowIterator.next();
        int nameIdx = getColumnIndex(headerRow, "name");
        int fatherNameIdx = getColumnIndex(headerRow, "fatherName");
        int motherNameIdx = getColumnIndex(headerRow, "motherName");
        int surnameIdx = getColumnIndex(headerRow, "surname");
        int sectionIdx = getColumnIndex(headerRow, "section");
        int sessionIdx = getColumnIndex(headerRow, "session");
        int stdClassIdx = getColumnIndex(headerRow, "stdClass");
        int emailIdx = getColumnIndex(headerRow, "email");
        int phoneNoIdx = getColumnIndex(headerRow, "phoneNo");
        int adharNoIdx = getColumnIndex(headerRow, "adharNo");
        int genderIdx = getColumnIndex(headerRow, "gender");
        int dobIdx = getColumnIndex(headerRow, "dateOfBirth");

        // Read each row and map to Student object
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Student student = new Student();

            student.setFirstName(getCellValue(row.getCell(nameIdx)));
            student.setFatherName(getCellValue(row.getCell(fatherNameIdx)));
            student.setMotherName(getCellValue(row.getCell(motherNameIdx)));
            student.setSurname(getCellValue(row.getCell(surnameIdx)));
            student.setSection(getCellValue(row.getCell(sectionIdx)));
            student.setSession(getCellValue(row.getCell(sessionIdx)));
            student.setStdClass(getCellValue(row.getCell(stdClassIdx)));
            student.setEmail(getCellValue(row.getCell(emailIdx)));
            student.setPhoneNo(getCellValue(row.getCell(phoneNoIdx)));
            student.setAdharNo(String.valueOf(getCellValue(row.getCell(adharNoIdx))));
            student.setGender(getCellValue(row.getCell(genderIdx)));

            // Handle date parsing
            if (dobIdx != -1) {
                Cell dobCell = row.getCell(dobIdx);
                if (dobCell != null && dobCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dobCell)) {
                    student.setDateOfBirth(dobCell.getLocalDateTimeCellValue().toLocalDate());
                } else {
                    student.setDateOfBirth(LocalDate.parse(getCellValue(dobCell)));
                }
            }

            students.add(student);
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

}
