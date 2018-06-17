/**
 * @author Anatoly Kirichenko
 * @email kianvl@mail.ru
 * License GNU General Public License >= 2
 */

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

//Чтение и запись бесформатного варианта LINKAGE
class PedigreeData {
    // Amount of persons in pedigree
    private int amntPrsns;
    // строковый персональный шифр
    private String prsnID[];
    // шифр пола
    private byte sexID[];
    // шифры родителей - как порядковый номер в списке
    private int prntID[][] = new int[2][];

    // Interfaces
    public void setAmntPrsns (int amntPrsns) {
        this.amntPrsns = amntPrsns;
    }
    public int getAmntPrsns () {
        return amntPrsns;
    }

    public void setPrsnID (int i, String prsnID) {
        this.prsnID[i] = prsnID;
    }
    public String getPrsnID (int i) {
        return prsnID[i];
    }

    public void setSexID (int i, byte sexID) {
        this.sexID[i] = sexID;
    }
    public byte getSexID (int i) {
        return sexID[i];
    }

    public void setPrntID (int fm, int i, int prntID) {
        this.prntID[fm][i] = prntID;
    }
    public int getPrntID (int fm, int i) {
        return prntID[fm][i];
    }

    void ArrayData() {
        prsnID = new String[amntPrsns];
        sexID = new byte[amntPrsns];
        prntID[0] = new int[amntPrsns];
        prntID[1] = new int[amntPrsns];
    }

    void RWData (String PGFileName, char rw) throws Exception {
        int i;

        if (rw!='r' & rw!='w') return;

        RandomAccessFile PGFile = new RandomAccessFile(PGFileName, "rw");

        if (rw == 'r') {
            amntPrsns = PGFile.readInt();
            ArrayData();
        }
        else {
            PGFile.setLength(0);
            PGFile.writeInt(amntPrsns);
        }

        for (i=0; i<amntPrsns; i++) {
            if (rw == 'r') {
                prsnID[i] = PGFile.readUTF();
                sexID[i] = PGFile.readByte();
                prntID[0][i] = PGFile.readInt();
                prntID[1][i] = PGFile.readInt();
            }
            else {
                PGFile.writeUTF(prsnID[i]);
                PGFile.writeByte(sexID[i]);
                PGFile.writeInt(prntID[0][i]);
                PGFile.writeInt(prntID[1][i]);
            }
        }

        PGFile.close ();
    }
}
