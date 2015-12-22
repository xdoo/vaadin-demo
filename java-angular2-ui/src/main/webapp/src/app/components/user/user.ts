import { Component, View } from 'angular2/core';

import {UserDetailService, User} from '../../services/userDetailService';

@Component({})
@View({
	templateUrl: 'app/components/user/user.html',
	directives: []
})
export class UserComp {
  user: User;
  constructor(public userDetailService: UserDetailService) {
    this.user = userDetailService.get()
  }

}
