import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Patient} from '../../model/Patient';
import {AuthService} from '../../core/auth/auth.service';
import {ApkBase} from "../apk-base/apk-base";
import {Observable} from 'rxjs/Observable';
import {ApkFormComponent} from "../apk-form/apk-form.component";
import {JwtHttp} from "../../core/jwt-http";

@Component({
  selector: 'app-apk-detail',
  templateUrl: './patient-add-requisition.component.html',
  styleUrls: ['./patient-add-requisition.component.css']
})
export class PatientAddRequisitionComponent extends ApkBase implements OnInit {

  @ViewChild(ApkFormComponent) apkFormComponent: ApkFormComponent;

  id: string;
  data: Patient;

  constructor(protected route: ActivatedRoute,
              protected http: JwtHttp,
              protected authService: AuthService) {
    super();
  }

  ngOnInit() {


    this.route.params.subscribe(params => {
      this.id = params.id;

      if (this.id) {
        const $data = this.http.get('/api/patient/' + this.id)
          .map(response => response.json())
          .share();

        $data.subscribe((data: Patient) => {
          this.data = data;
        });


      }
    });
  }

  protected getId() {
    return this.id;
  }

  userHasEditPermission(data: Patient) {
    return this.authService.userHasDataEditPermission(data);
  }
}