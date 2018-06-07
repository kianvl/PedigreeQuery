// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

class FamiliesRelationsRW {
    int AmntFml;
    int FmlRltnsVrt[][][];
    int FmlRltnsHrz[][][];
    int FmlRltnsVrtUp[][];
    int FmlRltnsHrzUp[][];

    void ArrayData () {
        int i;
        //Вертикальная сязь семьи по потомкам вниз номер семьи
        FmlRltnsVrt = new int[AmntFml][][];
        //горизонтальная связь семьи
        FmlRltnsHrz = new int[AmntFml][][];
        //вертикальная связь семьи по родителям
        FmlRltnsVrtUp = new int[AmntFml][];
        FmlRltnsHrzUp = new int[AmntFml][];
        for (i=0; i<AmntFml; i++) {
            FmlRltnsVrt[i] = new int[2][];
            FmlRltnsHrz[i] = new int[2][];
            FmlRltnsVrtUp[i] = new int[2];
            FmlRltnsHrzUp[i] = new int[2];
        }
    }

    boolean RWData (String FRFileName, char rw) throws Exception
    {
        int i, j, k, l;

        if (rw!='r' & rw!='w') return false;

        RandomAccessFile FRFile = new RandomAccessFile(FRFileName, "rw");

        if (rw == 'r') {
            AmntFml = FRFile.readInt();
            ArrayData();
        }
        else {
            FRFile.setLength(0);
            FRFile.writeInt(AmntFml);
        }

        for (i=0; i<AmntFml; i++) {
            if (rw == 'r') {
                for (j=0; j<2; j++) {
                    l = FRFile.readInt();
                    FmlRltnsVrt[i][j] = new int[l+1];
                    FmlRltnsVrt[i][j][0] = l;
                    for (k=1; k<=l; k++)
                        FmlRltnsVrt[i][j][k] = FRFile.readInt();
                    FmlRltnsVrtUp[i][j] = FRFile.readInt();
                }
                for (j=0; j<2; j++) {
                    l = FRFile.readInt();
                    FmlRltnsHrz[i][j] = new int[l+1];
                    FmlRltnsHrz[i][j][0] = l;
                    for (k=1; k<=l; k++)
                        FmlRltnsHrz[i][j][k] = FRFile.readInt();
                    FmlRltnsHrzUp[i][j] = FRFile.readInt();
                }
            }
            else {
                for (j=0; j<2; j++) {
                    for (k=0; k<=FmlRltnsVrt[i][j][0]; k++)
                        FRFile.writeInt(FmlRltnsVrt[i][j][k]);
                    FRFile.writeInt(FmlRltnsVrtUp[i][j]);
                }
                for (j=0; j<2; j++) {
                    for (k=0; k<=FmlRltnsHrz[i][j][0]; k++)
                        FRFile.writeInt(FmlRltnsHrz[i][j][k]);
                    FRFile.writeInt(FmlRltnsHrzUp[i][j]);
                }
            }
        }

        FRFile.close();
        return true;
    }
}
