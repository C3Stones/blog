package com.c3stones.controller;

import com.c3stones.entity.Classes;
import com.c3stones.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.c3stones.compent.Response.SUCCESS_CODE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * StudentController 单元测试
 *
 * @author CL
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StudentControllerTest {

    @Autowired
    private StudentController studentController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    /**
     * 接口测试
     */
    @Test
    public void studentTest() throws Exception {
        Classes classes = new Classes()
                .setCode("A")
                .setName("一班");

        Student student1 = new Student()
                .setStuNo("A01")
                .setName("小明")
                .setClasses(classes);

        log.info("新增学生1 ==>");
        save(student1);

        Student student2 = new Student()
                .setStuNo("A02")
                .setName("小李")
                .setClasses(classes);

        log.info("新增学生2 ==>");
        save(student2);

        log.info("查询学生 ==>");
        select(classes.getCode(), student1.getStuNo(),
                jsonPath("$.data.stuNo").value(student1.getStuNo()),
                jsonPath("$.data.classes.code").value(student1.getClasses().getCode()));

        select(classes.getCode(), student2.getStuNo(),
                jsonPath("$.data.stuNo").value(student2.getStuNo()),
                jsonPath("$.data.classes.code").value(student2.getClasses().getCode()));

        log.info("查询班级 ==>");
        selectList(classes.getCode(),
                jsonPath("$.data[0].classes.code").value(classes.getCode()));

        log.info("查询所有学生 ==>");
        selectAll(jsonPath("$.data", hasSize(2)));

        log.info("更新学生 ==>");
        Student newStudent1 = new Student()
                .setStuNo("A01")
                .setName("小鹏")
                .setClasses(classes);
        update(newStudent1);

        log.info("查询学生 ==>");
        select(null, newStudent1.getStuNo(),
                jsonPath("$.data.stuNo").value(newStudent1.getStuNo()),
                jsonPath("$.data.classes.code").value(newStudent1.getClasses().getCode()),
                jsonPath("$.data.name").value(newStudent1.getName()));

        log.info("删除学生 ==>");
        delete(student2.getClasses().getCode(), student2.getStuNo());

        log.info("查询所有学生 ==>");
        selectAll(jsonPath("$.data", hasSize(1)));

        log.info("删除所有学生 ==>");
        deleteAll();

        log.info("查询所有学生 ==>");
        selectAll(jsonPath("$.data", hasSize(0)));
    }

    /**
     * 新增学生
     *
     * @param student
     */
    private void save(Student student) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/student/save")
                        .param("stuNo", student.getStuNo())
                        .param("name", student.getName())
                        .param("classes.code", student.getClasses().getCode())
                        .param("classes.name", student.getClasses().getName()))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 查询学生
     *
     * @param classesCode
     * @param stuNo
     * @param matcher
     */
    private void select(String classesCode, String stuNo, ResultMatcher... matcher) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/get")
                        .param("classesCode", classesCode)
                        .param("stuNo", stuNo))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andExpectAll(matcher)                                          // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 查询班级学生
     *
     * @param classesCode
     * @param matcher
     */
    private void selectList(String classesCode, ResultMatcher... matcher) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/list")
                        .param("classesCode", classesCode))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andExpectAll(matcher)                                          // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 查询所有学生
     *
     * @param matcher
     */
    private void selectAll(ResultMatcher... matcher) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/all"))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andExpectAll(matcher)                                          // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }


    /**
     * 更新学生
     *
     * @param newStudent
     */
    private void update(Student newStudent) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/update")
                        .param("stuNo", newStudent.getStuNo())
                        .param("name", newStudent.getName())
                        .param("classes.code", newStudent.getClasses().getCode())
                        .param("classes.name", newStudent.getClasses().getName()))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 删除学生
     *
     * @param classesCode
     * @param stuNo
     */
    private void delete(String classesCode, String stuNo) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/delete")
                        .param("classes.code", classesCode)
                        .param("stuNo", stuNo))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }

    /**
     * 删除所有学生
     */
    private void deleteAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/deleteAll"))
                .andExpect(status().isOk())                                     // 增加断言
                .andExpect(jsonPath("$.code").value(SUCCESS_CODE))    // 增加断言
                .andDo(print())                                                 // 打印结果
                .andReturn();
    }
}