package com.hantsylabs.example.spring.test.webdriver;

import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.WebConnectionHtmlUnitDriver;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.hantsylabs.example.spring.config.AppConfig;
import com.hantsylabs.example.spring.config.WebConfig;
import com.hantsylabs.example.spring.test.Assertions;
import com.hantsylabs.example.spring.test.MockDataConfig;
import com.hantsylabs.example.spring.test.webdriver.pages.CreateTaskPage;
import com.hantsylabs.example.spring.test.webdriver.pages.TaskListPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppConfig.class, MockDataConfig.class, WebConfig.class})
@WebAppConfiguration
public class MockMvcHtmlUnitWebDriverCreateTaskTests {

	@Autowired
	WebApplicationContext context;

	WebDriver driver;

	@Before
	public void setUp() throws Exception {
		driver = MockMvcHtmlUnitDriverBuilder
				.webAppContextSetup(context)
				.withDelegate(new WebConnectionHtmlUnitDriver(BrowserVersion.CHROME))
				.contextPath("")
				.build();
	}

	@After
	public void tearDown() throws Exception {
		if(driver !=null){
			driver.close();
		}
	}

	@Test
	public void testCreateTasks() {
		CreateTaskPage createTask = CreateTaskPage.to(driver);
		TaskListPage taskList = createTask.newTask( "first task", "description of first task");
		
		Assertions.assertThat(taskList.getErrors()).isEqualTo("Task is created sucessfully!");
		Assertions.assertThat(taskList.getPageTitle()).isEqualTo("TASK LIST");
	}
	
	@Test
	public void testCreateTaskWithEmptyFields() {
		CreateTaskPage createTask = CreateTaskPage.to(driver);
		CreateTaskPage createTaskPage = createTask.newTaskWithEmptyFields();		
		Assertions.assertThat(createTaskPage.getTitleError()).isEqualTo("may not be empty");
		Assertions.assertThat(createTaskPage.getDescriptionError()).isEqualTo("size must be between 10 and 200");
		//
	}
}
