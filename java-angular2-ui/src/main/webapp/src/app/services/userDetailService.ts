import {Inject, Injectable} from 'angular2/core';
import {Http, HTTP_PROVIDERS} from 'angular2/http';

export interface User{
    username: string;
    forname: string;
    surname: string;
    birthdate: string;
    email: string;
    authorities: Array<string>;    
}

@Injectable()
export class UserDetailService {

    constructor(private http: Http) {
          this.userDetails();
    }

    get(): User{
        var user = localStorage.getItem('user');
        if(!user){
            this.userDetails();
        }
        return JSON.parse(user);
    }

    userDetails(){
        this.http.get('../user')
            .subscribe(
                  res => localStorage.setItem('user', res.text()),
                  err => this.logError(err),
                  () => console.log('User loaded' + JSON.stringify(this.get()))
            );

    }

    logError(err) {
      console.error('There was an error: ' + err);
    }
}
