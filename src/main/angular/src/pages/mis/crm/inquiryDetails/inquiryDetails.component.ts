import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';


@Component({
    selector: 'app-inquirydetails',
    templateUrl: './inquiryDetails.component.html',
    styleUrls: ['./inquiryDetails.component.css']
})
export class InquiryDetailsComponent implements OnInit {


    selectedInquiryDetials: any;
    logvalidation: true;
    inquiryForm: FormGroup;
    leadSources = [
        'BUILDING',
        'CORPORATE',
        'ADVERTISEMENT',
        'WEBSITE',
        'REFERENCE',
        'NEWSPAPER',
        'SIGNBOARDS',
        'FACEBOOK',
        'ADWORD',
        'ORGANIC',
        'OTHERS'];
    inquiryTypes = [
        'Web',
        'Walkin',
        'Call',
        'Email',
        'Newspaper'];
    dispositions = [
        'NewInquiry',
        'Followup',
        'Callback',
        'ParentMessage',
        'Enrolled',
        'Drop',
        'NotInterested',
        'Revisit'
    ];
    inquiryNumbers = [];
    centers: Array<any>;
    programs: Array<any>;
    groups = [];
    selectedCenter = {};
    inquiryDetails: any = [];
    newInquiry: number;
    tab: string;
    inquiryDisable: boolean;
    // callBackDisposition: any;
    // callBackNumber: number;
    // callBackDate: string;
    // callBackTime: string;
    // callBackComment: string;
    log = {
        callBack: '',
        callBackTime: '',
        callBackDate: '',
        callBackNumber: '',
        callDisposition: '',
        comment: '',
    };
    today: Date;
    time: any;


    constructor(
        private fb: FormBuilder,
        private adminService: AdminService,
        private alertService: AlertService
    ) { }

    @Input() set inquiryId(inquiryId: any) {
        this.today = new Date();
        // this.today.setDate(this.today.getDate());

        this.inquiryForm = this.inquiryDetialForm();
        this.newInquiry = inquiryId;
        if (inquiryId) {
            this.loadInquiry(inquiryId);
        } else {
            this.inquiryForm.get('inquiryDate').setValue('2018-12-21');
            this.inquiryForm.get('toTime').setValue(this.today.getHours() + ':' + this.today.getMinutes());
            this.inquiryForm.get('fromTime').setValue(this.today.getHours() + ':' + this.today.getMinutes());
        }
    }
    @Input() set currentTab(currentTab: any) {
        this.tab = currentTab;

    }

    ngOnInit() {
        this.getCenter();
        this.getPrograms();
        const todayDate = new Date().toISOString().slice(0, 10);
console.log(todayDate);

        // this.today.setDate(this.today.getDate());
        // console.log(this.today.setDate(this.today.getDate()));
    }

    getCenter() {
        this.adminService.getProgramCenter()
            .subscribe((res) => {
                this.centers = res;
            });
    }

    getPrograms() {
        this.adminService.getPrograms()
            .subscribe((res) => {
                this.programs = res;
                this.groups = res[0].groups;
            });
    }




    inquiryDetialForm() {

        return this.fb.group({
            centerCode: [''],
            centerId: [0],
            centerName: [''],
            childDob: [''],
            childFirstName: [''],
            childLastName: [''],
            fatherCompanyName: [''],
            address: this.fb.group({
                address: [''],
                addressType: [''],
                city: [''],
                phone: [''],
                state: [''],
                zipcode: [''],
            }),
            fatherEmail: [''],
            fatherFirstName: [''],
            fatherLastName: [''],
            fatherMobile: [''],
            feeOffer: [''],
            fromTime: [''],
            groupId: [0],
            groupName: [''],
            hobbies: [''],
            id: [null],
            inquiryDate: [{ value: this.today.toISOString().slice(0, 10), disabled: false }],
            inquiryNumber: [''],
            inquiryType: [''],
            leadSource: [''],
            motherCompanyName: [''],
            motherEmail: [''],
            motherFirstName: [''],
            motherLastName: [''],
            motherMobile: [''],
            programCode: [''],
            programId: [0],
            programName: [''],
            secondaryNumbers: [''],
            status: ['NewInquiry'],
            toTime: [''],
            type: [''],
            whoVisited: [''],
            log: []
        });
    }

    getInquiryDetials(inquiry) {

        this.inquiryForm = this.inquiryDetialForm();
        this.inquiryForm.patchValue(inquiry);
        const address = <FormGroup>this.inquiryForm.controls.address;
        address.controls.address.patchValue(inquiry.address.address);
    }

    loadInquiry(InquiryId) {
        this.adminService.loadInquiryDetials(InquiryId)
            .subscribe((res: any) => {
                this.selectedInquiryDetials = res;
                this.getInquiryDetials(res);
                this.inquiryNumbers.push(res.fatherMobile);
                this.inquiryNumbers.push(res.motherMobile);
                this.inquiryDetails = res.logs;
            });
    }



    selctedNumber(callBackNo) {
        this.log.callBackNumber = callBackNo;
    }

    saveForm() {


        this.log.callBack = ' ' + this.log.callBackDate + ' ' + this.log.callBackTime + ' ' + 'IST';
        this.inquiryForm.controls['log'].patchValue(this.log);

console.log(this.log);

        if (this.newInquiry) {
            this.inquiryForm.value['logs'] = this.inquiryDetails;

            this.adminService.updateInquiry(this.inquiryForm.value)
                .subscribe((res: any) => {
                    this.alertService.successAlert('');
                    this.inquiryDetails.push(this.log);
                });

        } else {

            this.centers.forEach(element => {
                if (element.code === this.inquiryForm.controls.centerCode.value) {
                    this.inquiryForm.controls.centerId.setValue(element.id);
                    this.inquiryForm.controls.centerName.setValue(element.name);
                }
            });

            this.programs.forEach(element => {
                if (element.code === this.inquiryForm.controls.programCode.value) {
                    this.inquiryForm.controls.programId.setValue(element.id);
                    this.inquiryForm.controls.programName.setValue(element.name);
                }
            });

            this.groups.forEach(element => {
                if (element.id === Number(this.inquiryForm.controls.groupId.value)) {
                    this.inquiryForm.controls.groupName.setValue(element.name);

                }
            });
            this.adminService.addNewInquiry(this.inquiryForm.value)
                .subscribe((res: any) => {
                    this.inquiryDetails.push(this.log);
                    this.alertService.successAlert('New Inquiry Add Succesfuly');
                });

        }




    }

}
