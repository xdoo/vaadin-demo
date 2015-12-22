import { Component, View } from 'angular2/core';
import {Http, Headers, HTTP_PROVIDERS } from 'angular2/http';

@Component({})
@View({
	templateUrl: 'app/components/home/home.html',
	directives: []
})
export class Home {

  title = "Home";
  user = {}
  constructor(private http: Http) {
  }

}
