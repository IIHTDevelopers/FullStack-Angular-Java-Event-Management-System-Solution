import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { EventService } from './event.service';
import { Event } from '../model/event.model';

describe('EventService', () => {
    let service: EventService;
    let httpTestingController: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [EventService],
        });
        service = TestBed.inject(EventService);
        httpTestingController = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpTestingController.verify();
    });

    describe('business', () => {
        it('should be created', () => {
            expect(service).toBeTruthy();
        });

        it('should fetch events', () => {
            const events: Event[] = [
                { id: 1, name: 'Event 1', description: 'Description 1', status: true },
                { id: 2, name: 'Event 2', description: 'Description 2', status: false },
            ];

            service.getAllEvents().subscribe((response: any) => {
                expect(response).toEqual(events);
            });

            const req = httpTestingController.expectOne('http://localhost:8081/event-management/events');
            expect(req.request.method).toEqual('GET');
            req.flush(events);
        });

        it('should search events by name', () => {
            const name = 'Event 1';
            const events: Event[] = [
                { id: 1, name: 'Event 1', description: 'Description 1', status: true },
            ];

            service.searchEventsByName(name).subscribe((response: any) => {
                expect(response).toEqual(events);
            });

            const req = httpTestingController.expectOne(`http://localhost:8081/event-management/events/searchByName?name=${name}`);
            expect(req.request.method).toEqual('GET');
            req.flush(events);
        });

        it('should search events by status', () => {
            const status = true;
            const events: Event[] = [
                { id: 1, name: 'Event 1', description: 'Description 1', status: true },
                { id: 3, name: 'Event 3', description: 'Description 3', status: true },
            ];

            service.searchEventsByStatus(status).subscribe((response: any) => {
                expect(response).toEqual(events);
            });

            const req = httpTestingController.expectOne(`http://localhost:8081/event-management/events/searchByStatus?status=${status}`);
            expect(req.request.method).toEqual('GET');
            req.flush(events);
        });

        it('should create a new event', () => {
            const newEvent: Event = {
                name: 'New Event',
                description: 'New Description',
                status: true,
                id: 0
            };

            service.createEvent(newEvent).subscribe((response: any) => {
                expect(response).toEqual(newEvent);
            });

            const req = httpTestingController.expectOne('http://localhost:8081/event-management/events');
            expect(req.request.method).toEqual('POST');
            req.flush(newEvent);
        });

        it('should update an existing event', () => {
            const updatedEvent: Event = {
                id: 1,
                name: 'Updated Event',
                description: 'Updated Description',
                status: true,
            };

            service.updateEvent(updatedEvent.id, updatedEvent).subscribe((response: any) => {
                expect(response).toEqual(updatedEvent);
            });

            const req = httpTestingController.expectOne(`http://localhost:8081/event-management/events/${updatedEvent.id}`);
            expect(req.request.method).toEqual('PUT');
            req.flush(updatedEvent);
        });

        it('should delete an event', () => {
            const eventId = 1;

            service.deleteEvent(eventId).subscribe((response: any) => {
                expect(response).toBeUndefined();
            });

            const req = httpTestingController.expectOne(`http://localhost:8081/event-management/events/${eventId}`);
            expect(req.request.method).toEqual('DELETE');
            req.flush({});
        });
    });
});
