// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;

class PQProjects extends JFrame {
    private JFrame PQPrjct = this;
    private String ProjectName;
    private String PdgrDtNm = "";
    private int IDC=0, FthC=0, MthC=0, SexC=0;
    private String DcsdDtNm = "";
    private int DIDC=0, DDtC=0;
    private String SCDtNm = "";
    private int CIDC=0, CDtC=0;
    private String SgntrNm[] = new String[16];
    private int SIDC[] = new int [16];
    private int SDtC[] = new int [16];
    int AmntSgntr = 0;
    private Font f;


    boolean RWData (char rw) throws Exception {
        int i;
        if (rw!='r' & rw!='w') return false;
        RandomAccessFile PQPrjctFile = new RandomAccessFile("Projects/" + ProjectName + "/Project.ini", "rw");
        if (rw == 'r')
            M0:{
                if (PQPrjctFile.length() == 0) {
                    rw = 'w';
                    for (i=0; i<16; i++)
                        SgntrNm[i] = "";
                    break M0;
                }
            }
        else
            PQPrjctFile.setLength(0);
        if (rw == 'r') {
            PdgrDtNm = PQPrjctFile.readUTF();
            IDC = PQPrjctFile.readInt();
            FthC = PQPrjctFile.readInt();
            MthC = PQPrjctFile.readInt();
            SexC = PQPrjctFile.readInt();

            DcsdDtNm = PQPrjctFile.readUTF();
            DIDC = PQPrjctFile.readInt();
            DDtC = PQPrjctFile.readInt();

            SCDtNm = PQPrjctFile.readUTF();
            CIDC = PQPrjctFile.readInt();
            CDtC = PQPrjctFile.readInt();

            for (i=0; i<16; i++) {
                SgntrNm[i] = PQPrjctFile.readUTF();
                SIDC[i] = PQPrjctFile.readInt();
                SDtC[i] = PQPrjctFile.readInt();
            }
        }
        else {
            PQPrjctFile.writeUTF(PdgrDtNm);
            PQPrjctFile.writeInt(IDC);
            PQPrjctFile.writeInt(FthC);
            PQPrjctFile.writeInt(MthC);
            PQPrjctFile.writeInt(SexC);

            PQPrjctFile.writeUTF(DcsdDtNm);
            PQPrjctFile.writeInt(DIDC);
            PQPrjctFile.writeInt(DDtC);

            PQPrjctFile.writeUTF(SCDtNm);
            PQPrjctFile.writeInt(CIDC);
            PQPrjctFile.writeInt(CDtC);

            for (i=0; i<16; i++) {
                PQPrjctFile.writeUTF(SgntrNm[i]);
                PQPrjctFile.writeInt(SIDC[i]);
                PQPrjctFile.writeInt(SDtC[i]);
            }
        }
        PQPrjctFile.close();
        return true;
    }


