package uk.ac.cf.spring.nhs.Provider.Controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import uk.ac.cf.spring.nhs.Common.util.NavMenuItem;

@Controller
@RequestMapping("/questionnairehub")
public class QuestionnaireHubController {

    // Hardcoded patient ID for testing purposes
    private final Long hardcodedPatientId = 2L;

    @ModelAttribute("navMenuItems")
    public List<NavMenuItem> navMenuItems() {
        return List.of(
                new NavMenuItem("Patient", "/patientprofile/info", "fa-solid fa-user-check"),
                new NavMenuItem("Set plan", "", "fa-solid fa-book"),
                new NavMenuItem("Appointments", " ", "fa-solid fa-user-check"),
                new NavMenuItem("Questionnaires", "/questionnairehub",
                        "fa-solid fa-book"),
                new NavMenuItem("Patient trends", " ", "fa-solid fa-user-check"),
                new NavMenuItem("Event log", " ", "fa-solid fa-book"),
                new NavMenuItem("Photos", " ", "fa-solid fa-camera"),
                new NavMenuItem("Email history", " ", "fa-solid fa-book"));
    }

    @GetMapping
    public String showQuestionnaireHub(Model model) {
        // Added hardcoded patientId to the model to use it in the view

        // TODO: remove hardcoded patientId

        model.addAttribute("patientId", hardcodedPatientId);
        return "patientprofile/questionnaireHub";
    }
}