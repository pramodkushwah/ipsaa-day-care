import { Component, OnInit, OnDestroy } from '@angular/core';
import { AlertService } from '../providers/alert/alert.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'app';
  showLoader: boolean;
  constructor(public alertService: AlertService) {

  }

  ngOnInit () {
    this.alertService.loading.asObservable().subscribe((val: boolean) => {
      this.showLoader = val;
    });
  }

  ngOnDestroy() {
    this.alertService.loading.unsubscribe();
  }
}
