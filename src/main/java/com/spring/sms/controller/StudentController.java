package com.spring.sms.controller;

import com.spring.sms.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.spring.sms.services.StudentService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/students")
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("")
    public String fetchAllStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/new")
    public String studentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "student_form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid Student student, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Failed to create new student");
            return "student_form";
        }
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("success", "Created Successfully");
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String updateStudentForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "update_form";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@Valid Student student, BindingResult bindingResult, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "redirect:/students";
        }
        Student existingStudents = studentService.getStudentById(id);
        assert existingStudents != null;
        existingStudents.setFirstName(student.getFirstName());
        existingStudents.setLastName(student.getLastName());
        existingStudents.setEmail(student.getEmail());
        studentService.updateStudent(existingStudents);
        redirectAttributes.addFlashAttribute("success", "Updated Successfully");
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        Student student = studentService.getStudentById(id);
        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Student Not found");
            return "redirect:/students";
        }
        studentService.deleteStudent(student);
        redirectAttributes.addFlashAttribute("success", "Deleted Successfully");
        return "redirect:/students";
    }

}
