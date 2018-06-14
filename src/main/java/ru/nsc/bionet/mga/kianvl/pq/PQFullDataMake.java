// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.io.RandomAccessFile;

class PQFullDataMake {
    boolean MakeFullData (JFrame PrntFrame, String ProjectName) throws Exception {
        RandomAccessFile PQPrjctFile = new RandomAccessFile("Projects/" + ProjectName + "/Project.ini", "r");

        int i, j, k, l;
        String PdgrDtNm = "";
        int IDC=0, FthC=0, MthC=0, sexC=0;
        String DcsdDtNm = "";
        int DIDC=0, DDtC=0;
        String SCDtNm = "";
        int CIDC=0, CDtC=0;
        String SgntrNm[] = new String[16];
        int SIDC[] = new int [16];
        int SDtC[] = new int [16];

        //Чтение списка файлов и правил их обработки
        PdgrDtNm = PQPrjctFile.readUTF();
        IDC = PQPrjctFile.readInt();
        FthC = PQPrjctFile.readInt();
        MthC = PQPrjctFile.readInt();
        sexC = PQPrjctFile.readInt();

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

        //Чтение данных о родственных связях
        //Проверка настроек
        if (PdgrDtNm.equals("") | IDC==0 | FthC==0 | MthC==0 | sexC==0) {
            JOptionPane.showMessageDialog (PrntFrame, "Data on pedigree are not configured!");
            return false;
        }

        PQProjectDataR PQPrjctDt = new PQProjectDataR(PrntFrame, 4);
        PQPrjctDt.ClmnIDs[0] = IDC;
        PQPrjctDt.ClmnIDs[1] = FthC;
        PQPrjctDt.ClmnIDs[2] = MthC;
        PQPrjctDt.ClmnIDs[3] = sexC;

        try {
            PQPrjctDt.ReadData("Projects/" + ProjectName + "/" + PdgrDtNm);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PrntFrame, "" + e);
        }

        //Создать файл полных данных
        PQFullDataRW PQFlDtRW;
        PQFlDtRW = new PQFullDataRW();
        PQFlDtRW.AmntPrsn = PQPrjctDt.Nstr-1;
        PQFlDtRW.AmntSgn = 0;
        for (i=0; i<16; i++) {
            if (!SgntrNm[i].equals("") & SIDC[i]!=0 & SDtC[i]!=0)
                PQFlDtRW.AmntSgn++;
        }
        PQFlDtRW.ArrayData();

        //Проверка ID на 0
        for (i=1; i<=PQFlDtRW.AmntPrsn; i++) {
            if (PQPrjctDt.PQPrjctDt[i][0].equals ("0")) {
                JOptionPane.showMessageDialog (PrntFrame, "Person ID = 0" + " in strings #" + PQPrjctDt.Ni[i]);
                return false;
            }
        }

        //Проверка дублирования ID
        for (i=1; i<PQFlDtRW.AmntPrsn; i++) {
            for (j=i+1; j<=PQFlDtRW.AmntPrsn; j++) {
                if (PQPrjctDt.PQPrjctDt[i][0].equals (PQPrjctDt.PQPrjctDt[j][0])) {
                    JOptionPane.showMessageDialog(PrntFrame, "Doubled person ID = " + PQPrjctDt.PQPrjctDt[i][0] + " in strings #" + PQPrjctDt.Ni[i] + " and #" + PQPrjctDt.Ni[j]);
                    return false;
                }
            }
        }

        String SxID[] = new String[2];

        //Проверка ID родителей и шифра пола
        SxID[0] = PQPrjctDt.PQPrjctDt[1][3];
        SxID[1] = "";
        for (i=2; i<=PQFlDtRW.AmntPrsn; i++) {
            if (!PQPrjctDt.PQPrjctDt[i][1].equals(PQPrjctDt.PQPrjctDt[i][2])) {
                if (PQPrjctDt.PQPrjctDt[i][1].equals("0") | PQPrjctDt.PQPrjctDt[i][2].equals("0")) {
                    JOptionPane.showMessageDialog(PrntFrame, "Wrong ParentIDs of person \"" + PQPrjctDt.PQPrjctDt[i][0] + "\" in string #" + PQPrjctDt.Ni[i] + ".\nBoth parents should be known or unknown.");
                    return false;
                }
            }
            if (!PQPrjctDt.PQPrjctDt[i][3].equals(SxID[0])) {
                if (SxID[1].equals("")) SxID[1] = PQPrjctDt.PQPrjctDt[i][3];
                else {
                    if (!SxID[1].equals(PQPrjctDt.PQPrjctDt[i][3])) {
                        JOptionPane.showMessageDialog(PrntFrame, "Invalid SexID of person \"" + PQPrjctDt.PQPrjctDt[i][0] + "\" in string #" + PQPrjctDt.Ni[i]);
                        return false;
                    }
                }
            }
        }

        byte iSxID[] = new byte[2];

