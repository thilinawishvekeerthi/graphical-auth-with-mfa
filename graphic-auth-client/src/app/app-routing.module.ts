import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserAccountCreationComponent } from './user-account-creation/user-account-creation.component';
import { IndexPageComponent } from './index-page/index-page.component';
import { GraphicLoginComponent } from './graphic-login/graphic-login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';

const routes: Routes = [
  {path:'', component: IndexPageComponent},
  {path:'create-account', component: UserAccountCreationComponent},
  {path:'login-account/:userName/:canvasX/:canvasY', component: GraphicLoginComponent},
  {path:'user-profile', component: UserProfileComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
