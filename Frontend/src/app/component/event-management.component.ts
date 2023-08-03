import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Event } from '../model/event.model';
import { EventService } from '../service/event.service';

@Component({
    selector: 'app-event-management',
    templateUrl: './event-management.component.html',
    styleUrls: []
})
export class EventManagementComponent implements OnInit {
    events: Event[] = [];
    eventForm!: FormGroup;
    searchForm!: FormGroup;

    constructor(private fb: FormBuilder, private eventService: EventService) { }

    get f() {
        return this.eventForm.controls;
    }

    ngOnInit(): void {
        this.eventForm = this.fb.group({
            id: [],
            name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
            description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(200)]],
            status: [true, Validators.required]
        });

        this.loadEvents();
        this.initSearchForm();
    }

    private initSearchForm(): void {
        this.searchForm = this.fb.group({
            searchName: [''],
            searchStatus: ['']
        });
    }

    loadEvents(): void {
        this.eventService.getAllEvents().subscribe(
            (events: Event[]) => {
                this.events = events;
            },
            (error) => {
                console.error('Error fetching events:', error);
            }
        );
    }

    onEdit(event: Event): void {
        this.eventForm.patchValue(event);
    }

    onDelete(event: Event): void {
        this.eventService.deleteEvent(event.id).subscribe(
            () => {
                this.loadEvents();
            },
            (error) => {
                console.error('Error deleting event:', error);
            }
        );
    }

    onSubmit(): void {
        if (this.eventForm.invalid) {
            this.eventForm.markAllAsTouched();
            return;
        }

        const updatedEvent: Event = this.eventForm.value;
        if (!updatedEvent.id) {
            if (!updatedEvent.name || !updatedEvent.description) {
                return;
            }
        }

        if (updatedEvent.id) {
            this.eventService.updateEvent(updatedEvent.id, updatedEvent).subscribe(
                (event: Event) => {
                    console.log('Event updated successfully!', event);
                    this.loadEvents();
                    this.resetForm();
                },
                (error) => {
                    console.error('Error updating event:', error);
                }
            );
        } else {
            this.eventService.createEvent(updatedEvent).subscribe(
                (event: Event) => {
                    console.log('Event created successfully!', event);
                    this.loadEvents();
                    this.resetForm();
                },
                (error) => {
                    console.error('Error creating event:', error);
                }
            );
        }
    }

    resetSearchForm(): void {
        this.searchForm.reset();
        this.loadEvents();
    }
    
    onSearch(): void {
        const formValues = this.searchForm.getRawValue();
        const searchName = formValues.searchName;
        const searchStatus = formValues.searchStatus;

        if (searchName && searchStatus !== null) {
            this.eventService.searchEventsByName(searchName).subscribe(
                (events: Event[]) => {
                    this.events = events;
                    console.log(this.events);
                },
                (error) => {
                    console.error('Error searching events by name:', error);
                }
            );
        } else if (searchName) {
            this.eventService.searchEventsByName(searchName).subscribe(
                (events: Event[]) => {
                    this.events = events;
                    console.log(this.events);
                },
                (error) => {
                    console.error('Error searching events by name:', error);
                }
            );
        } else if (searchStatus !== null) {
            this.eventService.searchEventsByStatus(searchStatus).subscribe(
                (events: Event[]) => {
                    this.events = events;
                },
                (error) => {
                    console.error('Error searching events by status:', error);
                }
            );
        } else {
            this.loadEvents();
        }
    }


    resetForm(): void {
        this.eventForm.reset({
            name: '',
            description: '',
            status: true
        });
    }
}
