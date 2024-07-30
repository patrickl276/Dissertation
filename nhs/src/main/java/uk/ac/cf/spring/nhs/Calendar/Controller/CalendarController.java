package uk.ac.cf.spring.nhs.Calendar.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.cf.spring.nhs.Calendar.Repositories.CalendarRepository;
import uk.ac.cf.spring.nhs.Common.util.DeviceDetector;

import java.util.Calendar;
import java.util.List;

@Controller
public class CalendarController {

    @Autowired
    private CalendarRepository calendarRepository;
    @GetMapping("/calendar")
    public ModelAndView Calendar(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (DeviceDetector.isMobile(request)) {
            modelAndView.setViewName("mobile/Calendar/calendar");
        } else {
            modelAndView.setViewName("desktop/calendar");

        }
        return modelAndView;
    }

    @GetMapping("/mobileaddappt")
    public ModelAndView getMobileAddAppt(){
        ModelAndView modelAndView = new ModelAndView("mobile/Calendar/addappointment");
        return modelAndView;
    }
}