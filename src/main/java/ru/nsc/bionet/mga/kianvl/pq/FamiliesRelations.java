// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

class FamiliesRelations {
    private String FileInPutName;
    private String FileGnrtnName;
    private String FileOutPutName;

    FamiliesRelations (String FileInPutName, String FileGnrtnName, String FileOutPutName) {
        this.FileInPutName = FileInPutName;
        this.FileGnrtnName = FileGnrtnName;
        this.FileOutPutName = FileOutPutName;
    }

    //поиск родства между ЯС
    void SeekRelations(JFrame ParentFrame) {
        int i, j, k, l, m;
        int itemp[];

        //чтение исходных данных
        FamiliesData FmlData = new FamiliesData();

        try {
            FmlData.RWData(FileInPutName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }

        FamiliesGenerationsRW FmlGnrtnData = new FamiliesGenerationsRW();

        try {
            FmlGnrtnData.RWData(FileGnrtnName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
            return;
        }

        FamiliesRelationsRW FmlsRltn = new FamiliesRelationsRW();
        FmlsRltn.AmntFml = FmlGnrtnData.AmntFml;
        FmlsRltn.ArrayData();

        itemp = new int[FmlGnrtnData.AmntFml];

        //поиск родственных связей
        for (i=0; i<FmlGnrtnData.AmntFml; i++) {
            //поиск родителей данной семьи среди потомков в других семьях - вертикальная связь
            for (j=0; j<2; j++) {
                m = 0;
                for (k=0; k<FmlData.AmntOfsprFml[FmlGnrtnData.FmlID[i]]; k++) {
                    M0:for (l=0; l<FmlGnrtnData.AmntFml; l++)
                    {
                        //кто из родителей имеет связь и с какой ЯС?
                        if (FmlData.OfsprFml[FmlGnrtnData.FmlID[i]][k] == FmlData.PrntFml[j][FmlGnrtnData.FmlID[l]]) {
                            FmlsRltn.FmlRltnsVrtUp[l][j] = i+1;
                            itemp[m] = l;
                            m++;
                            break M0;
                        }
                    }
                }
                //кто из потомков имеет связь и с какой ЯС?
                FmlsRltn.FmlRltnsVrt[i][j] = new int[m+1];
                FmlsRltn.FmlRltnsVrt[i][j][0] = m;
                for (k=0; k<m; k++)
                    FmlsRltn.FmlRltnsVrt[i][j][k+1] = itemp[k];

                //поиск родителей данной семьи среди родителей в других семьях - горизонтальная связь
                m = 0;
                M1:{
                    for (k=0; k<i; k++) {
                        if (FmlData.PrntFml[j][FmlGnrtnData.FmlID[i]] == FmlData.PrntFml[j][FmlGnrtnData.FmlID[k]]) {
                            FmlsRltn.FmlRltnsHrzUp[i][j] = k+1;
                            break M1;
                        }
                    }
                    for (k=i+1; k<FmlGnrtnData.AmntFml; k++) {
                        if (FmlData.PrntFml[j][FmlGnrtnData.FmlID[i]] == FmlData.PrntFml[j][FmlGnrtnData.FmlID[k]]) {
                            itemp[m] = k;
                            m++;
                        }
                    }
                }
                FmlsRltn.FmlRltnsHrz[i][j] = new int[m+1];
                FmlsRltn.FmlRltnsHrz[i][j][0] = m;
                for (k=0; k<m; k++)
                    FmlsRltn.FmlRltnsHrz[i][j][k+1] = itemp[k];
            }
        }

        //запись данных
        try {
            FmlsRltn.RWData(FileOutPutName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }
    }
}
