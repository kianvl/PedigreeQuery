// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

// персональные координаты: поколение и положение в линии поколения
class PersonXYRW {
    // Amount persons in pedigree
    private int amntPrsn;
    // персональный шифр
    private int prsnID[];
    // шифр пола
    private byte sexID[];
    // координата в линии поколения
    private int iX[];
    // поколение
    private int iY[];
    // является ли пойнтером
    boolean bNxt[];

    // Interfaces
    public void setAmntPrsn(int amntPrsn) {
        this.amntPrsn = amntPrsn;
    }
    public int getAmntPrsn() {
        return amntPrsn;
    }

    public void setPrsnID(int i, int prsnID) {
        this.prsnID[i] = prsnID;
    }
    public int getPrsnID(int i) {
        return prsnID[i];
    }

    public void setSexID(int i, byte sexID) {
        this.sexID[i] = sexID;
    }
    public byte getSexID(int i) {
        return sexID[i];
    }

    public void setiX(int i, int iX) {
        this.iX[i] = iX;
    }
    public int getiX(int i) {
        return iX[i];
    }

    public void setiY(int i, int iY) {
        this.iY[i] = iY;
    }
    public int getiY(int i) {
        return iY[i];
    }

    void ArrayData()     {
        prsnID = new int[amntPrsn];
        sexID = new byte[amntPrsn];
        iX = new int[amntPrsn];
        iY = new int[amntPrsn];
        bNxt = new boolean[amntPrsn];
    }


    void RWData (String PXYFileName, char rw) throws Exception {
        int i;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PXYFile = new RandomAccessFile(PXYFileName, "rw");

        if (rw == 'r') {
            amntPrsn = PXYFile.readInt();
            ArrayData();
        }
        else {
            PXYFile.setLength(0);
            PXYFile.writeInt(amntPrsn);
        }

        for (i=0; i<amntPrsn; i++) {
            if (rw == 'r') {
                prsnID[i] = PXYFile.readInt();
                sexID[i] = PXYFile.readByte();
                iX[i] = PXYFile.readInt();
                iY[i] = PXYFile.readInt();
                bNxt[i] = PXYFile.readBoolean();
            }
            else {
                PXYFile.writeInt(prsnID[i]);
                PXYFile.writeByte(sexID[i]);
                PXYFile.writeInt(iX[i]);
                PXYFile.writeInt(iY[i]);
                PXYFile.writeBoolean(bNxt[i]);
            }
        }

        PXYFile.close();
    }
}
