import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { DialogData } from './dialog-data';

@Component({
  selector: 'app-image-password-configuration',
  templateUrl: './image-password-configuration.component.html',
  styleUrls: ['./image-password-configuration.component.scss']
})
export class ImagePasswordConfigurationComponent implements OnInit {
  constructor( 
    public dialogRef: MatDialogRef<ImagePasswordConfigurationComponent>, 
    @Inject(MAT_DIALOG_DATA) public data: DialogData) { 
               }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
