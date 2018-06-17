// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

class Persons2d {
    JFrame ParentFrame;
    private FamiliesGenerationsRW FmlGnrtnData = new FamiliesGenerationsRW();
    private FamiliesData FmlsData = new FamiliesData();
    private PersonXYRW PrsnXYRW = new PersonXYRW();
    private int PdgrEdge;
    private int AmountGnrtn;
    private int ScaleXY[][];
    private int PdgrFrOff;
    private int FmlInWk;
    private boolean FrsnXok[];
    private boolean PrntsXok[] = new boolean[2];

    private boolean PutPrsnX(int PtXPrsnId, int PtXPrsnX) {
        if (ScaleXY[PtXPrsnX][PrsnXYRW.getiY(PtXPrsnId)] > 0) {
//			JOptionPane.showMessageDialog(ParentFrame, "Yacheika zanyata!!");
            return false;
        }
        PrsnXYRW.setiX(PtXPrsnId, PtXPrsnX);
        ScaleXY[PtXPrsnX][PrsnXYRW.getiY(PtXPrsnId)] = PtXPrsnId+1;
        FrsnXok[PtXPrsnId] = true;
        return true;
    }

    //расстановка родителей
    private boolean SeekParentsX(int FamilyID) {
        int i, j, n, PdgrFrOf;
        int nn[] = new int[2];

        //определены координаты у родителей или нет?
        for (j=0; j<2; j++) {
            PrntsXok[j] = false;
            for (i=0; i<PrsnXYRW.getAmntPrsn(); i++)
            M1:{
                if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                    PrntsXok[j] = FrsnXok[i];
                    break M1;
                }
            }
        }