    private void PQPrjctFNs(int n) {
        String s, s0;
        int i;
        JFileChooser fc = new JFileChooser("./Projects/" + ProjectName + "/");
        fc.showOpenDialog(PQPrjct);
        if (fc.getSelectedFile() == null)
            return;
        s = fc.getSelectedFile().getName();

        if (n == 16) {
            s0 = PdgrDtNm;
            PdgrDtNm = s;
//			IDC = FthC = MthC = SexC = 0;
            if (DcsdDtNm.equals(s0))
                DcsdDtNm = s;
            if (SCDtNm.equals(s0))
                SCDtNm = s;
            for (i=0; i<16; i++) {
                if (SgntrNm[i].equals(s0))
                    SgntrNm[i] = s;
            }
        }
        if (n == 17) {
            DcsdDtNm = s;
            DIDC = DDtC = 0;
        }
        if (n == 18) {
            SCDtNm = s;
            CIDC = CDtC = 0;
            File parent = new File("Projects/" + ProjectName + "/Color.ini");
            parent.delete();
        }
        if (n < 16) {
            SgntrNm[n] = s;
            SIDC[n] = SDtC[n] = 0;
        }

        PQPrjct.repaint();
        PQPrjct.setVisible(false);
        PQPrjct.setVisible(true);

        try {
            RWData('w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PQPrjct, "" + e);
        }
    }


    private void PQPrjctRmvFNs(int n) {
        Object[] options = {"Yes", "No"};
        int NJO = JOptionPane.showOptionDialog(null, "Do you realy want to remove the file from the project?", "", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if (NJO == JOptionPane.YES_OPTION) {
            if (n == 16) {
                PdgrDtNm = "";
                IDC = FthC = MthC = SexC = 0;
            }
            if (n == 17) {
                DcsdDtNm = "";
                DIDC = DDtC = 0;
            }
            if (n == 18) {
                SCDtNm = "";
                CIDC = CDtC = 0;
                File parent = new File("Projects/" + ProjectName + "/Color.ini");
                parent.delete();
            }
            if (n < 16) {
                SgntrNm[n] = "";
                SIDC[n] = SDtC[n] = 0;
            }

            PQPrjct.repaint();
            PQPrjct.setVisible(false);
            PQPrjct.setVisible(true);

            try {
                RWData('w');
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(PQPrjct, "" + e);
            }
        }
    }


    private void PQPrjctStClmn(int n, int m) {
        String s;
        int i, k;

        s = (String)JOptionPane.showInputDialog(PQPrjct, "Please enter new value");
        if (s.equals(""))
            return;
        try {
            k = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(PQPrjct, "Invalid format! Please try again.");
            return;
        }

        if (n == 16) {
            if (m == 0)
                IDC = k;
            if (m == 1)
                FthC = k;
            if (m == 2)
                MthC = k;
            if (m == 3)
                SexC = k;
        }
        if (n == 17) {
            if (m == 0)
                DIDC = k;
            if (m == 1)
                DDtC = k;
        }
        if (n == 18) {
            if (m == 0)
                CIDC = k;
            if (m == 1)
                CDtC = k;
            File parent = new File("Projects/" + ProjectName + "/Color.ini");
            parent.delete();
        }
        for (i=0; i<16; i++) {
            if (n == i) {
                if (m == 0)
                    SIDC[i] = k;
                if (m == 1)
                    SDtC[i] = k;
            }
        }

        PQPrjct.repaint();
        PQPrjct.setVisible(false);
        PQPrjct.setVisible(true);

        try {
            RWData('w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PQPrjct, "" + e);
        }
    }


    public PQProjects(final String ProjectName) {
        super("Project - " + ProjectName);
        WindowListener wL = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        };
        addWindowListener(wL);

        this.ProjectName = ProjectName;
        f = new Font("Dialog", Font.BOLD,16);
        setFont(f);

        try {
            RWData('r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PQPrjct, "" + e);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int i;
                int meX, meY;
                meX = me.getX();
                meY = me.getY();

                if (me.getButton() == 1) {
                    if (meX>280 & meX<500) {
                        if (meY>105 & meY<125)
                            PQPrjctFNs(16);
                        if (meY>180 & meY<200)
                            PQPrjctFNs(17);
                        if (meY>230 & meY<250)
                            PQPrjctFNs(18);
                        for (i=0; i<16; i++) {
                            if (meY>280+i*30 & meY<300+i*30)
                                PQPrjctFNs(i);
                        }
                    }
                    else {
                        if (meY>100 & meY<120) {
                            if (!PdgrDtNm.equals("")) {
                                if (meX>500 & meX<530)
                                    PQPrjctStClmn(16, 0);
                                if (meX>530 & meX<560)
                                    PQPrjctStClmn(16, 1);
                                if (meX>560 & meX<590)
                                    PQPrjctStClmn(16, 2);
                                if (meX>590 & meX<620)
                                    PQPrjctStClmn(16, 3);
                            }
                        }
                        if (meY>175 & meY<195) {
                            if (!DcsdDtNm.equals("")) {
                                if (meX>500 & meX<530)
                                    PQPrjctStClmn(17, 0);
                                if (meX>530 & meX<560)
                                    PQPrjctStClmn(17, 1);
                            }
                        }
                        if (meY>225 & meY<245) {
                            if (!SCDtNm.equals("")) {
                                if (meX>500 & meX<530)
                                    PQPrjctStClmn(18, 0);
                                if (meX>530 & meX<560)
                                    PQPrjctStClmn(18, 1);
                            }
                        }
                        if (meY>210 & meY<260) {
                            if (meX>570 & meX<620) {
                                JFrame PQStClr;
                                PQStClr = new PQSetColor(ProjectName);
                                PQStClr.setSize(640, 480);
                                PQStClr.repaint();
                                PQStClr.setVisible(true);
                            }
                        }
                        for (i=0; i<16; i++) {
                            if (meY>275+i*30 & meY<295+i*30) {
                                if (!SgntrNm[i].equals("")) {
                                    if (meX>500 & meX<530)
                                        PQPrjctStClmn(i, 0);
                                    if (meX>530 & meX<560)
                                        PQPrjctStClmn(i, 1);
                                }
                            }
                        }
                    }
                }
                if (me.getButton() == 3)
                    M0:{
                        if (meX<280 & meX>500)
                            break M0;
                        if (meY>105 & meY<125)
                            PQPrjctRmvFNs(16);
                        if (meY>180 & meY<200)
                            PQPrjctRmvFNs(17);
                        if (meY>230 & meY<250)
                            PQPrjctRmvFNs(18);
                        for (i=0; i<16; i++) {
                            if (meY>280+i*30 & meY<300+i*30)
                                PQPrjctRmvFNs(i);
                        }
                    }
            }
        });
    }


    public void paint (Graphics g) {
        int i;

        g.drawString("Data on", 120, 50);
        g.drawString("File name", 350, 50);
        g.drawString("Column ##", 520, 50);
        g.drawString("ID", 505, 80);
        g.drawString("Fth", 532, 80);
        g.drawString("Mth", 561, 80);
        g.drawString("Sex", 591, 80);
        g.drawString("ID", 505, 175);
        g.drawString("Data", 535, 175);
        g.drawString("ID", 505, 225);
        g.drawString("Data", 532, 225);
        g.drawString("ID", 505, 275);
        g.drawString("Data", 535, 275);
        g.drawLine(280, 60, 280, 750);
        g.drawLine(500, 60, 500, 750);
        g.drawLine(530, 60, 530, 750);
        g.drawLine(560, 60, 560, 160);
        g.drawLine(590, 60, 590, 160);
        g.drawLine(620, 60, 620, 750);

        g.drawRect(10, 80, 20, 20);
        g.drawOval(70, 80, 20, 20);
        g.drawLine(30, 90, 70, 90);

        g.drawOval(10, 130, 20, 20);
        g.drawRect(40, 130, 20, 20);
        g.drawOval(70, 130, 20, 20);
        g.drawLine(20, 130, 20, 115);
        g.drawLine(50, 130, 50, 115);
        g.drawLine(80, 130, 80, 115);
        g.drawLine(20, 115, 80, 115);
        g.drawLine(50, 90, 50, 115);
        g.drawString("pedigree", 120, 120);
        g.setColor(Color.white);
        g.fillRect(281, 105, 218, 20);
        g.fillRect(501, 105, 28, 20);
        g.fillRect(531, 105, 28, 20);
        g.fillRect(561, 105, 28, 20);
        g.fillRect(591, 105, 28, 20);
        g.setColor(Color.black);
        if (!PdgrDtNm.equals("")) {
            g.drawString(PdgrDtNm, 300, 120);
            g.drawString(""+IDC, 510, 120);
            g.drawString(""+FthC, 540, 120);
            g.drawString(""+MthC, 570, 120);
            g.drawString(""+SexC, 600, 120);
        }
        g.drawLine(0, 160, 620, 160);

        g.drawRect(10, 180, 20, 20);
        g.drawOval(70, 180, 20, 20);
        g.drawLine(32, 178, 8, 202);
        g.drawLine(92, 178, 68, 202);
        g.drawString("deceased persons", 120, 195);
        g.setColor(Color.white);
        g.fillRect(281, 180, 218, 20);
        g.fillRect(501, 180, 28, 20);
        g.fillRect(531, 180, 38, 20);
        g.setColor(Color.black);
        if (!DcsdDtNm.equals("")) {
            g.drawString(DcsdDtNm, 300, 195);
            g.drawString(""+DIDC, 510, 195);
            g.drawString(""+DDtC, 540, 195);
        }
        g.drawLine(0, 210, 620, 210);

        g.setColor(Color.red);
        g.fillRect(10, 230, 20, 20);
        g.setColor(Color.blue);
        g.fillOval(70, 230, 20, 20);
        g.setColor(Color.black);
        g.drawRect(10, 230, 20, 20);
        g.drawOval(70, 230, 20, 20);
        g.drawString("symbols colors", 120, 245);
        g.setColor(Color.white);
        g.fillRect(281, 230, 218, 20);
        g.fillRect(501, 230, 28, 20);
        g.fillRect(531, 230, 38, 20);
        g.setColor(Color.black);
        if (!SCDtNm.equals("")) {
            g.drawString(SCDtNm, 300, 245);
            g.drawString(""+CIDC, 510, 245);
            g.drawString(""+CDtC, 535, 245);
        }
        g.drawLine(570, 210, 570, 260);
        g.drawString("Set", 580, 225);
        g.drawString("colors", 572, 245);
        g.drawLine(0, 260, 620, 260);

        g.drawRect(10, 280, 20, 20);
        g.drawOval(70, 280, 20, 20);
        g.drawString("abc", 10, 320);
        g.drawString("def", 70, 320);
        for (i=0; i<16; i++) {
            g.drawString("Output info #" + (i+1), 120, 295+i*30);
            g.setColor(Color.white);
            g.fillRect(281, 280+i*30, 218, 20);
            g.fillRect(501, 280+i*30, 28, 20);
            g.fillRect(531, 280+i*30, 38, 20);
            g.setColor(Color.black);
            if (!SgntrNm[i].equals("")) {
                g.drawString(SgntrNm[i], 300, 295+i*30);
                g.drawString(""+SIDC[i], 510, 295+i*30);
                g.drawString(""+SDtC[i], 540, 295+i*30);
            }
        }
    }
}
