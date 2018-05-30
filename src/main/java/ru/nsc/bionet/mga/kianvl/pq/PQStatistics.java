// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.RandomAccessFile;

class PQStatistics extends JFrame {
    JFrame PQSttst = this;
    private int NPersons, CrntNPrsns, NFmls, CrntFmls;

    boolean RWData (char rw) throws Exception {
        int i;
        if (rw!='r' & rw!='w') return false;
        RandomAccessFile PQSizeFile = new RandomAccessFile("PQSize.ini", "rw");
        if (rw == 'r')
            M0:{
                if (PQSizeFile.length() == 0) {
                    rw = 'w';
                    break M0;
                }
            }
        else
            PQSizeFile.setLength(0);
        PQSizeFile.close();
        return true;
    }

    public PQStatistics() {
        super("Statistics");
        WindowListener wL = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        };
        addWindowListener(wL);

        String FlNmPdgrFmls = "temp/Pedigree.tmp";
        String FlNmFamilies = "temp/Families.tmp";
        String FlNmFmlGnrtn = "temp/FmlGnrtn.tmp";
        String FlNmPrsnsXY = "temp/PrsnsXY.tmp";

        PedigreeData PdgrData = new PedigreeData();

        //чтение данных о родственных связях в родословной
        try {
            PdgrData.RWData(FlNmPdgrFmls, 'r');
        }
        catch (Exception e) {
//			JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        NPersons = PdgrData.AmntPrsns;

        PersonXYRW PrsnXYRW = new PersonXYRW();
        try {
            PrsnXYRW.RWData(FlNmPrsnsXY, 'r');
        }
        catch (Exception e) {
//			JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        CrntNPrsns = PrsnXYRW.AmntPrsn;

        FamiliesData FmlData = new FamiliesData();
        try {
            FmlData.RWData(FlNmFamilies, 'r');
        }
        catch (Exception e) {
//			JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        NFmls = FmlData.AmntFml;

        FamiliesGenerationsRW FmlGnrtn = new FamiliesGenerationsRW();
        try {
            FmlGnrtn.RWData(FlNmFmlGnrtn, 'r');
        }
        catch (Exception e) {
//			JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        CrntFmls = FmlGnrtn.AmntFml;
    }


    public void paint (Graphics g) {
        String s;

        s = "Statistics on drawing pedigree.";
        g.drawString(s, 40, 70);

        s = "Drawing pedigree consists of ";
        s += NPersons;
        s += " persons, and ";
        s += NFmls;
        s += " families.";
        g.drawString(s, 40, 100);

        s = "Drawn shape of current pedigree consists of ";
        s += CrntNPrsns;
        s += " persons, and ";
        s += CrntFmls;
        s += " families.";
        g.drawString(s, 40, 125);
    }
}
