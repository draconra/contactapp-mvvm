package com.gojek.testgojek;

import com.gojek.testgojek.data.ContactService;
import com.gojek.testgojek.databinding.ActivityMainBinding;
import com.gojek.testgojek.model.Contact;
import com.gojek.testgojek.viewmodel.ContactsViewModel;
import com.gojek.testgojek.viewmodel.ContactsViewModelContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 *
 * @author Aditya Sumardi
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContactsViewModelTest {
    private ContactsViewModel contactsViewModel;
    private List<Contact> contacts;
    @Mock private ContactService contactService;
    @Mock private ContactsViewModelContract.MainView mainView;
    @Mock private ActivityMainBinding activityMainBinding;

    @Before
    public void setUpMainViewModelTest() {
        // inject the mocks
        MockitoAnnotations.initMocks(this);

        // Mocking the the ContactService here so we don't call the RandomUserGenerator Class (
        // we are simulating only a call to the api)
        // and all observables will now run on the same thread
        ContactApplication contactApplication = (ContactApplication) RuntimeEnvironment.application;
        contactApplication.setContactService(contactService);
        contactApplication.setScheduler(Schedulers.immediate());

        contactsViewModel = new ContactsViewModel(mainView, contactApplication);
    }


    @Test
    public void simulateGivenTheUserCallListFromApi() throws Exception {
        List<Contact> contacts = RandomUserGenerator.getContactList();
        doReturn(rx.Observable.just(contacts)).when(contactService).getContactsList();
    }

    @Test public void ensureTheViewsAreInitializedCorrectly() throws Exception {
        contactsViewModel.initialChecks();
        assertEquals(true, contactsViewModel.isConnected);
    }


}
