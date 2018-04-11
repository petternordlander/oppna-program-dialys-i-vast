import {Pd} from "./Pd";
import {Ansvarig} from "./Ansvarig";
import {Typ} from "./Typ";

export class Patient {
  adress: string;
  ansvarig: Ansvarig;
  efternamn: string;
  epost: string;
  fornamn: string;
  id: number;
  isDeleted: boolean;
  mobil: string;
  pas: number;
  pnr: string;
  portkod: string;
  postNr: string;
  postOrt: string;
  samtycke: boolean;
  telefon: string;
  utdelDag: string;
  utdelText: string;
  utdelVecka: string;
  lasText: string;
  ovrigt: string;

  // About the delivery
  leveransPaminnelse: boolean;
  avropsOmbud: string;
  leveransMottagningsOmbud: string;

  typ: Typ;

  pds: Array<Pd>

  private static template: Patient = new Patient();

  public static init(that: Patient) {
    that.sortPds = this.template.sortPds;
  }

  public sortPds() {
    if (this.pds != null) {
      this.pds.sort((a: Pd, b: Pd) =>
        ((a.datum > b.datum || (a.datum == b.datum && a.id > b.id)) ? -1 : 1));
      this.pds.forEach((pd: Pd) => {
        if (!pd.sortBestInfos)
          Pd.init(pd);
        pd.sortBestInfos();
      });
    }
  }

}


