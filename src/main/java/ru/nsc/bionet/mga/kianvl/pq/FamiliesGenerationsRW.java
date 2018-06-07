// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

// данные о поколениях семей
class FamiliesGenerationsRW {
    // количество семей
    int AmntFml;
    // шифры семей
    int FmlID[];
    // поколения семей
    int FmlGnrtn[];


    private void ArrayData() {
        FmlID = new int[AmntFml];
        FmlGnrtn = new int[AmntFml];
    }


    boolean RWData (String FGFileName, char rw) throws Exception {
        int i;

        if (rw!='r' & rw!='w') return false;

        RandomAccessFile FGFile = new RandomAccessFile(FGFileName, "rw");

        if (rw == 'r') {
            AmntFml = FGFile.readInt();
            ArrayData();
        }
        else {
            FGFile.setLength(0);
            FGFile.writeInt(AmntFml);
        }

        for (i=0; i<AmntFml; i++) {
            if (rw == 'r') {
                FmlID[i] = FGFile.readInt();
                FmlGnrtn[i] = FGFile.readInt();
            }
            else {
                FGFile.writeInt(FmlID[i]);
                FGFile.writeInt(FmlGnrtn[i]);
            }
        }

        FGFile.close();

        return true;
    }
}
