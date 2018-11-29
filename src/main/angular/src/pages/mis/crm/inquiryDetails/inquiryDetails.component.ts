import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { AdminService } from '../../../../providers/admin/admin.service';
import { AlertService } from '../../../../providers/alert/alert.service';


@Component({
    selector: 'app-inquirydetails',
    templateUrl: './inquiryDetails.component.html',
    styleUrls: ['./inquiryDetails.component.css']
})
export class InquiryDetailsComponent implements OnInit {


    selectedInquiryDetials: any;
    induiryForm: FormGroup;
    callBackDisposition: any;
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
    workingInquiry: any;
    inquiryNumbers = ['9017697290', '1769720154', '545454545', '4848598'];
    centers: Array<any>;
    programs: Array<any>;
    groups = [];
    selectedCenter = {};
    inquiryDetails: any ;
    constructor(
        private fb: FormBuilder,
        private adminService: AdminService,
        private alertService: AlertService
    ) { }


    @Input() set inquiryId(inquiryId: any) {
        this.induiryForm = this.inquiryDetialForm();

        if (inquiryId) {
            this.loadInquiry(inquiryId);
        } else {
            // this.getInquiryDetials({});
        }
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
            logs: this.fb.group({
                description: [''],
                id: [''],
                name: [''],
                schoolName: [''],
                securityDeposit: [''],
                callDisposition: [''],
                studentId: [''],
                transportFee: [''],
            }),

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

        this.induiryForm = this.inquiryDetialForm();
        this.induiryForm.patchValue(inquiry);
        const address = <FormGroup>this.induiryForm.controls.address;
        address.controls.address.patchValue(inquiry.address.address);
        // console.log(this.induiryForm.controls.address.value);

    }

    loadInquiry(InquiryId) {
        this.adminService.loadInquiryDetials(InquiryId)
            .subscribe((res: any) => {
                this.selectedInquiryDetials = res;
                this.getInquiryDetials(res);
                this.inquiryDetails = res.logs;
            }, (err) => {
                this.alertService.errorAlert(err);
            });
    }

}
