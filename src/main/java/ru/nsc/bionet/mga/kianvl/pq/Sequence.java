// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.RandomAccessFile;

class Sequence {
    int AmntPrsns;
    int Sqnc[];

    void DoStart (String FileNameSequence, int ProbandID) throws Exception {
        RandomAccessFile FileSequence = new RandomAccessFile(FileNameSequence, "rw");
        FileSequence.setLength(0);
        FileSequence.writeInt(1);
        FileSequence.writeInt(ProbandID);
        FileSequence.close();
    }

    void DoSequence (String FileNameSequence, int ProbandID) throws Exception {
        int AmountSequence;
        long FileSizeSequence;

        RandomAccessFile FileSequence = new RandomAccessFile(FileNameSequence, "rw");
        AmountSequence = FileSequence.readInt();
        FileSizeSequence = FileSequence.length();
        FileSequence.seek(0);

        if (ProbandID < 0) {
            if (AmountSequence > 1) {
                FileSequence.writeInt(AmountSequence-1);
                FileSequence.setLength(FileSizeSequence-4);
            }
        }
        else {
            FileSequence.writeInt(AmountSequence+1);
            FileSequence.seek(FileSizeSequence);
            FileSequence.writeInt(ProbandID);
        }

        FileSequence.close();
    }

    void GetSequence (String FileNameSequence) throws Exception {
        int i;
        RandomAccessFile FileSequence = new RandomAccessFile(FileNameSequence, "rw");
        AmntPrsns = FileSequence.readInt();
        Sqnc = new int[AmntPrsns];
        for (i=0; i<AmntPrsns; i++)
            Sqnc[i] = FileSequence.readInt();
        FileSequence.close();
    }
}
