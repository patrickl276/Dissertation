import { treatmentText, symptomNameText, symptomSeverityText } from '../textMappings.js';

class EventCard extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({ mode: 'open' });
    }

    static get observedAttributes() {
        return ['event-data'];
    }

    attributeChangedCallback(name, oldValue, newValue) {
        if (name === 'event-data') {
            try {
                this.event = JSON.parse(newValue);
                this.render(); // Re-render when the event data changes
            } catch (e) {
                console.error('Invalid JSON passed to event-data attribute:', e);
            }
        }
    }

    connectedCallback() {
        if (this.event) {
            this.render();
        }
    }

    render() {
        const event = this.event;
        const date = event.date;

        // Use defaults for missing data
        const symptoms = Array.isArray(event.symptoms) ? event.symptoms : [];
        const treatments = Array.isArray(event.treatments) ? event.treatments : [];

        this.shadowRoot.innerHTML = `
            <link rel="stylesheet" href="/css/diary/eventCard.css">
            <div class="event">
                <div class="event-header">
                    <h3>${date || 'Date not available'}</h3>
                    <button class="delete-event">&times;</button>
                </div>
                <div class="event-body">
                    <div class="event-duration">
                        <h4>Duration</h4>
                          <p>${event.duration || 'N/A'} days</p>
                    </div>
                    <div class="event-symptoms">
                        <h4>Symptoms</h4>
                        <ul>
                            ${symptoms.length > 0
                                ? symptoms.map(symptom => `<li>${symptomNameText[symptom.name] || 'N/A'} - ${symptomSeverityText[symptom.severity] || 'N/A'}</li>`).join('')
                                : '<li>No symptoms reported</li>'
                            }
                        </ul>
                    </div>
                    <div class="event-treatments">
                        <h4>Treatments</h4>
                        <ul>
                            ${treatments.length > 0
                                ? treatments.map(treatment => `<li>${treatmentText[treatment.type] || 'N/A'}</li>`).join('')
                                : '<li>No treatments reported</li>'
                            }
                        </ul>
                    </div>
                </div>
            </div>
        `;

        // Add delete event listener
        this.shadowRoot.querySelector('.delete-event').addEventListener('click', () => this.removeEvent());
    }

    removeEvent() {
        // Trigger an event to inform the parent that the event should be removed (for example, to remove it from the backend)
        const eventRemove = new CustomEvent('removeEvent', {
            detail: { eventId: this.event.id }, // Pass the event ID to identify which event to delete
            bubbles: true,
            composed: true
        });
        this.dispatchEvent(eventRemove);

        // Remove the element from the DOM
        this.remove();
    }
}

customElements.define('event-card', EventCard);
