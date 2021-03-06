// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.io.File;

class NewLinkageData {
    private JFrame ParentFrame;

    boolean DoNewData (File EPSFile, String FilePdgrName, String FilePrsnXYName) {
        int i, j;

        // чтение данных о всей родословной
        PedigreeData PdgrData = new PedigreeData();
        try {
            PdgrData.RWData(FilePdgrName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        // чтение данных о выбранных индивидах
        PersonXYRW PrsnXYRW = new PersonXYRW();
        try {
            PrsnXYRW.RWData(FilePrsnXYName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        LinkageDataW LnkgDataW = new LinkageDataW();

        LnkgDataW.AmntPrsns = PrsnXYRW.getAmntPrsn();

        LnkgDataW.ArrayData();
        for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
            LnkgDataW.PrsnID[i] = PdgrData.getPrsnID(PrsnXYRW.getPrsnID(i));
            for (j=0; j<2; j++) {
                if (PdgrData.getPrntID(j, PrsnXYRW.getPrsnID(i)) == 0)
                    LnkgDataW.PrntsID[j][i] = "0";
                else
                    LnkgDataW.PrntsID[j][i] = PdgrData.getPrsnID(PdgrData.getPrntID(j, PrsnXYRW.getPrsnID(i)));
            }
            if (PdgrData.getSexID(PrsnXYRW.getPrsnID(i)) == 1)
                LnkgDataW.SexID[i] = "male";
            else
                LnkgDataW.SexID[i] = "female";
        }

        try {
            LnkgDataW.WData (EPSFile);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        return true;
    }
}
