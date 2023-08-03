import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { EventManagementComponent } from './event-management.component';
import { EventService } from '../service/event.service';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

describe('EventManagementComponent', () => {
    let component: EventManagementComponent;
    let fixture: ComponentFixture<EventManagementComponent>;
    let eventService: EventService;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule({
            declarations: [EventManagementComponent],
            imports: [ReactiveFormsModule, HttpClientModule],
            providers: [EventService],
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(EventManagementComponent);
        component = fixture.componentInstance;
        eventService = TestBed.inject(EventService);
        fixture.detectChanges();
    });

    describe('business', () => {
        it('should create the EventManagementComponent', () => {
            expect(component).toBeTruthy();
        });

        it('should declare eventForm & searchForm', () => {
            expect(component.eventForm).toBeDefined();
            expect(component.searchForm).toBeDefined();
        });

        it('should initialize the eventForm', () => {
            const eventForm = {
                id: null,
                name: '',
                description: '',
                status: true,
            };
            expect(component.eventForm.value).toEqual(eventForm);
        });

        it('should validate the name field in the eventForm', () => {
            const nameControl = component.eventForm.get('name');
            nameControl?.setValue('Valid Name');
            expect(nameControl?.valid).toBeTruthy();

            nameControl?.setValue('');
            expect(nameControl?.invalid).toBeTruthy();

            nameControl?.setValue('Pr');
            expect(nameControl?.invalid).toBeTruthy();
        });

        it('should validate the description field in the eventForm', () => {
            const descriptionControl = component.eventForm.get('description');
            descriptionControl?.setValue('Valid Description');
            expect(descriptionControl?.valid).toBeTruthy();

            descriptionControl?.setValue('');
            expect(descriptionControl?.invalid).toBeTruthy();

            descriptionControl?.setValue('Abc');
            expect(descriptionControl?.invalid).toBeTruthy();
        });

        it('should have name, description, and status fields in the eventForm', () => {
            const nameInput: DebugElement = fixture.debugElement.query(By.css('input[formControlName="name"]'));
            const descriptionTextarea: DebugElement = fixture.debugElement.query(By.css('textarea[formControlName="description"]'));
            const statusSelect: DebugElement = fixture.debugElement.query(By.css('select[formControlName="status"]'));

            expect(nameInput).toBeTruthy();
            expect(descriptionTextarea).toBeTruthy();
            expect(statusSelect).toBeTruthy();

            const eventForm: FormGroup = component.eventForm;
            expect(eventForm.get('name')).toBeTruthy();
            expect(eventForm.get('description')).toBeTruthy();
            expect(eventForm.get('status')).toBeTruthy();
        });

        it('should have name and status fields in the searchForm', () => {
            const nameInput: DebugElement = fixture.debugElement.query(By.css('input[formControlName="searchName"]'));
            const statusSelect: DebugElement = fixture.debugElement.query(By.css('select[formControlName="searchStatus"]'));

            expect(nameInput).toBeTruthy();
            expect(statusSelect).toBeTruthy();

            const searchForm: FormGroup = component.searchForm;
            expect(searchForm.get('searchName')).toBeTruthy();
            expect(searchForm.get('searchStatus')).toBeTruthy();
        });

        it('should display table headings: Name, Description, Status, and Actions', () => {
            const tableHeaders: DebugElement[] = fixture.debugElement.queryAll(By.css('thead th'));
            const expectedHeadings = ['Name', 'Description', 'Status', 'Actions'];
            expect(tableHeaders.length).toBe(expectedHeadings.length);
            for (let i = 0; i < tableHeaders.length; i++) {
                expect(tableHeaders[i].nativeElement.textContent).toContain(expectedHeadings[i]);
            }
        });

        it('should validate the status field in the eventForm', () => {
            const statusControl = component.eventForm.get('status');
            statusControl?.setValue(true);
            expect(statusControl?.valid).toBeTruthy();

            statusControl?.setValue('');
            expect(statusControl?.invalid).toBeTruthy();
        });

        it('should disable the submit button when the eventForm is invalid', () => {
            component.eventForm.patchValue({
                name: 'Valid Name',
                description: 'Valid Description',
                status: true,
            });
            fixture.detectChanges();

            const submitButton: HTMLButtonElement | null = fixture.nativeElement.querySelector(
                'button[type="submit"]'
            );
            expect(submitButton?.disabled).toBe(false);

            const nameControl = component.eventForm.get('name');
            nameControl?.setValue('');
            fixture.detectChanges();

            expect(submitButton?.disabled).toBe(true);
        });

        it('should enable the submit button when the eventForm is valid', () => {
            component.eventForm.patchValue({
                name: 'Valid Name',
                description: 'Valid Description',
                status: true,
            });
            fixture.detectChanges();

            const submitButton: HTMLButtonElement | null = fixture.nativeElement.querySelector(
                'button[type="submit"]'
            );
            expect(submitButton?.disabled).toBe(false);
        });

        it('should call onSubmit() when the form is submitted with valid data', () => {
            jest.spyOn(eventService, 'createEvent');
            component.eventForm.patchValue({
                name: 'Valid Name',
                description: 'Valid Description',
                status: true,
            });
            fixture.detectChanges();

            const submitButton: HTMLButtonElement | null = fixture.nativeElement.querySelector(
                'button[type="submit"]'
            );
            submitButton?.click();
            fixture.detectChanges();

            expect(eventService.createEvent).toHaveBeenCalled();
        });

        it('should initialize the searchForm', () => {
            const searchForm: FormGroup = component.searchForm;
            const expectedSearchFormValues = {
                searchName: '',
                searchStatus: '',
            };
            expect(searchForm.value).toEqual(expectedSearchFormValues);
        });

        it('should validate the searchName field in the searchForm', () => {
            const searchNameControl = component.searchForm.get('searchName');
            searchNameControl?.setValue('Valid Search Name');
            expect(searchNameControl?.valid).toBeTruthy();
        });

        it('should validate the searchStatus field in the searchForm', () => {
            const searchStatusControl = component.searchForm.get('searchStatus');
            searchStatusControl?.setValue(true);
            expect(searchStatusControl?.valid).toBeTruthy();
        });

        it('should have name and status fields in the searchForm template', () => {
            const nameInput: DebugElement = fixture.debugElement.query(By.css('input[formControlName="searchName"]'));
            const statusSelect: DebugElement = fixture.debugElement.query(By.css('select[formControlName="searchStatus"]'));

            expect(nameInput).toBeTruthy();
            expect(statusSelect).toBeTruthy();
        });
    });
});
