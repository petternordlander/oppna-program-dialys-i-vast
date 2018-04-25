import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Patient} from '../../model/Patient';
import {AuthService} from '../../core/auth/auth.service';
import {JwtHttp} from "../../core/jwt-http";
import {BestInfo} from "../../model/BestInfo";
import {Pd} from "../../model/Pd";
import {MatPaginator, MatTableDataSource} from "@angular/material";
import {BestRad} from "../../model/BestRad";

@Component({
  selector: 'app-apk-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {

  id: string;
  data: Patient;
  showOldRequisitions: boolean;
  dataSourceSenasteRekvisition: BestInfo[] = [];
  dataSource1 = new MatTableDataSource<BestInfo>();
  displayedColumns = ['id', 'datum', 'utskrivare', 'levdatum'];
  panelOpenState: Number[] = [];
  @ViewChild('page1') page1 : MatPaginator;

  constructor(protected route: ActivatedRoute,
              protected http: JwtHttp,
              public authService: AuthService) {
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      this.id = params.id;

      this.showOldRequisitions = false;

      if (this.id) {
        const $data = this.http.get('/api/patient/' + this.id)
          .map(response => response.json())
          .share();

        $data.subscribe((data: Patient) => {
          this.data = data;
          Patient.init(this.data);
          this.data.sortPds();

          setTimeout(() => this.dataSource1.paginator = this.page1);
          this.dataSource1.data = data.pds[0].bestInfos;

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
