import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../providers/localstorage/storage';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pp',
  templateUrl: './pp.component.html',
  styleUrls: ['./pp.component.css']
})
export class PpComponent implements OnInit {


  constructor(
    private storage: StorageService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  onLogout() {
    this.storage.clearData();
    this.router.navigate(['/login']);

    }
}

