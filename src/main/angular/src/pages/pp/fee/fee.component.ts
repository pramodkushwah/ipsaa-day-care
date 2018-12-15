import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../providers/admin/admin.service';
import { ParentService } from '../../../providers/parentPotel/parent.service';
import { AlertService } from '../../../providers/alert/alert.service';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-fee',
  templateUrl: './fee.component.html',
  styleUrls: ['./fee.component.css']
})
export class FeeComponent implements OnInit {
  studentfeeledger: any;
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
  constructor(
    private adminService: AdminService,
    private parentService: ParentService,
    private alertService: AlertService,
  ) { }

  ngOnInit() {
    this.getStudents();
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
      });
  }


  checkout() {

  }



  slipDownload(id) {
    this.parentService.downloadFeeSlip(id)
      .subscribe((res: any) => {
        const blob = new Blob([res.body], {
        });
        FileSaver.saveAs(blob, res.headers.get('fileName'));
      });
  }


  receiptDownload(id) {
    this.parentService.downloadFeeReceipt(id)
      .subscribe((res: any) => {
        const blob = new Blob([res.body], {
        });
        FileSaver.saveAs(blob, res.headers.get('fileName'));
      });
  }

}