        //Идентификация шифра пола
        if ((SxID[0].equalsIgnoreCase("male") & SxID[1].equalsIgnoreCase("female")) | (SxID[0].equalsIgnoreCase("female") & SxID[1].equalsIgnoreCase("male"))) {
            if (SxID[0].equalsIgnoreCase("male")) {
                iSxID[0] = 1;
                iSxID[1] = 2;
            }
            else {
                iSxID[0] = 2;
                iSxID[1] = 1;
            }
        }
        else {
            Object[] options = {SxID[0], SxID[1]};
            int NJO = JOptionPane.showOptionDialog (null, "Click button with ID for male!", "Choose sex ID", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
            if (NJO == JOptionPane.YES_OPTION) {
                iSxID[0] = 1;
                iSxID[1] = 2;
            }
            if (NJO == JOptionPane.NO_OPTION) {
                iSxID[0] = 2;
                iSxID[1] = 1;
            }
        }

        //Чтение данных о настройке цветов
        for (i=0; i<PQFlDtRW.AmntPrsn; i++) {
            for (j=0; j<3; j++)
                PQFlDtRW.ClrRGB[i][j] = 255;
        }
        if (!SCDtNm.equals("") & CIDC!=0 & CDtC!=0) {
            PQProjectDataR PQClrsDt = new PQProjectDataR(PrntFrame, 2);
            PQClrsDt.ClmnIDs[0] = CIDC;
            PQClrsDt.ClmnIDs[1] = CDtC;
            try {
                PQClrsDt.ReadData("Projects/" + ProjectName + "/" + SCDtNm);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(PrntFrame, "" + e);
            }

            RandomAccessFile PQClrsIni = new RandomAccessFile("Projects/" + ProjectName + "/Color.ini", "rw");

            PQFlDtRW.AmntClr = PQClrsIni.readInt();
            PQFlDtRW.ClrCds = new int[PQFlDtRW.AmntClr][];
            PQFlDtRW.SClr = new String[PQFlDtRW.AmntClr];
            for (i=0; i<PQFlDtRW.AmntClr; i++) {
                PQFlDtRW.SClr[i] = PQClrsIni.readUTF();
                PQFlDtRW.ClrCds[i] = new int[3];
                for (j=0; j<3; j++)
                    PQFlDtRW.ClrCds[i][j] = PQClrsIni.readInt();
            }
            PQClrsIni.close();

            //Запись новой цветовой схемы
            for (i=1; i<=PQFlDtRW.AmntPrsn; i++) {
                for (j=1; j<PQClrsDt.Nstr; j++) {
                    if (PQPrjctDt.PQPrjctDt[i][0].equals(PQClrsDt.PQPrjctDt[j][0])) {
                        for (k=0; k<PQFlDtRW.AmntClr; k++) {
                            if (PQFlDtRW.SClr[k].equals(PQClrsDt.PQPrjctDt[j][1])) {
                                for (l=0; l<3; l++)
                                    PQFlDtRW.ClrRGB[i-1][l] = PQFlDtRW.ClrCds[k][l];
                            }
                        }
                    }
                }
            }
        }

        //Чтение данных о перечеркивании символов
        if (!DcsdDtNm.equals ("") & DIDC!=0 & DDtC!=0) {
            PQProjectDataR PQCrsDt = new PQProjectDataR (PrntFrame, 2);
            PQCrsDt.ClmnIDs[0] = DIDC;
            PQCrsDt.ClmnIDs[1] = DDtC;
            try {
                PQCrsDt.ReadData ("Projects/" + ProjectName + "/" + DcsdDtNm);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog (PrntFrame, "" + e);
            }

            for (i=1; i<=PQFlDtRW.AmntPrsn; i++) {
                for (j=1; j<PQCrsDt.Nstr; j++) {
                    if (PQPrjctDt.PQPrjctDt[i][0].equals (PQCrsDt.PQPrjctDt[j][0])) {
                        if (!PQCrsDt.PQPrjctDt[j][1].equals ("0"))
                            PQFlDtRW.CrsLn[i-1] = true;
                    }
                }
            }
        }

        //Чтение данных о настройке подписей
        l = 0;
        for (i=0; i<16; i++) {
            if (!SgntrNm[i].equals("") & SIDC[i]!=0 & SDtC[i]!=0) {
                PQProjectDataR PQSgnDt = new PQProjectDataR(PrntFrame, 2);
                PQSgnDt.ClmnIDs[0] = SIDC[i];
                PQSgnDt.ClmnIDs[1] = SDtC[i];
                try {
                    PQSgnDt.ReadData("Projects/" + ProjectName + "/" + SgntrNm[i]);
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(PrntFrame, "" + e);
                }
                PQFlDtRW.SignsNm[l] = PQSgnDt.PQPrjctDt[0][1];
                for (j=1; j<=PQFlDtRW.AmntPrsn; j++) {
                    for (k=1; k<PQSgnDt.Nstr; k++) {
                        if (PQPrjctDt.PQPrjctDt[j][0].equals(PQSgnDt.PQPrjctDt[k][0]))
                            PQFlDtRW.Signs[j-1][l] = PQSgnDt.PQPrjctDt[k][1];
                    }
                }
                l++;
            }
        }

        //Передача данных в массивы полных данных
        PQFlDtRW.PrsnIDNm = PQPrjctDt.PQPrjctDt[0][0];
        for (i=0; i<2; i++)
            PQFlDtRW.PrntIDNm[i] = PQPrjctDt.PQPrjctDt[0][i+1];
        PQFlDtRW.SexIDNm = PQPrjctDt.PQPrjctDt[0][3];
        for (i=0; i<PQFlDtRW.AmntPrsn; i++) {
            PQFlDtRW.PrsnID[i] = PQPrjctDt.PQPrjctDt[i+1][0];
            if (PQPrjctDt.PQPrjctDt[i+1][3].equals(SxID[0]))
                PQFlDtRW.SexID[i] = iSxID[0];
            else
                PQFlDtRW.SexID[i] = iSxID[1];
        }

        //Проверка шифра пола и наличия всех родителей
        for (i=1; i<=PQFlDtRW.AmntPrsn; i++) {
            if (!PQPrjctDt.PQPrjctDt[i][1].equals("0")) {
                for (j=1; j<=2; j++) {
                    M0: {
                        for (k=0; k<PQFlDtRW.AmntPrsn; k++) {
                            if (PQPrjctDt.PQPrjctDt[i][j].equals(PQFlDtRW.PrsnID[k])) {
                                PQFlDtRW.PrntID[i-1][j-1] = k+1;
                                if (PQFlDtRW.SexID[k] != j) {
                                    JOptionPane.showMessageDialog (PrntFrame, "Mistaken SexID of person \"" + PQFlDtRW.PrsnID[k] + "\" in string #" + PQPrjctDt.Ni[k+1] + ".\n According to data on parents of person \"" + PQFlDtRW.PrsnID[i-1] + "\" in string #" + PQPrjctDt.Ni[i]);
                                    return false;
                                }
                                break M0;
                            }
                        }
                        JOptionPane.showMessageDialog (PrntFrame, "There is no data on parent \"" + PQPrjctDt.PQPrjctDt[i][j] + "\" of person \"" + PQFlDtRW.PrsnID[i] + "\" in string #" + PQPrjctDt.Ni[i]);
                        return false;
                    }
                }
            }
        }

        //Запись полных данных
        PQFlDtRW.RWData ("temp/FullData.tmp", 'w');

        //Запрос пробанда
        String ProbandID;
        boolean b;
        b = false;
        do {
            ProbandID = JOptionPane.showInputDialog(PrntFrame, "Please enter proband ID");
            M1:{
                for (i=0; i<PQFlDtRW.AmntPrsn; i++) {
                    if (PQFlDtRW.PrsnID[i].equals(ProbandID)) {
                        b = true;
                        break M1;
                    }
                }
                JOptionPane.showMessageDialog(PrntFrame, "Invalid proband ID - \"" + ProbandID + "\"!");
            }
        } while (!b);

        int PrsnPdgr[];
        PrsnPdgr = new int[PQFlDtRW.AmntPrsn];
        for (i=0; i<PQFlDtRW.AmntPrsn; i++) {
            if (PQFlDtRW.PrsnID[i].equals(ProbandID))
                PrsnPdgr[0] = i;
        }

        PedigreeData PdgrData = new PedigreeData();

        PdgrData.setAmntPrsns(1);
        for (i=0; i<PdgrData.getAmntPrsns(); i++) {
            k = PrsnPdgr[i];
            for (j=0; j<PQFlDtRW.AmntPrsn; j++) {
                if (PQFlDtRW.PrntID[k][PQFlDtRW.SexID[j]-1]==j+1 | PQFlDtRW.PrntID[j][PQFlDtRW.SexID[k]-1]==k+1)
                M2:{
                    for (l=0; l<PdgrData.getAmntPrsns(); l++) {
                        if (PrsnPdgr[l] == j)
                            break M2;
                    }
                    PrsnPdgr[PdgrData.getAmntPrsns()] = j;
                    PdgrData.setAmntPrsns(PdgrData.getAmntPrsns()+1);
                }
            }
        }

        PdgrData.ArrayData ();

        for (i=0; i<PdgrData.getAmntPrsns(); i++) {
            PdgrData.setPrsnID(i, PQFlDtRW.PrsnID[PrsnPdgr[i]]);
            PdgrData.setSexID(i, PQFlDtRW.SexID[PrsnPdgr[i]]);
            for (j=0; j<2; j++) {
                for (k=0; k<PQFlDtRW.AmntPrsn; k++) {
                    if (PQFlDtRW.PrntID[PrsnPdgr[i]][j] == k+1) {
                        for (l=0; l<PdgrData.getAmntPrsns(); l++) {
                            if (PrsnPdgr[l] == k)
                                PdgrData.setPrntID(j, i, l);
                        }
                    }
                }
            }
        }

        try {
            PdgrData.RWData("temp/Pedigree.tmp", 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(PrntFrame, "" + e);
            return false;
        }

        return true;
    }
}
