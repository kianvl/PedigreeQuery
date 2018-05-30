// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

// Посемейные данные
class FamiliesData {
    // количество семей
    public int AmntFml;
    // родители
    public int PrntFml[][] = new int[2][];
    // количество потомков в семье
    public int AmntOfsprFml[];
    // список потомков в семье
    public int OfsprFml[][];


    // полное создание всех массивов
    private void ArrayData() {
        PrntFml[0] = new int[AmntFml];
        PrntFml[1] = new int[AmntFml];
        AmntOfsprFml = new int[AmntFml];
        OfsprFml = new int[AmntFml][];
    }


    // чтение и запись данных о семьях
    boolean RWData (String FmlFileName, char rw) throws Exception {
        int i, j;

        if (rw!='r' & rw!='w') return false;

        RandomAccessFile FmlFile = new RandomAccessFile(FmlFileName, "rw");

        if (rw == 'r') {
            AmntFml = FmlFile.readInt();
            ArrayData();
        }
        else {
            FmlFile.setLength(0);
            FmlFile.writeInt(AmntFml);
        }

        for (i=0; i<AmntFml; i++) {
            if (rw == 'r') {
                PrntFml[0][i] = FmlFile.readInt();
                PrntFml[1][i] = FmlFile.readInt();
                AmntOfsprFml[i] = FmlFile.readInt();
                OfsprFml[i] = new int[AmntOfsprFml[i]];
                for (j=0; j<AmntOfsprFml[i]; j++)
                    OfsprFml[i][j] = FmlFile.readInt();
            }
            else {
                FmlFile.writeInt(PrntFml[0][i]);
                FmlFile.writeInt(PrntFml[1][i]);
                FmlFile.writeInt(AmntOfsprFml[i]);
                for (j=0; j<AmntOfsprFml[i]; j++)
                    FmlFile.writeInt(OfsprFml[i][j]);
            }
        }

        FmlFile.close ();
        return true;
    }
}
