import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG_URI } from 'src/environments/enviroment.api.config';

@Injectable({
  providedIn: 'root'
})
export class IndexPageService {

  constructor(private http: HttpClient) { }

  getUserConfig(userName:any):Observable<any>{
    return this.http.get(API_CONFIG_URI.CONFIG_BY_USER_NAME+`${userName}`);
  }
}
