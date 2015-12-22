package com.yesgraph.android;

import android.content.Context;
import android.content.Intent;

import com.yesgraph.android.application.YesGraph;
import com.yesgraph.android.utils.SendEmailManager;
import com.yesgraph.android.utils.SendSmsManager;
import com.yesgraph.android.utils.SenderManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by Klemen on 21.12.2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class MockTests {

    @Mock
    Context context;

    @Test
    public void testSendEmailMock() {

        SendEmailManager emailManager = Mockito.mock(SendEmailManager.class);

        when(emailManager.sendEmail()).thenReturn(true);

        assertEquals(emailManager.sendEmail(), true);

    }

    /**
     * Validate if invites sent to contacts
     *
     * @throws Exception
     */
    @Test
    public void testValidateInvitesSend() throws Exception {

        SenderManager manager = Mockito.mock(SenderManager.class);

        when(manager.inviteContacts(context)).thenReturn(true);

        assertEquals(manager.inviteContacts(context), true);

    }

}
