// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

//Чтение и запись бесформатного варианта LINKAGE
class PedigreeData {
    // количество индивидов
    private int AmntPrsns;
    // строковый персональный шифр
    String PrsnID[];
    // шифры родителей - как порядковый номер в списке
    int PrntsID[][] = new int[2][];
    // шифр пола
    int SexID[];

    private void ArrayData() {
        PrsnID = new String[AmntPrsns];
        PrntsID[0] = new int[AmntPrsns];
        PrntsID[1] = new int[AmntPrsns];
        SexID = new int[AmntPrsns];
    }

    void RWData (String PGFileName, char rw) throws Exception {
        int i;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PGFile = new RandomAccessFile(PGFileName, "rw");

        if (rw == 'r') {
            AmntPrsns = PGFile.readInt();
            ArrayData();
        }
        else {
            PGFile.setLength(0);
            PGFile.writeInt(AmntPrsns);
        }

        for (i=0; i<AmntPrsns; i++) {
            if (rw == 'r') {
                PrsnID[i] = PGFile.readUTF();
                PrntsID[0][i] = PGFile.readInt();
                PrntsID[1][i] = PGFile.readInt();
                SexID[i] = PGFile.readInt();
            }
            else {
                PGFile.writeUTF(PrsnID[i]);
                PGFile.writeInt(PrntsID[0][i]);
                PGFile.writeInt(PrntsID[1][i]);
                PGFile.writeInt(SexID[i]);
            }
        }

        PGFile.close ();
    }
}
