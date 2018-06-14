// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

class PQFullDataRW {
    int AmntPrsn;
    int AmntSgn;
    int AmntClr;
    int ClrCds[][];
    String SClr[];
    String PrsnIDNm;
    String PrsnID[];
    String PrntIDNm[] = new String[2];
    int PrntID[][];
    String SexIDNm;
    byte SexID[];
    private String CrsLnNm;
    boolean CrsLn[];
    int ClrRGB[][];
    String SignsNm[];
    String Signs[][];


    void ArrayData() {
        int i, j;

        PrsnIDNm = "";
        ClrCds = new int[AmntClr][];
        SClr = new String[AmntClr];
        for (i=0; i<AmntClr; i++) {
            ClrCds[i] = new int[3];
            SClr[i] = "";
        }
        PrsnID = new String[AmntPrsn];
        for (i=0; i<AmntPrsn; i++)
            PrsnID[i] = "";
        for (i=0; i<2; i++)
            PrntIDNm[i] = "";
        PrntID = new int[AmntPrsn][];
        SexIDNm = "";
        SexID = new byte[AmntPrsn];
        CrsLnNm = "";
        CrsLn = new boolean[AmntPrsn];
        ClrRGB = new int[AmntPrsn][];
        SignsNm = new String[AmntSgn];
        for (i=0; i<AmntSgn; i++)
            SignsNm[i] = "";
        Signs = new String[AmntPrsn][];
        for (i=0; i<AmntPrsn; i++) {
            PrntID[i] = new int[2];
            CrsLn[i] = false;
            ClrRGB[i] = new int[3];
            Signs[i] = new String[AmntSgn];
            for (j=0; j<AmntSgn; j++)
                Signs[i][j] = "";
        }
    }


    void RWData (String PXYFileName, char rw) throws Exception {
        int i, j;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PXYFile = new RandomAccessFile(PXYFileName, "rw");

        if (rw == 'r') {
            AmntPrsn = PXYFile.readInt();
            AmntSgn = PXYFile.readInt();
            AmntClr = PXYFile.readInt();
            ArrayData();
            for (i=0; i<AmntClr; i++) {
                SClr[i] = PXYFile.readUTF();
                for (j=0; j<3; j++)
                    ClrCds[i][j] = PXYFile.readInt();
            }
            PrsnIDNm = PXYFile.readUTF();
            for (i=0; i<2; i++)
                PrntIDNm[i] = PXYFile.readUTF();
            SexIDNm = PXYFile.readUTF();
            CrsLnNm = PXYFile.readUTF();
            for (i=0; i<AmntSgn; i++)
                SignsNm[i] = PXYFile.readUTF();
        }
        else {
            PXYFile.setLength(0);
            PXYFile.writeInt(AmntPrsn);
            PXYFile.writeInt(AmntSgn);
            PXYFile.writeInt(AmntClr);
            for (i=0; i<AmntClr; i++) {
                PXYFile.writeUTF(SClr[i]);
                for (j=0; j<3; j++)
                    PXYFile.writeInt(ClrCds[i][j]);
            }
            PXYFile.writeUTF(PrsnIDNm);
            for (i=0; i<2; i++)
                PXYFile.writeUTF(PrntIDNm[i]);
            PXYFile.writeUTF(SexIDNm);
            PXYFile.writeUTF(CrsLnNm);
            for (i=0; i<AmntSgn; i++)
                PXYFile.writeUTF(SignsNm[i]);
        }

        for (i=0; i<AmntPrsn; i++) {
            if (rw == 'r') {
                PrsnID[i] = PXYFile.readUTF();
                for (j=0; j<2; j++)
                    PrntID[i][j] = PXYFile.readInt();
                SexID[i] = PXYFile.readByte();
                CrsLn[i] = PXYFile.readBoolean();
                for (j=0; j<3; j++)
                    ClrRGB[i][j] = PXYFile.readInt();
                for (j=0; j<AmntSgn; j++)
                    Signs[i][j] = PXYFile.readUTF();
            }
            else {
                PXYFile.writeUTF(PrsnID[i]);
                for (j=0; j<2; j++)
                    PXYFile.writeInt(PrntID[i][j]);
                PXYFile.writeByte(SexID[i]);
                PXYFile.writeBoolean(CrsLn[i]);
                for (j=0; j<3; j++)
                    PXYFile.writeInt(ClrRGB[i][j]);
                for (j=0; j<AmntSgn; j++)
                    PXYFile.writeUTF(Signs[i][j]);
            }
        }

        PXYFile.close();
    }
}
