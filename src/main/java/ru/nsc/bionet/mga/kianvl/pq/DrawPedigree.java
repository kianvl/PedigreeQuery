// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.RandomAccessFile;

class DrawPedigree extends JPanel
{
    private Dimension DPdim;
    private JFrame ParentFrame;
    Color cvet0, cvet1;
    private String FilePrsnXYName;
    private String PdgrLnsFileName;
    String FilePdgrName;
    private int DimX, DimY;

    private int SS = 20; //Размер символа
    private int SD = 10; //Расстояние между символами по горизонтали
    private int SP = 12; //Расстояние от символа до подписи.
    private int PL = 12; //Расстояние от нижней подписи, до ближайшей к ней горизонтальной линии
    private int SV0 = 15; //Высота индивидуальной сибовой
    private int SV[];
    private int LD = 6;//Расстояние между линиями
    private int AS = 8;//Диаметр дуги
    private int AS2;
    private int SSD; //SS + SD
    private int SS2; //SS/2;
    int AmntPrsn;
    int PrsnXY[][];
    int PrsnID[];
    int RqdPrsnID;
    boolean bNxt[];


    boolean RWData (char rw) throws Exception
    {
        int i;
        if (rw!='r' & rw!='w') return false;
        RandomAccessFile PQSizeFile = new RandomAccessFile("PQSize.ini", "rw");
        if (rw == 'r')
            M0:{
                if (PQSizeFile.length() == 0)
                {
                    rw = 'w';
                    break M0;
                }
            }
        else
        {
            PQSizeFile.setLength(0);
        }
        if (rw == 'r')
        {
            SS = PQSizeFile.readInt();
            SD = PQSizeFile.readInt();
            SP = PQSizeFile.readInt();
            PL = PQSizeFile.readInt();
            SV0 = PQSizeFile.readInt();
            LD = PQSizeFile.readInt();
            AS = PQSizeFile.readInt();
        }
        else
        {
            PQSizeFile.writeInt(SS);
            PQSizeFile.writeInt(SD);
            PQSizeFile.writeInt(SP);
            PQSizeFile.writeInt(PL);
            PQSizeFile.writeInt(SV0);
            PQSizeFile.writeInt(LD);
            PQSizeFile.writeInt(AS);
        }
        PQSizeFile.close();
        return true;
    }


    public DrawPedigree(JFrame ParentFrame, String FilePrsnXYName, String PdgrLnsFileName)
    {
        super();
        DimX = DimY = 1;
        DPdim = new Dimension(DimX, DimY);
        this.ParentFrame = ParentFrame;
        this.FilePrsnXYName = FilePrsnXYName;
        this.PdgrLnsFileName = PdgrLnsFileName;
        setOpaque(true);
        setBackground(Color.white);
    }


    public Dimension getPreferredSize()
    {
        return DPdim;
    }


    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (FilePrsnXYName == null) return;
        {
            File tempf = new File (FilePrsnXYName);
            if (!tempf.exists())
                return;
        }

        /*
        PersonXYRW PrsnXYRW = new PersonXYRW();
        try
        {
            PrsnXYRW.RWData(FilePrsnXYName, 'r');
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }

        try
        {
            RWData('r');
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        int i, j, k, l;
        int iX1, iY1, iX2, iY2;
        int PdgrMxX, PdgrMxY;
        int GnrtLvl[];

        AmntPrsn = PrsnXYRW.AmntPrsn;
        PrsnXY = new int[AmntPrsn][];
        for (i=0; i<AmntPrsn; i++)
            PrsnXY[i] = new int[2];
        PrsnID = new int[AmntPrsn];
        bNxt = new boolean[AmntPrsn];
        */

        SSD = SS + SD;
        SS2 = SS / 2;
        AS2 = AS / 2;

