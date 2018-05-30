// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

class PedigreeLinesRW {
    int AmntFml;
    int PrntXY[][][];
    int PrntLvl[];
    int AmntPrntArc[];
    int PrntArc[][];
    int SbLnLvl[][]; //Уровень сибовой линии
    int AmntSbLnArc[];
    int SbLnArc[][];
    int AmntOfsp[];
    int SbPrsn[][];
    int PrntOfspn[][];
    int PrOfLnLvl[];
    int AmntPrOfLnArc[];
    int PrOfLnArc[][];


    private void ArrayData() {
        int i, j;

        PrntXY = new int[AmntFml][][];
        for (i=0; i<AmntFml; i++)
            PrntXY[i] = new int[2][];
        for (i=0; i<AmntFml; i++) {
            for (j=0; j<2; j++)
                PrntXY[i][j] = new int[2];
        }
        PrntLvl = new int[AmntFml];

        AmntPrntArc = new int[AmntFml];
        PrntArc = new int[AmntFml][];

        SbLnLvl = new int[AmntFml][];
        for (i=0; i<AmntFml; i++)
            SbLnLvl[i] = new int[2];

        AmntSbLnArc = new int[AmntFml];
        SbLnArc = new int[AmntFml][];

        AmntOfsp = new int[AmntFml];

        SbPrsn = new int[AmntFml][];

        PrntOfspn = new int[AmntFml][];
        for (i=0; i<AmntFml; i++)
            PrntOfspn[i] = new int[2];

        PrOfLnLvl = new int[AmntFml];
        AmntPrOfLnArc = new int[AmntFml];
        PrOfLnArc = new int[AmntFml][];
    }


    void RWData (String PdgrLnsFileName, char rw) throws Exception {
        int i, j, k;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PdgrLnsFile = new RandomAccessFile(PdgrLnsFileName, "rw");

        if (rw == 'r') {
            AmntFml = PdgrLnsFile.readInt();
            ArrayData();
        }
        else {
            PdgrLnsFile.setLength(0);
            PdgrLnsFile.writeInt(AmntFml);
        }

        if (rw == 'r') {
            for (i=0; i<AmntFml; i++) {
                for (j=0; j<2; j++) {
                    for (k=0; k<2; k++)
                        PrntXY[i][j][k] = PdgrLnsFile.readInt();
                }
                PrntLvl[i] = PdgrLnsFile.readInt();
                AmntPrntArc[i] = PdgrLnsFile.readInt();
                PrntArc[i] = new int[AmntPrntArc[i]];
                for (j=0; j<AmntPrntArc[i]; j++)
                    PrntArc[i][j] = PdgrLnsFile.readInt();
                for (j=0; j<2; j++)
                    SbLnLvl[i][j] = PdgrLnsFile.readInt();
                AmntSbLnArc[i] = PdgrLnsFile.readInt();
                SbLnArc[i] = new int[AmntSbLnArc[i]];
                for (j=0; j<AmntSbLnArc[i]; j++)
                    SbLnArc[i][j] = PdgrLnsFile.readInt();
                AmntOfsp[i] = PdgrLnsFile.readInt();
                SbPrsn[i] = new int[AmntOfsp[i]];
                for (j=0; j<AmntOfsp[i]; j++)
                    SbPrsn[i][j] = PdgrLnsFile.readInt();
                for (j=0; j<2; j++)
                    PrntOfspn[i][j] = PdgrLnsFile.readInt();
                PrOfLnLvl[i] = PdgrLnsFile.readInt();
                AmntPrOfLnArc[i] = PdgrLnsFile.readInt();
                PrOfLnArc[i] = new int[AmntPrOfLnArc[i]];
                for (j=0; j<AmntPrOfLnArc[i]; j++)
                    PrOfLnArc[i][j] = PdgrLnsFile.readInt();
            }
        }
        else {
            for (i=0; i<AmntFml; i++) {
                for (j=0; j<2; j++) {
                    for (k=0; k<2; k++)
                        PdgrLnsFile.writeInt(PrntXY[i][j][k]);
                }
                PdgrLnsFile.writeInt(PrntLvl[i]);
                PdgrLnsFile.writeInt(AmntPrntArc[i]);
                for (j=0; j<AmntPrntArc[i]; j++)
                    PdgrLnsFile.writeInt(PrntArc[i][j]);
                for (j=0; j<2; j++)
                    PdgrLnsFile.writeInt(SbLnLvl[i][j]);
                PdgrLnsFile.writeInt(AmntSbLnArc[i]);
                for (j=0; j<AmntSbLnArc[i]; j++)
                    PdgrLnsFile.writeInt(SbLnArc[i][j]);
                PdgrLnsFile.writeInt(AmntOfsp[i]);
                for (j=0; j<AmntOfsp[i]; j++)
                    PdgrLnsFile.writeInt(SbPrsn[i][j]);
                for (j=0; j<2; j++)
                    PdgrLnsFile.writeInt(PrntOfspn[i][j]);
                PdgrLnsFile.writeInt(PrOfLnLvl[i]);
                PdgrLnsFile.writeInt(AmntPrOfLnArc[i]);
                for (j=0; j<AmntPrOfLnArc[i]; j++)
                    PdgrLnsFile.writeInt(PrOfLnArc[i][j]);
            }
        }

        PdgrLnsFile.close();
    }
}
