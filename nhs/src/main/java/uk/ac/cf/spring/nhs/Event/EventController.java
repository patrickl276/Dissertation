package uk.ac.cf.spring.nhs.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.cf.spring.nhs.Common.util.NavMenuItem;
import uk.ac.cf.spring.nhs.Diary.Entity.Event;
import uk.ac.cf.spring.nhs.Diary.Service.EventService;
import uk.ac.cf.spring.nhs.Security.CustomUserDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/diary")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public String events(Model model,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        long userId = userDetails.getUserId();
        List<Event> events = eventService.getEventsByUserId(userId);
        model.addAttribute("events", events);
        return "diary/events";
    }

    @PostMapping("/events")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createEvent(
            @ModelAttribute Event event,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        long userId = userDetails.getUserId();
        event.setUserId(userId);
        Event savedEvent = eventService.saveEvent(event);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Event created successfully");
        response.put("event", savedEvent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @ModelAttribute("navMenuItems")
    public List<NavMenuItem> navMenuItems() {
        return List.of(
                new NavMenuItem("Diary", "/diary", "fa-solid fa-book"),
                new NavMenuItem("Check-in", "/diary/checkin", "fa-solid fa-user-check"),
                new NavMenuItem("Photos", "/diary/photos", "fa-solid fa-camera"),
                new NavMenuItem("Events", "/diary/events", "fa-solid fa-receipt")
        );
    }
}