        //Чтение дополнительных обозначений
        /*
        PQFullDataRW PQFlDtRW;
        PQFlDtRW = new PQFullDataRW();
        try
        {
            PQFlDtRW.RWData ("temp/FullData.tmp", 'r');
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        PdgrMxX = PdgrMxY = 0;
        for (i=0; i<AmntPrsn; i++)
        {
            if (PrsnXYRW.iX[i] > PdgrMxX)
                PdgrMxX = PrsnXYRW.iX[i];
            if (PrsnXYRW.iY[i] > PdgrMxY)
                PdgrMxY = PrsnXYRW.iY[i];
        }
        PdgrMxX++;
        DimX = (PdgrMxX) * SSD;

        PedigreeLinesRW PdgrLnsRW = new PedigreeLinesRW();
        try
        {
            PdgrLnsRW.RWData(PdgrLnsFileName, 'r');
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        */

        /*
        GnrtLvl = new int[PdgrMxY+1];
        SV = new int[PdgrMxY+1];
        l = 0;
        for (j=0; j<PdgrLnsRW.AmntFml; j++)
        {
            if (PdgrLnsRW.SbLnLvl[j][0] == 1)
            {
                if (PdgrLnsRW.PrntLvl[j] > l)
                    l = PdgrLnsRW.PrntLvl[j];
            }
        }
        GnrtLvl[0] = LD*(l+1);
        for (i=1; i<=PdgrMxY; i++)
        {
            k = 0;
            l = 0;
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            {
                if (PdgrLnsRW.SbLnLvl[j][0] == i)
                {
                    if (PdgrLnsRW.SbLnLvl[j][1] > k)
                        k = PdgrLnsRW.SbLnLvl[j][1];
                    if (PdgrLnsRW.PrOfLnLvl[j] > k)
                        k = PdgrLnsRW.PrOfLnLvl[j];
                }
                if (PdgrLnsRW.SbLnLvl[j][0] == i+1)
                {
                    if (PdgrLnsRW.PrntLvl[j] > l)
                        l = PdgrLnsRW.PrntLvl[j];
                }
            }
            SV[i] = SV0 + l*LD;
            GnrtLvl[i] = GnrtLvl[i-1] + SS + SP*PQFlDtRW.AmntSgn + SV[i] + k*LD + PL;
        }
        DimY = GnrtLvl[PdgrMxY] + SS+SP*PQFlDtRW.AmntSgn+SV[PdgrMxY];

        DPdim = new Dimension(DimX, DimY);

        PedigreeData PdgrData = new PedigreeData();
        try
        {
            PdgrData.RWData(FilePdgrName, 'r');
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }
        */

        String s;

        /*
        k = 0;
        //Индивидуальные символы
        for (i=0; i<AmntPrsn; i++)
        {
            PrsnXY[i][0] = PrsnXYRW.iX[i] * SSD;
            PrsnXY[i][1] = GnrtLvl[PrsnXYRW.iY[i]];
            PrsnID[i] = PrsnXYRW.PrsnID[i];
            bNxt[i] = PrsnXYRW.bNxt[i];
            for (j=0; j<PQFlDtRW.AmntPrsn; j++)
            {
                if (PdgrData.PrsnID[PrsnID[i]].equals(PQFlDtRW.PrsnID[j]))
                {
                    cvet0 = new Color(PQFlDtRW.ClrRGB[j][0], PQFlDtRW.ClrRGB[j][1], PQFlDtRW.ClrRGB[j][2]);
                    k = j;
                }
            }
            if (PrsnXYRW.bNxt[i])
                cvet1 = new Color (255, 0, 0);
            else
                cvet1 = new Color (0, 0, 0);
            if (PrsnXYRW.SexID[i] == 1)
            {
                g.setColor (cvet0);
                g.fillRect (PrsnXY[i][0], PrsnXY[i][1], SS, SS);
                g.setColor (cvet1);
                g.drawRect (PrsnXY[i][0], PrsnXY[i][1], SS, SS);
            }
            else
            {
                g.setColor (cvet0);
                g.fillOval(PrsnXY[i][0], PrsnXY[i][1], SS, SS);
                g.setColor (cvet1);
                g.drawOval(PrsnXY[i][0], PrsnXY[i][1], SS, SS);
            }
            g.setColor(Color.black);
            for (j=0; j<PQFlDtRW.AmntPrsn; j++)
            {
                if (PdgrData.PrsnID[PrsnID[i]].equals(PQFlDtRW.PrsnID[j]))
                {
                    if (PQFlDtRW.CrsLn[j])
                        g.drawLine(PrsnXY[i][0]+SS+2, PrsnXY[i][1]-2, PrsnXY[i][0]-2, PrsnXY[i][1]+SS+2);
                }
            }
//			s = ""; // ?
//			s += PrsnID[i]; // ?
//			g.drawString(s, PrsnXY[i][0], PrsnXY[i][1]+SP); //?
            for (j=0; j<PQFlDtRW.AmntSgn; j++)
                g.drawString(PQFlDtRW.Signs[k][j], PrsnXY[i][0], PrsnXY[i][1]+SS+SP*(j+1));
        }
        */

