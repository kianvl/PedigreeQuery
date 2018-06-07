// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import java.io.File;
import java.io.RandomAccessFile;

class PQEPSFile {
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
    private int AmntPrsn;
    private int PrsnXY[][];
    private int PrsnID[];
    private boolean bNxt[];
    private int DimX, DimY;


    void PQEPSFile (File EPSFile, String FilePrsnXYName, String PdgrLnsFileName) throws Exception {
        PersonXYRW PrsnXYRW = new PersonXYRW();
        PrsnXYRW.RWData (FilePrsnXYName, 'r');
        RWData ('r');

        int i, j, k, l, m;
        int iX1, iY1, iX2, iY2;
        int PdgrMxX, PdgrMxY;
        int GnrtLvl[];

        AmntPrsn = PrsnXYRW.AmntPrsn;
        PrsnXY = new int[AmntPrsn][];
        for (i=0; i<AmntPrsn; i++)
            PrsnXY[i] = new int[2];
        PrsnID = new int[AmntPrsn];
        bNxt = new boolean[AmntPrsn];

        SSD = SS + SD;
        SS2 = SS / 2;
        AS2 = AS / 2;

        //Чтение дополнительных обозначений
        PQFullDataRW PQFlDtRW;
        PQFlDtRW = new PQFullDataRW();
        PQFlDtRW.RWData ("temp/FullData.tmp", 'r');

        PdgrMxX = PdgrMxY = 0;
        for (i=0; i<AmntPrsn; i++) {
            if (PrsnXYRW.iX[i] > PdgrMxX)
                PdgrMxX = PrsnXYRW.iX[i];
            if (PrsnXYRW.iY[i] > PdgrMxY)
                PdgrMxY = PrsnXYRW.iY[i];
        }
        PdgrMxX++;
        DimX = (PdgrMxX) * SSD;

        PedigreeLinesRW PdgrLnsRW = new PedigreeLinesRW();
        PdgrLnsRW.RWData(PdgrLnsFileName, 'r');

        GnrtLvl = new int[PdgrMxY+1];
        SV = new int[PdgrMxY+1];
        l = 0;
        for (j=0; j<PdgrLnsRW.AmntFml; j++) {
            if (PdgrLnsRW.SbLnLvl[j][0] == 1) {
                if (PdgrLnsRW.PrntLvl[j] > l)
                    l = PdgrLnsRW.PrntLvl[j];
            }
        }

        GnrtLvl[0] = LD*(l+1);
        for (i=1; i<=PdgrMxY; i++) {
            k = 0;
            l = 0;
            for (j=0; j<PdgrLnsRW.AmntFml; j++) {
                if (PdgrLnsRW.SbLnLvl[j][0] == i) {
                    if (PdgrLnsRW.SbLnLvl[j][1] > k)
                        k = PdgrLnsRW.SbLnLvl[j][1];
                    if (PdgrLnsRW.PrOfLnLvl[j] > k)
                        k = PdgrLnsRW.PrOfLnLvl[j];
                }
                if (PdgrLnsRW.SbLnLvl[j][0] == i+1) {
                    if (PdgrLnsRW.PrntLvl[j] > l)
                        l = PdgrLnsRW.PrntLvl[j];
                }
            }
            SV[i] = SV0 + l*LD;
            GnrtLvl[i] = GnrtLvl[i-1] + SS + SP*PQFlDtRW.AmntSgn + SV[i] + k*LD + PL;
        }
        DimY = GnrtLvl[PdgrMxY] + SS+SP*PQFlDtRW.AmntSgn+SV[PdgrMxY];


        PedigreeData PdgrData = new PedigreeData();
        PdgrData.RWData ("temp/Pedigree.tmp", 'r');

        WriteEPSFile WrtEPSFile = new WriteEPSFile();
        WrtEPSFile.EPSFileInitilize (EPSFile.getPath(), DimX, DimY);
        int cvet[][] = new int[PQFlDtRW.AmntPrsn][];
        for (i=0; i<PQFlDtRW.AmntPrsn; i++)
            cvet[i] = new int[3];
        for (i=0; i<3; i++)
            cvet[0][i] = 0;
        int Ncvet = 1;
        for (i=0; i<PQFlDtRW.AmntPrsn; i++)
        M0:{
            if (PQFlDtRW.ClrRGB[i][0]==255 & PQFlDtRW.ClrRGB[i][1]==255 & PQFlDtRW.ClrRGB[i][2]==255)
                break M0;
            for (j=0; j<Ncvet; j++) {
                if (cvet[j][0]==PQFlDtRW.ClrRGB[i][0] & cvet[j][1]==PQFlDtRW.ClrRGB[i][1] & cvet[j][2]==PQFlDtRW.ClrRGB[i][2])
                    break M0;
            }
            for (j=0; j<3; j++)
                cvet[Ncvet][j] = PQFlDtRW.ClrRGB[i][j];
            Ncvet++;
        }
        for (i=0; i<Ncvet; i++)
            WrtEPSFile.EPSFileColors (i, cvet[i][0], cvet[i][1], cvet[i][2]);
        WrtEPSFile.EPSFileObjects ();
        WrtEPSFile.EPSLineWidth (0.5);

        String s;

        k = 0;
        //Индивидуальные символы
        for (i=0; i<AmntPrsn; i++) {
            PrsnXY[i][0] = PrsnXYRW.iX[i] * SSD;
            PrsnXY[i][1] = GnrtLvl[PrsnXYRW.iY[i]];
            PrsnID[i] = PrsnXYRW.PrsnID[i];
            bNxt[i] = PrsnXYRW.bNxt[i];
            m = -1;
            for (j=0; j<PQFlDtRW.AmntPrsn; j++) {
                if (PdgrData.PrsnID[PrsnID[i]].equals(PQFlDtRW.PrsnID[j])) {
                    for (l=0; l<Ncvet; l++) {
                        if (cvet[l][0]==PQFlDtRW.ClrRGB[j][0] & cvet[l][1]==PQFlDtRW.ClrRGB[j][1] & cvet[l][2]==PQFlDtRW.ClrRGB[j][2])
                            m = l;
                    }
                    k = j;
                }
            }
            // Квадратик
            if (PrsnXYRW.SexID[i] == 1) {
                if (m != -1) {
                    WrtEPSFile.EPSLineStart (PrsnXY[i][0], DimY-PrsnXY[i][1]);
                    WrtEPSFile.EPSLineTo (PrsnXY[i][0]+SS, DimY-PrsnXY[i][1]);
                    WrtEPSFile.EPSLineTo (PrsnXY[i][0]+SS, DimY-PrsnXY[i][1]-SS);
                    WrtEPSFile.EPSLineTo (PrsnXY[i][0], DimY-PrsnXY[i][1]-SS);
                    WrtEPSFile.EPSLineEnd (m, 1, 1);
                }
                WrtEPSFile.EPSLineStart (PrsnXY[i][0], DimY-PrsnXY[i][1]);
                WrtEPSFile.EPSLineTo (PrsnXY[i][0]+SS, DimY-PrsnXY[i][1]);
                WrtEPSFile.EPSLineTo (PrsnXY[i][0]+SS, DimY-PrsnXY[i][1]-SS);
                WrtEPSFile.EPSLineTo (PrsnXY[i][0], DimY-PrsnXY[i][1]-SS);
                WrtEPSFile.EPSLineEnd (0, 1, 0);
            }
            else {
                if (m != -1)
                    WrtEPSFile.EPSFileArc (1, PrsnXY[i][0]+SS2, DimY-PrsnXY[i][1]-SS2, SS2, SS2, 0, 360, 1, m);
                WrtEPSFile.EPSFileArc (1, PrsnXY[i][0]+SS2, DimY-PrsnXY[i][1]-SS2, SS2, SS2, 0, 360, 0, 0);
            }
            for (j=0; j<PQFlDtRW.AmntPrsn; j++) {
                if (PdgrData.PrsnID[PrsnID[i]].equals(PQFlDtRW.PrsnID[j])) {
                    if (PQFlDtRW.CrsLn[j]) {
                        WrtEPSFile.EPSLineStart (PrsnXY[i][0]+SS+2, DimY-PrsnXY[i][1]+2);
                        WrtEPSFile.EPSLineTo (PrsnXY[i][0]-2, DimY-PrsnXY[i][1]-SS-2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                }
            }
            for (j=0; j<PQFlDtRW.AmntSgn; j++)
                WrtEPSFile.EPSFileText (10, PrsnXY[i][0], DimY-PrsnXY[i][1]-SS-SP*(j+1), 1, 1, 0, PQFlDtRW.Signs[k][j], 0);
        }

        for (i=0; i<PdgrLnsRW.AmntFml; i++) {
            //родительская линия
            iY1 = GnrtLvl[PdgrLnsRW.PrntXY[i][0][1]] + SS2;
            iX1 = PdgrLnsRW.PrntXY[i][0][0]*SSD+SS;
            if (PdgrLnsRW.PrntLvl[i] == 0) {
                iX2 = PdgrLnsRW.PrntXY[i][1][0]*SSD;
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX2, DimY-iY1);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
            }
            else {
                iX2 = iX1 + SD/2;
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX2, DimY-iY1);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
                iY2 = iY1 - SS2 - LD*PdgrLnsRW.PrntLvl[i];
                WrtEPSFile.EPSLineStart (iX2, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX2, DimY-iY2);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
                iX1 = PdgrLnsRW.PrntXY[i][1][0]*SSD - SD/2;
                if (PdgrLnsRW.AmntPrntArc[i] == 0) {
                    WrtEPSFile.EPSLineStart (iX2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                }
                else {
                    WrtEPSFile.EPSLineStart (iX2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (PdgrLnsRW.PrntArc[i][0]*SSD+SS2-AS2, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                    for (j=0; j<PdgrLnsRW.AmntPrntArc[i]-1; j++) {
                        WrtEPSFile.EPSFileArc (0, PdgrLnsRW.PrntArc[i][j]*SSD+SS2, DimY-PrsnXY[i][1]-iY2, AS2, AS2, 0, 180, 0, 0);
                        WrtEPSFile.EPSLineStart (PdgrLnsRW.PrntArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (PdgrLnsRW.PrntArc[i][j+1]*SSD+SS2-AS2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    WrtEPSFile.EPSFileArc (0, PdgrLnsRW.PrntArc[i][j]*SSD+SS2, DimY-iY2, AS2, AS2, 0, 180, 0, 0);
                    WrtEPSFile.EPSLineStart (PdgrLnsRW.PrntArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                }
                WrtEPSFile.EPSLineStart (iX1, DimY-iY2);
                WrtEPSFile.EPSLineTo (iX1, DimY-iY1);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
                iX2 = iX1 + SD/2;
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX2, DimY-iY1);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
            }

            //линия сибов
            iY1 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]];
            iY2 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]]-SV[PdgrLnsRW.SbLnLvl[i][0]]-PdgrLnsRW.SbLnLvl[i][1]*LD;
            iX1 = 0;
            for (j=0; j<PdgrLnsRW.AmntOfsp[i]; j++) {
                iX1 = PdgrLnsRW.SbPrsn[i][j]*SSD+SS2;
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
            }
            if (PdgrLnsRW.AmntOfsp[i] > 1) {
                if (PdgrLnsRW.AmntSbLnArc[i] == 0) {
                    WrtEPSFile.EPSLineStart (PdgrLnsRW.SbPrsn[i][0]*SSD+SS2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                }
                else {
                    WrtEPSFile.EPSLineStart (PdgrLnsRW.SbPrsn[i][0]*SSD+SS2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (PdgrLnsRW.SbLnArc[i][0]*SSD+SS2-AS2, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                    for (j=0; j<PdgrLnsRW.AmntSbLnArc[i]-1; j++) {
                        WrtEPSFile.EPSFileArc (0, PdgrLnsRW.SbLnArc[i][j]*SSD+SS2, DimY-iY2, AS2, AS2, 0, 180, 0, 0);
                        WrtEPSFile.EPSLineStart (PdgrLnsRW.SbLnArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (PdgrLnsRW.SbLnArc[i][j+1]*SSD+SS2-AS2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    WrtEPSFile.EPSFileArc (0, PdgrLnsRW.SbLnArc[i][j]*SSD+SS2, DimY-iY2, AS2, AS2, 0, 180, 0, 0);
                    WrtEPSFile.EPSLineStart (PdgrLnsRW.SbLnArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                    WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                }
            }

            //Линия родители-потомки
            iX1 = PdgrLnsRW.PrntOfspn[i][0]*SSD+SS2;
            iY1 = GnrtLvl[PdgrLnsRW.PrntXY[i][0][1]]+SS2;
            if (PdgrLnsRW.PrntLvl[i] > 0)
                iY1 -= SS2 + LD*PdgrLnsRW.PrntLvl[i];
            if (PdgrLnsRW.PrntOfspn[i][0] == PdgrLnsRW.PrntOfspn[i][1]) {
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
            }
            else {
                iY2 -= (PdgrLnsRW.PrOfLnLvl[i]*LD);
                WrtEPSFile.EPSLineStart (iX1, DimY-iY1);
                WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
                iX2 = PdgrLnsRW.PrntOfspn[i][1]*SSD+SS2;
                if (PdgrLnsRW.AmntPrOfLnArc[i] == 0) {
                    WrtEPSFile.EPSLineStart (iX1, DimY-iY2);
                    WrtEPSFile.EPSLineTo (iX2, DimY-iY2);
                    WrtEPSFile.EPSLineEnd (0, 0, 0);
                }
                else {
                    if (PdgrLnsRW.PrntOfspn[i][0] < PdgrLnsRW.PrntOfspn[i][1]) {
                        WrtEPSFile.EPSLineStart (iX1, DimY-iY2);
                        WrtEPSFile.EPSLineTo (PdgrLnsRW.PrOfLnArc[i][0]*SSD+SS2-AS2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    else {
                        WrtEPSFile.EPSLineStart (iX2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (PdgrLnsRW.PrOfLnArc[i][0]*SSD+SS2-AS2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    for (j=0; j<PdgrLnsRW.AmntPrOfLnArc[i]-1; j++) {
                        WrtEPSFile.EPSFileArc (0, PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2, DimY-iY2, AS2, AS2, 0, 180, 0, 0);
                        WrtEPSFile.EPSLineStart (PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (PdgrLnsRW.PrOfLnArc[i][j+1]*SSD+SS2-AS2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    WrtEPSFile.EPSFileArc (0, PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2, DimY-iY2, AS2, AS2, 0, 180, 0, 0);
                    if (PdgrLnsRW.PrntOfspn[i][0] < PdgrLnsRW.PrntOfspn[i][1]) {
                        WrtEPSFile.EPSLineStart (PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (iX2, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                    else {
                        WrtEPSFile.EPSLineStart (PdgrLnsRW.PrOfLnArc[i][j]*SSD+SS2+AS2, DimY-iY2);
                        WrtEPSFile.EPSLineTo (iX1, DimY-iY2);
                        WrtEPSFile.EPSLineEnd (0, 0, 0);
                    }
                }
                iY1 = GnrtLvl[PdgrLnsRW.SbLnLvl[i][0]]-SV[PdgrLnsRW.SbLnLvl[i][0]];
                WrtEPSFile.EPSLineStart (iX2, DimY-iY2);
                WrtEPSFile.EPSLineTo (iX2, DimY-iY1);
                WrtEPSFile.EPSLineEnd (0, 0, 0);
            }

        }
/*
		//Легенда...
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
        WrtEPSFile.EPSFileClose ();
    }

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
            SS = PQSizeFile.readInt();
            SD = PQSizeFile.readInt();
            SP = PQSizeFile.readInt();
            PL = PQSizeFile.readInt();
            SV0 = PQSizeFile.readInt();
            LD = PQSizeFile.readInt();
            AS = PQSizeFile.readInt();
        }
        else {
            PQSizeFile.writeInt(SS);
            PQSizeFile.writeInt(SD);
            PQSizeFile.writeInt(SP);
            PQSizeFile.writeInt(PL);
            PQSizeFile.writeInt(SV0);
            PQSizeFile.writeInt(LD);
            PQSizeFile.writeInt(AS);
        }

        PQSizeFile.close();
    }
}
