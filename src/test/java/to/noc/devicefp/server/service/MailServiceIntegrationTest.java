/**
 * Copyright (C) 2013, Dmitry Holodov. All rights reserved.
 */
package to.noc.devicefp.server.service;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import to.noc.devicefp.server.config.MailConfig;
import to.noc.devicefp.server.config.PropertiesConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    PropertiesConfig.class,
    MailConfig.class,
    MailServiceImpl.class
} )
public class MailServiceIntegrationTest {

    @Autowired
    private MailService mailService;

    @Test(timeout=30000)
    public void testSendMail() {
        String cc = null;
        String subj = "Automated Test Subject";
        String htmlBody =
                "This is a multiline test.\n\nSincerely,\nAutomated Test System\n";
        assertEquals(true, mailService.sendMail(cc, subj, htmlBody));
    }
}
