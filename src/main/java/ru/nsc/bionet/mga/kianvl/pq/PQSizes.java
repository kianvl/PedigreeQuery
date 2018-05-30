// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.RandomAccessFile;

class PQSizes extends JFrame {
    private JFrame PQSzs = this;
    private int Szs[] = {20, 10, 12, 12, 15, 6, 8, 10};
//	int SS = 20; //Размер символа
//	int SD = 10; //Расстояние между символами по горизонтали
//	int SP = 12; //Расстояние от символа до подписи.
//	int PL = 12; //Расстояние от нижней подписи, до ближайшей к ней горизонтальной линии
//	int SV = 15; //Высота индивидуальной сибовой
//	int LD = 6; //Расстояние между линиями
//	int AS = 8; //Диаметр дуги
//	размер шрифта
    private int AS2;
    private int SSD; //SS + SD
    private int SS2; //SS/2;
    private int S;
    private int KrayY = 100;
    private int KrayX = 100;


    private void RWData(char rw) throws Exception {
        int i;
        if (rw!='r' & rw!='w') return;
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
        if (rw == 'r') {
            for (i=0; i<8; i++)
                Szs[i] = PQSizeFile.readInt();
        }
        else {
            for (i=0; i<8; i++)
                PQSizeFile.writeInt(Szs[i]);
        }
        PQSizeFile.close();
    }


