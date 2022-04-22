import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../shared/services/authentication.service';
@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  userName : string ;

  constructor( private authService: AuthenticationService) { 
    this.userName = authService.userName;
  }

  ngOnInit(): void {
  }

  clickLogOut(){
    this.authService.loggOut();
  }
}
