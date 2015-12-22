import {View, Component, OnInit} from 'angular2/core';
import {Location, RouteConfig, RouterLink, Router, ROUTER_PROVIDERS, ROUTER_DIRECTIVES } from 'angular2/router';
import {Http, Headers, HTTP_PROVIDERS } from 'angular2/http';

import {BuergerList} from './components/buergerList/buergerList';
import {Home} from './components/home/home';
import {UserComp} from './components/user/user';

import {LoggedInRouterOutlet} from './LoggedInOutlet';
import {UserDetailService} from './services/userDetailService';

@Component({
	selector: 'app'
})
@View({
	directives: [ROUTER_DIRECTIVES],
	templateUrl: 'app/app.html'
})
@RouteConfig([
    //{path: '/', redirectTo: '/Home'},
    {path: '/home', component: Home, name: 'Home' },
    {path: '/buerger', component: BuergerList, name: 'BuergerList' },
    {path: '/user', component: UserComp, name: 'User' }
    //{path: '/login', redirectTo: '../login'}
])
export class App implements OnInit{
	title = 'Angular Bürgerservice Prototype';
  user: any;
  authenticated = false;

  constructor(public router: Router, public http: Http, public location: Location, public userDetailService: UserDetailService) {
      
  }

  ngOnInit() {
    this.user = this.userDetailService.get();
    if(this.user.username){
      this.authenticated = true;
    }
  }
  
  logout(){
    //http returns an observable.
    this.http.post('../logout', '')
        .subscribe(
          err => console.log(err),
          () => console.log('Logout Complete')
     );

  } 
  
  logoutServer(){
        this.http.post('../logout-server', '')
        .subscribe(
          err => {
            this.logout();
            console.log('Logout from server complete');
            this.location.go('/');
          },
          () => {
            this.logout();
            console.log('Logout from server complete');
            this.router.parent.navigate(['/']);
          }    
     );
     this.authenticated = false;
  }
  
  nav(){
    this.router.navigateByUrl("/");
  }
}
