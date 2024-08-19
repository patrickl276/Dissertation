package uk.ac.cf.spring.nhs.UserQuestionnaire.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.cf.spring.nhs.Questionnaire.Model.Questionnaire;
import uk.ac.cf.spring.nhs.Security.AuthenticationInterface;
import uk.ac.cf.spring.nhs.Security.CustomUserDetails;
import uk.ac.cf.spring.nhs.UserQuestion.Service.UserQuestionService;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Model.UserQuestionnaire;
import uk.ac.cf.spring.nhs.UserQuestionnaire.Service.UserQuestionnaireService;

@RestController
@RequestMapping("/api/userQuestionnaires")
public class UserQuestionnaireController {

    @Autowired
    private UserQuestionnaireService userQuestionnaireService;

    @Autowired
    private AuthenticationInterface authenticationFacade;

    @Autowired
    private UserQuestionService userQuestionService;

    // Helper method to get the current user's ID
    private Long getCurrentUserId() {
        Object principal = authenticationFacade.getAuthentication().getPrincipal();
        return ((CustomUserDetails) principal).getUserId();
    }

    /**
     * Retrieves a list of user questionnaires for the authenticated user.
     *
     * @return a list of user questionnaires
     */
    @GetMapping("/user")
    public ResponseEntity<List<UserQuestionnaire>> getUserQuestionnaires() {
        Long userID = getCurrentUserId();
        List<UserQuestionnaire> userQuestionnaires = userQuestionnaireService.getUserQuestionnaires(userID);
        return ResponseEntity.ok(userQuestionnaires);
    }

    /**
     * Retrieves a list of completed user questionnaires for the authenticated user.
     *
     * @return a list of completed user questionnaires
     */
    @GetMapping("/user/completed")
    public ResponseEntity<List<UserQuestionnaire>> getCompletedUserQuestionnaires() {
        Long userID = getCurrentUserId();
        List<UserQuestionnaire> userQuestionnaires = userQuestionnaireService.getCompletedUserQuestionnaires(userID);
        return ResponseEntity.ok(userQuestionnaires);
    }

    /**
     * Retrieves a list of incomplete user questionnaires for the authenticated
     * user.
     *
     * @return a ResponseEntity containing a list of incomplete user questionnaires
     */
    @GetMapping("/user/incomplete")
    public ResponseEntity<List<UserQuestionnaire>> getIncompleteUserQuestionnaires() {
        Long userID = getCurrentUserId();
        List<UserQuestionnaire> userQuestionnaires = userQuestionnaireService.getIncompleteUserQuestionnaires(userID);
        return ResponseEntity.ok(userQuestionnaires);
    }

    /**
     * Retrieves a user questionnaire for the authenticated user and questionnaire
     * ID.
     *
     * @param questionnaireId the ID of the questionnaire
     * @return a ResponseEntity containing the user questionnaire, or a 404 response
     *         if not found
     */
    @GetMapping("/user/questionnaire/{questionnaireId}")
    public ResponseEntity<UserQuestionnaire> getUserQuestionnaire(@PathVariable Long questionnaireId) {
        Long userID = getCurrentUserId();
        Optional<UserQuestionnaire> userQuestionnaire = userQuestionnaireService.getUserQuestionnaire(userID,
                questionnaireId);

        return userQuestionnaire.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user questionnaire for the authenticated user.
     *
     * @param userQuestionnaire the user questionnaire to be created
     * @return the created user questionnaire
     */
    @PostMapping
    public ResponseEntity<UserQuestionnaire> createUserQuestionnaire(@RequestBody UserQuestionnaire userQuestionnaire) {
        Long userID = getCurrentUserId();
        userQuestionnaire.setUserID(userID);
        UserQuestionnaire savedUserQuestionnaire = userQuestionnaireService.saveUserQuestionnaire(userQuestionnaire);
        return ResponseEntity.ok(savedUserQuestionnaire);
    }

    /**
     * Updates an existing user questionnaire for the authenticated user.
     *
     * @param id                the ID of the user questionnaire to update
     * @param userQuestionnaire the updated user questionnaire
     * @return the updated user questionnaire, or a 404 response if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserQuestionnaire> updateUserQuestionnaire(@PathVariable Long id,
            @RequestBody UserQuestionnaire userQuestionnaire) {
        Long userID = getCurrentUserId();
        Optional<UserQuestionnaire> existingUserQuestionnaire = userQuestionnaireService.getUserQuestionnaire(userID,
                id);

        if (existingUserQuestionnaire.isPresent()) {
            Questionnaire existingQuestionnaire = existingUserQuestionnaire.get().getQuestionnaire();
            userQuestionnaire.setQuestionnaire(existingQuestionnaire);
            userQuestionnaire.setUserQuestionnaireId(id);
            userQuestionnaire.setUserID(userID); // Ensure the user ID is set correctly
            UserQuestionnaire updatedUserQuestionnaire = userQuestionnaireService
                    .saveUserQuestionnaire(userQuestionnaire);
            return ResponseEntity.ok(updatedUserQuestionnaire);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a user questionnaire by its ID for the authenticated user.
     *
     * @param id the ID of the user questionnaire to delete
     * @return a ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserQuestionnaire(@PathVariable Long id) {
        Long userID = getCurrentUserId();
        Optional<UserQuestionnaire> existingUserQuestionnaire = userQuestionnaireService.getUserQuestionnaire(userID,
                id);
        if (existingUserQuestionnaire.isPresent()) {
            userQuestionnaireService.deleteUserQuestionnaire(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Handles the saving of user responses for a specific questionnaire without
     * marking it as complete.
     *
     * @param questionnaireId the ID of the questionnaire being saved
     * @param responses       a map of question IDs to user responses
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/save/{questionnaireId}")
    public ResponseEntity<?> saveUserQuestions(
            @PathVariable Long questionnaireId,
            @RequestBody Map<String, String> responses) {
        try {
            userQuestionService.saveResponsesWithoutCompletion(questionnaireId, responses);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving responses");
        }
    }
}