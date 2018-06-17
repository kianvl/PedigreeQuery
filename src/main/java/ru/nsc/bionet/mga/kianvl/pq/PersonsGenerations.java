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
        PrsnXYRW.setAmntPrsn(PdgrData.getAmntPrsns());
        PrsnXYRW.ArrayData();

        AmntPrsns = 0;
        // выбор индивидов среди потомков семей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            for (j=0; j<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[i]]; j++) {
                PrsnXYRW.setPrsnID(AmntPrsns, FmlsData.OfsprFml[FmlGnrtnData.FmlID[i]][j]);
                PrsnXYRW.setSexID(AmntPrsns, PdgrData.getSexID(PrsnXYRW.getPrsnID(AmntPrsns)));
                PrsnXYRW.setiY(AmntPrsns, FmlGnrtnData.FmlGnrtn[i]+1);
                AmntPrsns++;
            }
        }

        // выбор индивидов среди родителей семей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            for (j=0; j<2; j++)
            M0:{
                for (k=0; k<AmntPrsns; k++) {
                    if (PrsnXYRW.getPrsnID(k) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]]) {
                        g[j] = k;
                        break M0;
                    }
                }
                PrsnXYRW.setPrsnID(AmntPrsns, FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]]);
                PrsnXYRW.setSexID(AmntPrsns, PdgrData.getSexID(PrsnXYRW.getPrsnID(AmntPrsns)));
                PrsnXYRW.setiY(AmntPrsns, FmlGnrtnData.FmlGnrtn[i]);
                g[j] = AmntPrsns;
                AmntPrsns++;
            }
            if (PrsnXYRW.getiY(g[0]) != PrsnXYRW.getiY(g[1]))
                return false;
        }

        PrsnXYRW.setAmntPrsn(AmntPrsns);

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
