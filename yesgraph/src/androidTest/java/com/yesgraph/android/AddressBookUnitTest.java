package com.yesgraph.android;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.test.ApplicationTestCase;

import com.yesgraph.android.models.Contact;
import com.yesgraph.android.models.ContactList;
import com.yesgraph.android.models.RankedContact;
import com.yesgraph.android.models.Source;
import com.yesgraph.android.network.AddressBook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemen on 9.12.2015.
 */
public class AddressBookUnitTest extends ApplicationTestCase<Application> {
    public AddressBookUnitTest() {
        super(Application.class);
    }

    @Mock
    private Context mockContext;

    @Override
    protected void setUp() throws Exception {
        mockContext = new MockDelegatedContext(getContext());
    }


    /**
     * Validate mapping data between JSON object and YSGRankedContact object
     */
    public void testValidateMappingFromJsonToYSGRankedContact() throws JSONException {

        JSONObject jsonContact = getContactJsonObject();

        RankedContact contact = new RankedContact(jsonContact);

        boolean emailsNotNull = contact.emails() != null;
        assertEquals(true, emailsNotNull);

        boolean hasTwoEmails = contact.emails().size() == jsonContact.getJSONArray("emails").length();
        assertEquals(true, hasTwoEmails);

        boolean hasTwoPhones = contact.phones().size() == jsonContact.getJSONArray("phones").length();
        assertEquals(true, hasTwoPhones);

        boolean nameNotNull = contact.name() != null;
        assertEquals(true, nameNotNull);

        boolean phoneNotNull = contact.phone() != null;
        assertEquals(true, phoneNotNull);

        boolean emailNotNull = contact.email() != null;
        assertEquals(true, emailNotNull);

        int actualRank = contact.getRank();
        int expectedRank = 7;
        assertEquals(expectedRank, actualRank);

        double actualScore = contact.getScore();
        double expectedScore = 12.5;
        assertEquals(expectedScore, actualScore);

    }


    /**
     * Validate contacts list ordered ascending
     */
    public void testValidateContactsListOrderedAsc() throws JSONException {

        JSONArray jsonContacts = new TestUtils().getJsonArray();

        ContactList ysgContactList = new AddressBook().contactListFromResponse(jsonContacts);

        boolean areAscOrdered = isAscOrdered(ysgContactList);

        assertEquals(true, areAscOrdered);

    }

    /**
     * Validate mapping from YSGRankedContact to JsonObject
     */
    public void testValidateMappingYSGRankedContactToJson() throws JSONException {

        RankedContact contact = getRankedContact();

        JSONObject jsonObject = contact.toJSONObjectExtended();

        boolean hasName = jsonObject.has("name");
        assertEquals(true, hasName);

        boolean hasEmail = jsonObject.has("email");
        assertEquals(true, hasEmail);

        boolean hasPhones = jsonObject.has("phone");
        assertEquals(true, hasPhones);

        int actualPhonesCount = jsonObject.getJSONArray("phones").length();
        int expectedPhonesCount = contact.phones().size();

        assertEquals(expectedPhonesCount, actualPhonesCount);

        boolean hasEmails = jsonObject.has("emails");
        assertEquals(true, hasEmails);

        int actualEmailsCount = jsonObject.getJSONArray("emails").length();
        int expectedEmailsCount = contact.emails().size();

        assertEquals(expectedEmailsCount, actualEmailsCount);

        int actualRank = jsonObject.getInt("rank");
        int expectedRank = contact.getRank();
        assertEquals(expectedRank, actualRank);

        double actualScore = jsonObject.getDouble("score");
        double expectedScore = contact.getScore();
        assertEquals(expectedScore, actualScore);

    }


    /**
     * HELPER METHODS
     */

