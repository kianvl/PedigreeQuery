// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

// Поиск семей по пойнтерам
class FamiliesBySequence {
    private JFrame ParentFrame;

    FamiliesBySequence (JFrame ParentFrame) {
        this.ParentFrame = ParentFrame;
    }


    void GatherFamilies (String SqncFileName, String FmlFileName, String FbySFileName) throws Exception
    {
        int i, j, k;

        // Чтение списка пойнтеров
        Sequence Sqnc = new Sequence();
        try {
            Sqnc.GetSequence(SqncFileName);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        // Чтение семейных данных
        FamiliesData FmlData = new FamiliesData();
        try {
            FmlData.RWData(FmlFileName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        FamiliesGenerationsRW FmlGnrtn = new FamiliesGenerationsRW();

        FmlGnrtn.AmntFml = 0;
        FmlGnrtn.FmlID = new int[FmlData.AmntFml];
        FmlGnrtn.FmlGnrtn = new int[FmlData.AmntFml];

        // выбор семей по пойнтерам
        for (i=0; i<Sqnc.AmntPrsns; i++) {
            for (j=0; j<FmlData.AmntFml; j++)
            M0:{
                for (k = 0; k < FmlGnrtn.AmntFml; k++) {
                    if (FmlGnrtn.FmlID[k] == j)
                        break M0;
                }
                for (k = 0; k < FmlData.AmntOfsprFml[j]; k++) {
                    if (Sqnc.Sqnc[i] == FmlData.OfsprFml[j][k]) {
                        FmlGnrtn.FmlID[FmlGnrtn.AmntFml] = j;
                        FmlGnrtn.AmntFml++;
                        break M0;
                    }
                }
                for (k = 0; k < 2; k++) {
                    if (Sqnc.Sqnc[i] == FmlData.PrntFml[k][j]) {
                        FmlGnrtn.FmlID[FmlGnrtn.AmntFml] = j;
                        FmlGnrtn.AmntFml++;
                        break M0;
                    }
                }
            }
        }

        //Запись поколений семей
        try {
            FmlGnrtn.RWData(FbySFileName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }
    }
}
