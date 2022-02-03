import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG_URI } from 'src/environments/enviroment.api.config';

@Injectable({
  providedIn: 'root'
})
export class GraphicLoginService {

  constructor(private http: HttpClient) { }

  getImagePassword(userName:any):Observable<any>{
    return this.http.get(API_CONFIG_URI.GET_PASSWORD_IMAGE+`${userName}`);
  }

  Authenticate(authRequest:any):Observable<any>{
  
    let body = new URLSearchParams();
    body.set('userName', authRequest.userName);
    body.set('passWord', authRequest.passWord);
    body.set('totp', authRequest.totp);

    let options = {
        headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };

    return this.http.post(API_CONFIG_URI.LOG_USER_IN, body.toString(), options);
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
