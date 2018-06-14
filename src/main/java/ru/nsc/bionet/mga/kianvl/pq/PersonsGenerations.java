// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

// поколения отдельных индивидов
class PersonsGenerations {
    boolean SeekGenerations (JFrame ParentFrame, String FilePdgrName, String FileFmlGnrtName, String FileFmlDataName, String FilePrsnXYName) {
        int AmntPrsns;
        int i, j, k, g[];
        g = new int[2];

        // чтение данных о всей родословной
        PedigreeData PdgrData = new PedigreeData();
        try {
            PdgrData.RWData(FilePdgrName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        // чтение данных о поколениях семей
        FamiliesGenerationsRW FmlGnrtnData = new FamiliesGenerationsRW();
        try {
            FmlGnrtnData.RWData(FileFmlGnrtName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        // чтение данных о семьях
        FamiliesData FmlsData = new FamiliesData();
        try {
            FmlsData.RWData(FileFmlDataName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        // начало работы с индивидуальными поколениями
        PersonXYRW PrsnXYRW = new PersonXYRW();
        PrsnXYRW.AmntPrsn = PdgrData.getAmntPrsns();
        PrsnXYRW.ArrayData();

        AmntPrsns = 0;
        // выбор индивидов среди потомков семей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            for (j=0; j<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[i]]; j++) {
                PrsnXYRW.PrsnID[AmntPrsns] = FmlsData.OfsprFml[FmlGnrtnData.FmlID[i]][j];
                PrsnXYRW.SexID[AmntPrsns] = PdgrData.getSexID(PrsnXYRW.PrsnID[AmntPrsns]);
                PrsnXYRW.iY[AmntPrsns] = FmlGnrtnData.FmlGnrtn[i] + 1;
                AmntPrsns++;
            }
        }

        // выбор индивидов среди родителей семей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            for (j=0; j<2; j++)
            M0:{
                for (k=0; k<AmntPrsns; k++) {
                    if (PrsnXYRW.PrsnID[k] == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]]) {
                        g[j] = k;
                        break M0;
                    }
                }
                PrsnXYRW.PrsnID[AmntPrsns] = FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]];
                PrsnXYRW.SexID[AmntPrsns] = PdgrData.getSexID(PrsnXYRW.PrsnID[AmntPrsns]);
                PrsnXYRW.iY[AmntPrsns] = FmlGnrtnData.FmlGnrtn[i];
                g[j] = AmntPrsns;
                AmntPrsns++;
            }
            if (PrsnXYRW.iY[g[0]] != PrsnXYRW.iY[g[1]])
                return false;
        }

        PrsnXYRW.AmntPrsn = AmntPrsns;

        try {
            PrsnXYRW.RWData(FilePrsnXYName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        return true;
    }
}
