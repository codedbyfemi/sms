package com.example.sms.service;

import com.example.sms.model.Student;
import com.example.sms.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student addStudent(Student student) {
        studentRepository
            .findByEmail(student.getEmail())
            .ifPresent(s -> {
                throw new RuntimeException(
                    "Email already exists: " + student.getEmail()
                );
            });
        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository
            .findById(id)
            .orElseThrow(() ->
                new RuntimeException("Student not found with id: " + id)
            );
    }

    public Student updateStudent(Long id, Student updated) {
        Student existing = getStudentById(id);

        studentRepository
            .findByEmail(updated.getEmail())
            .ifPresent(s -> {
                if (!s.getId().equals(id)) {
                    throw new RuntimeException(
                        "Email already in use: " + updated.getEmail()
                    );
                }
            });

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setAge(updated.getAge());
        return studentRepository.save(existing);
    }

    public void deleteStudent(Long id) {
        getStudentById(id); // ensures student exists
        studentRepository.deleteById(id);
    }
}