        //колличество потомков с неопределенными координатами
        PdgrFrOff = 0;
        for (i=0; i<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[FamilyID]]; i++) {
        M2:for (j=0; j<PrsnXYRW.getAmntPrsn(); j++) {
            if (PrsnXYRW.getPrsnID(j) == FmlsData.OfsprFml[FmlGnrtnData.FmlID[FamilyID]][i]) {
                if (!FrsnXok[j])
                    PdgrFrOff++;
                break M2;
                }
            }
        }

        //если хотябы один из родителей неопределен
        if (!PrntsXok[0] | !PrntsXok[1]) {
            //если оба родителя неопределены
            if (!PrntsXok[0] & !PrntsXok[1]) {
//				JOptionPane.showMessageDialog(ParentFrame, "Oba roditelya bez X - " + FamilyID);
                for (j=0; j<2; j++) {
                    M0:for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
                        if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                            if (j == 0) {
                                if (!PutPrsnX(i, PdgrEdge+1)) return false;
                            }
                            else if (!PutPrsnX(i, PdgrEdge+3)) return false;
                            break M0;
                        }
                    }
                }
                PdgrEdge += 3;
            }
            //если только один из родителей неопределен
            else {
                n = 0;
                PdgrFrOf = 0;
                //Ищем родителя с координатой
                M3:for (j=0; j<2; j++) {
                    if (PrntsXok[j]) {
                        for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
                            if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                                ShiftPdgr(i);
                                //n - координата этого родителя
                                n = PrsnXYRW.getiX(i);
                                //является ли он родителем в других ЯС
/*
								for (k=0; k<FmlGnrtnData.AmntFml; k++)
								{
									if (k != FamilyID)
									{
										if (PrsnXYRW.PrsnID[i] == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[k]])
										{
											//если ДА, то определяем размер ЯС и ее правую границу
											JOptionPane.showMessageDialog(ParentFrame, "Parent - " + PrsnXYRW.PrsnID[i]);
											for (l=0; l<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[k]]; l++)
											{
												for (m=0; m<PrsnXYRW.AmntPrsn; m++)
												{
													if (PrsnXYRW.PrsnID[m] == FmlsData.OfsprFml[FmlGnrtnData.FmlID[k]][l])
													{
														if (FrsnXok[m] & (PdgrFrOf <= PrsnXYRW.iX[m]))
														{
															PdgrFrOf = PrsnXYRW.iX[m];
															JOptionPane.showMessageDialog(ParentFrame, "" + FrsnXok[m] + ", Level - " + PdgrFrOf);
//															JOptionPane.showMessageDialog(ParentFrame, "Level - " + PdgrFrOf);
														}
													}
												}
											}
											for (l=0; l<2; l++)
											{
												for (m=0; m<PrsnXYRW.AmntPrsn; m++)
												{
													if (PrsnXYRW.PrsnID[m] == FmlsData.PrntFml[l][FmlGnrtnData.FmlID[k]])
													{
														if (FrsnXok[m] & (PdgrFrOf <= PrsnXYRW.iX[m]))
														{
															PdgrFrOf = PrsnXYRW.iX[m];
															JOptionPane.showMessageDialog(ParentFrame, "" + FrsnXok[m] + ", Level - " + PdgrFrOf);
														}
													}
												}
											}
										}
									}
								}
								*/
                                break M3;
                            }
                        }
                    }
                }
                //Ищем родителя без координаты
                M4:for (j=0; j<2; j++) {
                    if (!PrntsXok[j]) {
                        for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
                            if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                                if (PdgrFrOf == 0) {
                                    if (!PutPrsnX(i, n+2))
                                        return false;
                                }
                                else {
                                    if (!PutPrsnX(i, PdgrFrOf+2))
                                        return false;
                                }
                                break M4;
                            }
                        }
                    }
                }
            }
        }
        else {
//			JOptionPane.showMessageDialog(ParentFrame, "Oba roditelya s X - " + FamilyID);
            //Ищем ID родителей
            for (j=0; j<2; j++)
            M5:{
                for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
                    if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                        //nn - ID родителей
                        nn[j] = i;
                        break M5;
                    }
                }
            }
            if (PrsnXYRW.getiX(nn[0]) < PrsnXYRW.getiX(nn[1])) {
                PrntsXok[1] = FrsnXok[nn[1]] = false;
                ScaleXY[PrsnXYRW.getiX(nn[1])][PrsnXYRW.getiY(nn[1])] = 0;
                ShiftPdgr(nn[0]);
                return PutPrsnX(nn[1], PrsnXYRW.getiX(nn[0]) + 2);
            }
            else {
                PrntsXok[0] = FrsnXok[nn[0]] = false;
                ScaleXY[PrsnXYRW.getiX(nn[0])][PrsnXYRW.getiY(nn[0])] = 0;
                ShiftPdgr(nn[1]);
                return PutPrsnX(nn[0], PrsnXYRW.getiX(nn[1]) + 2);
            }
        }
        return true;
    }


    private void ShiftPdgr(int BrkPnt) {
        int i, j, k, l, iBrkPnt, iSt;
        int n=0;
//        int iPrnt[] = new int[2]; // Never used
        boolean bb;

        iBrkPnt = PrsnXYRW.getiX(BrkPnt);
        iSt = PdgrFrOff;
        if (PdgrFrOff < 3)
            iSt = PdgrFrOff+1;

        M3:for (i=0; i<FmlInWk; i++) {
            for (j=0; j<2; j++) {
                if (FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]] == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FmlInWk]]) {
//					//if (PdgrFrOff==2 | PdgrFrOff==3)
                    if (PdgrFrOff==1) {
                        iSt = 3;
                        break M3;
                    }
                }
            }
        }

        for (i=PdgrEdge; i>=iBrkPnt; i--) {
            for (j=0; j<AmountGnrtn; j++)
            M1:{
                if ((ScaleXY[i][j]!=0) & (ScaleXY[i][j]!=(BrkPnt+1))) {
                    if (((i==iBrkPnt) | (i==iBrkPnt+1)) & i>1) {
                        for (k=0; k<FmlInWk; k++) {
                            bb = false;
                            M2:for (l=0; l<2; l++) {
                                if (FmlsData.PrntFml[l][FmlGnrtnData.FmlID[k]] == PrsnXYRW.getPrsnID(ScaleXY[i][j]-1)) {
                                    if (l==0)
                                        n = 1;
                                    else
                                        n = 0;
                                    bb = true;
                                    break M2;
                                }
                            }
                            if (bb) {
                                for (l=0; l<AmountGnrtn; l++) {
                                    if ((ScaleXY[i-2][l]!=0) & (ScaleXY[i-2][l]!=(BrkPnt+1))) {
                                        if (FmlsData.PrntFml[n][FmlGnrtnData.FmlID[k]] == PrsnXYRW.getPrsnID(ScaleXY[i-2][l]-1))
                                            break M1;
                                    }
                                }
                            }
                        }
                    }
                    PrsnXYRW.setiX(ScaleXY[i][j]-1, PrsnXYRW.getiX(ScaleXY[i][j]-1) + iSt);
                    ScaleXY[i+iSt][j] = ScaleXY[i][j];
                    ScaleXY[i][j] = 0;
                }
            }
        }
        PdgrEdge += iSt;