    private void PQSetSize (int n) {
        boolean b;
        String s;

        M1:{
            b = false;
            do {
                M0:{
                    s = (String)JOptionPane.showInputDialog(PQSzs, "Please enter new value");
                    if (s.equals(""))
                        break M1;
                    try {
                        Szs[n] = Integer.parseInt(s);
                    }
                    catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(PQSzs, "Invalid format! Please try again.");
                        break M0;
                    }
                    b = true;
                }
            } while (!b);
            PQSzs.repaint();
            PQSzs.setVisible(false);
            PQSzs.setVisible(true);
            try {
                RWData('w');
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(PQSzs, "" + e);
            }
        }
    }


    public PQSizes() {
        super("Sizes");
        WindowListener wL = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        };
        addWindowListener(wL);

        try {
            RWData('r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PQSzs, "" + e);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                int i;
                int meX, meY;
                meX = me.getX();
                meY = me.getY();

                if (me.getButton() == 1) {
                    i = 0;
                    //Размер значка
                    if (meX>KrayX+SSD & meX<KrayX+SSD+15 & meY<KrayY+S*2+Szs[0]+40 & meY>KrayY+S*2+Szs[0]+30)
                        i = 1;
                    //Расстояние между значками
                    if (meX>KrayX+5*SSD+Szs[0] & meX<KrayX+5*SSD+Szs[0]+15 & meY<KrayY+S+Szs[0]+40 & meY>KrayY+S+Szs[0]+30)
                        i = 2;
                    //Расстояние от символа до подписи.
                    if (meX>KrayX-45 & meX<KrayX-30 & meY<KrayY+Szs[0]+Szs[2] & meY>KrayY+Szs[0]+Szs[2]-10)
                        i = 3;
                    //Размер шрифта
//					if (meX>KrayX+6*SSD+Szs[0]+45 & meX<KrayX+6*SSD+Szs[0]+60 & meY<KrayY+Szs[0]+Szs[2] & meY>KrayY+Szs[0]+Szs[2]-10)
//						i = 8;
                    //Расстояние от нижней подписи, до ближайшей к ней горизонтальной линии
                    if (meX>KrayX-75 & meX<KrayX-60 & meY<KrayY+Szs[0]+Szs[2]+Szs[3] & meY>KrayY+Szs[0]+Szs[2]+Szs[3]-10)
                        i = 4;
                    //Высота индивидуальной сибовой
                    if (meX>KrayX+6*SSD+Szs[0]+30 & meX<KrayX+6*SSD+Szs[0]+45 & meY<KrayY+S & meY>KrayY+S-10)
                        i = 5;
                    //Расстояние между линиями
                    if (meX>KrayX+3*SSD-Szs[1]/2-20 & meX<KrayX+3*SSD-Szs[1]/2-5 & meY<KrayY-25 & meY>KrayY-35)
                        i = 6;
                    //Диаметр дуги
                    if (meX>KrayX+3*SSD+SS2+5 & meX<KrayX+3*SSD+SS2+20 & meY<KrayY-25 & meY>KrayY-35)
                        i = 7;
                    if (i > 0)
                        PQSetSize (i-1);
                }
            }
        });
    }


    public void paint (Graphics g) {
        String s;

        SSD = Szs[0] + Szs[1];
        SS2 = Szs[0]/2;
        AS2 = Szs[6]/2;

        g.drawRect(KrayX, KrayY, Szs[0], Szs[0]);
        g.drawOval(KrayX+2*SSD, KrayY, Szs[0], Szs[0]);
        g.drawOval(KrayX+4*SSD, KrayY, Szs[0], Szs[0]);
        g.drawRect(KrayX+6*SSD, KrayY, Szs[0], Szs[0]);
        S = Szs[0]+Szs[2]+Szs[3]+Szs[4]+Szs[5];
        g.drawOval(KrayX, KrayY+S, Szs[0], Szs[0]);
        g.drawRect(KrayX+2*SSD, KrayY+S, Szs[0], Szs[0]);
        g.drawRect(KrayX+3*SSD, KrayY+S, Szs[0], Szs[0]);
        g.drawOval(KrayX+4*SSD, KrayY+S, Szs[0], Szs[0]);
        g.drawRect(KrayX+5*SSD, KrayY+S, Szs[0], Szs[0]);
        g.drawRect(KrayX+6*SSD, KrayY+S, Szs[0], Szs[0]);
        g.drawRect(KrayX+SSD, KrayY+S*2, Szs[0], Szs[0]);

        g.drawLine(KrayX+Szs[0], KrayY+SS2, KrayX+2*SSD, KrayY+SS2);
        g.drawLine(KrayX+4*SSD+Szs[0], KrayY+SS2, KrayX+6*SSD, KrayY+SS2);
        g.drawLine(KrayX+Szs[0], KrayY+SS2+S, KrayX+2*SSD, KrayY+SS2+S);

        g.drawLine(KrayX+SS2, KrayY+S, KrayX+SS2, KrayY+S-Szs[4]-Szs[5]);
        g.drawLine(KrayX+2*SSD+SS2, KrayY+S, KrayX+2*SSD+SS2, KrayY+S-Szs[4]);
        g.drawLine(KrayX+3*SSD+SS2, KrayY+S, KrayX+3*SSD+SS2, KrayY+S-Szs[4]-Szs[5]);
        g.drawLine(KrayX+4*SSD+SS2, KrayY+S, KrayX+4*SSD+SS2, KrayY+S-Szs[4]);
        g.drawLine(KrayX+5*SSD+SS2, KrayY+S, KrayX+5*SSD+SS2, KrayY+S-Szs[4]);
        g.drawLine(KrayX+6*SSD+SS2, KrayY+S, KrayX+6*SSD+SS2, KrayY+S-Szs[4]);

        g.drawLine(KrayX+SS2, KrayY+S-Szs[4]-Szs[5], KrayX+3*SSD+SS2, KrayY+S-Szs[4]-Szs[5]);
        g.drawLine(KrayX+2*SSD+SS2, KrayY+S-Szs[4], KrayX+3*SSD+SS2-AS2, KrayY+S-Szs[4]);
        g.drawLine(KrayX+3*SSD+SS2+AS2, KrayY+S-Szs[4], KrayX+6*SSD+SS2, KrayY+S-Szs[4]);
        g.drawArc(KrayX+3*SSD+SS2-AS2, KrayY+S-Szs[4]-AS2, Szs[6], Szs[6], 0, 180);

        g.drawLine(KrayX+SSD+SS2, KrayY+SS2, KrayX+SSD+SS2, KrayY+S-Szs[4]-Szs[5]);
        g.drawLine(KrayX+5*SSD+SS2, KrayY+SS2, KrayX+5*SSD+SS2, KrayY+S-Szs[4]);
        g.drawLine(KrayX+SSD+SS2, KrayY+SS2+S, KrayX+SSD+SS2, KrayY+S*2);

        g.drawString("Abc", KrayX, KrayY+Szs[0]+Szs[2]);
        g.drawString("Abc", KrayX+6*SSD, KrayY+Szs[0]+Szs[2]);

        //Размер значка
        g.drawLine(KrayX+SSD, KrayY+S*2, KrayX+SSD, KrayY+S*2+Szs[0]+30);
        g.drawLine(KrayX+SSD+Szs[0], KrayY+S*2, KrayX+SSD+Szs[0], KrayY+S*2+Szs[0]+30);
        g.drawLine(KrayX+SSD-5, KrayY+S*2+Szs[0]+25, KrayX+SSD+Szs[0]+5, KrayY+S*2+Szs[0]+25);
        s = "";
        s += Szs[0];
        g.drawString(s, KrayX+SSD, KrayY+S*2+Szs[0]+40);

        //Расстояние между значками
        g.drawLine(KrayX+5*SSD+Szs[0], KrayY+S, KrayX+5*SSD+Szs[0], KrayY+S+Szs[0]+30);
        g.drawLine(KrayX+6*SSD, KrayY+S, KrayX+6*SSD, KrayY+S+Szs[0]+30);
        g.drawLine(KrayX+5*SSD+Szs[0]-5, KrayY+S+Szs[0]+25, KrayX+6*SSD+5, KrayY+S+Szs[0]+25);
        s = "";
        s += Szs[1];
        g.drawString(s, KrayX+5*SSD+Szs[0], KrayY+S+Szs[0]+40);

        //Размер шрифта
//		g.drawLine(KrayX+6*SSD, KrayY+Szs[0]+Szs[2], KrayX+6*SSD+Szs[0]+60, KrayY+Szs[0]+Szs[2]);
//		s = "";
//		s += Szs[7];
//		g.drawString(s, KrayX+6*SSD+Szs[0]+45, KrayY+Szs[0]+Szs[2]);

        //Расстояние от символа до подписи.
        g.drawLine(KrayX, KrayY+Szs[0], KrayX-30, KrayY+Szs[0]);
        g.drawLine(KrayX, KrayY+Szs[0]+Szs[2], KrayX-60, KrayY+Szs[0]+Szs[2]);
        g.drawLine(KrayX-25, KrayY+Szs[0]-5, KrayX-25, KrayY+Szs[0]+Szs[2]+5);
        s = "";
        s += Szs[2];
        g.drawString(s, KrayX-45, KrayY+Szs[0]+Szs[2]);

        //Расстояние от нижней подписи, до ближайшей к ней горизонтальной линии
        g.drawLine(KrayX+SS2, KrayY+Szs[0]+Szs[2]+Szs[3], KrayX-60, KrayY+Szs[0]+Szs[2]+Szs[3]);
        g.drawLine(KrayX-55, KrayY+Szs[0]+Szs[2]-5, KrayX-55, KrayY+Szs[0]+Szs[2]+Szs[3]+5);
        s = "";
        s += Szs[3];
        g.drawString(s, KrayX-75, KrayY+Szs[0]+Szs[2]+Szs[3]);

        //Высота индивидуальной сибовой
        g.drawLine(KrayX+6*SSD, KrayY+S, KrayX+6*SSD +Szs[0]+30, KrayY+S);
        g.drawLine(KrayX+6*SSD+SS2, KrayY+S-Szs[4], KrayX+6*SSD +Szs[0]+30, KrayY+S-Szs[4]);
        g.drawLine(KrayX+6*SSD +Szs[0]+25, KrayY+S-Szs[4]-5, KrayX+6*SSD+Szs[0]+25, KrayY+S+5);
        s = "";
        s += Szs[4];
        g.drawString(s, KrayX+6*SSD+Szs[0]+30, KrayY+S);

        //Расстояние между линиями
        g.drawLine(KrayX+3*SSD-Szs[1]/2, KrayY+S-Szs[4]+5, KrayX+3*SSD-Szs[1]/2, KrayY-25);
        g.drawLine(KrayX+3*SSD-Szs[1]/2, KrayY-25, KrayX+3*SSD-Szs[1]/2-20, KrayY-25);
        s = "";
        s += Szs[5];
        g.drawString(s, KrayX+3*SSD-Szs[1]/2-20, KrayY-25);

        //Диаметр дуги
        g.drawLine(KrayX+3*SSD+SS2, KrayY+S, KrayX+3*SSD+SS2, KrayY-25);
        g.drawLine(KrayX+3*SSD+SS2, KrayY-25, KrayX+3*SSD+SS2+20, KrayY-25);
        s = "";
        s += Szs[6];
        g.drawString(s, KrayX+3*SSD+SS2+5, KrayY-25);
    }
}