    @NonNull
    private JSONObject getContactJsonObject() throws JSONException {

        JSONObject jsonContact = new JSONObject();

        jsonContact.put("name", "John");
        jsonContact.put("phone", "123-432-533");
        jsonContact.put("email", "john@poviolabs.com");

        jsonContact.put("rank", 7);
        jsonContact.put("score", 12.5);

        JSONArray emails = new JSONArray();

        JSONObject email1 = new JSONObject();
        email1.put("emails", "john@gmail.com");

        JSONObject email2 = new JSONObject();
        email2.put("emails", "john@email.com");

        emails.put(email1);
        emails.put(email2);

        jsonContact.put("emails", emails);

        JSONArray phones = new JSONArray();

        JSONObject phone1 = new JSONObject();
        email1.put("phones", "124-321-533");

        JSONObject phone2 = new JSONObject();
        email2.put("phones", "435-235-523");

        phones.put(phone1);
        phones.put(phone2);

        jsonContact.put("phones", phones);


        return jsonContact;
    }


    /**
     * Check ascending ordered
     *
     * @param contactsList list of asc ordered contacts
     * @return true if is asc ordered
     */
    private boolean isAscOrdered(ContactList contactsList) {

        boolean isOrdered = true;

        for (int i = 0; i < contactsList.getEntries().size(); i++) {

            if (i + 1 > contactsList.getEntries().size() - 1) {
                break;
            }

            RankedContact contact = contactsList.getEntries().get(i);
            RankedContact nextContact = contactsList.getEntries().get(i + 1);

            if (contact.getRank() > nextContact.getRank()) {
                isOrdered = false;
                break;
            }
        }
        return isOrdered;
    }


    @NonNull
    private RankedContact getRankedContact() {

        RankedContact contact = new RankedContact();
        contact.setName("John");
        contact.setEmail("jonh@hotmail.com");
        contact.setPhone("4554-6567756");

        ArrayList<String> emails = new ArrayList<>();
        emails.add("john@email.com");
        emails.add("john@gmail.com");

        contact.setEmails(emails);

        ArrayList<String> phones = new ArrayList<>();
        phones.add("123-456-542");
        phones.add("123-456-324");

        contact.setPhones(phones);

        contact.setRank(16);
        contact.setScore(54.2);

        return contact;
    }

    /**
     * Validate contact list from json object (server response)
     *
     * @throws JSONException
     */
    public void testGetContactListFromJSONArray() throws JSONException {

        JSONObject jsonContactOne = getContactJsonObject();
        JSONObject jsonContactTwo = getContactJsonObject();
        JSONObject jsonContactThree = getContactJsonObject();

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(0, jsonContactOne);
        jsonArray.put(1, jsonContactTwo);
        jsonArray.put(2, jsonContactThree);

        JSONObject data = new JSONObject();
        data.put("data", jsonArray);

        Message message = new Message();
        message.obj = data;

        ContactList contactList = new AddressBook().getContactList(mockContext, message);

        assertTrue(contactList != null);

    }

    public void testCheckSplittedContactsItems() {

        int limitedSize = 3;

        ContactList contactList = new ContactList();
        ArrayList<RankedContact> entries = new TestUtils().getRankedContacts(10);
        contactList.setEntries(entries);
        contactList.setSource(new Source());
        contactList.setUseSuggestions(true);

        List<ContactList> batchedContacts = new AddressBook().getLimitedContactsList(limitedSize, contactList);

        boolean isSplitted = true;

        // check size of each list
        for (int i = 0; i < batchedContacts.size(); i++) {

            ContactList list = batchedContacts.get(i);

            int entriesSize = list.getEntries().size();

            if (entriesSize > limitedSize) {
                isSplitted = false;
                break;
            }
        }

        assertTrue(isSplitted);
    }

    public void testCheckSplitFirstContactList() {

        int limitedSize = 2;

        ContactList contactList = new ContactList();
        ArrayList<RankedContact> entries = new TestUtils().getRankedContacts(5);
        contactList.setEntries(entries);
        contactList.setSource(new Source());
        contactList.setUseSuggestions(true);

        ContactList firstBatchedContacts = new AddressBook().getFirstBatchedContacts(limitedSize, contactList);

        int actualSize = firstBatchedContacts.getEntries().size();

        assertEquals(limitedSize, actualSize);

    }
}