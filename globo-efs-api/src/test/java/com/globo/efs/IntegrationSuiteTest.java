package com.globo.efs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.globo.efs.integration.NotificationIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	NotificationIntegrationTest.class
})
public class IntegrationSuiteTest {

}
