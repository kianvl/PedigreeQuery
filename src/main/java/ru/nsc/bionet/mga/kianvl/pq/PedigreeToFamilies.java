// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

//разбивка родословной на ядерные семьи
class PedigreeToFamilies {
    private String FileInPutName;
    private String FileOutPutName;

    PedigreeToFamilies (String FileInPutName, String FileOutPutName) {
        this.FileInPutName = FileInPutName;
        this.FileOutPutName = FileOutPutName;
    }

    void PedToFam(JFrame ParentFrame) {
        int i, j, k;

        PedigreeData PdgrData = new PedigreeData();

        //чтение данных о родственных связях в родословной
        try {
            PdgrData.RWData(FileInPutName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }

        FamiliesData FmlData = new FamiliesData();

        FmlData.PrntFml[0] = new int[PdgrData.getAmntPrsns()];
        FmlData.PrntFml[1] = new int[PdgrData.getAmntPrsns()];
        FmlData.AmntOfsprFml = new int[PdgrData.getAmntPrsns()];

        FmlData.AmntFml = 0;
        for (i=0; i<PdgrData.getAmntPrsns(); i++) {
            if (PdgrData.getPrntID(0, i)!=0 | PdgrData.getPrntID(1, i)!=0)
            M0:{
                for (j=0; j<FmlData.AmntFml; j++) {
                    if ((PdgrData.getPrntID(0, i)==FmlData.PrntFml[0][j]) & (PdgrData.getPrntID(1, i)==FmlData.PrntFml[1][j])) {
                        FmlData.AmntOfsprFml[j]++;
                        break M0;
                    }
                }
                FmlData.PrntFml[0][FmlData.AmntFml] = PdgrData.getPrntID(0, i);
                FmlData.PrntFml[1][FmlData.AmntFml] = PdgrData.getPrntID(1, i);
                FmlData.AmntOfsprFml[FmlData.AmntFml] = 1;
                FmlData.AmntFml++;
            }
        }

        FmlData.OfsprFml = new int[FmlData.AmntFml][];

        for (i=0; i<FmlData.AmntFml; i++) {
            k = 0;
            FmlData.OfsprFml[i] = new int[FmlData.AmntOfsprFml[i]];
            for (j=0; j<PdgrData.getAmntPrsns(); j++) {
                if ((PdgrData.getPrntID(0, j)==FmlData.PrntFml[0][i]) & (PdgrData.getPrntID(1, j)==FmlData.PrntFml[1][i])) {
                    FmlData.OfsprFml[i][k] = j;
                    k++;
                }
            }
        }

        try {
            FmlData.RWData(FileOutPutName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }
    }
}
