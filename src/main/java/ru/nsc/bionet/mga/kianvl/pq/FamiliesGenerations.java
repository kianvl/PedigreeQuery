// Author Anatoly V. Kirichenko kianvl@mail.ru
// License GNU General Public License >= 2

package ru.nsc.bionet.mga.kianvl.pq;

import javax.swing.*;

class FamiliesGenerations {
    private JFrame ParentFrame;
    private FamiliesGenerationsRW FmlGnrtData;
    private FamiliesRelationsRW FmlRltn;
    private int FmlRltnF[];
    private int FmlDpndnc[];


    //переопределение поколений
    private void FmlGnrtAlgnmnt(int n) {
        int i;
        for (i=0; i<FmlGnrtData.AmntFml; i++) {
            if (FmlDpndnc[i] == n+1) {
                FmlDpndnc[i] = FmlDpndnc[n];
                FmlGnrtData.FmlGnrtn[i] += FmlGnrtData.FmlGnrtn[n];
            }
        }
    }


    //Расчет поколений
    private void DoGeneration () {
        int i, j, k, l, n;
        boolean FGcheck;

        //рассчет поколений
        do {
            FGcheck = false;
            //идем по семьям
            for (i=0; i<FmlRltn.AmntFml; i++) {
                //Если одна связь, то беремся за эту семью
                if (FmlRltnF[i] == 1)
                M0:{
                    for (j=0; j<2; j++) {
                        //Если связь через потомка в этой семье и родителя в др.
                        if (FmlRltn.FmlRltnsVrt[i][j][0] == 1) {
                            //указываем зависимость i-ой семьи
                            FmlDpndnc[i] = FmlRltn.FmlRltnsVrt[i][j][1]+1;
                            //задаем поколение i-ой семье
                            FmlGnrtData.FmlGnrtn[i] = -1;
                            //удаляем указание на вертикальную зависимость для i-ой семьи
                            FmlRltn.FmlRltnsVrt[i][j][0] = 0;
                            //удаляем указание на вертикальную зависимость для связной семьи
                            FmlRltn.FmlRltnsVrtUp[FmlDpndnc[i]-1][j] = 0;
                            //уменьшаем колличество связей для i-ой семьи на 1
                            FmlRltnF[i]--;
                            //уменьшаем колличество связей для связной семьи на 1
                            FmlRltnF[FmlDpndnc[i]-1]--;
                            //переопределяем поколения
                            FmlGnrtAlgnmnt (i);
                            FGcheck = true;
                            break M0;
                        }
                        //Если связь через родителя в этой семье и потомка в др.
                        if (FmlRltn.FmlRltnsVrtUp[i][j] > 0) {
                            //указываем зависимость i-ой семьи
                            FmlDpndnc[i] = FmlRltn.FmlRltnsVrtUp[i][j];
                            //
                            n = FmlRltn.FmlRltnsVrtUp[i][j]-1;
                            //удаляем указание на вертикальную зависимость для i-ой семьи
                            FmlRltn.FmlRltnsVrtUp[i][j] = 0;
                            //задаем поколение i-ой семье
                            FmlGnrtData.FmlGnrtn[i] = 1;
                            //уменьшаем колличество связей для i-ой семьи на 1
                            FmlRltnF[i]--;
                            //уменьшаем колличество связей для связной семьи на 1
                            FmlRltnF[FmlDpndnc[i]-1]--;
                            //переопределяем поколения
                            FmlGnrtAlgnmnt (i);
                            FGcheck = true;
                            if (FmlRltn.FmlRltnsVrt[n][j][0] > 1)
                            M1:{
                                for (k=1; k<FmlRltn.FmlRltnsVrt[n][j][0]; k++) {
                                    if (FmlRltn.FmlRltnsVrt[n][j][k] == i) {
                                        for (l=k; l<FmlRltn.FmlRltnsVrt[n][j][0]; l++)
                                            FmlRltn.FmlRltnsVrt[n][j][l] = FmlRltn.FmlRltnsVrt[n][j][l+1];
                                        FmlRltn.FmlRltnsVrt[n][j][FmlRltn.FmlRltnsVrt[n][j][0]] = i;
                                        break M1;
                                    }
                                }
                            }
                            FmlRltn.FmlRltnsVrt[n][j][0]--;
                            break M0;
                        }

                        //Если связь через родителя и родителя в др.
                        if (FmlRltn.FmlRltnsHrz[i][j][0] == 1) {
                            FmlDpndnc[i] = FmlRltn.FmlRltnsHrz[i][j][1]+1;
                            FmlRltn.FmlRltnsHrz[i][j][0] = 0;
                            FmlRltn.FmlRltnsHrzUp[FmlDpndnc[i]-1][j] = 0;
                            FmlRltnF[i]--;
                            FmlRltnF[FmlDpndnc[i]-1]--;
                            FmlGnrtAlgnmnt (i);
                            FGcheck = true;
                            break M0;
                        }
                        //Если связь через родителя и родителя в др. (обратная)
                        if (FmlRltn.FmlRltnsHrzUp[i][j] > 0) {
                            FmlDpndnc[i] = FmlRltn.FmlRltnsHrzUp[i][j];
                            n = FmlRltn.FmlRltnsHrzUp[i][j]-1;
                            FmlRltn.FmlRltnsHrzUp[i][j] = 0;
                            FmlRltnF[i]--;
                            FmlRltnF[FmlDpndnc[i]-1]--;
                            FmlGnrtAlgnmnt (i);
                            FGcheck = true;
                            if (FmlRltn.FmlRltnsHrz[n][j][0] > 1)
                            M1:{
                                for (k=1; k<FmlRltn.FmlRltnsHrz[n][j][0]; k++) {
                                    if (FmlRltn.FmlRltnsHrz[n][j][k] == i) {
                                        for (l=k; l<FmlRltn.FmlRltnsHrz[n][j][0]; l++)
                                            FmlRltn.FmlRltnsHrz[n][j][l] = FmlRltn.FmlRltnsHrz[n][j][l+1];
                                        FmlRltn.FmlRltnsHrz[n][j][FmlRltn.FmlRltnsHrz[n][j][0]] = i;
                                        break M1;
                                    }
                                }
                            }
                            FmlRltn.FmlRltnsHrz[n][j][0]--;
                            break M0;
                        }
                    }
                }
            }
        }
        while (FGcheck);
    }

/*
    //Разрезание петли (еще не реализовано!)
    private boolean CutLoop () {
        int i, j, n;
        //Поиск кандидатов на разрыв

        //поиск ЯС, у которой есть только две связи и обе вверх
        for (i=0; i<FmlRltn.AmntFml; i++)
        M0:{
            //Если связь и поколение определены - пропускаем ЯС
            if (FmlDpndnc[i] != 0)
                break M0;

            //Если две связи, то беремся за эту семью
//			if (FmlRltnF[i] != 2)
//			break M0;

            //Если нет одной связи вверх - пропускаем ЯС
            if (!(FmlRltn.FmlRltnsVrtUp[i][0] == 0 & FmlRltn.FmlRltnsVrtUp[i][1] == 0))
                break M0;
            JOptionPane.showMessageDialog (ParentFrame, "F #" + i + " has 2 links up");

            //разрыв петли над этой ЯС
//			n = FmlRltn.FmlRltnsVrtUp[i][0]-1;
            JOptionPane.showMessageDialog (ParentFrame, "" + FmlRltn.FmlRltnsVrtUp[i][0]);
            FmlRltnF[FmlRltn.FmlRltnsVrtUp[i][0]-1]--;
            FmlRltn.FmlRltnsVrtUp[i][0] = 0;
            FmlRltnF[i]--;
//			if (FmlRltn.FmlRltnsVrt[i][0][0]==1)
//				FmlRltn.FmlRltnsVrt[i][j][0] = 0;
        }

        return true;
    }
*/

/*
	//Обработка петли (еще не реализовано!)
	private void GnrtnOfLoop()
	{
		int i, j, n;
		int nn[][] = new int[2][2];
		boolean bb;
		boolean b[] = new boolean[2];

//		for (i=0; i<FmlRltn.AmntFml; i++)
//			JOptionPane.showMessageDialog(ParentFrame, "Семья #" + (i+1) + " зависит от семьи #" + FmlDpndnc[i]);

		do
		{
			bb = false;
			for (i=0; i<FmlRltn.AmntFml; i++)
			M0:{
				n = -1;

				//Если связь и поколение определены - пропускаем ЯС
				if (FmlDpndnc[i] != 0)
					break M0;

				//Если нет связей вверх - пропускаем ЯС
				if (FmlRltn.FmlRltnsVrtUp[i][0]==0 & FmlRltn.FmlRltnsVrtUp[i][1]==0)
					break M0;

				//По каждой связи вверх нужно определить ее состоятельность.
				//Сотоятельность - для родительской ЯС определено поколение или такой семьи нет
				for (j=0; j<2; j++)
				M1:{
					nn[j][0] = nn[j][1] = -1;
					b[j] = false;

					//Если нет связи вверх по j-му родителю - пропускаем этого родителя
					if (FmlRltn.FmlRltnsVrtUp[i][j] == 0)
					{
						b[j] = true;
						break M1;
					}

					//nn-ая ЯС - ЯС, в которой потомок = родитель i-ой семьи
					nn[j][0] = FmlRltn.FmlRltnsVrtUp[i][j]-1;

					//Если у nn-ой ЯС нет связей вверх - i-ая зависит от нее и поколение +1
					if (FmlRltn.FmlRltnsVrtUp[nn[j][0]][0]==0 & FmlRltn.FmlRltnsVrtUp[nn[j][0]][1]==0)
					{
						nn[j][1] = 1;
						b[j] = true;
						break M1;
					}

					//Если у nn-ой семьи есть связь вверх и ее поколение еще не определено - пропускаем nn-ую ЯС
					if (FmlDpndnc[nn[j][0]]==0)
						break M1;

					//Если у nn-ой семьи есть связь вверх и ее поколение определено
					//i-ая зависит от тойже, что и nn-ая и поколение nn-ой +1
					nn[j][1] = FmlGnrtData.FmlGnrtn[nn[j][0]] + 1;
					nn[j][0] = FmlDpndnc[nn[j][0]]-1;
					b[j] = true;
				}

				//Если обе связи несостоятельны - пропускаем i-ую ЯС
				if (!b[0] | !b[1])
					break M0;

				M2:{
					//Есть обе родительские ЯС
					if (nn[0][0]>=0 & nn[1][0]>=0)
					{
						//Если родители i-ой ЯС - кровные родственники
						if (nn[0][0] == nn[1][0])
						{
							if (nn[0][1] > nn[1][1])
								n = 0;
							else
								n = 1;
							break M2;
						}

						FmlDpndnc[nn[1][0]] = nn[0][0]+1;
						FmlGnrtData.FmlGnrtn[nn[1][0]] = nn[1][1] - nn[0][1];
						FmlGnrtAlgnmnt(nn[1][0]);
						n = 0;
						bb = true;
					}
					//Только одна родительская ЯС
					else
					{
						if (nn[0][0]>=0)
							n = 0;
						else
							n = 1;
					}
				}

				FmlDpndnc[i] = nn[n][0]+1;
				FmlGnrtData.FmlGnrtn[i] = nn[n][1];
				FmlGnrtAlgnmnt(i);
				bb = true;
			}
		}while(bb);

//		for (i=0; i<FmlRltn.AmntFml; i++)
//			JOptionPane.showMessageDialog(ParentFrame, "Семья #" + (i+1) + " зависит от семьи #" + FmlDpndnc[i]);

		n = 0;
		for (i=0; i<FmlRltn.AmntFml; i++)
		{
			if (FmlDpndnc[i] == 0)
				n++;
		}
		if (n > 1)
		{
			JOptionPane.showMessageDialog(ParentFrame, "4 - " + (FmlDpndnc[3]+1));
			for (i=0; i<FmlRltn.AmntFml; i++)
			{
				JOptionPane.showMessageDialog(ParentFrame, "Семья #" + (i+1) + " зависит от семьи #" + FmlDpndnc[i]);
				if (FmlDpndnc[i] == 0)
					JOptionPane.showMessageDialog(ParentFrame, "Поколения неопределены!! - " + (i+1));
			}
		}
	}
*/


