package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.entity.Course;
import com.workintech.spring17challenge.entity.HighCourseGpa;
import com.workintech.spring17challenge.entity.LowCourseGpa;
import com.workintech.spring17challenge.entity.MediumCourseGpa;
import com.workintech.spring17challenge.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final List<Course> courses = new ArrayList<>();
    private final LowCourseGpa lowCourseGpa;
    private final MediumCourseGpa mediumCourseGpa;
    private final HighCourseGpa highCourseGpa;

    @Autowired
    public CourseController(LowCourseGpa lowCourseGpa, MediumCourseGpa mediumCourseGpa, HighCourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name) {
        return courses.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("Course with name '" + name + "' not found", HttpStatus.NOT_FOUND));
    }


    @PostMapping
    public ResponseEntity<Map<Double , Course>> addCourse(@RequestBody Course course){
        if(course.getId() == null){
            throw new ApiException("Please enter valid id" , HttpStatus.BAD_REQUEST);
        }
        if(course.getCredit() == null){
            throw  new ApiException("Credit cannot be null" , HttpStatus.BAD_REQUEST);
        }
        if(course.getName() == null){
            throw new ApiException("Name cannot be null" , HttpStatus.BAD_REQUEST);

        }
        if(course.getGrade() == null){
            throw new ApiException("Grade cannot be null" , HttpStatus.BAD_REQUEST);
        }


        if(course.getCredit() < 0 || course.getCredit() > 4){
            throw new ApiException("The credit cannot be less then 0 and more than 4" , HttpStatus.BAD_REQUEST);
        }

        courses.add(course);

        double gpa = calculateTotalGpa(course);

        Map<Double , Course> response = new HashMap<>();
        response.put(gpa , course);

        return new ResponseEntity<>(response , HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public String updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        if(updatedCourse.getCredit() < 0 || updatedCourse.getCredit() > 4){
            throw new ApiException("The credit cannot be less then 0 and more than 4" , HttpStatus.BAD_REQUEST);
        }

        Optional<Course> existingCourse = courses.stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setName(updatedCourse.getName());
            course.setCredit(updatedCourse.getCredit());
            course.setGrade(updatedCourse.getGrade());

            double totalGpa = calculateTotalGpa(course);
            return "Course updated. Total GPA: " + totalGpa;
        }

        throw new ApiException("Coult not found cource with that id", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable int id) {
        boolean removed = courses.removeIf(c -> c.getId() == id);

        if (!removed) {
            throw new ApiException("Course with ID '" + id + "' not found.",HttpStatus.NOT_FOUND);
        }

        return "Course deleted.";
    }

    private double calculateTotalGpa(Course course) {
        double coefficient = course.getGrade().getCoefficient();
        int credit = course.getCredit();

        if (credit <= 2) {
            return coefficient * credit * lowCourseGpa.getGpa();
        } else if (credit == 3) {
            return coefficient * credit * mediumCourseGpa.getGpa();
        } else {
            return coefficient * credit * highCourseGpa.getGpa();
        }
    }
}
