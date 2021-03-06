import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSliderModule } from '@angular/material/slider';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatRadioModule } from '@angular/material/radio';
import { MatCardModule } from '@angular/material/card';
import { MatGridListModule } from '@angular/material/grid-list'
import { MatSidenavModule} from '@angular/material/sidenav';
import { MatIconModule} from '@angular/material/icon'
import { ReactiveFormsModule } from '@angular/forms';
import { MatBadgeModule} from '@angular/material/badge';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { IndexPageComponent } from './index-page/index-page.component';
import { UserAccountCreationComponent } from './user-account-creation/user-account-creation.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { GraphicLoginComponent } from './graphic-login/graphic-login.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { ResetLoginComponent } from './reset-login/reset-login.component';
import { ImagePasswordConfigurationComponent } from './shared/components/image-password-configuration/image-password-configuration.component';
import { MatDialogModule } from '@angular/material/dialog';
@NgModule({
  declarations: [
    AppComponent,
    IndexPageComponent,
    UserAccountCreationComponent,
    GraphicLoginComponent,
    UserProfileComponent,
    ResetLoginComponent,
    ImagePasswordConfigurationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatSliderModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    MatGridListModule,
    MatSidenavModule,
    MatIconModule,
    MatBadgeModule,
    MatProgressBarModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatMenuModule,
    MatDialogModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
