// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.RandomAccessFile;

class PQSetColor extends JFrame {
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
    private String SClr[];
    private int AmntClr = 0;
    private int ClrCds[][];
    private Font f;

    private void PQStClrs(int n, int m) {
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

        ClrCds[n][m] = k;

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

    private void RData() throws Exception {
        int i;
        RandomAccessFile PQPrjctFile = new RandomAccessFile("Projects/" + ProjectName + "/Project.ini", "r");

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
        PQPrjctFile.close();
    }

    boolean RWData (char rw) throws Exception {
        int i, j;
        if (rw!='r' & rw!='w') return false;
        RandomAccessFile PQPrjctFile = new RandomAccessFile("Projects/" + ProjectName + "/Color.ini", "rw");

        if (rw == 'r') {
            AmntClr = PQPrjctFile.readInt();
            ClrCds = new int[AmntClr][];
            SClr = new String[AmntClr];
            for (i=0; i<AmntClr; i++) {
                SClr[i] = PQPrjctFile.readUTF();
                ClrCds[i] = new int[3];
                for (j=0; j<3; j++)
                    ClrCds[i][j] = PQPrjctFile.readInt();
            }
        }
        else {
            PQPrjctFile.setLength(0);
            PQPrjctFile.writeInt(AmntClr);
            for (i=0; i<AmntClr; i++) {
                PQPrjctFile.writeUTF(SClr[i]);
                for (j=0; j<3; j++)
                    PQPrjctFile.writeInt(ClrCds[i][j]);
            }
        }
        PQPrjctFile.close();
        return true;
    }

    public PQSetColor (String ProjectName) {
        super("Set colors for project - " + ProjectName);
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
            RData();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PQPrjct, "" + e);
        }
        if (SCDtNm.equals("") | CIDC==0 | CDtC==0) {
            JOptionPane.showMessageDialog(PQPrjct, "At first you need to setup file!");
            setVisible(false);
            return;
        }
        File parent = new File("Projects/" + ProjectName + "/Color.ini");
        if (!parent.exists()) {
            PQProjectDataR PQPrjctDt = new PQProjectDataR(PQPrjct, 2);
            PQPrjctDt.ClmnIDs[0] = CIDC;
            PQPrjctDt.ClmnIDs[1] = CDtC;
            try {
                PQPrjctDt.ReadData("Projects/" + ProjectName + "/" + SCDtNm);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog (PQPrjct, "" + e);
            }
            int i, j;
            SClr = new String[PQPrjctDt.Nstr-1];
            SClr[0] = PQPrjctDt.PQPrjctDt[1][1];
            AmntClr = 1;
            for (i=2; i<PQPrjctDt.Nstr; i++)
                M0:{
                    for (j=0; j<AmntClr; j++) {
                        if (SClr[j].equals(PQPrjctDt.PQPrjctDt[i][1]))
                            break M0;
                    }
                    SClr[AmntClr] = PQPrjctDt.PQPrjctDt[i][1];
                    AmntClr++;
                }
            ClrCds = new int[AmntClr][];
            for (i=0; i<AmntClr; i++) {
                ClrCds[i] = new int[3];
                for (j=0; j<3; j++)
                    ClrCds[i][j] = 255;
            }
            try {
                RWData('w');
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(PQPrjct, "" + e);
            }
        }
        else
        {
            try {
                RWData('r');
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(PQPrjct, "" + e);
            }
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int i, j;
                int meX, meY;
                meX = me.getX();
                meY = me.getY();

                if (me.getButton() == 1) {
                    if (meX>180 & meX<300 & meY>60 & meY<60+40*AmntClr) {
                        for (i=0; i<AmntClr; i++) {
                            if (meY>75+40*i & meY<90+40*i) {
                                for (j=0; j<3; j++) {
                                    if (meX>200+40*j & meX<230+40*j)
                                        PQStClrs(i,j);
                                }
                            }
                        }
                    }
                }
            }
        });
    }


    public void paint (Graphics g) {
        if (AmntClr > 0) {
            int i, j;
            Color cl;

            g.drawString("Feature", 20, 50);
            g.drawLine(180, 0, 180, 60+40*AmntClr);
            g.drawString("Color code", 210, 50);
            g.drawLine(320, 0, 320, 60+40*AmntClr);
            g.drawString("Samples", 345, 50);
            g.drawLine(0, 60, 430, 60);
            for (i=0; i<AmntClr; i++) {
                g.drawString(SClr[i], 20, 90+40*i);
                for (j=0; j<3; j++)
                    g.drawString("" + ClrCds[i][j], 200+40*j, 90+40*i);
                cl = new Color(ClrCds[i][0], ClrCds[i][1], ClrCds[i][2]);
                g.setColor(cl);
                g.fillRect(345, 75+40*i, 20, 20);
                g.fillOval(385, 75+40*i, 20, 20);
                g.setColor(Color.black);
                g.drawRect(345, 75+40*i, 20, 20);
                g.drawOval(385, 75+40*i, 20, 20);
                g.drawLine(0, 100+40*i, 430, 100+40*i);
            }
        }
    }
}
