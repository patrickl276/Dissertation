package uk.ac.cf.spring.nhs.Diary.DTO;

import java.util.ArrayList;
import java.util.List;

public class CheckinFormDTO {
    private String mood;
    private List<SymptomDTO> symptoms;
    private List<MeasurementDTO> measurements;
    private String notes;
    private List<PhotoDTO> photos;

    public CheckinFormDTO() {
        this.symptoms = new ArrayList<>();
        this.measurements = new ArrayList<>();
        this.photos = new ArrayList<>();
    }

    public CheckinFormDTO(String mood, List<SymptomDTO> symptoms, List<MeasurementDTO> measurements, String notes, List<PhotoDTO> photos) {
        this.mood = mood;
        this.symptoms = symptoms != null ? symptoms : new ArrayList<>();
        this.measurements = measurements != null ? measurements : new ArrayList<>();
        this.notes = notes;
        System.out.println("photos: " + photos);
        this.photos = photos != null ? photos : new ArrayList<>();
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public List<SymptomDTO> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<SymptomDTO> symptoms) {
        this.symptoms = symptoms;
    }

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDTO> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "CheckinFormDTO{" +
                "mood='" + mood + '\'' +
                ", symptoms=" + symptoms +
                ", measurements=" + measurements +
                ", notes='" + notes + '\'' +
                ", photos=" + photos +
                '}';
    }
}