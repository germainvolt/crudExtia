package com.extia.crudExtia.controller;

import com.extia.crudExtia.CrudExtiaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@ActiveProfiles({"test"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.MOCK,classes = CrudExtiaApplication.class)
@WebAppConfiguration
public class CrudExtiaApplicationTest {
    @Test
    public void contextLoads() {
    }

}
