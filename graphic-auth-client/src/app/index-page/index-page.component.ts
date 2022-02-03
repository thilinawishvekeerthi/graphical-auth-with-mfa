import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { IndexPageService } from './index-page.service';

@Component({
  selector: 'app-index-page',
  templateUrl: './index-page.component.html',
  styleUrls: ['./index-page.component.scss']
})
export class IndexPageComponent implements OnInit {

  userNameFormControl = new FormControl('', [Validators.required]);

  constructor(private indexPageService : IndexPageService,
              private _snackBar: MatSnackBar,
              private router: Router) { }

  ngOnInit(): void {
  }

  nextButtonClick(){
    let userName  = this.userNameFormControl.value;
    this.indexPageService.getUserConfig(userName).subscribe(res=>{
      if(res){
        this.router.navigate(['login-account',btoa(userName) ,res.canvasX, res.canvasY ]);
      }
    },err=>{
      this._snackBar.open("Account does not exist","close",{
        horizontalPosition:"center",
        verticalPosition: "top",
        duration: 4 * 1000,
      });
    });
  }
}
