import { AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { DomSanitizer } from '@angular/platform-browser';
import { PRIMARY_OUTLET, Router } from '@angular/router';
import { fromEvent } from 'rxjs';
import { UserAccountCreationService } from './user-account-creation.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-user-account-creation',
  templateUrl: './user-account-creation.component.html',
  styleUrls: ['./user-account-creation.component.scss']
})
export class UserAccountCreationComponent implements OnInit,  AfterViewInit {

  @ViewChild('passwordCanvas') public passwordCanvas? : ElementRef;
  @ViewChild('fileUpload') public fileUpload? : ElementRef;
  userNameFormControl = new FormControl('', [Validators.required]);
  totpFormControl = new FormControl('', [Validators.required]);
  private canvasRenderContext!: CanvasRenderingContext2D;
  private defaultImageUrl : string = "assets/image/default-image.jpg";
  public imagePasswordUploaded :boolean = false;
  public fileName : string = "";
  private imagefile: any;
  public numberOfPassPoints:number = 4;
  public clickPoints: any[] = [];
  public repeatClickPoints : any[] = [];
  public canvasX: number = 0;
  public canvasY: number = 0;
  passPointObj : any = null;
  tolerance : number = 10;
  public repeatProgressColor : ThemePalette =PRIMARY_OUTLET ;

  constructor(private userAccountService: UserAccountCreationService,
              private sanitizer:DomSanitizer,
              private _snackBar: MatSnackBar,
              private router: Router) { }
  
  ngAfterViewInit(): void { 
    this.initCanvas();
    fromEvent(this.passwordCanvas?.nativeElement, 'mousedown').subscribe(res => {
      this.mouseClickFunctions(<MouseEvent>res);
    });
  }

  ngOnInit(): void {
    this.canvasX = window.innerWidth-401;
    this.canvasY = window.innerHeight-6;
  }

  @HostListener('window:resize', ['$event'])
  initCanvas(){
    (<HTMLCanvasElement>this.passwordCanvas?.nativeElement).width= this.canvasX;
    (<HTMLCanvasElement>this.passwordCanvas?.nativeElement).height= this.canvasY;
      let canvas : HTMLCanvasElement = this.passwordCanvasRef;
    let canvasContent = canvas.getContext('2d');
    this.canvasRenderContext = canvasContent ? canvasContent: new CanvasRenderingContext2D() ;
    if(canvasContent){
      if(this.fileName){
        this.setUploadImageFromCache();
      }else{
        let backgroundImage = new Image();
        backgroundImage.src = this.defaultImageUrl;
        backgroundImage.onload=()=>{
          this.canvasRenderContext.drawImage(backgroundImage, 0, 0,this.canvasX, this.canvasY);
        }
      }
    }
  }

  renderClickPoints(event : MouseEvent){
    this.canvasRenderContext.beginPath();
    this.canvasRenderContext.arc(event.offsetX, event.offsetY, this.tolerance, 50, 0, true);
    this.canvasRenderContext.fillStyle ="white";
    this.canvasRenderContext.fill();
    this.canvasRenderContext.restore();
    setTimeout(()=>{
      let backgroundImage = new Image();
      backgroundImage.src = this.defaultImageUrl;
      backgroundImage.onload=()=>{
        if(this.fileName)
            this.setUploadImageFromCache();
        else
            this.canvasRenderContext.drawImage(backgroundImage, 0, 0, this.canvasX, this.canvasY);
      }
      this.canvasRenderContext.closePath();
    },300);
  }

  get passwordCanvasRef (): HTMLCanvasElement{
    return  (<HTMLCanvasElement>this.passwordCanvas?.nativeElement);
  }

 public fileTyepeError : boolean = false;
  onFileSelected($event: any){
    let fileType = $event.target.files[0]?.type;
    if(fileType && (fileType == "image/png" || fileType == "image/jpeg")){
      this.resetPoints();
      this.fileTyepeError =false;
      this.imagefile = $event.target.files[0];
      this.setUploadImageFromCache(); 
      this.imagePasswordUploaded = true;    
    }else{
      this.fileTyepeError = true;
    }
    
    console.log($event);
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
    
  }

