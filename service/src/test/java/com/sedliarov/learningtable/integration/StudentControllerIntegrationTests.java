package com.sedliarov.learningtable.integration;

import com.sedliarov.learningtable.mapper.StudentMapper;
import com.sedliarov.learningtable.model.dto.StudentDto;
import com.sedliarov.learningtable.model.entity.Student;
import com.sedliarov.learningtable.repository.StudentRepository;
import com.sedliarov.learningtable.utils.StudentFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Student integration controller with rest api tests.
 *
 * @author Kirill Sedliarov
 */

public class StudentControllerIntegrationTests extends RestIntegrationTestBase {

  private static final String STUDENTS_URL = "/students/";

  @Autowired
  private StudentRepository studentRepository;

  // TODO: 9/5/2022 Need to create beans for all Mappers in Configuration class. And use @Autowired in tests.
  @Autowired
  private StudentMapper mapper;

  @Test
  void testGetStudentById() {
    // given
    // TODO: 9/5/2022 Need to implement Fixture{EntityName} static class and use in tests, like this case.
    Student newStudent = StudentFixture.createEntity();
    Student savedStudent = studentRepository.save(newStudent);
    StudentDto studentMapper = mapper.entityToDto(newStudent);
    // when
    ResponseEntity<StudentDto> student =
        exchangeGetWithoutAuth(STUDENTS_URL + savedStudent.getStudentId(), StudentDto.class);
    // then
    assertThat(student.getBody()).isEqualTo(studentMapper);
  }

  @Test
  void negativeTestGetStudentById() {
    // given
    Student newStudent = StudentFixture.createEntityWithId(UUID.fromString("3e1e6d16-451b-4748-b6a0-8f4a84a0a53a"));
    Student savedStudent = studentRepository.save(newStudent);
    StudentDto studentMapper = mapper.entityToDto(newStudent);
    // when
    ResponseEntity<StudentDto> student =
        exchangeGetWithoutAuth(STUDENTS_URL + savedStudent.getStudentId(), StudentDto.class);
    // then
    assertThat(student.getBody()).isEqualTo(studentMapper);
  }

  @Test
  void testGetStudents() {
    // given
    Student newStudent1 = StudentFixture.createEntity();
    Student savedStudent1 = studentRepository.save(newStudent1);
    Student newStudent2 = StudentFixture.createEntityWithFirstAndSecondName("Aria", "Arievna");
    Student savedStudent2 = studentRepository.save(newStudent2);
    StudentDto studentMapper1 = mapper.entityToDto(savedStudent1);
    StudentDto studentMapper2 = mapper.entityToDto(savedStudent2);
    List<StudentDto> equalList = new ArrayList<>();
    equalList.add(studentMapper1);
    equalList.add(studentMapper2);
    // when
    ResponseEntity<List<StudentDto>> students =
        exchangeGetAllWithoutAuth(STUDENTS_URL, List.class);
    // then
    System.out.println(studentMapper1.toString());
    System.out.println((StudentDto) students.getBody().get(1));
    System.out.println(studentMapper2);
    assertThat(!students.getBody().isEmpty());
    assertThat(students.getBody()).isEqualTo(equalList);
    assertThat(students.getBody().get(0)).isEqualTo(studentMapper1);
    assertThat(students.getBody().get(1)).isEqualTo(studentMapper2);
  }

  @Test
  void testAddStudent() {
    // given
    StudentDto newStudent = StudentFixture.createDto();
    // when
    ResponseEntity<StudentDto> student =
        exchangeAddWithoutAuth(STUDENTS_URL, newStudent, StudentDto.class);
    // then
    assertThat(student.getStatusCodeValue()).isEqualTo(201);
    assertThat(student.getBody().getFirstName()).isEqualTo(newStudent.getFirstName());
    assertThat(student.getBody().getSecondName()).isEqualTo(newStudent.getSecondName());
    assertThat(student.getBody().getNote()).isEqualTo(newStudent.getNote());
    assertThat(student.getBody().getGroup()).isEqualTo(newStudent.getGroup());
  }

  @Test
  void testDeleteStudent() {
    // given
    StudentDto newStudent = StudentFixture.createDto();
    // when
    ResponseEntity<StudentDto> student =
        exchangeDeleteWithoutAuth(STUDENTS_URL + "/" + newStudent.getStudentId());
    // then
    ResponseEntity<StudentDto> check =
        exchangeGetWithoutAuth(STUDENTS_URL + "/" + newStudent.getStudentId(), StudentDto.class);
    assertThat(student).isNull();
    assertThat(check.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testUpdateStudent() {
    // given
    Student newStudent = StudentFixture.createEntity();
    Student savedStudent = studentRepository.save(newStudent);
    StudentDto newStudentDto = StudentFixture.createDtoWithFirstAndSecondName("Maria", "Sharapova");
    // when
    ResponseEntity<StudentDto> student =
        exchangeUpdateWithoutAuth(STUDENTS_URL + "/" + newStudent.getStudentId(), newStudentDto);
    // then
    ResponseEntity<StudentDto> check =
        exchangeGetWithoutAuth(STUDENTS_URL + "/" + newStudent.getStudentId(), StudentDto.class);
    assertThat(student).isNull();
    assertThat(savedStudent.getStudentId()).isEqualTo(check.getBody().getStudentId());
    assertThat(check.getBody().getFirstName()).isEqualTo("Maria");
    assertThat(check.getBody().getSecondName()).isEqualTo("Sharapova");
    assertThat(check.getBody().getNote()).isEqualTo(savedStudent.getNote());
  }
}