    boolean SeekGenerations (JFrame ParentFrame, String FileFmlRltName, String FileFmlGnrtName) {
        this.ParentFrame = ParentFrame;
        FmlRltn = new FamiliesRelationsRW();
        int i, j, n;

        try {
            FmlRltn.RWData(FileFmlRltName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        FmlRltnF = new int[FmlRltn.AmntFml];

        for (i=0; i<FmlRltn.AmntFml; i++) {
            for (j=0; j<2; j++) {
                FmlRltnF[i] += FmlRltn.FmlRltnsVrt[i][j][0];
                FmlRltnF[i] += FmlRltn.FmlRltnsHrz[i][j][0];
                if (FmlRltn.FmlRltnsVrtUp[i][j] > 0)
                    FmlRltnF[i]++;
                if (FmlRltn.FmlRltnsHrzUp[i][j] > 0)
                    FmlRltnF[i]++;
            }
        }

        FmlGnrtData = new FamiliesGenerationsRW();
        try {
            FmlGnrtData.RWData(FileFmlGnrtName, 'r');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }

        FmlDpndnc = new int[FmlRltn.AmntFml];

        DoGeneration ();

        n = 0;
        for (i=0; i<FmlRltn.AmntFml; i++) {
            if (FmlDpndnc[i] == 0)
                n++;
        }
        if (n > 1) {
//			JOptionPane.showMessageDialog(ParentFrame, "LOOP - " + n);
//			for (i=0; i<FmlRltn.AmntFml; i++)
//			{
//				if (FmlDpndnc[i] == 0)
//				{
//					JOptionPane.showMessageDialog(ParentFrame, "Loop for " + (i+1) + ", N = " + FmlRltnF[i]);
//					if ((FmlRltn.FmlRltnsVrtUp[i][0] == 0) & (FmlRltn.FmlRltnsVrtUp[i][1] == 0))
//						JOptionPane.showMessageDialog(ParentFrame, "В петле родоначальник - " + (i+1));
//				}
//			}
            JOptionPane.showMessageDialog(ParentFrame, "Loop!!");
//			CutLoop ();
            DoGeneration ();
//			GnrtnOfLoop();
//			return false;
        }

        n = 2147483647;
        for (i=0; i<FmlRltn.AmntFml; i++) {
            if (FmlGnrtData.FmlGnrtn[i] < n)
                n = FmlGnrtData.FmlGnrtn[i];
        }
        for (i=0; i<FmlRltn.AmntFml; i++)
            FmlGnrtData.FmlGnrtn[i] -= n;

        try {
            FmlGnrtData.RWData(FileFmlGnrtName, 'w');
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(ParentFrame, "" + e);
        }
        return true;
    }
}
