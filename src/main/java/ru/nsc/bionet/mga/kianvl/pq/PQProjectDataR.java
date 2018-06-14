// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;
import java.io.RandomAccessFile;

//пробел - 32 (20)
//табуляция - 9 (09)
//переход на др строку - 13 (0D)
//возврат коретки - 10 (0A)
class PQProjectDataR {
    int N, Nstr;
    int ClmnIDs[];
    int Ni[];
    private int AmntClmn;
    private long Fl, i;
    public char ch;
    public String Str;
    String PQPrjctDt[][];
    private JFrame RKDFrame;

    PQProjectDataR (JFrame RKDFrame, int AmntClmn) {
        this.RKDFrame = RKDFrame;
        this.AmntClmn = AmntClmn;
        ClmnIDs = new int[AmntClmn];
    }

    void ReadData(String PrjctDtFileName) throws Exception {
        String StrBuff;
        int NnC, nR, nC, j, k, ii, jj;
        RandomAccessFile FR = new RandomAccessFile(PrjctDtFileName, "r");
        Fl = FR.length();

        //Подсчет количества строк
        for (i=0; i<Fl; i++) {
            ii = FR.read();
            if (ii == 10)
                Nstr++;
        }

        //Выделение массива
        PQPrjctDt = new String[Nstr][];
        Ni = new int[Nstr];
        for (j=0; j<Nstr; j++)
            PQPrjctDt[j] = new String[AmntClmn];

        FR.seek(0);

        StrBuff = "";
        NnC = nR = nC = jj = 0;
        //nC - счетчик столбцов
        //nR, jj - счетчик строк
        //Идем по строчкам
        for (i=0; i<Fl; i++) {
            //Читаем символ
            ii = FR.read();
            //Если не пробельный символ, то набираем слово
            if (ii!=32 & ii!=9 & ii!=13 & ii!=10 & i!=Fl-1)
                StrBuff += (char)ii;
            else {
                if (!StrBuff.equals("")) {
                    for (j=0; j<AmntClmn; j++) {
                        //Вложение набранной последовательности в нужную ячейку
                        if ((ClmnIDs[j]-1) == nC)
                            PQPrjctDt[nR][j] = StrBuff;
                    }
                }
                //если конец строки
                if (ii == 10) {
                    jj++;
                    //если в строке нет ни одной записи
                    if (nC == 0)
                        Nstr--;
                    else {
                        Ni[nR] = jj;
                        //если первая строка, то запоминаем количество столбцов
                        if (nR == 0)
                            NnC = nC;
                        else {
                            if (NnC != nC) {
                                if (NnC > nC) {
                                    JOptionPane.showMessageDialog(RKDFrame, "Not enought data in string #" + jj + "\n File \"" + PrjctDtFileName + "\"");
                                    return;
                                }
                                else {
                                    JOptionPane.showMessageDialog(RKDFrame, "Too much data in string #" + jj + "\n File \"" + PrjctDtFileName + "\"");
                                    return;
                                }
                            }
                        }
                        nR++;
                        nC = 0;
                        StrBuff = "";
                    }
                }
                if (ii==9 | ii==32) {
                    if (!StrBuff.equals("")) {
                        nC++;
                        StrBuff = "";
                    }
                }
            }
        }
        FR.close();
    }
}
