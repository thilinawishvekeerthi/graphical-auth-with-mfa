import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserAccountCreationComponent } from './user-account-creation/user-account-creation.component';
import { IndexPageComponent } from './index-page/index-page.component';

const routes: Routes = [
  {path:'', component: IndexPageComponent},
  {path:'create-account', component: UserAccountCreationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
