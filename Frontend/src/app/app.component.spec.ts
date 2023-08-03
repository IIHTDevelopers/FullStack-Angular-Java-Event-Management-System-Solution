import { TestBed, ComponentFixture } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { EventManagementComponent } from './component/event-management.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

describe('AppComponent', () => {
    let fixture: ComponentFixture<AppComponent>;
    let component: AppComponent;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [AppComponent, EventManagementComponent],
            imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule]
        }).compileComponents();

        fixture = TestBed.createComponent(AppComponent);
        component = fixture.componentInstance;
    });

    describe('business', () => {
        it('should create the app', () => {
            expect(component).toBeTruthy();
        });

        it('should display the correct title text Event Management App in h1 tag', () => {
            fixture.detectChanges();
            const titleElement: HTMLElement = fixture.nativeElement.querySelector('h1');
            expect(titleElement.textContent).toContain('Event Management App');
        });
    });
});
