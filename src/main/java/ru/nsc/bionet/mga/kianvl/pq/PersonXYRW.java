// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

// персональные координаты: поколение и положение в линии поколения
class PersonXYRW {
    // количество людей
    int AmntPrsn;
    // персональный шифр
    int PrsnID[];
    // шифр пола
    int SexID[];
    // координата в линии поколения
    int iX[];
    // поколение
    int iY[];
    // является ли пойнтером
    boolean bNxt[];


    void ArrayData()     {
        PrsnID = new int[AmntPrsn];
        SexID = new int[AmntPrsn];
        iX = new int[AmntPrsn];
        iY = new int[AmntPrsn];
        bNxt = new boolean[AmntPrsn];
    }


    void RWData (String PXYFileName, char rw) throws Exception {
        int i;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PXYFile = new RandomAccessFile(PXYFileName, "rw");

        if (rw == 'r') {
            AmntPrsn = PXYFile.readInt();
            ArrayData();
        }
        else {
            PXYFile.setLength(0);
            PXYFile.writeInt(AmntPrsn);
        }

        for (i=0; i<AmntPrsn; i++) {
            if (rw == 'r') {
                PrsnID[i] = PXYFile.readInt();
                SexID[i] = PXYFile.readInt();
                iX[i] = PXYFile.readInt();
                iY[i] = PXYFile.readInt();
                bNxt[i] = PXYFile.readBoolean();
            }
            else {
                PXYFile.writeInt(PrsnID[i]);
                PXYFile.writeInt(SexID[i]);
                PXYFile.writeInt(iX[i]);
                PXYFile.writeInt(iY[i]);
                PXYFile.writeBoolean(bNxt[i]);
            }
        }

        PXYFile.close();
    }
}