        /*
        for (i=0; i<PdgrLnsRW.AmntFml; i++)
        {
            //родительская линия
            iY1 = GnrtLvl[PdgrLnsRW.PrntXY[i][0][1]] + SS2;
            iX1 = PdgrLnsRW.PrntXY[i][0][0]*SSD+SS;
            if (PdgrLnsRW.PrntLvl[i] == 0)
            {
                iX2 = PdgrLnsRW.PrntXY[i][1][0]*SSD;
                g.drawLine(iX1, iY1, iX2, iY1);
            }
            else
            {
                iX2 = iX1 + SD/2;
                g.drawLine(iX1, iY1, iX2, iY1);
                iY2 = iY1 - SS2 - LD*PdgrLnsRW.PrntLvl[i];
                g.drawLine(iX2, iY1, iX2, iY2);
                iX1 = PdgrLnsRW.PrntXY[i][1][0]*SSD - SD/2;
                if (PdgrLnsRW.AmntPrntArc[i] == 0)
                    g.drawLine(iX2, iY2, iX1, iY2);
                else
                {
                    g.drawLine(iX2, iY2, PdgrLnsRW.PrntArc[i][0]*SSD+SS2-AS2, iY2);
                    for (j=0; j<PdgrLnsRW.AmntPrntArc[i]-1; j++)
                    {
                        g.drawArc(PdgrLnsRW.PrntArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                        g.drawLine(PdgrLnsRW.PrntArc[i][j]*SSD+SS2+AS2, iY2, PdgrLnsRW.PrntArc[i][j+1]*SSD+SS2-AS2, iY2);
                    }
                    g.drawArc(PdgrLnsRW.PrntArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                    g.drawLine(PdgrLnsRW.PrntArc[i][j]*SSD+SS2+AS2, iY2, iX1, iY2);
                }
                g.drawLine(iX1, iY2, iX1, iY1);
                iX2 = iX1 + SD/2;
                g.drawLine(iX1, iY1, iX2, iY1);
            }

            //линия сибов
            iY1 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]];
            iY2 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]]-SV[PdgrLnsRW.SbLnLvl[i][0]]-PdgrLnsRW.SbLnLvl[i][1]*LD;
            iX1 = 0;
            for (j=0; j<PdgrLnsRW.AmntOfsp[i]; j++)
            {
                iX1 = PdgrLnsRW.SbPrsn[i][j]*SSD+SS2;
                g.drawLine(iX1, iY1, iX1, iY2);
            }
            if (PdgrLnsRW.AmntOfsp[i] > 1)
            {
                if (PdgrLnsRW.AmntSbLnArc[i] == 0)
                    g.drawLine(PdgrLnsRW.SbPrsn[i][0]*SSD+SS2, iY2, iX1, iY2);
                else
                {
                    g.drawLine(PdgrLnsRW.SbPrsn[i][0]*SSD+SS2, iY2, PdgrLnsRW.SbLnArc[i][0]*SSD+SS2-AS2, iY2);
                    for (j=0; j<PdgrLnsRW.AmntSbLnArc[i]-1; j++)
                    {
                        g.drawArc(PdgrLnsRW.SbLnArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                        g.drawLine(PdgrLnsRW.SbLnArc[i][j]*SSD+SS2+AS2, iY2, PdgrLnsRW.SbLnArc[i][j+1]*SSD+SS2-AS2, iY2);
                    }
                    g.drawArc(PdgrLnsRW.SbLnArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                    g.drawLine(PdgrLnsRW.SbLnArc[i][j]*SSD+SS2+AS2, iY2, iX1, iY2);
                }
            }

            //Линия родители-потомки
            iX1 = PdgrLnsRW.PrntOfspn[i][0]*SSD+SS2;
            iY1 = GnrtLvl[PdgrLnsRW.PrntXY[i][0][1]]+SS2;
//			s = "";
//			s += i+1;
//			g.drawString(s, iX1, iY1-LD);
            if (PdgrLnsRW.PrntLvl[i] > 0)
                iY1 -= SS2 + LD*PdgrLnsRW.PrntLvl[i];
            if (PdgrLnsRW.PrntOfspn[i][0] == PdgrLnsRW.PrntOfspn[i][1])
                g.drawLine(iX1, iY1, iX1, iY2);
            else
            {
                iY2 -= (PdgrLnsRW.PrOfLnLvl[i]*LD);
                g.drawLine(iX1, iY1, iX1, iY2);
                iX2 = PdgrLnsRW.PrntOfspn[i][1]*SSD+SS2;
                if (PdgrLnsRW.AmntPrOfLnArc[i] == 0)
                    g.drawLine(iX1, iY2, iX2, iY2);
                else
                {
                    if (PdgrLnsRW.PrntOfspn[i][0] < PdgrLnsRW.PrntOfspn[i][1])
                        g.drawLine(iX1, iY2, PdgrLnsRW.PrOfLnArc[i][0]*SSD+SS2-AS2, iY2);
                    else
                        g.drawLine(iX2, iY2, PdgrLnsRW.PrOfLnArc[i][0]*SSD+SS2-AS2, iY2);
                    for (j=0; j<PdgrLnsRW.AmntPrOfLnArc[i]-1; j++)
                    {
                        g.drawArc(PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                        g.drawLine(PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, iY2, PdgrLnsRW.PrOfLnArc[i][j+1]*SSD+SS2-AS2, iY2);
                    }
                    g.drawArc(PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2-AS2, iY2-AS2, AS, AS, 0, 180);
                    if (PdgrLnsRW.PrntOfspn[i][0] < PdgrLnsRW.PrntOfspn[i][1])
                        g.drawLine(PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, iY2, iX2, iY2);
                    else
                        g.drawLine(PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, iY2, iX1, iY2);
                }
                iY1 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]]-SV[PdgrLnsRW.SbLnLvl[i][0]];
                g.drawLine(iX2, iY2, iX2, iY1);
            }
        }
        */

        //Легенда...
        /*
        g.drawLine (0, DimY+22, 300, DimY+22);
        g.drawString ("Keys", 10, DimY+20);
        g.drawString ("Shading", 10, DimY+40);
        for (i=0; i<PQFlDtRW.AmntClr; i++)
        {
            cvet0 = new Color(PQFlDtRW.ClrCds[i][0], PQFlDtRW.ClrCds[i][1], PQFlDtRW.ClrCds[i][2]);
            g.setColor (cvet0);
            g.fillRect (10, DimY+50+SS*2*i, SS, SS);
            g.fillOval (10+SS*2, DimY+50+SS*2*i, SS, SS);
            g.setColor (Color.black);
            g.drawRect (10, DimY+50+SS*2*i, SS, SS);
            g.drawOval (10+SS*2, DimY+50+SS*2*i, SS, SS);
            s = "- " + PQFlDtRW.SClr[i];
            g.drawString (s, 10+SS*4, DimY+50+SS*2*i+SS/2+4);
        }

        g.drawString ("Output information", SS*4+200, DimY+40);
        for (i=0; i<PQFlDtRW.AmntSgn; i++)
            g.drawString (PQFlDtRW.SignsNm[i], SS*4+200, DimY+50+SP*(i+1));
        */
    }
}