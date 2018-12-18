import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { AlertService } from '../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';
import { Router } from '@angular/router';

@Component({
  selector: 'app-fee',
  templateUrl: './fee.component.html',
  styleUrls: ['./fee.component.css']
})
export class FeeComponent implements OnInit {
  studentfeeledger: any;
  studentfeeledgerId: number;
  fee: any;
  payment: any;
  checkoutDetails: any;
  selectedStudent: string;
  studentId: number;
  parent: any = [];
  details: any;
  feeledger: any;
  disabledDownloadFeeReceipt: any;
  disabledDownloadFeeSlip: any;
  myDetailId: any;
  ipsaaClub = false;
  constructor(
    private router: Router,
    private parentService: ParentService,
    private alertService: AlertService,
  ) { }

  ngOnInit() {
    this.getStudents();
    this.getMyParentDeatil();
  }
  getStudents() {
    this.parentService.getStudentDetails()
      .subscribe((res: any) => {
        console.log(res);
        this.parent = res;
        this.studentId = this.parent[0].id;
        this.getStudentsDetails(this.studentId);
        this.getStudentFeeledgerDetails(this.studentId);
      });
  }

  getStudentsDetails(std_id) {
    this.parentService.getStudentFee(std_id)
      .subscribe((res: any) => {
        this.fee = res;
        if (this.fee.program.id === 72932732558618) {
          this.ipsaaClub = true;
        } else {
          this.ipsaaClub = false;
        }
      });
  }

  onStudentChange(id) {
    this.getStudentsDetails(id);
    this.getStudentFeeledgerDetails(id);
  }

  getStudentFeeledgerDetails(std_id) {
    this.parentService.getStudentFeeledger(std_id)
      .subscribe((res: any) => {
        this.studentfeeledger = res;
        this.studentfeeledgerId = res.id;
        this.getFullBillingDetails();
      });
  }

  getMyParentDeatil() {
    this.parentService.getMyDetails()
      .subscribe((res: any) => {
        this.myDetailId = res.id;
      });
  }

  getFullBillingDetails() {
    this.parentService.hdfcCheckout(this.studentfeeledgerId, this.myDetailId)
      .subscribe((res: any) => {
        this.checkoutDetails = res;
      });
  }

  checkout() {
    // $("#checkout")
    // .attr('action',$scope.checkoutDetails.checkoutDetailsUrl+'/'+$scope.studentfeeledger.id + '/' + $scope.myDetails.id)
    // .attr('target','_blank')
    // .attr('method','get')
    // .submit();
    // this.router.navigate(['/pp/checkout/ ', { p1: this.studentfeeledger.id, p2: this.myDetailId }]);

  }



  slipDownload(id) {
    this.parentService.downloadFeeSlip(id)
      .subscribe((res: any) => {
        const blob = new Blob([res.bytes], {
        });
        FileSaver.saveAs(blob, res.fileName);
      });
  }


  receiptDownload(id) {
    this.parentService.downloadFeeReceipt(id)
      .subscribe((res: any) => {
        const blob = new Blob([res.bytes], {
        });
        FileSaver.saveAs(blob, res.fileName);
      });
  }

}