  setUploadImageFromCache(){
    var reader = new FileReader();
    reader.onload = (event :any)=>{
        let backgroundImage = new Image();
        backgroundImage.src = event.target.result;
        backgroundImage.onload =()=>{
          this.canvasRenderContext.drawImage(backgroundImage, 0, 0,this.canvasX, this.canvasY);
          this.fileName = "upload completed"
          this.drawGrid();
        }
       
    }
    reader.readAsDataURL(this.imagefile);    
  }

  uploadButtonClick($event: MouseEvent){
    $event.preventDefault();
    this.fileUploadRef.click();
  }

  get fileUploadRef () : HTMLInputElement{
    return (<HTMLInputElement>this.fileUpload?.nativeElement);
  }

  selectedClickPointvalid(clickPoint: any, repeatPoint: any): boolean{
    let x_difference = clickPoint.x - repeatPoint.x;
    let x_squar = Math.pow(x_difference, 2);
    let y_difference =  clickPoint.y - repeatPoint.y;
    let y_squar = Math.pow(y_difference, 2);
    let r_square = Math.pow(this.tolerance, 2);
    return (x_squar + y_squar) <= r_square;
  }

  resetPoints(){
    this.clickPoints = [];
    this.repeatClickPoints = [];
  }

  mouseClickFunctions(res: MouseEvent){
    let event : MouseEvent = (<MouseEvent>res);
    console.log(event);
    this.passPointObj = {x:event.offsetX , y:event.offsetY};
    
    if(this.imagePasswordUploaded && this.numberOfPassPoints != this.clickPoints.length){
      this.clickPoints.push(this.passPointObj);
      this.renderClickPoints(event);
    }else if(this.numberOfPassPoints == this.clickPoints.length && this.repeatClickPoints.length != this.numberOfPassPoints){
      this.renderClickPoints(event);
      let index = this.repeatClickPoints.length;
      let _passPointObj = this.clickPoints[index];
      if(this.selectedClickPointvalid(_passPointObj,this.passPointObj)){
        this.repeatProgressColor = 'primary';
        this.repeatClickPoints.push(this.passPointObj);
      }else{
        this.passPointObj = "Error miss match";
        this.repeatClickPoints = [];
        this.repeatProgressColor = 'warn';
      }
      
      
    }
  }

  public qrCodeEnable :boolean = false;
  public qrUri : string = "";
  nextButtonClick(){
    this.userAccountService.uploadImage(this.imagefile).subscribe(res=>{
      if(res){
        let account = {
          userName : this.userNameFormControl.value,
          passWord : this.userAccountService.generatePassword(this.clickPoints),
          email: `${this.userNameFormControl.value}@email.com`,
          active : true,
          mfa : true,
          passPoints: this.userAccountService.generatePassword(this.clickPoints),
          numberOfPassPoints: this.numberOfPassPoints,
          tolerance: this.tolerance,
          canvasX: this.canvasX,
          canvasY: this.canvasY,
          imageRef: res
        }
        this.userAccountService.createAccount(account).subscribe(res=>{
          if(res && res.secretImageUri != null){
            this.qrCodeEnable = true;
            this.qrUri = res.secretImageUri;
          }
        }, err=>{
          if(err.status == 403){
            this._snackBar.open("User name already exists!!","close",{
              horizontalPosition:"left",
              verticalPosition: "top",
              duration: 4 * 1000,
            });
          }
          
        });
      }
    })
  }

  qrCode(){
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.qrUri);
  }

  verify(){
    let verifyRequest ={
      totp : this.totpFormControl.value,
      userName: this.userNameFormControl.value
    }
    this.userAccountService.verifyTotp(verifyRequest).subscribe(res=>{
      if(res){
        this._snackBar.open("totp correct","close",{
          horizontalPosition:"left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
        this.router.navigate(['']);
      }else{
        this._snackBar.open("totp incorrect","close",{
          horizontalPosition:"left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
      }
    });
  }
}
