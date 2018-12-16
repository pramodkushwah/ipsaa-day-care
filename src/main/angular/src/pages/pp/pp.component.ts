import { Component, OnInit } from '@angular/core';
import { ParentService } from '../../providers/parentPotel/parent.service';
import { User } from '../../providers/user/user';

@Component({
  selector: 'app-pp',
  templateUrl: './pp.component.html',
  styleUrls: ['./pp.component.css']
})
export class PpComponent implements OnInit {


  constructor(private userService: User) { }

  ngOnInit() {
  }

  onLogout() {
    this.userService.logout();
  }

}

