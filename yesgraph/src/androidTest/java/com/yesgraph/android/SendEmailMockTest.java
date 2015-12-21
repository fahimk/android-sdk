package com.yesgraph.android;

import com.yesgraph.android.utils.SendEmailManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by Klemen on 21.12.2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class SendEmailMockTest {

    @Test
    public void testSendEmailMock() {

        SendEmailManager emailManager = Mockito.mock(SendEmailManager.class);

        when(emailManager.sendEmail()).thenReturn(true);

        assertEquals(emailManager.sendEmail(), true);

    }
}
