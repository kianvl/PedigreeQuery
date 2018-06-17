// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

class PedigreeLines {
    boolean SeakLinesXY(JFrame ParentFrame, String FileFmlGnrtName, String FileFmlDataName, String FilePrsnXYName, String PdgrLnsFileName) {
        int i, j, k;

        FamiliesGenerationsRW FmlGnrtnData = new FamiliesGenerationsRW();
        try {
            FmlGnrtnData.RWData(FileFmlGnrtName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        FamiliesData FmlsData = new FamiliesData();
        try {
            FmlsData.RWData(FileFmlDataName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        PersonXYRW PrsnXYRW = new PersonXYRW();
        try {
            PrsnXYRW.RWData(FilePrsnXYName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

//		JOptionPane.showMessageDialog(ParentFrame, "Lines!!");

        PedigreeLinesRW PdgrLnsRW = new PedigreeLinesRW();


        PdgrLnsRW.AmntFml = FmlGnrtnData.AmntFml;
        PdgrLnsRW.ArrayData();
        int PrntXbf[] = new int[2];
        int PrntYbf[] = new int[2];


        for (i=0; i<PdgrLnsRW.AmntFml; i++) {
            //Родительская линия
            for (j=0; j<2; j++) {
                M0:for (k=0; k<PrsnXYRW.getAmntPrsn(); k++) {
                    if (PrsnXYRW.getPrsnID(k) == FmlsData.PrntFml[j][FmlGnrtnData.FmlID[i]]) {
                        PdgrLnsRW.PrntXY[i][j][0] = PrsnXYRW.getiX(k);
                        PdgrLnsRW.PrntXY[i][j][1] = PrsnXYRW.getiY(k);
                        break M0;
                    }
                }
            }
            if (PdgrLnsRW.PrntXY[i][0][0] > PdgrLnsRW.PrntXY[i][1][0]) {
                k = PdgrLnsRW.PrntXY[i][0][0];
                PdgrLnsRW.PrntXY[i][0][0] = PdgrLnsRW.PrntXY[i][1][0];
                PdgrLnsRW.PrntXY[i][1][0] = k;
                k = PdgrLnsRW.PrntXY[i][0][1];
                PdgrLnsRW.PrntXY[i][0][1] = PdgrLnsRW.PrntXY[i][1][1];
                PdgrLnsRW.PrntXY[i][1][1] = k;
            }

            PrntXbf[0] = PdgrLnsRW.PrntXY[i][0][0];
            PrntXbf[1] = PdgrLnsRW.PrntXY[i][1][0];
            PrntYbf[0] = PdgrLnsRW.PrntXY[i][0][1];
            PrntYbf[1] = PdgrLnsRW.PrntXY[i][1][1];

            //для многоженцев
            M8:if ((PrntXbf[1]-PrntXbf[0]) > 2) {
                for (j=0; j<PrsnXYRW.getAmntPrsn(); j++) {
                    if ((PrsnXYRW.getiY(j)==PrntYbf[0]) | (PrsnXYRW.getiY(j)==PrntYbf[1])) {
                        if ((PrsnXYRW.getiX(j)>PrntXbf[0]) & (PrsnXYRW.getiX(j)<PrntXbf[1])) {
                            PdgrLnsRW.PrntLvl[i] = 1;
                            break M8;
                        }
                    }
                }
            }

            //Линия сибов
            PdgrLnsRW.SbLnLvl[i][0] = FmlGnrtnData.FmlGnrtn[i]+1;

            //Индивидуальные линии сибов
            PdgrLnsRW.AmntOfsp[i] = FmlsData.AmntOfsprFml[FmlGnrtnData.FmlID[i]];
            PdgrLnsRW.SbPrsn[i] = new int[PdgrLnsRW.AmntOfsp[i]];
            for (j=0; j<PdgrLnsRW.AmntOfsp[i]; j++) {
                M1:for (k=0; k<PrsnXYRW.getAmntPrsn(); k++) {
                    if (PrsnXYRW.getPrsnID(k) == FmlsData.OfsprFml[FmlGnrtnData.FmlID[i]][j]) {
                        PdgrLnsRW.SbPrsn[i][j] = PrsnXYRW.getiX(k);
                        break M1;
                    }
                }
            }

            //На первом месте крайний левый сиб
            for (j=1; j<PdgrLnsRW.AmntOfsp[i]; j++) {
                if (PdgrLnsRW.SbPrsn[i][0] > PdgrLnsRW.SbPrsn[i][j]) {
                    k = PdgrLnsRW.SbPrsn[i][0];
                    PdgrLnsRW.SbPrsn[i][0] = PdgrLnsRW.SbPrsn[i][j];
                    PdgrLnsRW.SbPrsn[i][j] = k;
                }
            }
            //На последнем месте крайний правый сиб
            for (j=1; j<PdgrLnsRW.AmntOfsp[i]-1; j++) {
                if (PdgrLnsRW.SbPrsn[i][j] > PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1]) {
                    k = PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1];
                    PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1] = PdgrLnsRW.SbPrsn[i][j];
                    PdgrLnsRW.SbPrsn[i][j] = k;
                }
            }

            //Линия родители-потомки
            PdgrLnsRW.PrntOfspn[i][0] = PrntXbf[1] - 1;
            if (PdgrLnsRW.AmntOfsp[i] == 1)
                PdgrLnsRW.PrntOfspn[i][1] = PdgrLnsRW.SbPrsn[i][0];
            else {
                if ((PdgrLnsRW.SbPrsn[i][0]<PdgrLnsRW.PrntOfspn[i][0]) & (PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1]>PdgrLnsRW.PrntOfspn[i][0]))
                    PdgrLnsRW.PrntOfspn[i][1] = PdgrLnsRW.PrntOfspn[i][0];
                else {
                    if (PdgrLnsRW.SbPrsn[i][0]>=PdgrLnsRW.PrntOfspn[i][0])
                        PdgrLnsRW.PrntOfspn[i][1] = PdgrLnsRW.SbPrsn[i][0]+1;
                    else
                        PdgrLnsRW.PrntOfspn[i][1] = PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1]-1;
                }
            }
        }

        //Упорядочивание горизонтальных линий
        int MaxX = 0;
        int iX1=0, iX2=0, jX1=0, jX2=0;
        boolean bb;
        for (i=0; i<PrsnXYRW.getAmntPrsn(); i++) {
            if (PrsnXYRW.getiX(i) >= MaxX)
                MaxX = PrsnXYRW.getiX(i)+1;
        }
        int NFml = 0;
        for (i=1; i<=MaxX; i++) {
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            M2:{
                iX1 = PdgrLnsRW.SbPrsn[j][0];
                iX2 = PdgrLnsRW.SbPrsn[j][PdgrLnsRW.AmntOfsp[j]-1];
                if (PdgrLnsRW.AmntOfsp[j] == 1)
                    break M2;
                if ((iX2 - iX1) != i)
                    break M2;
                do {
                    bb = false;
                    for (k=0; k<PdgrLnsRW.AmntFml; k++)
                    M3:{
                        if (k==j)
                            break M3;
                        if (PdgrLnsRW.AmntOfsp[k] == 1)
                            break M3;
                        jX1 = PdgrLnsRW.SbPrsn[k][0];
                        jX2 = PdgrLnsRW.SbPrsn[k][PdgrLnsRW.AmntOfsp[k]-1];
                        if ((jX2-jX1)>i | (((jX2-jX1)==i) & k>j))
                            break M3;
                        if ((PdgrLnsRW.SbLnLvl[j][0]==PdgrLnsRW.SbLnLvl[k][0]) & (PdgrLnsRW.SbLnLvl[j][1]==PdgrLnsRW.SbLnLvl[k][1])) {
                            if ((iX2<jX1) | (iX1>jX2))
                                break M3;
                            PdgrLnsRW.SbLnLvl[j][1]++;
                            bb = true;
                        }
                    }
                }while(bb);
            }
        }

        for (i=0; i<PdgrLnsRW.AmntFml; i++) {
            if (PdgrLnsRW.AmntOfsp[i]>1 & PdgrLnsRW.PrntOfspn[i][0]!=PdgrLnsRW.PrntOfspn[i][1])
                PdgrLnsRW.PrOfLnLvl[i] = PdgrLnsRW.SbLnLvl[i][1] + 1;
        }
        for (i=1; i<=MaxX; i++) {
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            M4:{
                if (PdgrLnsRW.PrntOfspn[j][0] != PdgrLnsRW.PrntOfspn[j][1]) {
                    if (PdgrLnsRW.PrntOfspn[j][0] > PdgrLnsRW.PrntOfspn[j][1]) {
                        iX1 = PdgrLnsRW.PrntOfspn[j][1];
                        iX2 = PdgrLnsRW.PrntOfspn[j][0];
                    }
                    else {
                        iX1 = PdgrLnsRW.PrntOfspn[j][0];
                        iX2 = PdgrLnsRW.PrntOfspn[j][1];
                    }
                    if ((iX2 - iX1) != i)
                        break M4;
                    do {
                        bb = false;
                        for (k=0; k<PdgrLnsRW.AmntFml; k++)
                        M5:{
                            if (k==j)
                                break M5;
                            M6:if (PdgrLnsRW.AmntOfsp[k] > 1) {
                                jX1 = PdgrLnsRW.SbPrsn[k][0];
                                jX2 = PdgrLnsRW.SbPrsn[k][PdgrLnsRW.AmntOfsp[k]-1];
                                if ((PdgrLnsRW.SbLnLvl[j][0]==PdgrLnsRW.SbLnLvl[k][0]) & (PdgrLnsRW.PrOfLnLvl[j]==PdgrLnsRW.SbLnLvl[k][1])) {
                                    if ((iX2<jX1) | (iX1>jX2))
                                        break M6;
                                    PdgrLnsRW.PrOfLnLvl[j]++;
                                    bb = true;
                                }
                            }
                            if (PdgrLnsRW.PrntOfspn[k][0] != PdgrLnsRW.PrntOfspn[k][1]) {
                                if (PdgrLnsRW.PrntOfspn[k][0] > PdgrLnsRW.PrntOfspn[k][1]) {
                                    jX1 = PdgrLnsRW.PrntOfspn[k][1];
                                    jX2 = PdgrLnsRW.PrntOfspn[k][0];
                                }
                                else {
                                    jX1 = PdgrLnsRW.PrntOfspn[k][0];
                                    jX2 = PdgrLnsRW.PrntOfspn[k][1];
                                }
                                if ((jX2-jX1)>i | (((jX2-jX1)==i) & k>j))
                                    break M5;
                                if ((PdgrLnsRW.SbLnLvl[j][0]==PdgrLnsRW.SbLnLvl[k][0]) & (PdgrLnsRW.PrOfLnLvl[j]==PdgrLnsRW.PrOfLnLvl[k])) {
                                    if ((iX2<jX1) | (iX1>jX2))
                                        break M5;
                                    PdgrLnsRW.PrOfLnLvl[j]++;
                                    bb = true;
                                }
                            }
                        }
                    }while(bb);
                }
            }
        }

        //Упорядочивание родительских для многоженцев - PdgrLnsRW.PrntLvl[]
        for (i=1; i<=MaxX; i++) {
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            M9:{
                if (PdgrLnsRW.PrntLvl[j] == 0)
                    break M9;
                iX1 = PdgrLnsRW.PrntXY[j][0][0];
                iX2 = PdgrLnsRW.PrntXY[j][1][0];
                if (iX2-iX1 != i)
                    break M9;
                do {
                    bb = false;
                    for (k=0; k<PdgrLnsRW.AmntFml; k++)
                    M10:{
                        if (j == k)
                            break M10;
                        if (PdgrLnsRW.PrntLvl[k] == 0)
                            break M10;
                        jX1 = PdgrLnsRW.PrntXY[k][0][0];
                        jX2 = PdgrLnsRW.PrntXY[k][1][0];
                        if ((jX2-jX1 > i) | ((jX2-jX1 == i) & k>j))
                            break M10;
                        if (PdgrLnsRW.SbLnLvl[j][0] != PdgrLnsRW.SbLnLvl[k][0])
                            break M10;
                        if (iX1>jX2 | iX2<jX1)
                            break M10;
                        if (PdgrLnsRW.PrntLvl[j] == PdgrLnsRW.PrntLvl[k]) {
                            PdgrLnsRW.PrntLvl[j]++;
                            bb = true;
                        }
                    }
                } while(bb);
            }
        }


        for (i=0; i<PdgrLnsRW.AmntFml; i++)
        M11:{
            if (PdgrLnsRW.PrntLvl[i] == 0)
                break M11;
            iX1 = PdgrLnsRW.PrntXY[i][0][0];
            iX2 = PdgrLnsRW.PrntXY[i][1][0];
            PdgrLnsRW.PrntArc[i] = new int[iX2-iX1];
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            M12:{
                if (PdgrLnsRW.SbLnLvl[i][0] != PdgrLnsRW.SbLnLvl[j][0]+1)
                    break M12;
                jX1 = PdgrLnsRW.SbPrsn[j][0];
                jX2 = PdgrLnsRW.SbPrsn[j][PdgrLnsRW.AmntOfsp[j]-1];
                if (iX1>=jX2 | iX2<=jX1)
                    break M12;
                for (k=0; k<PdgrLnsRW.AmntOfsp[j]; k++) {
                    if (PdgrLnsRW.SbPrsn[j][k]>iX1 & PdgrLnsRW.SbPrsn[j][k]<iX2) {
                        PdgrLnsRW.PrntArc[i][PdgrLnsRW.AmntPrntArc[i]] = PdgrLnsRW.SbPrsn[j][k];
                        PdgrLnsRW.AmntPrntArc[i]++;
                    }
                }
            }
            if (PdgrLnsRW.AmntPrntArc[i] > 1) {
                for (j=0; j<PdgrLnsRW.AmntPrntArc[i]-1; j++) {
                    if (PdgrLnsRW.PrntArc[i][j] > PdgrLnsRW.PrntArc[i][j+1]) {
                        k = PdgrLnsRW.PrntArc[i][j];
                        PdgrLnsRW.PrntArc[i][j] = PdgrLnsRW.PrntArc[i][j+1];
                        PdgrLnsRW.PrntArc[i][j+1] = k;
                    }
                }
            }
        }

        //Поиск дуг
        for (i=0; i<PdgrLnsRW.AmntFml; i++) {
            //i - номер семьи, для которой ищем дуги
            iX1 = PdgrLnsRW.SbPrsn[i][0];
            iX2 = PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1];
            PdgrLnsRW.SbLnArc[i] = new int[iX2 - iX1];
            for (j=0; j<PdgrLnsRW.AmntFml; j++)
            M4:{
                //j - номер семьи, с которой пересекается i-ая семья
                //если сама с собой
                if (i == j)
                    break M4;
                //если разные поколения
                if (PdgrLnsRW.SbLnLvl[i][0] != PdgrLnsRW.SbLnLvl[j][0])
                    break M4;
                //если не перекрываются потомки
                if ((iX2<PdgrLnsRW.SbPrsn[j][0]) | (iX1>PdgrLnsRW.SbPrsn[j][PdgrLnsRW.AmntOfsp[j]-1])) {
                    if ((iX2>PdgrLnsRW.PrntOfspn[j][0]) & (iX1<PdgrLnsRW.PrntOfspn[j][0])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.SbLnLvl[i][1])
                            break M4;
                        PdgrLnsRW.SbLnArc[i][PdgrLnsRW.AmntSbLnArc[i]] = PdgrLnsRW.PrntOfspn[j][0];
                        PdgrLnsRW.AmntSbLnArc[i]++;
                    }
                    break M4;
                }
                //если уровень i-ой выше или равен j-ой
                if (PdgrLnsRW.SbLnLvl[i][1] >= PdgrLnsRW.SbLnLvl[j][1]) {
                    if ((iX2>PdgrLnsRW.PrntOfspn[j][1]) & (iX1<PdgrLnsRW.PrntOfspn[j][1])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.SbLnLvl[i][1]) {
                            PdgrLnsRW.SbLnArc[i][PdgrLnsRW.AmntSbLnArc[i]] = PdgrLnsRW.PrntOfspn[j][1];
                            PdgrLnsRW.AmntSbLnArc[i]++;
                        }
                    }
                    if ((iX2>PdgrLnsRW.PrntOfspn[j][0]) & (iX1<PdgrLnsRW.PrntOfspn[j][0])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.SbLnLvl[i][1])
                            break M4;
                        PdgrLnsRW.SbLnArc[i][PdgrLnsRW.AmntSbLnArc[i]] = PdgrLnsRW.PrntOfspn[j][0];
                        PdgrLnsRW.AmntSbLnArc[i]++;
                    }
                    break M4;
                }
                else {
                    for (k=0; k<PdgrLnsRW.AmntOfsp[j]; k++) {
                        if ((PdgrLnsRW.SbPrsn[j][k] < PdgrLnsRW.SbPrsn[i][PdgrLnsRW.AmntOfsp[i]-1]) & (PdgrLnsRW.SbPrsn[j][k] > PdgrLnsRW.SbPrsn[i][0])) {
                            PdgrLnsRW.SbLnArc[i][PdgrLnsRW.AmntSbLnArc[i]] = PdgrLnsRW.SbPrsn[j][k];
                            PdgrLnsRW.AmntSbLnArc[i]++;
                        }
                    }
                }
            }
        }

        for (i=0; i<PdgrLnsRW.AmntFml; i++)
        M6:{
            //i - номер семьи, для которой ищем дуги
            if (PdgrLnsRW.PrntOfspn[i][0] == PdgrLnsRW.PrntOfspn[i][1])
                break M6;
            if (PdgrLnsRW.PrntOfspn[i][0] > PdgrLnsRW.PrntOfspn[i][1]) {
                iX1 = PdgrLnsRW.PrntOfspn[i][1];
                iX2 = PdgrLnsRW.PrntOfspn[i][0];
            } else {
                iX1 = PdgrLnsRW.PrntOfspn[i][0];
                iX2 = PdgrLnsRW.PrntOfspn[i][1];
            }
            PdgrLnsRW.PrOfLnArc[i] = new int[iX2 - iX1];

            for (j = 0; j < PdgrLnsRW.AmntFml; j++)
            M7:{
                //j - номер семьи, с которой пересекается i-ая семья
                //если сама с собой
                if (i == j)
                    break M7;
                //если разные поколения
                if (PdgrLnsRW.SbLnLvl[i][0] != PdgrLnsRW.SbLnLvl[j][0])
                    break M7;
                //если не перекрываются потомки
                if ((iX2 < PdgrLnsRW.SbPrsn[j][0]) | (iX1 > PdgrLnsRW.SbPrsn[j][PdgrLnsRW.AmntOfsp[j] - 1])) {
                    if ((iX2 > PdgrLnsRW.PrntOfspn[j][0]) & (iX1 < PdgrLnsRW.PrntOfspn[j][0])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.PrOfLnLvl[i])
                            break M7;
                        PdgrLnsRW.PrOfLnArc[i][PdgrLnsRW.AmntPrOfLnArc[i]] = PdgrLnsRW.PrntOfspn[j][0];
                        PdgrLnsRW.AmntPrOfLnArc[i]++;
                    }
                    break M7;
                }
                //если уровень i-ой выше или равен j-ой
                if (PdgrLnsRW.PrOfLnLvl[i] >= PdgrLnsRW.SbLnLvl[j][1]) {
                    if ((iX2 > PdgrLnsRW.PrntOfspn[j][1]) & (iX1 < PdgrLnsRW.PrntOfspn[j][1])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.PrOfLnLvl[i]) {
                            PdgrLnsRW.PrOfLnArc[i][PdgrLnsRW.AmntPrOfLnArc[i]] = PdgrLnsRW.PrntOfspn[j][1];
                            PdgrLnsRW.AmntPrOfLnArc[i]++;
                        }
                    }
                    if ((iX2 > PdgrLnsRW.PrntOfspn[j][0]) & (iX1 < PdgrLnsRW.PrntOfspn[j][0])) {
                        if (PdgrLnsRW.PrOfLnLvl[j] > PdgrLnsRW.PrOfLnLvl[i])
                            break M7;
                        PdgrLnsRW.PrOfLnArc[i][PdgrLnsRW.AmntPrOfLnArc[i]] = PdgrLnsRW.PrntOfspn[j][0];
                        PdgrLnsRW.AmntPrOfLnArc[i]++;
                    }
                    break M7;
                }
                else {
                    for (k = 0; k < PdgrLnsRW.AmntOfsp[j]; k++) {
                        if ((PdgrLnsRW.SbPrsn[j][k] < iX2) & (PdgrLnsRW.SbPrsn[j][k] > iX1)) {
                            PdgrLnsRW.PrOfLnArc[i][PdgrLnsRW.AmntPrOfLnArc[i]] = PdgrLnsRW.SbPrsn[j][k];
                            PdgrLnsRW.AmntPrOfLnArc[i]++;
                        }
                    }
                }
            }
        }

        //Сортировка дуг
        for (i=0; i<PdgrLnsRW.AmntFml; i++) {
            if (PdgrLnsRW.AmntSbLnArc[i] > 1) {
                do {
                    bb = false;
                    for (j=0; j<PdgrLnsRW.AmntSbLnArc[i]-1; j++) {
                        if (PdgrLnsRW.SbLnArc[i][j] > PdgrLnsRW.SbLnArc[i][j+1]) {
                            k = PdgrLnsRW.SbLnArc[i][j];
                            PdgrLnsRW.SbLnArc[i][j] = PdgrLnsRW.SbLnArc[i][j+1];
                            PdgrLnsRW.SbLnArc[i][j+1] = k;
                            bb = true;
                        }
                    }
                } while(bb);
            }
            if (PdgrLnsRW.AmntPrOfLnArc[i] > 1) {
                do {
                    bb = false;
                    for (j=0; j<PdgrLnsRW.AmntPrOfLnArc[i]-1; j++) {
                        if (PdgrLnsRW.PrOfLnArc[i][j] > PdgrLnsRW.PrOfLnArc[i][j+1]) {
                            k = PdgrLnsRW.PrOfLnArc[i][j];
                            PdgrLnsRW.PrOfLnArc[i][j] = PdgrLnsRW.PrOfLnArc[i][j+1];
                            PdgrLnsRW.PrOfLnArc[i][j+1] = k;
                            bb = true;
                        }
                    }
                } while(bb);
            }
        }

        try {
            PdgrLnsRW.RWData(PdgrLnsFileName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return false;
        }

        return true;
    }
}
