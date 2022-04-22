import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserAccountCreationComponent } from './user-account-creation/user-account-creation.component';
import { IndexPageComponent } from './index-page/index-page.component';
import { GraphicLoginComponent } from './graphic-login/graphic-login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ResetLoginComponent } from './reset-login/reset-login.component';
import { AuthGuardService as AuthGuard} from './shared/services/auth-guard.service';

const routes: Routes = [
  {path:'', component: IndexPageComponent},
  {path:'create-account', component: UserAccountCreationComponent},
  {path:'reset-account/:userName/:canvasX/:canvasY', component: ResetLoginComponent},
  {path:'login-account/:userName/:canvasX/:canvasY', component: GraphicLoginComponent},
  {path:'user-profile', component: UserProfileComponent, canActivate: [AuthGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
