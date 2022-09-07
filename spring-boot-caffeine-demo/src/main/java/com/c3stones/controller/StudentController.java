package com.c3stones.controller;

import com.c3stones.clients.CacheClient;
import com.c3stones.compent.Response;
import com.c3stones.entity.Student;
import com.c3stones.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 学生Controller
 *
 * @author CL
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private CacheClient cacheClient;

    /**
     * 新增学生
     *
     * @param student
     * @return
     */
    @RequestMapping("/save")
    public Response save(Student student) {
        cacheClient.putOfNative(student.getClasses().getCode(), student.getStuNo(), student);
        return Response.success();
    }

    /**
     * 更新学生
     *
     * @param student 学生
     * @return
     */
    @RequestMapping("/update")
    public Response update(Student student) {
        Student oldStudent = cacheClient.getByNative(student.getClasses().getCode(), student.getStuNo(), Student.class);
        if (Objects.nonNull(oldStudent)) {
            BeanUtil.copyPropertiesIgnoreNull(student, oldStudent);
            cacheClient.putOfNative(student.getClasses().getCode(), student.getStuNo(), oldStudent);
        }
        return Response.success();
    }

    /**
     * 删除学生
     *
     * @param student 学生
     * @return
     */
    @RequestMapping("/delete")
    public Response delete(Student student) {
        cacheClient.removeOfNative(student.getClasses().getCode(), student.getStuNo());
        return Response.success();
    }

    /**
     * 删除所有学生
     *
     * @return
     */
    @RequestMapping("/deleteAll")
    public Response delete() {
        cacheClient.loadCaches().forEach(cache -> cacheClient.removeAllOfNative(cache));
        return Response.success();
    }

    /**
     * 查询学生
     *
     * @param classesCode 班号
     * @param stuNo       学号
     * @return
     */
    @RequestMapping("/get")
    public Response get(String classesCode, String stuNo) {
        AtomicReference<Student> student = new AtomicReference<>();
        if (Objects.isNull(classesCode)) {
            cacheClient.loadCaches().forEach(cache -> {
                Student temp = cacheClient.getByNative(cache, stuNo, Student.class);
                if (Objects.nonNull(temp)) {
                    student.set(temp);
                    return;
                }
            });
        } else {
            student.set(cacheClient.getByNative(classesCode, stuNo, Student.class));
        }
        return Response.success(student.get());
    }

    /**
     * 查询班级学生
     *
     * @param classesCode
     * @return
     */
    @RequestMapping("list")
    public Response list(String classesCode) {
        List<Student> list = cacheClient.getAllByNative(classesCode, Student.class);
        return Response.success(list);
    }

    /**
     * 查询所有学生
     *
     * @return
     */
    @RequestMapping("/all")
    public Response all() {
        List<Student> list = new ArrayList<>();
        cacheClient.loadCaches().forEach(cache -> {
            list.addAll(cacheClient.getAllByNative(cache, Student.class));
        });
        return Response.success(list);
    }

}
