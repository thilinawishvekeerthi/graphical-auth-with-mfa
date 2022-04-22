import { AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { fromEvent } from 'rxjs';
import { GraphicLoginService } from './graphic-login.service';
import { AuthenticationService } from '../shared/services/authentication.service';
import { Auth } from '../shared/services/auth';

@Component({
  selector: 'app-graphic-login',
  templateUrl: './graphic-login.component.html',
  styleUrls: ['./graphic-login.component.scss']
})
export class GraphicLoginComponent implements OnInit, AfterViewInit {

  @ViewChild('passwordCanvas') public passwordCanvas? : ElementRef;
  private canvasRenderContext!: CanvasRenderingContext2D;
  totpFormControl = new FormControl('', [Validators.required]);
  public canvasX: number = 0;
  public canvasY: number = 0;
  public numberOfPassPoints:number = 4;
  public imageloading:boolean =true;
  public clickPoints: any[] = [];
  private userName : string ="";
  public userNameEncrypted : string ="";
  private passwordImage:any;
  passPointObj: any;
  private tolerance:number = 10;
  passwordRestButtonEnabled : boolean = false;

  constructor(private loginService : GraphicLoginService,
              private route: ActivatedRoute,
              private _snackBar: MatSnackBar,
              private router: Router,
              private authService : AuthenticationService) { }

  ngAfterViewInit(): void {
    fromEvent(this.passwordCanvas?.nativeElement, 'mousedown').subscribe(res => {
      this.mouseClickFunctions(<MouseEvent>res);
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userName = atob(params['userName']);
      this.canvasX = Number(params['canvasX']);
      this.canvasY = Number(params['canvasY']);
      this.userNameEncrypted = btoa(this.userName);
      this.loginService.getImagePassword(this.userName).subscribe(res=>{
        if(res){
          this.passwordImage =res.file;
          this.initCanvas();
        }
      });
    });
  }

  @HostListener('window:resize', ['$event'])
  initCanvas(){
    (<HTMLCanvasElement>this.passwordCanvas?.nativeElement).width= this.canvasX;
    (<HTMLCanvasElement>this.passwordCanvas?.nativeElement).height= this.canvasY;
      let canvas : HTMLCanvasElement = this.passwordCanvasRef;
    let canvasContent = canvas.getContext('2d');
    this.canvasRenderContext = canvasContent ? canvasContent: new CanvasRenderingContext2D() ;
    if(canvasContent){
      if(this.passwordImage){
        this.setUploadImageFromCache();
      }
    }
  }

  setUploadImageFromCache(){
    let backgroundImage = new Image();
    backgroundImage.src = this.passwordImage;
    backgroundImage.onload =()=>{
    this.canvasRenderContext.drawImage(backgroundImage, 0, 0,this.canvasX, this.canvasY);
      this.drawGrid();
    }
       
  }

  drawGrid(){
    let padding = 0;
    if(this.canvasRenderContext){
      for (var x = 0; x <= this.canvasX; x += 25) {
        this.canvasRenderContext.moveTo(0.3 + x + padding, padding);
        this.canvasRenderContext.lineTo(0.3 + x + padding, this.canvasY + padding);
    }

    for (var x = 0; x <= this.canvasY; x += 25) {
      this.canvasRenderContext.moveTo(padding, 0.3 + x + padding);
      this.canvasRenderContext.lineTo(this.canvasX + padding, 0.3 + x + padding);
    }
      this.canvasRenderContext.strokeStyle = 'white';
      this.canvasRenderContext.strokeStyle = "rgba(255, 255, 255, 0.05)";
      this.canvasRenderContext.stroke();
    }
    this.imageloading =false;
  }

  mouseClickFunctions(res: MouseEvent){
    if(!this.twofactorEnable){
      let event : MouseEvent = (<MouseEvent>res);
      this.passPointObj = {x:event.offsetX , y:event.offsetY};
      this.clickPoints.push(this.passPointObj);
      this.renderClickPoints(event);
    }
  }

  renderClickPoints(event : MouseEvent){
    this.canvasRenderContext.beginPath();
    this.canvasRenderContext.arc(event.offsetX, event.offsetY, this.tolerance, 50, 0, true);
    this.canvasRenderContext.fillStyle ="white";
    this.canvasRenderContext.fill();
    this.canvasRenderContext.restore();
    setTimeout(()=>{
      this.setUploadImageFromCache();
      this.canvasRenderContext.closePath();
    },300);
  }

  
  get passwordCanvasRef (): HTMLCanvasElement{
    return  (<HTMLCanvasElement>this.passwordCanvas?.nativeElement);
  }

  public twofactorEnable : boolean =false;
  loginButtonClick( totp? :boolean ){
    let authRequest = {userName : this.userName, passWord : this.loginService.generatePassword(this.clickPoints), totp : null };
    if(totp){
      authRequest.totp = this.totpFormControl.value;
    }
    this.loginService.Authenticate(authRequest).subscribe(res=>{
      if(res && res.two_factor_auth != undefined){
        this.twofactorEnable = res.two_factor_auth;
        this.passwordRestButtonEnabled = false;
      }else if(res){
        this.setAuthenticationDetails(res);
        this._snackBar.open("Authentication Success !!","close",{
          horizontalPosition:"left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
        this.router.navigate(['user-profile']);
      }
    }, err=>{
      this.resetPoints();
      this._snackBar.open("Authentication Fails","close",{
        horizontalPosition:"left",
        verticalPosition: "top",
        duration: 4 * 1000,
      });
      this.passwordRestButtonEnabled = true;
    });
  }
  private setAuthenticationDetails(res: any) {
    let auth = new Auth();
    auth.acessToken = res.access_token;
    auth.refreshToken = res.refresh_token;
    this.authService.Auth = auth;
  }

  resetPoints(){
    this.clickPoints = [];
  }

  backButton(){
    this.router.navigate(['']);
  }

  resetPasswordRedirect(){
   this.router.navigate(['login-account',btoa(this.userName),this.canvasX,this.canvasY]);
  }
}
