package uk.ac.cf.spring.nhs.Widget.Factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.ac.cf.spring.nhs.Widget.Model.Widget;

public class WidgetFactoryUnitTests {

    private WidgetFactory widgetFactory;

    @BeforeEach
    public void setUp() {
        widgetFactory = new WidgetFactory();
    }

    @Test
    public void testCreateTaskCompletionWidget() {
        Widget widget = widgetFactory.createWidget("task-completion-widget");
        assertNotNull(widget);
        assertTrue(widget insatnceof TaskCompletionWidget);
        assertEquals("task-completion-widget", widget.getId());
        assertEquals("Task Completion tracker", widget.getTitle());
    }

    @Test
    public void testCreateUnknownWidget() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            widgetFactory.createWidget("unknown");
        });

        String expectedMessage = "Unknown widget type";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void testCreateAppointmentWidget() {
        Widget widget = widgetFactory.createWidget("appointment-widget");
        assertNotNull(widget);
        assertTrue(widget instanceof AppointmentWidget);
        assertEquals("appointment-widget", widget.getId());
        assertEquals("Appointment tracker", widget.getTitle());
    }
}
