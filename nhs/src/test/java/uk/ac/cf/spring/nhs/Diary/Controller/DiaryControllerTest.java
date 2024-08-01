package uk.ac.cf.spring.nhs.Diary.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import uk.ac.cf.spring.nhs.Common.util.DeviceDetector;
import uk.ac.cf.spring.nhs.Common.util.NavMenuItem;
import uk.ac.cf.spring.nhs.Diary.Entity.DiaryEntry;
import uk.ac.cf.spring.nhs.Diary.Service.DiaryService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(DiaryController.class)
public class DiaryControllerTest {

    @InjectMocks
    private DiaryController diaryController;

    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    private AutoCloseable closeable;
    private MockedStatic<DeviceDetector> mockedStatic;
    private List<DiaryEntry> dummyEntries;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(DeviceDetector.class);

        dummyEntries = Arrays.asList(
                new DiaryEntry(new Date(), "Formatted Date 1", "Content 1"),
                new DiaryEntry(new Date(), "Formatted Date 2", "Content 2")
        );

        when(diaryService.getDiaryEntries()).thenReturn(dummyEntries);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        mockedStatic.close();
    }

    @Test
    public void diaryReturnsCorrectView() {
        Model model = mock(Model.class);
        String viewName = diaryController.diary(model);
        assertEquals("diary/diary", viewName);
        verify(model).addAttribute(eq("diaryEntries"), eq(dummyEntries));
    }

    @Test
    public void checkinReturnsCorrectView() {
        Model model = mock(Model.class);
        String viewName = diaryController.checkin(model);
        assertEquals("diary/checkin", viewName);
    }

    @Test
    public void testDiary() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/diary"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("diary/diary"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("diaryEntries"))
                .andExpect(MockMvcResultMatchers.model().attribute("diaryEntries", dummyEntries));
    }

    @Test
    void testNavMenuItems() {
        List<NavMenuItem> expectedNavMenuItems = List.of(
                new NavMenuItem("Diary", "/diary", "fa-solid fa-book"),
                new NavMenuItem("Check-in", "/diary/checkin", "fa-solid fa-user-check"),
                new NavMenuItem("Photos", "/diary/photos", "fa-solid fa-camera")
        );

        List<NavMenuItem> actualNavMenuItems = diaryController.navMenuItems();

        assertEquals(expectedNavMenuItems.size(), actualNavMenuItems.size());

        for (int i = 0; i < expectedNavMenuItems.size(); i++) {
            assertEquals(expectedNavMenuItems.get(i).getTitle(), actualNavMenuItems.get(i).getTitle());
            assertEquals(expectedNavMenuItems.get(i).getUrl(), actualNavMenuItems.get(i).getUrl());
            assertEquals(expectedNavMenuItems.get(i).getIcon(), actualNavMenuItems.get(i).getIcon());
        }
    }
}
