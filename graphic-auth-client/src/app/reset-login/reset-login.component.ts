import {  Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { PRIMARY_OUTLET, Router, ActivatedRoute } from '@angular/router';
import { fromEvent } from 'rxjs';
import { UserAccountCreationService } from '../user-account-creation/user-account-creation.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GraphicLoginService } from '../graphic-login/graphic-login.service';
import { MatDialog } from '@angular/material/dialog';
import { ImagePasswordConfigurationComponent } from '../shared/components/image-password-configuration/image-password-configuration.component';

@Component({
  selector: 'app-reset-login',
  templateUrl: './reset-login.component.html',
  styleUrls: ['./reset-login.component.scss']
})
export class ResetLoginComponent implements OnInit {

  @ViewChild('passwordCanvas') public passwordCanvas? : ElementRef;
  @ViewChild('fileUpload') public fileUpload? : ElementRef;
  userNameFormControl = new FormControl('', [Validators.required]);
  totpFormControl = new FormControl('', [Validators.required]);
  private canvasRenderContext!: CanvasRenderingContext2D;
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
  private userName : string ="";
  private passwordImage:any;
  public verifyTotpProcess = true;
  private imageId : number = 0;

  constructor(private userAccountService: UserAccountCreationService,
              private route: ActivatedRoute,
              private _snackBar: MatSnackBar,
              private loginService : GraphicLoginService,
              private router: Router,
              public dialog: MatDialog) { }
  
  ngAfterViewInit(): void { 
    this.initCanvas();
    fromEvent(this.passwordCanvas?.nativeElement, 'mousedown').subscribe(res => {
      this.mouseClickFunctions(<MouseEvent>res);
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userName = atob(params['userName']);
      this.canvasX = Number(params['canvasX']);
      this.canvasY = Number(params['canvasY']);
      this.loginService.getImagePassword(this.userName).subscribe(res=>{
        if(res){
          this.imageId = res.id;
          this.fileName = "selection completed"
          this.passwordImage =res.file;
          this.initCanvas();
        }
      });
      this.userNameFormControl.patchValue(this.userName);
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
      if(this.fileName){
        this.setUploadImageFromCache();
      }
    }
  }

  fillStyle: string = "#ffffff";
  renderClickPoints(event : MouseEvent){
    this.canvasRenderContext.beginPath();
    this.canvasRenderContext.arc(event.offsetX, event.offsetY, this.tolerance, 50, 0, true);
    this.canvasRenderContext.fillStyle = this.fillStyle;
    this.canvasRenderContext.fill();
    this.canvasRenderContext.restore();
    setTimeout(()=>{
      let backgroundImage = new Image();
      backgroundImage.src = this.passwordImage;
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
    if(this.imagefile){
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
    }else if(this.passwordImage){
      let backgroundImage = new Image();
      backgroundImage.src = this.passwordImage;
      backgroundImage.onload =()=>{
      this.canvasRenderContext.drawImage(backgroundImage, 0, 0,this.canvasX, this.canvasY);
        this.drawGrid();
      }
    }
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


  nextButtonClick(){
    if(this.imagefile){
      this.userAccountService.uploadImage(this.passwordImage).subscribe(res=>{
        if(res){
          let resetUserRequest = {
            userName: this.userName,
            passWord : this.userAccountService.generatePassword(this.clickPoints),
            passPoints: this.userAccountService.generatePassword(this.clickPoints),
            numberOfPassPoints: this.numberOfPassPoints,
            tolerance: this.tolerance,
            canvasX: this.canvasX,
            canvasY: this.canvasY,
            verifyToken: this.verifyToken,
            imageRef: res
          }
          this.resetAccount(resetUserRequest);
        }
      })
    }else if(this.passwordImage){
      let resetUserRequest = {
        userName: this.userName,
        passWord : this.userAccountService.generatePassword(this.clickPoints),
        passPoints: this.userAccountService.generatePassword(this.clickPoints),
        numberOfPassPoints: this.numberOfPassPoints,
        tolerance: this.tolerance,
        canvasX: this.canvasX,
        canvasY: this.canvasY,
        verifyToken: this.verifyToken,
        imageRef: this.imageId
      }
      this.resetAccount(resetUserRequest);
    }
   
  }

  verifyToken : string = "";
  private resetAccount(resetUserRequest: any) {
    this.userAccountService.resetAccount(resetUserRequest).subscribe(res => {
      if (res) {
        this._snackBar.open("password reset complete", "close", {
          horizontalPosition: "left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
        this.router.navigate(['']);
      }
    }, err => {
      if (err.status == 403) {
        this._snackBar.open("Invalid credentioals", "close", {
          horizontalPosition: "left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
      }

    });
  }

  verify(){
    let verifyRequest ={
      totp : this.totpFormControl.value,
      userName: this.userNameFormControl.value
    }
    this.userAccountService.verifyTotpUpdate(verifyRequest).subscribe(res=>{
      if(res && res.verified){
        this._snackBar.open("totp correct","close",{
          horizontalPosition:"left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
        this.verifyTotpProcess = false;
        this.fileName = "image selected";
        this.imagePasswordUploaded = true;
        this.verifyToken = res.veryToken;
        this.initCanvas();
      }else{
        this.totpFormControl.patchValue('');
        this._snackBar.open("totp incorrect","close",{
          horizontalPosition:"left",
          verticalPosition: "top",
          duration: 4 * 1000,
        });
      }
    });
  }

  backButton(){
    this.router.navigate(['']);
  }

  openConfiguraionDialog(){
    const dialogRef = this.dialog.open(ImagePasswordConfigurationComponent, {
      width: '300px',
      data: {numberOfPassPoints: this.numberOfPassPoints, fillStyle: this.fillStyle},
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.numberOfPassPoints  = result.numberOfPassPoints;
        this.fillStyle = result.fillStyle;
     
      }
    });
  }
}
