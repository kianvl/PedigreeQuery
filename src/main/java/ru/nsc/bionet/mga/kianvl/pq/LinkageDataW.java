// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.File;
import java.io.FileWriter;

class LinkageDataW
{
    // количество индивидов
    int AmntPrsns;
    // строковый персональный шифр
    String PrsnID[];
    // шифры родителей - как порядковый номер в списке
    String PrntsID[][] = new String[2][];
    // шифр пола
    String SexID[];

    void ArrayData () {
        PrsnID = new String[AmntPrsns];
        PrntsID[0] = new String[AmntPrsns];
        PrntsID[1] = new String[AmntPrsns];
        SexID = new String[AmntPrsns];
    }

    void WData (File LnkgFileName) throws Exception {
        int i;
        String s;

        FileWriter LnkgFile;
        LnkgFile = new FileWriter (LnkgFileName);

        s = "ID	Fth	Mth	Sex\n";
        LnkgFile.write (s);
        for (i=0; i<AmntPrsns; i++) {
            s = PrsnID[i] + "	" + PrntsID[0][i] + "	" + PrntsID[1][i] + "	" + SexID[i] + "\n";
            LnkgFile.write (s);
        }

        LnkgFile.close ();
    }
}
