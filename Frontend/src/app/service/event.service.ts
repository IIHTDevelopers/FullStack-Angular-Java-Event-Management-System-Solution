import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Event } from '../model/event.model';

@Injectable({
    providedIn: 'root'
})
export class EventService {
    private apiUrl = 'http://localhost:8081/event-management/events';

    constructor(private http: HttpClient) { }

    getAllEvents(): Observable<Event[]> {
        return this.http.get<Event[]>(this.apiUrl);
    }

    getEventById(id: number): Observable<Event> {
        return this.http.get<Event>(`${this.apiUrl}/${id}`);
    }

    createEvent(event: Event): Observable<Event> {
        return this.http.post<Event>(this.apiUrl, event);
    }

    updateEvent(id: number, event: Event): Observable<Event> {
        return this.http.put<Event>(`${this.apiUrl}/${id}`, event);
    }

    deleteEvent(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }

    searchEventsByName(name: string): Observable<Event[]> {
        return this.http.get<Event[]>(`${this.apiUrl}/searchByName?name=${name}`);
    }

    searchEventsByStatus(status: boolean): Observable<Event[]> {
        return this.http.get<Event[]>(`${this.apiUrl}/searchByStatus?status=${status}`);
    }
}
