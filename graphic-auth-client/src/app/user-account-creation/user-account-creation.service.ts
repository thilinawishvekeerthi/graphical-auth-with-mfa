import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG_URI } from 'src/environments/enviroment.api.config';

@Injectable({
  providedIn: 'root'
})
export class UserAccountCreationService {

  constructor(private http: HttpClient) { }

  createAccount(account:any):Observable<any>{
    return this.http.post(API_CONFIG_URI.CREATE_ACOUNT, account);
  }

  uploadImage(file :any):Observable<any>{
    let formData = new FormData();
    formData.append("multipartFile",file,file.name);
    return this.http.post(API_CONFIG_URI.IMAGE_UPLOAD, formData);
  }

  verifyTotp(verifyRequest:any):Observable<any>{
    return this.http.post(API_CONFIG_URI.VERYFY_TOTP, verifyRequest);
  }

  generatePassword(passPoints: any[]):string{
    let password = "";
    let index = 0;
    passPoints.forEach(point=>{
      password += `${point.x},${point.y}`;
      if(index != passPoints.length -1) password +=`|`;
      index++;
    });
    return password;
  }

}

