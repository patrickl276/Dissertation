package uk.ac.cf.spring.nhs.Event.Entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import uk.ac.cf.spring.nhs.Symptom.Entity.Symptom;
import uk.ac.cf.spring.nhs.Treatment.Entity.Treatment;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EventID")
    private Long id;

    @Column(name = "EventDate", nullable = false)
    private LocalDate date;

    @Column(name = "EventDuration")
    private Integer duration;

    @Column(name = "UserID")
    private long userId;

    @OneToMany(mappedBy = "relatedEntityId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "relatedEntityTypeFilter", condition = "related_entity_type = 'Event'")
    private Set<Symptom> symptoms;

    @OneToMany(mappedBy = "relatedEntityId", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "relatedEntityTypeFilter", condition = "related_entity_type = 'Event'")
    private Set<Treatment> treatments;

    protected Event() {}

    public Event(LocalDate date, Integer duration, long userId) {
        this.date = date;
        this.duration = duration;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(Set<Symptom> symptoms) {
        this.symptoms = symptoms;
    }

    public Set<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(Set<Treatment> treatments) {
        this.treatments = treatments;
    }
}