//		if (PdgrFrOff==1 & iSt==3)
//			JOptionPane.showMessageDialog(ParentFrame, "Tot samyy!!");
    }


    private boolean SeekOffspringX(int FamilyID) {
        if (PdgrFrOff == 0)
            return true;

        int i, j, k, l, OffsX;
        int iPrntsX[] = new int[2];
        boolean bl, br;

        for (j=0; j<2; j++) {
            M0:for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
                if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[FamilyID]]) {
                    iPrntsX[j] = PrsnXYRW.getiX(i);
                    break M0;
                }
            }
        }

        if (iPrntsX[0] < iPrntsX[1])
            OffsX = iPrntsX[0];
        else
            OffsX = iPrntsX[1];

        for (i=0; i<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[FamilyID]]; i++) {
            M1:for (j=0; j<PrsnXYRW.getAmntPrsn(); j++) {
                if (PrsnXYRW.getPrsnID(j) == FmlsData.OfsprFml[FmlGnrtnData.FmlID[FamilyID]][i]) {
                    if (!FrsnXok[j]) {
                        if (PdgrFrOff > 1) {
                            if (!PutPrsnX(j, OffsX)) return false;
                            if (OffsX > PdgrEdge)
                                PdgrEdge = OffsX;
                            OffsX++;
                            if (PdgrFrOff == 2)
                                OffsX++;
                            break M1;
                        }
                        else {
                            OffsX++;
                            if (FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[FamilyID]] == 1) {
                                if (!PutPrsnX(j, OffsX)) return false;
                                return true;
                            }
                            else {
                                bl = br = false;
                                for (k=0; k<FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[FamilyID]]; k++)
                                M2:{
                                    if (k != i) {
                                        for (l=0; l<PrsnXYRW.getAmntPrsn(); l++) {
                                            if (PrsnXYRW.getPrsnID(l) == FmlsData.OfsprFml[FmlGnrtnData.FmlID[FamilyID]][k]) {
                                                if (PrsnXYRW.getiX(l) < OffsX) {
                                                    bl = true;
                                                    break M2;
                                                }
                                                else {
                                                    br = true;
                                                    break M2;
                                                }
                                            }
                                        }
                                    }
                                }
                                if (bl & br)
                                    return PutPrsnX(j, OffsX);
                                else {
                                    if (bl)
                                        return PutPrsnX(j, OffsX + 1);
                                    else
                                        return PutPrsnX(j, OffsX - 1);
                                }
                            }
                        }
                    }
                    break M1;
                }
            }
        }
        return true;
    }


    //главная функция
    boolean SeekPersonsX (JFrame ParentFrame, String FileFmlGnrtName, String FileFmlDataName, String FilePrsnXYName) {
        this.ParentFrame = ParentFrame;
        int i, j, k, l, m, n;
        boolean bb;

        //открытие файла с поколениями семей
        try {
            FmlGnrtnData.RWData(FileFmlGnrtName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }
        //чтение данных о составе семей
        try {
            FmlsData.RWData(FileFmlDataName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }
        try {
            PrsnXYRW.RWData(FilePrsnXYName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }
        //определение колличества поколений
        AmountGnrtn = 0;
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            if (AmountGnrtn <= FmlGnrtnData.FmlGnrtn[i])
                AmountGnrtn = FmlGnrtnData.FmlGnrtn[i]+1;
        }
        AmountGnrtn++;
        //выделение массива - сетка родословной
        ScaleXY = new int[PrsnXYRW.getAmntPrsn()*2][];
        for (i=0; i<PrsnXYRW.getAmntPrsn()*2; i++)
            ScaleXY[i] = new int[AmountGnrtn];

        FrsnXok = new boolean[PrsnXYRW.getAmntPrsn()];

        PdgrEdge = -1;

        int iPrnt[] = new int[2];
        //расстановка родителей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            FmlInWk = i;
            SeekParentsX(i);
            if (!SeekOffspringX(i)) return false;
        }
        //Избавляемся от лишних гэпов
        do {
            bb = false;
            for (k=0; k<PdgrEdge-1; k++)
            M3:{
                for (j=0; j<AmountGnrtn; j++) {
                    if (ScaleXY[k][j] > 0)
                        break M3;
                }

                for (i=0; i<FmlGnrtnData.AmntFml; i++) {
                    for (m=0; m<2; m++)
                    M4:{
                        for (n=0; n<PrsnXYRW.getAmntPrsn(); n++) {
                            if (PrsnXYRW.getPrsnID(n) == FmlsData.PrntFml[m][FmlGnrtnData.FmlID[i]]) {
                                iPrnt[m] = PrsnXYRW.getiX(n);
                                break M4;
                            }
                        }
                    }
                    if (((iPrnt[0]<k) & (iPrnt[1]==k+1)) | ((iPrnt[1]<k) & (iPrnt[0]==k+1)))
                        break M3;
                }

                for (j=k; j<PdgrEdge; j++) {
                    for (l=0; l<AmountGnrtn; l++) {
                        if (ScaleXY[j+1][l] > 0) {
                            ScaleXY[j][l] = ScaleXY[j+1][l];
                            ScaleXY[j+1][l] = 0;
                            PrsnXYRW.setiX(ScaleXY[j][l]-1, PrsnXYRW.getiX(ScaleXY[j][l]-1)-1);
                        }
                    }
                }
                PdgrEdge--;
                bb = true;
            }
        } while(bb);
        //Поиск тех, по кому можно расширять родословную.
        for (i=0; i<PrsnXYRW.getAmntPrsn(); i++)
        M1:{
            for (j=0; j<FmlsData.AmntFml; j++)
            M0:{
                bb = false;
                M2:{
                    for (k=0; k<2; k++) {
                        if (PrsnXYRW.getPrsnID(i) == FmlsData.PrntFml[k][j]) {
                            bb = true;
                            break M2;
                        }
                    }
                    for (k=0; k<FmlsData.AmntOfsprFml[j]; k++) {
                        if (PrsnXYRW.getPrsnID(i) == FmlsData.OfsprFml[j][k]) {
                            bb = true;
                            break M2;
                        }
                    }
                }
                if (bb) {
                    for (l=0; l<FmlGnrtnData.AmntFml; l++) {
                        if (FmlGnrtnData.FmlID[l] == j)
                            break M0;
                    }
                    PrsnXYRW.bNxt[i] = true;
                    break M1;
                }
            }
        }


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
