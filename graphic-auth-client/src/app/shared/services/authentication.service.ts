import { nullSafeIsEquivalent } from '@angular/compiler/src/output/output_ast';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Auth } from './auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private _userName : string = "";

  private _Auth: Auth = new Auth();

  constructor(public router: Router) { }

  public get Auth(): Auth {
    let authString = localStorage.getItem('auth');
    if(authString){
      let authObj  = JSON.parse(authString);
      let auth = new Auth();
      auth.acessToken = authObj._acessToken;
      auth.refreshToken = authObj._refreshToken;
      this._Auth = auth;
    }
    return this._Auth;
  }
  public set Auth(value: Auth) {
    this._Auth = value;
    localStorage.setItem('auth',JSON.stringify(this._Auth));
  }

  public isLoggedIn() : boolean{
    if(this.Auth && this.Auth.acessToken){
      let payloadString = atob(this.Auth.acessToken.split('.')[1]);
      let payload = JSON.parse(payloadString);
      return payload.exp && payload.exp > (Date.now() / 1000);
    }
    return false;
  }

  public loggOut(){
    localStorage.removeItem('auth');
    this.router.navigate(['']);
  }

  public get userName() : string{
    if(this.Auth && this.Auth.acessToken){
      let payloadString = atob(this.Auth.acessToken.split('.')[1]);
      let payload = JSON.parse(payloadString);
      this._userName = payload.sub;
    }
    return this._userName;
  }
}
