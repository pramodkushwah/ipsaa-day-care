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
    inquiryDetails: any;
    newInquiry: number;
    tab: string;
    inquiryDisable: boolean;
    callBackDisposition: any;
    callBackNumber: number;
    callBackDate: string;
    callBackTime: string;
    callBackComment: string;
    log = {
        callBack: '',
        callBackTime: '',
        callBackDate: '',
        callBackNumber: '',
        callDisposition: '',
        comment: '',
    };


    constructor(
        private fb: FormBuilder,
        private adminService: AdminService,
        private alertService: AlertService
    ) { }

    @Input() set inquiryId(inquiryId: any) {
        this.inquiryForm = this.inquiryDetialForm();
        this.newInquiry = inquiryId;
        if (inquiryId) {
            this.loadInquiry(inquiryId);
        } else {
            // this.getInquiryDetials({});
        }
    }
    @Input() set currentTab(currentTab: any) {
        this.tab = currentTab;

    }

    ngOnInit() {
        this.getCenter();
        this.getPrograms();
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
            centerId: [''],
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
            groupId: [''],
            groupName: [''],
            hobbies: [''],
            id: [''],
            inquiryDate: [''],
            inquiryNumber: [''],
            inquiryType: [''],
            leadSource: [''],
            motherCompanyName: [''],
            motherEmail: [''],
            motherFirstName: [''],
            motherLastName: [''],
            motherMobile: [''],
            programCode: [''],
            programId: [''],
            programName: [''],
            secondaryNumbers: [''],
            status: [''],
            toTime: [''],
            type: [''],
            whoVisited: [''],


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
            }, (err) => {
                this.alertService.errorAlert(err);
            });
    }



    selctedNumber(callBackNo) {
        this.log.callBackNumber = callBackNo;
    }

    saveForm() {

        console.log(this.newInquiry);

        if (this.log.callDisposition) {
            this.log.callBack = ' ' + this.log.callBackDate + ' ' + this.log.callBackTime + ' ' + 'IST';
            this.inquiryForm.value['log'] = this.log;

            console.log(this.log);
        }





        console.log(this.inquiryForm.value);
        if (this.newInquiry) {
            this.inquiryForm.value['logs'] = this.inquiryDetails;

            this.adminService.updateInquiry(this.inquiryForm.value)
                .subscribe((res: any) => {
                    this.alertService.successAlert('');
                }, (err) => {
                    this.alertService.errorAlert(err);
                });

        } else {

            this.adminService.addNewInquiry(this.inquiryForm.value)
                .subscribe((res: any) => {
                    this.alertService.successAlert('');
                }, (err) => {
                    this.alertService.errorAlert(err);
                });

        }




    }

}
